;;;;
;;;;	LLROOM.SC
;;;;	(c) Sierra On-Line, Inc, 1990
;;;;
;;;;	Author: Doug Oldfield
;;;;	November 7, 1990
;;;;
;;;;	Our game has changes to the init & doit methods of Room.
;;;;	In order for us to keep these changes without having to maintain our
;;;;	own GAME.SC, I have subclassed Room.  All our Rooms are now LLRooms
;;;;
;;;;	Classes:
;;;;	  LLRoom


(script# LLROOM)

(public
	enterRmScript	0
	leaveRmScript  1
)

; this is here just so the stupid thing will compile
(instance roomControls of Controls
	(properties
		name "controls"
	)
)

(class LLRoom kindof Room
	(method (init &tmp wide high)
		(= number curRoomNum)
		(= controls roomControls)

		(= perspective picAngle)
		;(= skipFactor 1)

		;Draw a picture (if non zero) in proper style
		(if picture
			(self drawPic:picture)
		)

		; if it's not a scrolling room and if the room doesn't have a script
		; we're assuming the script is for entering room, since we're in the init:
		(cond
			; in case we're doing speed test or something
			((not (cast contains: ego))
				NULL
			)
			(script
				NULL
			)
			((not (ego normal?))
				NULL
			)
;			((not ((User alterEgo?) edgeHit?))
;				(HandsOn)
;			)
			((OneOf style SCROLLRIGHT SCROLLLEFT SCROLLUP SCROLLDOWN)
				(= wide (+ 1 (/ (CelWide ((User alterEgo?) view?) ((User alterEgo?) loop?) ((User alterEgo?) cel?)) 2)))
				(= high (+ 1 (/ (CelHigh ((User alterEgo?) view?) ((User alterEgo?) loop?) ((User alterEgo?) cel?)) 2)))

				;Reposition ego if he hit an edge in the previous room.
				(switch ((User alterEgo?) edgeHit?)
					(NORTH
						((User alterEgo?) y: (- southEdge 1))
					)
					(WEST
						((User alterEgo?) x: (- eastEdge wide))
					)
					(SOUTH
						((User alterEgo?) y: (+ horizon high))
					)
					(EAST
						((User alterEgo?) x: (+ westEdge wide))
					)
				)
				((User alterEgo?)
					edgeHit:		0,
					userSpeed:
				)
				(HandsOn)
			)
			(else
				(self setScript:	enterRmScript NULL prevRoomNum)
			)
		)
	)

	(method	(doit &tmp nRoom)
		;; Send the doit: to any script, then check to see if ego has
		;; hit the edge of the screen.
		;; - revised by Pablo 11/19/88 to save space

		(cond
			(script
				(script doit:)
			)
			; in case we're doing speed test or something
			((not (cast contains: ego))
				NULL
			)
			((= nRoom
				(switch ((User alterEgo?) edgeHit?)
					(NORTH	north)
					(EAST		east)
					(SOUTH	south)
					(WEST		west)
				)
			 )
				(self setScript: leaveRmScript NULL nRoom)
			)
		)
	);doit
)


; room doit method calls this script when ego hits an edge
; room number that corresponds to which edge ego hit is passed in register
; default behavior:
;	hit north (horizon)
;		just change rooms
;	hit south, east or west
;		move forward in current heading til off screen

(instance leaveRmScript of Script
	(properties
		name	"lRS"
	)

	(method (changeState ns &tmp high wide)
		(switchto (= state ns)
			(
				(HandsOff)
;				(User
;					canInput:		FALSE,
;					canControl:		FALSE
;				)
;				; do the wait cursor so it looks like HandsOff
;; i don't do anything with the cursor cuz it seems to be handled
;; by the system
;;				(theGame setCursor: waitCursor TRUE)

				(= wide (CelWide (ego view?) (ego loop?) (ego cel?)))

				; Used to do noBlkMoves but turned out to be exception to
				; the rule.  Now we just do normal boring PolyPaths.
				(switch register
					((client north?)
						(curRoom newRoom: register)
					)
					((client south?)
						(= high (CelHigh (ego view?) (ego loop?) (ego cel?)))
						; becauser he's stopped at the moment high & wide are set,
;						; he has no head & his cel is shorter than in walking
;						; loop, so add height of head to compensate
;						(if (IsObject (ego _head?))
;							(+= high (CelHigh
;											((ego _head?) view?)
;											((ego _head?) loop?)
;											((ego _head?) cel?)
;										)
;							)
;						)
						(ego setMotion: PolyPath (ego x?) (+ southEdge high) self)
					)
					((client east?)
						(ego setMotion: PolyPath (+ eastEdge wide) (ego y?) self)
					)
					((client west?)
						(ego setMotion: PolyPath (- westEdge wide) (ego y?) self)
					)
				)
			)
			(
				; cuz sometimes ego hits an illegal spot before moving completely off
				; screen.  this looks bad with scrolling
				(ego hide:)
				(= cycles 2)
			)
			(
				(curRoom
					setScript:	0,
					newRoom:		register
				)
			)
		)
	)
);leaveRmScript


; room init method calls this script if there's not a script already attached
; to the room and it's style isn't one of the SCROLLs
; prevRoomNum is passed in register
; default behavior
;	from north
;		ego appear at (+ horizon yStep)
;	from south
;		start below screen & walk up
;	from east
;		walk from east
;	from west
;		walk form west

(instance enterRmScript of Script
	(properties
		name	"eRS"
	)

	(procedure	(SeeIfOffX)
		(cond
			((< (ego x?) westEdge)
				(ego x: (+ westEdge (* (ego xStep?) 2)))
			)
			((> (ego x?) eastEdge)
				(ego x: (- eastEdge (* (ego xStep?) 2)))
			)
		)
	)
	(procedure	(SeeIfOffY)
		(cond
			((< (ego y?) (curRoom horizon?))
				(ego y: (+ (curRoom horizon?) (* (ego yStep?) 2)))
			)
			((> (ego y?) southEdge)
				(ego y: (- southEdge (* (ego yStep?) 2)))
			)
		)
	)

	(method (changeState ns &tmp high wide)
		(switchto (= state ns)
			(
				; cuz sometimes there were cycles left over from a previous
				; invocation
				(= cycles 0)

				(HandsOff)
;				(User
;					canInput:		FALSE,
;					canControl:		FALSE
;				)
;;				(theGame setCursor: waitCursor TRUE)

				(= high (CelHigh (ego view?) (ego loop?) (ego cel?)))
				(= wide (CelWide (ego view?) (ego loop?) (ego cel?)))

				(switch register
					((client north?)
						(SeeIfOffX)
						(ego y: (+ (curRoom horizon?) (ego yStep?)))
						(= cycles 1)
					)
					((client south?)
						(SeeIfOffX)
						(ego
							y:	(+ southEdge high),
							setMotion: MoveTo (ego x?) (- southEdge (* (ego yStep?) 2)) self
						)
					)
					((client east?)
						(SeeIfOffY)
						(ego
							x:	(+ eastEdge (/ wide 2)),
							setMotion: MoveTo (- eastEdge (+ (* (ego xStep?) 2) 10)) (ego y?) self
						)
					)
					((client west?)
						(SeeIfOffY)
						(ego
							x: (- westEdge (/ wide 2)),
							setMotion: MoveTo (+ westEdge (+ (* (ego xStep?) 2) 10)) (ego y?) self
						)
					)
					; for when you come into a room via teleport or if the game
					; legitimately tosses you into a room from somewhere else
					(else
						(= cycles 1)
					)
				)
			)
			(
				(ego userSpeed:)
				(HandsOn)
;				(User
;					canInput:	TRUE,
;					canControl:	TRUE
;				)
;;				(theGame setCursor: normalCursor (HaveMouse))

				(self dispose:)
			)
		)
	)
);enterRmScript


;; these guys cue if client runs into something, rather than hang the game
;
;(instance noBlkMoveTo of MoveTo
;	(properties
;		name	"nBMT"
;	)
;	(method (doit)
;		(super doit:)
;		(if (client isBlocked?)	(self moveDone:))
;	)
;)

