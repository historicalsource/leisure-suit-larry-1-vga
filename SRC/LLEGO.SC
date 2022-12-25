;;;;
;;;;	LLEGO.SC
;;;;	(c) Sierra On-Line, Inc, 1990
;;;;
;;;;	Author: Doug Oldfield
;;;;	November 13, 1990
;;;;
;;;;	An ego with a separate head.  His head only shows up when he's not
;;;;	walking
;;;;
;;;;	Classes:
;;;;		SQEgo
;;;;		Head


(script# LLEGO)


(local
	headCel = [0 4 2 5 1 7 3 6]
)

(class LLEgo kindof Ego
	(properties
		_head			0		; an object that is his head
		normal		TRUE	; if FALSE, no head - & whatever we think of later
		moveHead		TRUE	; a flag - true if you want head to move - false to stop
	)

	(methods
		headView			; changes the head view
		egoSpeed			; switches him to animation speed
		userSpeed		; switches him to user-set speed
	)

	; doing showSelf here will make sure the head gets
	; on when the view changes cuz the head doit only
	; changes when hideActor is TRUE
	(method (headView theView)
		(_head view:	theView)

		; force the head to show itself, but only if it's not currently
		; hidden
		(if (not (& (_head signal?) hideActor))
			(_head showSelf:)
		)
	)

	; so if you hide ego, his head will hide, too
	(method (hide)
		(if _head
			(_head hide:)
		)
		(super hide:)
	)

	; so if you dispose ego, his head will dispose, too
	(method (dispose)
		(if _head
			(_head dispose:)
		)
		(super dispose:)
	)

	; so if you stopUpd ego, his head will stopUpd, too
	(method (stopUpd)
		(if _head
			(_head stopUpd:)
		)
		(super stopUpd:)
	)

	(method (egoSpeed num)
		(if argc
			(self
				cycleSpeed:		num,
				moveSpeed:		num
			)
		else
			(self
				cycleSpeed:		larrySpeed,
				moveSpeed:		larrySpeed
			)
		)
	)

	(method (userSpeed)
		(self
			cycleSpeed:		(theGame egoMoveSpeed?),
			moveSpeed:		(theGame egoMoveSpeed?)
		)
	)
)


(class Head kindof Prop
	(properties
		client 		0
		_cnt			0
		_offSet		0
	)

	(methods
		lookAround
	)

	(method (init owner)
		(self
			client: 			owner,
			ignoreActors:	TRUE,
		)

		(= loop (- (NumLoops self) 1))

		(owner _head: self)
		(super init:)

		; this has to be AFTER super init cuz
		; hideActor flag gets cleared
		(self hide:,)
	)

	(method (doit)
		(cond
			;	put ego into standing mode if not walking
			((and
				; if client normal is TRUE
				(client normal?)
				; if client is hidden, don't show
				(not (& (client signal?) hideActor))
				; all this junk is just so there's no conflict with stopWalker
				(client isStopped:)
				(IsObject (client cycler?))
				((client cycler?) isKindOf: StopWalk)
				(== (client view?) ((client cycler?) vStopped?))
			 )
				; if i'm not updating
				(if (and
						(& signal notUpd)
						(not (& signal startUpdOn))
					 )
					; if ego is updating, i have to begin updating also
					(if (& (client signal?) notUpd)
						(return)
					else
						(self startUpd:)
					)
				)

				; show myself
				(self showSelf:)

				; this has to get done each doit so lookAround can run
				(self lookAround:)
			)
			(else
				; if i'm not already hidden, hide myself
				(if (not (& signal hideActor))
					(self hide:)
				)
			)
		)
		(super doit:)
	)

	(method (showSelf &tmp i)
		; 1st time thru
		(if (& signal hideActor)
			; only set cel 1st time thru cuz lookAround sets
			; cel when it wants to
			(self cel: (client loop?))

			; so we don't cycle faster than the current cycleSpeed
			(= _cnt cycleSpeed)

			; figure out which array element we're currently on
			(for ((= i 0)) (<= i 7) ((++ i))
				(if (== cel [headCel i])
					(= _offSet i)
				)
			)
		)

		(self
			setPri:	(client priority?),
			loop:		(- (NumLoops self) 1),
			x:			(client x?),
			y:			(client y?),
 			z:			(CelHigh view? (client loop?) (client cel?)),
			show:,
		)
	)

	; make him turn his head
	(method (lookAround &tmp i)
		; if client's moveHead is FALSE, don't move
		(if (client moveHead?)
			; _cnt is tied to cycleSpeed
			(if (< (-- _cnt) 1)
				(= _cnt cycleSpeed)

				; get a number: -1, 0, or 1
				(= i (+ _offSet (- (Random 0 2) 1)))

				; wrap if too small or large
				(if (> i 7)	(= i 0))
				(if (< i 0)	(= i 7))

				; set the cel
				(= cel [headCel i])
			)
		)
	)
)


