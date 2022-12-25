;**
;**	Logics for Room 500 -- QuikiMart Exterior
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Mike Larsen
;**
;**	Last Update:  June 18, 1991
;**

(script# 500)

(public
	rm500		0
	aBum		1
)


;(define	mDrunk					120)
;(define	sCitySounds				800)
;(define	sNeonBuzz				803)

(local
	aBumThreat
	bumLoop
	bumBegged
	bumVerb = 0
	bumObj  = 0
	giveBumCash
	givingWine
	ringInc = 0
	aBumPolyPts = [0 0 0 0 0 0 0 0]
)

(procedure
	MakePoly
)


(define RINGTIME 50)

(define	sPhoneRingingHere			504)
(define	sPhoneHangUpHere			507)

(define	mDrunk	120)

(define pQuikiMartExterior		500)

;(define vQuikiMartExterior		500)

(define vLarryPhoning			501)
	(define	lReachingPhone	0)
	(define	lShocked			1)
	(define	lPanting			2)

(define vBum						502)
	(define	lBumWalkingR		0)
	(define	lBumWalkingL		1)
	(define	lBumHeadR 			2)
	(define	lBumHeadL 			3)
	(define	lBumReachingR		4)	;+4 for reaching
	(define	lBumReachingL		5)
	(define	lBumBottleR			6)	;+6 for lifting bottle
	(define	lBumBottleL			7)
	(define	lBumDrinkR			8)	;+8 for drinking
	(define	lBumDrinkL			9)

(instance rm500 of LLRoom
	(properties
		picture	500
		west		170
		east		600
		north		510
		lookStr	"You are outside Lost Wages' one and only Quiki-Mart
					Convenience Store. A telephone hangs from the utility
					pole, minus its directory."
	)
	(method (init)
		(LoadMany VIEW	vLarryPhoning vLarryGetting)
		(LoadMany SOUND sPhoneHangUpHere sPhoneRingingHere)
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess,
				init:	172 149 215 149 215 152 172 152,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	319 135 270 135 258 147 81 147 81 137 129 131 319 4,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	68 140 42 140 35 112 0 112 0 0 76 0 76 136,
				yourself
			)
		)
		(fTelephone
			init:,
			approachVerbs:	actorVerbs verbLook
		)
		(fEntryLights	init:)
		(fLight 			init:)
		(street 			init:)
		(darkAlley		init:)
		(fWindow			init:)
		(fArtGallery 	init:)
		(fSign 			init:)
		(switch prevRoomNum
			(600
				(self style: SCROLLRIGHT)
				(if
					(< (ego y?) 135)
					(ego y: 135)
				)
			)
			(510
				(HandsOff)
				(curRoom setScript: sFromStore)
			)
			(505
				(ego
					moveHead:	FALSE,
					normal:		FALSE,
					view:			vLarryPhoning,
					setLoop:		lReachingPhone,
					setCel:		5,
					x:				172,
					y:				150,
				)
				(HandsOff)
				(if
					debugging
					(Bset fLarryPanting)
				)
				(if
					(Btst fLarryPanting)
					(curRoom setScript: sHot&Bothered)
				else
					(curRoom setScript: sFromTelephone)
				)
			)
			(170
				(ego y: 145)
			)
			(200
				NULL
			)
			(else		;TP
				(ego
					posn:		160 150
				)
			)
		)
		(ego
			init:,
		)
		(super init:)
		(if
			(or
				debugging
				(and
					(ObjInRoom iPocketKnife)
					(> (ego x?) 50)
					(!= prevRoomNum 505)
				)
			)
			(if (Btst fBumTgl)
				(aBum setScript: sBumBegs)
;				(Load VIEW vBum)
				(Load SOUND sDrinking mDrunk)
				(aBum
					cycleSpeed:	howFast,
					moveSpeed:	(+ 1 howFast),
					init:
				)
				(aBumHead
					cycleSpeed:	(+	3 howFast),
					init:			,
				)
			)
			(Btgl fBumTgl)
		)
		(self setRegions: SIDEWALK)
		(if
			(== debugging 2)
			(Bset fSurveyCalled)
		)
		(if
			(and
				(!= prevRoomNum 505)
				(Btst fSurveyCalled)
			)
			(sfxPhoneRinging prevSignal: 0)
			(sfxPhoneRinging play:)
			(Bset fPhoneRinging)
		)
	)
	(method (doit)
		(super doit: &rest)
		(cond
			(script)
			((< (ego y?) 125)
				(curRoom newRoom: 170)
			)
			((SteppedOn ego cBLUE)
				(HandsOff)
				(curRoom setScript: sToStore)
			)
			((== (sfxPhoneRinging prevSignal?) 1)
				(switch ringInc
					(0
						(Cls)
						(Print "\"Ring.\"" #time 3 #dispose HIGH)
					)
					(1 (Cls)
						(Print "\"Ring ring.\"" #time 3 #dispose HIGH)
					)
					(2
						(Cls)
						(Print "\"Ring ring ring.\"" #time 3 #dispose HIGH)
					)
					(3
						(Cls)
						(Print "\"Ring ring ring ring.\"" #time 3 #dispose HIGH)
					)
					(4	(Cls)
						(Print "How long are we going to do this before you
							answer the phone, Larry?!" #time 4 #dispose HIGH))
				)
				(sfxPhoneRinging prevSignal: 0)
				(if 
					(== ringInc 8)
					(= ringInc 4)
				else
					(++ ringInc)
				)
			)
		)
	)
	(method (dispose)
		(theMusic fade:)
		(super dispose:)
	)
)

;
;	SCRIPTS
;

(instance sFromStore of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					posn:			99 131,
					setMotion:	MoveTo 82 138 self
 				)
			)
			(
				(ego
					setMotion:	MoveTo 79 148 self
 				)
			)
			(
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sToStore of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego setMotion MoveTo 85 134 self)
			)
			(
				(ego setMotion: PolyPath 118 130 self)
			)
			(
				(curRoom newRoom: 510)
			)
		)
	)
)

(instance sBumBegs of Script
	(method (doit)
		(super doit: &rest)
		(cond
			(	(and
					(== state 1)
					(< (aBum distanceTo: ego) 45)
				)
				(= cycles 1)
			)
			(	
				(or
					(and
						(== state 4)
						(ego mover?)
					)
					(== (curRoom script?) (ScriptID LLROOM 1))
					(== (ego view?) vLarryFlattened)
				)
				(= state 7)
			)
		)
	)
	(method (changeState newState)
		(switchto (= state newState)
			(;0
				(aBum
					setCycle:	Walk,
					setMotion:	MoveTo 0 158 self
				)
			)
			(;1
				(theMusic
					number:		mDrunk,
					setLoop:		1,
					flags:		1,
					play			127,
				)
				(aBum
					setCycle:	Walk,
					setMotion:	MoveTo 160 158 self
				)
			)
			(;2
				(HandsOff)
				(Face ego aBum)
				(aBum setMotion: 0)
				(= cycles 1)
			)
			(;3
				(= bumBegged TRUE)	
				(aBum
					setMotion:	0,
					setLoop:		lBumReachingR,
					setCycle:	EndLoop
				)
				(aBumHead
					cycleSpeed:	(+ 3 howFast),
					setCycle:	RandCycle 7 self
				)
			)
			(;4
				(if
					(not (curRoom script?))
					(HandsOn)
				)
				(Print "\"Got any sshhpare change, buddy?\""
					#time: 5
				)
				(= seconds 5)
			)
			(;5
				(aBumHead
					setCycle:	RandCycle 10 self
				)
			)
			(;6
				(Print "\"Well, ok. Ya got any sshhpare wine, then?\""
					#time 5
				)
				(= seconds 5)
			)
			(;7
				(aBum
					setLoop:		lBumReachingR,
					setCycle:	BegLoop
				)
				(aBumHead
					setCycle:	RandCycle 10 self
				)
			)
			(;8
				(Print "\"Al'right. Be that way!\""
					#time: 5
				)
				(= seconds 5)
			)
			(;9
				(aBum setScript: sBumLeaves)
			)
		)
	)
)

(instance sGetKnife of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(if
					(and
						(<= (aBum x?) 60)
						(== (aBum loop?) lBumWalkingL)
					)
					(Print "He's headed for that dark alley, Larry maybe you
								  should	just catch him next time."
					)
					(self dispose:)
				)
				(= cycles 1)
			)
			(
				(HandsOff)
				(cond
					((<= (ego x?) (aBum x?))
						(= bumLoop 1)
						(aBum
							view:			vBum,
							setMotion:	0,
							setLoop:		lBumWalkingL
						)
					)
					((> (ego x?) (aBum x?))
						(aBum
							view:			vBum,
							setMotion:	0,
							setLoop:		lBumWalkingR
						)
					)
				)
				(if
					(> (aBum distanceTo: ego) 45)
					(if
 						(<= (ego x?) (aBum x?))
						(ego
							setMotion:	PolyPath (- (aBum x?) 45) (aBum y?) self
						)
					else
						(ego
							setMotion:	PolyPath (+ (aBum x?) 45) (aBum y?) self
						)

					)
				else
					(= cycles 1)
				)
			)
			(
				(Face ego aBum)
				(= cycles 3)
			)
			(
				(ego put: iWine)
				(aBum
					setLoop:		(+ 4 (mod (aBum loop?) 2)),		;reaching
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(
				(aBum
					setLoop:		(+ 6 (mod (aBum loop?) 2)),		;bottle
					setCycle:	EndLoop self
				)
			)
			(
				(sfxGlug	play: self)
				(Print "\"Glug, glug, glug, glug, glug, glug, glug, etc.\""
					HIGH
					#dispose:
				)
				(aBum
					setLoop:		(+ 8 (mod (aBum loop?) 2)),		;drinking
					setCycle:	ForwardCounter
				)
			)
			(
				(Cls)
				(aBum
					setLoop:		(+ 6 (mod (aBum loop?) 2)),		;bottle
					setCel:		3,
					setCycle:	BegLoop self
				)
			)
			(
				(aBum setLoop: bumLoop)
				(= cycles 1)
			)
			(
				(aBum
					setLoop:		(+ 4 (mod (aBum loop?) 2)),		;reaching
					setCel:		0,
				)
				(= cycles 1)
			)
			(
				(aBumHead
					cycleSpeed:	(+ 4 howFast),
					setCycle:	RandCycle 20 self
				)
			)
			(
				(Print "\"I ain't got no remote control, but, sssshay,
					I'll tell ya what: here'ssh my trussshty ol' pocket knife.
					Ya never know!
					There are sssshome kinky girlssh in this town.
					Sssshexy hunkssh like ussh might have to defend
					oursshelves sometime!\""
				)
				(= cycles 1)
			)
			(
				(aBum
					setCycle:	BegLoop self,
				)
			)
			(
				(ego get: iPocketKnife)
				(Points pGotKnife	ptsGotKnife)
				(Print "With that, he \"sssshtumbles\" off into the darkness.")
				(= cycles 1)
			)
			(
				(if
					(not (curRoom script?))
					(HandsOn)
				)
				(aBum setScript: sBumLeaves)
			)
		)
	)
)

(instance sBumInteraction of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(if
					(and
						(<= (aBum x?) 60)
						(== (aBum loop?) lBumWalkingL)
					)
					(Print "He's headed for that dark alley, Larry maybe you
								  should	just catch him next time."
					)
					(self dispose:)
				)
				(= cycles 1)
			)
			(
				(HandsOff)
				(aBum setMotion:	0)
				(if
					(> (aBum distanceTo: ego) 45)
					(if
 						(<= (ego x?) (aBum x?))
						(ego
							setMotion:	PolyPath (- (aBum x?) 45) (aBum y?) self
						)
					else
						(ego
							setMotion:	PolyPath (+ (aBum x?) 45) (aBum y?) self
						)

					)
				else
					(= cycles 1)
				)
			)
			(
				(aBum
					setLoop:		(+ 4 (mod (aBum loop?) 2)),		;reaching
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(
				;*** This switch is for Larry's comments
				(if
					(== bumVerb TRUE)
					(switch register
						(verbSmellTaste
							(Print "Cheezzzz! This guy could make that dog smell refreshing!")
						)
						(verbDo
							(Print "You search the bum diligently and find nothing but
								a strong aroma, which you allow him to keep!"
							)
						)
					)
				)
				(self cue:)
			)
			(
				(if
					(== bumObj TRUE)
					(switch register
						(iRing
							(Print "\"Here ya go, pal. Take my diamond ring!
								A guy like me doesn't need commitment!\"")
						)
						(iRemoteControl
							(Print "\"I got this from a fella that reminds me of you,\"
								you say to the bum.")
						)
						(iRose
							(Print "\"Peace, baby!\" you say,
								handing the flower to the bum.")
						)
						(iCandy
							(Print "\"Want a piece?\" you say.")
						)
						(iMagazine
							(Print "\"Have you seen the kind of filth they're selling
								inside this convenience store!\" you state, indignantly.
								\"Why, it's an outrage!\"")
						)
						(iPills
							(Print "\"Take this bottle of pills,\" you tell the bum.
								\"I'm sure I'll have no use for any
								aphrodisiacs this evening.
								After all, I only hung on the side of a fire escape
								dangling from a velveteen ribbon to get these babies!\"")
						)
						(iWhiskey
							(Print "Here take a swig of this gramps.")
						)
					)
				)
				(self cue:)
			)
			(
				(if
					(OneOf register verbDo verbTalk verbZipper verbSmellTaste
						iRemoteControl	iRose iCandy iMagazine iPills iRing iWhiskey
					)
					(aBum
						setLoop:		(+ 4 (mod (aBum loop?) 2)),		;reaching
						setCycle:	BegLoop,
					)
					(aBumHead
						init:			,
						cycleSpeed:	(+ 3 howFast),
						setCycle:	RandCycle 15 self
					)
				else
					(self cue:)
				)
			)
			(
				;*** This switch is for bum's response, (if any).
				(if
					(== bumVerb TRUE)
					(switch register
						(verbDo
							(Print "\"Ssshay, buddy,\" grins the bum,
								\"I kinda enjoyed that!\"")
						)
						(verbTalk
			  				(Print "\"Howsha 'bout a drink, Shonney?\"")
						)
						(verbZipper
							(Print "\"HEY! I may be down, but I ain't THAT out!\"")
						)
					)
				)
				(self cue:)
			)
			(
				(if
					(== bumObj TRUE)
					(switch register
						(iRemoteControl
							(ego put: iRemoteControl)
							(Print "\"Hey, thankshh, Misshter.
								Now I can go visshit that hooker!\"")
						)
						(iRose
							(ego put: iRose)
							(Print "\"Make love, not war!\" he replies,
								pocketing your rose.")
						)
						(iCandy
							(ego put: iCandy)
							(Print "\"I'm trying to cut back,\" the bum replies.")
						)
						(iMagazine
							(ego put: iMagazine)
							(Print "\"Yeah. I'll give it further ssshtudy back
								at my placessh.\"")
						)
						(iPills
							(ego put: iPills)
							(Print "\"Thanks. How droll.\"")
						)
						(iRing
 							(ego put: iRing)
							(Print "\"Thankshh
								Does thissh mean we're engashhged?\"")
						)
						(iWhiskey
							(ego put: iWhiskey)
							(Print "\"Burp. Thanks, I needed that! See ya 'round!!\"")
						)
					)
				)
				(self cue:)
			)
			(
				(= bumVerb FALSE)
				(= bumObj FALSE)
				(aBum setScript: sBumLeaves)
			)
		)
	)
)


(instance sBumLeaves of Script
	(method (doit)
		(super doit: &rest)
		(if
			(and
				(== state 2)
				(!= aBumThreat	TRUE)
				(aBum isBlocked: TRUE)
			)
			(= aBumThreat TRUE)
			(Print "\"Outta my way shhonney, I know marssshhal artshhh!\"")
		)
	)
	(method (changeState newState)
		(switchto (= state newState)
			(0
				(if
					(not (curRoom script?))
					(HandsOn)
				)
				(= seconds 1)
			)
			(1
				(if
					(and
						(OneOf (aBum loop?) lBumReachingR lBumReachingL)
						(!= (aBum cel?) 0)
					)
					(aBum
						setCycle:	BegLoop self
					)
				else
					(= cycles 1)
				)
			)
			(2
				(aBum
					setLoop:		lBumWalkingL,
					setCycle:	Walk,
					setMotion:	PolyPath -28 155 self
				)
			)
			(3
				(aBum dispose:)
				(theMusic fade:)
				(self dispose:)
			)
		)
	)
)

(instance sHot&Bothered of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= cycles 5)
			)
			(
				(ego
					egoSpeed:	(* 4 larrySpeed),
					view:			vLarryPhoning,
					setLoop:		lShocked,
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(
				(ego
					setLoop:		lPanting,
					setCycle:	ForwardCounter 10 self
				)
			)
			(
				(ego
					view:			vLarryPhoning,
					setLoop:		lReachingPhone,
					setCel:		5,
					setCycle:	CycleTo 2 1 self
				)
			)
			(
				(sfxHangUpHere play:)
				(ego setCycle: BegLoop self)
			)
			(
				(Print "You hang up the telephone and wipe the sweat from
					your hands!" HIGH)
				(HandsOn)
				(Bclr fLarryPanting)
				(ego normal: TRUE)
				(NormalEgo 0)
				(self dispose:)
			)
		)
	)
)



(instance sFromTelephone of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= cycles 5)
			)
			(
				(ego moveHead:	FALSE)
				(ego
					egoSpeed:	(* 3 larrySpeed),
					view:			vLarryPhoning,
					setLoop:		lReachingPhone,
					setCel:		5,
					setCycle:	CycleTo 2 -1 self
				)
			)
			(
				(sfxHangUpHere play:)
				(ego
					setCycle:	BegLoop self
				)
			)
			(
				(HandsOn)
				(ego normal: TRUE)
				(NormalEgo 0)
				(self dispose:)
			)
		)
	)
)
(instance sToTelephone of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= cycles 5)
			)
			(
				(ego moveHead:	FALSE)
				(ego
					egoSpeed:	(* 3 larrySpeed),
					view:			vLarryPhoning,
					setLoop:		lReachingPhone,
					setCel:		0,
					setCycle:	CycleTo 2 1 self
				)
			)
			(
				(if
					(Btst fPhoneRinging)
					(sfxPhoneRinging stop:)
				)
				(sfxHangUpHere play:)
				(ego
					setCycle:	EndLoop self
				)
			)
			(
				(if
					(Btst fPhoneRinging)
					(Points pAnsweredPhone ptsAnsweredPhone)
				)
				(curRoom newRoom: 505)
			)
		)
	)
)



;**************
;*** SOUNDS	***
;**************

(instance sfxGlug of Sound
	(properties
		number	sDrinking
		vol		127
		loop		1
		flags		1
	)
)
(instance sfxHangUpHere of Sound
	(properties
		number		sPhoneHangUpHere
		loop			1
		vol			127
		flags			1
	)
)

;****************
;*** FEATURES ***
;****************

(instance fEntryLights of Feature
	(properties
		x				 48
		y				 69
		z				 0
		heading		 0
		nsLeft		 31
		nsTop			 62
		nsBottom		 77
		nsRight		 66
 		description	"the lights"
		lookStr		"The lights above the doorway illuminate the
						entrance to the Quiki-Mart store."
		sightAngle	 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance darkAlley of Feature
	(properties
		x 31
		y 68
		z 0
		heading 0
		nsLeft 0
		nsTop -1
		nsBottom 138
		nsRight 63
 		description "the dark alley"
		lookStr 		"You're not sure, but you think you hear cries of \"Help\"
						coming from that dark alley over there."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbTalk
				(Print "\"Hello!\" you cry into the darkness,
					\"is there anybody back there?\"")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance street of Feature
	(properties
		x 159
		y 175
		z 0
		heading 0
		nsLeft 0
		nsTop 161
		nsBottom 189
		nsRight 319
 		description "the street"
		lookStr 		"When you look hard enough, you think you can just barely
						make out the outline of a car speeding toward you."
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
(instance fWindow of Feature
	(properties
		x 159
		y 79
		z 0
		heading 0
		nsLeft 98
		nsTop 50
		nsBottom 108
		nsRight 220
 		description "the window"
		lookStr 		"This window looks perfectly rectangular -- to you!"
		sightAngle 40
		approachX	170
		approachY	120
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "It does feel like glass.")
			)
			(verbTalk
				(Print "They could hear you better if you went
					inside the store.")
			)
			(verbUse
				(switch invItem
					(iRing
						(Print "You swipe the diamond ring across the
							Quiki-Mart window, leaving a giant scratch in the
							glass.  You hope you were not observed!")
					)
					(else
						(super doVerb: theVerb invItem)
					)
				)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance fSign of Feature
   (properties
      x 130
      y 146
      z 125
      heading 0
      nsLeft 22
      nsTop -1
      nsBottom 44
      nsRight 239
      description "the sign"
      lookStr 		"This sign indicates you are currently in front of the
						lovely Lost Wages Quiki-Mart."
      sightAngle 40
   )
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "That sign is too high for you to reach.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance fTelephone of Feature
	(properties
		x 186
		y 150		;111
		z 39		;0
		heading 0
		nsLeft 178
		nsTop 87
		nsBottom 135
		nsRight 194
 		description "the telephone"
		sightAngle 	40
		approachX	172
		approachY	150
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Points pReadTelephone ptsReadTelephone)
				(Print "There are some numbers scribbled on the side of the
					telephone. The only thing legible (and printable) is:
					\"555-6969.\""
				)
			)
			(verbDo
				(HandsOff)
				(curRoom setScript: sToTelephone)
			)
			(verbTalk
				(self doVerb: verbDo)
			)
			(verbUse
				(switch invItem
					(iWallet
						(self doVerb: verbDo)
					)
					(else
						(super doVerb: theVerb invItem &rest)
					)
				)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance fLight of Feature
   (properties
      x 171
      y 154
      z 107
      heading 0
      nsLeft 163
      nsTop 40
      nsBottom 55
      nsRight 180
      description "the light"
      lookStr 		"The street light casts its yellow glow across the area,
						thoughtfully illuminating the many pieces of trash that
						line this thoroughfare."
      sightAngle 40
   )
)
(instance fArtGallery of Feature
   (properties
      x 286
      y 89
      z 0
      heading 0
      nsLeft 254
      nsTop 47
      nsBottom 131
      nsRight 319
      description "the art gallery"
      lookStr 		"The Lost Wages Art Emporium will satisfy your every need
						for modern art, as long as you're looking for something
						with dead fish!"
      sightAngle 40
   )
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "The locked gate effectively prevents you from any
					cultural enlightenment by entering the Art Gallery.")
			)
			(verbTalk
				(Print "You have no wish to talk to a bunch of paintings.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance aBumHead of Prop
	(properties
		view	vBum
		z		1000
		description "the bum"
		lookStr 		"The bum looks like a man that could (ab)use a drink."
		signal ignrAct
	)
	(method (doVerb theVerb invItem)
		(aBum doVerb: theVerb invItem)
	)
	(method (doit:)
		(super doit:)
		(if
			(OneOf (aBum loop?) lBumReachingR lBumReachingL)
			(aBumHead z: 22)
		else
			(aBumHead z: 1000)
		)
		(cond
			((== (aBum loop?) lBumReachingR)
				(aBumHead
					loop:	lBumHeadR,
					x:		(+ (aBum x?) 10),
					y:		(aBum y?),
				)
			)
			((== (aBum loop?) lBumReachingL)
				(aBumHead
					loop:	lBumHeadL,
					x:		(- (aBum x?) 10),
					y:		(aBum y?),
				)
			)
		)
	)
)

(instance aBum of Person
	(properties
		view			vBum
		loop			lBumWalkingR
		x				-60
		y				154
		z				0
		xStep			2
		yStep			1
		description	"the bum"
		lookStr 		"The bum looks like a man that could (ab)use a drink."
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(= bumVerb TRUE)
				(aBum setScript: sBumInteraction NULL theVerb)
			)
			(verbTalk
				(= bumVerb TRUE)
				(aBum setScript: sBumInteraction NULL theVerb)
			)
			(verbZipper
				(= bumVerb TRUE)
				(aBum setScript: sBumInteraction NULL theVerb)
			)
			(verbSmellTaste
				(= bumVerb TRUE)
				(aBum setScript: sBumInteraction NULL theVerb)
			)
			(verbUse
				(= bumObj TRUE)
				(switch invItem
					(iWine
						(aBum setScript: 	sGetKnife)
					)
					(iWhiskey
						(aBum setScript: sBumInteraction NULL invItem)
					)
					(iWallet
						(Print "You fish a dollar out of your wallet
							and hand it to the bum.
							\"Gossshhh thanssskks, buddy,\" he says sarcastically.
							\"With all thisssh money,
							maybe I can finally invessst in sssome sssmall company,
							growth mutual fundsssh!\""
						)
						(-= larryDollars 1)
					)
					(iBreathSpray
						(Print "Although the bum IS in need of an
							industrial-strength deodorant, your breath spray
							doesn't have enough horsepower to kill that aroma!")
					)
					(iWatch
						(Print "\"Say, would you like a watch?\" you yell to the
							bum.")
						(Print "\"No,\" he responds, \"would you like TO watch?\"")
					)
					(iApple
						(Printf "useVerb hit, with invItem = %d" invItem)
						(Print "\"An apple a day keeps the doctor away!\" you
							pontificate.")
						(Print "\"A gallon a day keeps the pink elephants away,\"
							he mutters.")
					)
					(iRing
						(aBum setScript: sBumInteraction NULL invItem)
					)
					(iRemoteControl
						(aBum setScript: sBumInteraction NULL invItem)
					)
					(iRose
						(aBum setScript: sBumInteraction NULL invItem)
					)
					(iLubber
						(Print "No, Larry. You'd better keep that for yourself.")
					)
					(iCandy
						(aBum setScript: sBumInteraction NULL invItem)
					)
					(iMagazine
						(aBum setScript: sBumInteraction NULL invItem)
					)
					(iPills
						(aBum setScript: sBumInteraction NULL invItem)
					)
					(else
						(Printf "That bum has no need for %s."
							((Inventory at: invItem) description?))
					)
				)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance sfxPhoneRinging of Sound
	(properties
		number		sPhoneRingingHere
		loop			-1
		flags			1
	)
)


(instance aBumPoly of Polygon
	(properties
		type: 	PBarredAccess
	)
)

(procedure (MakePoly &tmp rL rT rR rB)
	(= rL (- (aBum brLeft?) 5))
	(= rT (- (aBum brTop?) 5))
	(= rR (+ (aBum brRight?) 6))
	(= rB (aBum brBottom?))

	(= [aBumPolyPts 0] (= [aBumPolyPts 6] rL))
	(= [aBumPolyPts 1] (= [aBumPolyPts 3] rT))
	(= [aBumPolyPts 2] (= [aBumPolyPts 4] rR))
	(= [aBumPolyPts 5] (= [aBumPolyPts 7] rB))
	(aBumPoly points: @aBumPolyPts, size: 4)
	(curRoom addObstacle: aBumPoly)
)
