;**
;**	Logics for Room 400 -- QuikiWed Exterior
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Mike Larsen
;**
;**	Last Update:  June 21, 1991
;**


(script# 400)

(public
	rm400		0
)

(define pQuikiWedExterior		400)
	(define vQuikiWedExterior	400)
		(define lQuikiWedSign	0)
		(define lReflection		1)
		(define lBigFountain		2)
		(define lSmallFountain	3)
		(define lQuikiWedDoors	4)

	(define vFlasher				402)
		(define lFlasher			0)

(define	sFlasherOpening 		400)
(define	sFlasherClosing 		401)

;(define	sCitySounds				800)
;(define	sDoorOpen				801)
;(define	sDoorClose				802)
;(define	sNeonBuzz				803)



(instance rm400 of LLRoom
	(properties
		picture	400
		north		410
		west		300
		east		170
		lookStr	"You are outside Lost Wages' finest instant marriage center,
					the \"Quiki-Wed\" Wedding Chapel, another in the vast
					conglomerate that has become the fabulous \"Quiki\" empire.
					An interesting form resides beneath that light pole."
	)
	(method (init)
;		(LoadMany VIEW vFlasher vQuikiWedExterior)
		(LoadMany SOUND
			sFountain
			sFlasherOpening
			sFlasherClosing
			sDoorOpen
			sDoorClose
		)
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess,
				init:	254 137 310 137 310 145 254 145,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	0 0 319 0 319 128 258 131 207 131 196 134 162 135 92 131
						36 131 36 134 63 134 63 138 0 138,
				yourself
			)
		)
		(theMusic3
			loop:			1,
			flags:		1,
		)
		(switch prevRoomNum
			(300
				(self style: SCROLLLEFT)
			)
			(410
				(if
					(Btst fWhereDidSheGo)
					(self setScript: sWhereIsShe)
				else
					(HandsOff)
					(self setScript: sFromDoor)
				)
			)
			(170
				(ego
					init:,
					y:		130,
				)
			)
			(200
				NULL
			)
			(else		;TP
				(ego
					posn:		160 140
				)
			)
		)
		(ego
			init:		,
		)
		(flasher
			init:			,
			stopUpd:		,
			approachVerbs:						;**	Not look so get 2 messages
         	verbDo
				verbUse
				verbZipper
				verbSmellTaste
				,
		)
		(quikiSign
			cycleSpeed:	howFast,
			setCycle:	Forward,
			init:,
		)
		(billionSign 	init:)
		(reflection
			cycleSpeed:	howFast,
			setCycle:	Forward,
			init:,
		)
		(bigFountain
			cycleSpeed:	howFast,
			setCycle:	Forward,
			init:,
		)
		(smallFountain
			cycleSpeed:	howFast,
			setCycle:	Forward,
			init:,
		)
		(flasherOpenSFX init:)
		(flasherCloseSFX init:)
		(super init:)
		(fountainSFX
			init:,
			play:
		)
		(self setRegions: SIDEWALK)
		(doors
			init:		,
			stopUpd:	,
		)
	)
	(method (doit)
		(super doit: &rest)
		(cond
 			(	(and	 							;flasher flashes you
					(== (flasher cel?) 0)
					(ego inRect: 250 130 300 140)
				)
				(flasher setCycle: EndLoop flasher)
				(flasherOpenSFX play:)
			)
			(	(and
					(== (flasher cel?) 3)
					(not
						(ego inRect: 250 130 300 140)
					)
				)
				(flasher setCycle: BegLoop flasher)
				(flasherCloseSFX play:)
			)
		)
	)
)

;
; 	SCRIPTS
;

(instance sEnterDoor of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego setHeading: 0)
				(doors
					setCycle:	EndLoop self
				)
				(theMusic3
					setLoop:		1,
					number:		sDoorOpen,
					play:
				)
			)
			(
				(ego setMotion: MoveTo 217 125 self)
			)
			(
				(theMusic3
					number:		sDoorClose,
					play:
				)
				(doors setCycle: BegLoop self)
			)
			(
				(curRoom newRoom: 410)
			)
		)
	)
)

(instance sFromDoor of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(doors
					cel:			3,
				)
				(ego
					posn: 		217 125,
					setMotion:	MoveTo 217 135 self
				)
			)
			(
				(theMusic3
					number:		sDoorClose,
					play:
				)
				(doors setCycle: BegLoop self)
			)
			(
				(doors			stopUpd:)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)
(instance sWhereIsShe of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(doors
					cel:			3,
				)
				(ego
					posn: 		217 125,
					setMotion:	MoveTo 217 135 self
				)
			)
			(
				(doors setCycle: BegLoop self)
			)
			(
				(Print "\"Hey, where did she go??\" you think?" HIGH)
				(doors			stopUpd:)
				(ego
					view:			811,
					setLoop:		0,
					setCel:		10,
					setCycle:	EndLoop self
				)
			)
			(
				(NormalEgo 2)
				(= cycles 1)
			)
			(
				(Bclr fWhereDidSheGo)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)
(instance sLookFlasher of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego setHeading: (GetAngle (ego x?) (ego y?) (flasher x?) (flasher y?)) self)
			)
			(
				(= cycles 2)
			)
			(
				(if (!= (flasher cel?) 0)
					(Print "Wait, it's not a guy in a trench coat!
						It's two midgets in a trench coat!!
						And, they have nothing to hide.
						What a town!")
				else
					(Print "It's a guy wearing a trench coat.")
				)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

;*********************
;*** SOUND EFFECTS ***
;*********************

(instance fountainSFX of Sound
	(properties
		number	sFountain
		vol		127
		loop		-1
		flags		0
	)
)
(instance flasherOpenSFX of Sound
	(properties
		number	sFlasherOpening
		vol		127
		loop		1
		flags		0
	)
)
(instance flasherCloseSFX of Sound
	(properties
		number	sFlasherClosing
		vol		127
		loop		1
		flags		0
	)
)

;*************
;*** PROPS ***
;*************

(instance doors of Prop
   (properties
      x 				216
      y 				128
      z 				0
      heading 		0
      view 			vQuikiWedExterior
      loop 			lQuikiWedDoors
      cel 			0
		cycleSpeed	1
      description "the doors"
      lookStr 		"\"Through these doors walk the finest suckers in
						the world!\""
      sightAngle 	40
		signal		ignrAct
   )
	(method (cue)
		(super cue:)
		(HandsOff)
		(curRoom setScript: sEnterDoor)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(ego
					setMotion:	PolyPath 217 131 self
				)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance bigFountain of Prop
	(properties
		view		vQuikiWedExterior
		loop		lBigFountain
		cel		0
		x			78
		y			126
		detailLevel 1
 		description "the chapel fountain"
		lookStr "Between the Casino and the Quiki-Wed Chapel
					a lovely fountain gurgles gently in this good night.
					Above it, a pair of cherubs frolic in a tasteful sculpture."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "You get a quick thrill by leaning over the gurgling
					waters to grab a little feel of sculpture!")
			)
			(verbSmellTaste
				(Print "You wonder if they sell reproductions of these fountains
					in the casino's gift shop?")
			)
			(verbZipper
				(Print "Did you ever think you may drink too much fluid?")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)
(instance smallFountain of Prop
	(properties
		view		vQuikiWedExterior
		loop		lSmallFountain
		cel		0
		x			9
		y			189
		priority 15
		detailLevel 1
		signal fixPriOn
		description "the small fountain"
		lookStr 		"Most of that fountain is in the next scene."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)
(instance quikiSign of Prop
	(properties
		view		vQuikiWedExterior
		loop		lQuikiWedSign
		cel		0
		x			210
		y			40
		detailLevel 2
		description "the Quiki-Wed sign"
		lookStr 		"How romantic -- service with a smile!"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)
(instance billionSign of Feature
	(properties
		x 220
		y 128
		z 92
		heading 0
		nsLeft 170
		nsTop 8
		nsBottom 65
		nsRight 270
 		description "the Quiki-Wed sign"
		lookStr 		"How romantic -- \"Over 1 Billion Served!\""
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance reflection of Prop
	(properties
		view		vQuikiWedExterior
		loop		lReflection
		cel		0
		x			78
		y			93
		detailLevel 2
 		description "the chapel fountain"
	)
	(method (doVerb theVerb invItem)
		(bigFountain doVerb: theVerb invItem)
	)
)
(instance flasher of Person
	(properties
		view			vFlasher
		loop			lFlasher
		cel			0
		x				276
		y				143
		approachX 	251
		approachY 	131	
		description "the flasher"
		sightAngle 	40
		signal 		ignrAct
	)
	(method (cue) 
		(super cue:)
		(self stopUpd:)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(HandsOff)
				(curRoom setScript: sLookFlasher)
			)
			(verbTalk
				(if (!= (flasher cel?) 0)
					(Print "\"Say,\" you ask them. \"By any chance,
						are either of you two named `Shorty?'\""
					)
				else
					(Print "You greet the stranger in the trench	coat.")
					(Print "\"Hey, Sonny! Come over here!!\" he replies.")
				)
				(Points pTalkedToFlasher ptsTalkedToFlasher)
			)
         (verbDo
				(Print "\"Gitchur hands off'n me!\""
					#at:	-1 120
				)
				(Print "\"Gitchur hands off'n me!\""
					#at:	-1 160
				)
			)
			(verbUse
				(Printf "\"Hey, Sonny! Why don'chu jes' keep %s?\" he says,
					ungratefully."
					((Inventory at: invItem) description?))
			)
			(verbZipper
				(Print "\"Hey!\""
					#at:	-1 120
				)
				(Print "\"Who's the real weird-o around here?\""
					#at:	-1 160
				)
			)
			(verbSmellTaste
				(Print "You're sorry you asked to smell that trench coat!")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

