;**
;**	Logics for Room 385 -- Eve Closeup
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Mike Larsen
;**
;**	Last Update:  June 19, 1991
;**


(script#		385)

(public
	rm385		0
)

(local
	talked = 0
	looked = 0
	lookDirection
	randRegister
)

(procedure
	JetsOn
	JetsOff
)

(define eyesX 159)
;(define eyesY 48)
(define eyesY 192)
(define eyesZ 144)
(define mouthX 156)
(define mouthY 68)


(define	pEveCloseup				385)

	(define	vEveCloseup			385)
		(define	lRightBlink		0)
		(define	lLeftBlink		1)
		(define	lAngryEyes		2)
		(define	lEveSmile		3)
		(define	lEveFrown		4)
		(define	lKiss				5)
		(define	lLick				6)
		(define	lLookRight		7)
		(define	lLookLeft		8)

	(define	vTubBubbles			386)
		(define	lChestBubbles	0)
		(define	lSmallBubble	1)
		(define	lLeftRim			3)
		(define	lButton 			4)

	(define	vEvesArm				387)
		(define	lEmptyHand		0)
		(define	lHandWithApple	1)


(instance rm385 of Room
	(properties
		picture		385
	)
	(method (init)
		(LoadMany VIEW
			vEveCloseup
			vTubBubbles
			vEvesArm
		)
		(herEyes
			init:		,
			cycleSpeed:	(+ 1 howFast),
		)
		(eyeRight
			init:		,
			cycleSpeed:	(+ 1 howFast),
		)
		(eyeLeft
			init:		,
			cycleSpeed:	(+ 1 howFast),
		)
		(herMouth
			init:		,
			cycleSpeed:	(+ 1 howFast),
		)
		(fEveHead 	init:)
		(fBoobs 		init:)
		(fEveArms 	init:)
		(fTowel 		init:)
		(ego
			init:,
			normal:	FALSE,
			z:			1000
		)
		(aSpaButton
			init:,
			stopUpd:,
		)
		(super		init:)
		(curRoom setScript: sWanderEyes)
		(if (Btst fJetsOff)
			(JetsOff)
		else
			(JetsOn)
		)
		(theMusic
			send:		4 mMUTE	FALSE,
			send:		9 mMUTE	FALSE,
			send:		5 mMUTE	TRUE,
		)
	)
	(method (doit)
		(super doit: &rest)
		(if
			(>= randRegister 5)
			(= lookDirection 1)
		else
			(= lookDirection 0)
		)
		(cond
			((== (curRoom script?) 0)
				(curRoom setScript: sWanderEyes)
			)
			((ego mover?)
				(curRoom newRoom: 380)
			)
		)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(switch (++ looked)
					(1
						(Print "Wow!
							Just the way you like your women: nearby and naked!
							She has a beautiful face and a gorgeous body.
							You've waited a lifetime for this moment." HIGH)
					)
					(2
						(Print "What a beautiful face!
							She's leaning back, relaxing in the spa with
							the water bubbling up around her.
							It's even possible you could become
							sexually aroused by this!" HIGH)
					)
					(else
						(Print "It's difficult to take your eyes off the beautiful
							woman with whom you share the hot tub.")
						(Print "And her eyes tell you she's interested in you,
							too, Larry!")
					)
				)
				(if (!= (curRoom script?) sEveHappy)
					(curRoom setScript: sEveHappy)
				)
				(return TRUE)
			)
			(verbDo
				(Print "You feel certain she likes that.")
				(if (!= (curRoom script?) sEveHappy)
					(curRoom setScript: sEveHappy)
				)
				(return TRUE)
			)
			(verbTalk
				(switch (++ talked)
					(1
						(Print "\"Who loves ya, baby?\"" HIGH)
						(Print "She says nothing, but her smile tells you bushels."
							HIGH)
					)
					(2
						(Print "\"Have you ever tried it underwater?\"
							you coyly ask." HIGH)
						(Print "She doesn't say." HIGH)
					)
					(3
						(Print "\"What's your name, gorgeous?\" you ask." HIGH)
						(Print "She says nothing, but her smile speaks volumes."
							HIGH)
					)
					(4
						(Print "\"What's your sign, dollface?\" you ask." HIGH)
						(Print "At last she speaks. \"Yield,\" she says simply."
							HIGH)
					)
					(else
						(Print "Although she smiles,
							it appears she's a woman of few words.
							She's waiting for you to determine what
							it is she wants!" HIGH)
					)
				)
				(if (!= (curRoom script?) sEveHappy)
					(curRoom setScript: sEveHappy)
				)
				(return TRUE)
			)
			(verbSmellTaste
				(Print "\"You smell so good, Baby,\" you tell her.
					\"What is that perfume you're wearing?\"")
				(Print "\"`Apple Blossom,'\" she smiles.")
				(if (!= (curRoom script?) sEveHappy)
					(curRoom setScript: sEveHappy)
				)
				(return TRUE)
			)
			(verbZipper
				(Print "SLAP!!!")
				(Print "(Oops!)" LOW)
				(if (!= (curRoom script?) sEveAngry)
					(curRoom setScript:	sEveAngry)
				)
				(return TRUE)
			)
			(verbUse
				(switch invItem
					(iWatch
						(Print "\"Now's the time, Larry!\" she says.")
						(curRoom setScript:	sEveAngry)
					)
					(iApple
						(HandsOff)
						(curRoom setScript:	sEveEatsApple)
						(Points pGaveApple ptsGaveApple)
						(Bset fEveHasApple)
					)
					(iDiscoPass
						(Print "\"Why, Larry,\" she smiles,
							\"are you trying to throw a `pass' at me?\"")
						(curRoom setScript:	sEveAngry)
					)
					(iWhiskey
						(if (!= (curRoom script?) sEveAngry)
							(curRoom setScript:	sEveAngry)
						)
						(Print "\"How's about a shot of Lefty's cheap well whiskey?\"
							you ask her.")
						(Print "She ignores you completely.
							Obviously this woman is used to the finer things
							in life.")
					)
					(iRemoteControl
						(Print "A few infrared rays won't help you here, Larry.")
					)
					(iLubber
						(if (!= (curRoom script?) sEveHappy)
							(curRoom setScript:	sEveHappy)
						)
						(Print "\"That might be useful to you later,\" she says
							with a smile.")
					)
					(iHammer
						(Print "That's not the proper \"tool\" to use with
							this lady, Larry!")
					)
					(iPocketKnife
						(if (!= (curRoom script?) sEveAngry)
							(curRoom setScript:	sEveAngry)
						)
						(Print "Bad idea, Larry. Threatening her will do no good!")
					)
					(else
						(Print "Eve is mostly interested in just one, sweet thing.")
						(Print "(And that's not it!)" LOW)
					)
				)
				(return TRUE)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)
(instance sWanderEyes of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= randRegister (Random 0 10))
				(herMouth stopUpd:)
				(= seconds (Random 4 10))
			)
			(
				(cond
					((== lookDirection 0)
						(herEyes
							init:			,
							setLoop:		lLookLeft,
							setCel:		0,
							setCycle:	EndLoop
						)
					)
					((== lookDirection	1)
						(herEyes
							init:			,
							setLoop:		lLookRight,
							setCel:		0,
							setCycle:	EndLoop
						)
					)
				)
				(= seconds (Random 2 6))
			)
			(
				(= randRegister (Random 0 10))
				(herEyes
					setCycle:	CycleTo 0 -1
				)
				(= seconds (Random 2 6))
			)
			(
				(cond
					((== lookDirection 1)
						(herEyes
							init:			,
							setLoop:		lLookLeft,
							setCel:		0,
							setCycle:	EndLoop
						)
					)
					((== lookDirection	0)
						(herEyes
							init:			,
							setLoop:		lLookRight,
							setCel:		0,
							setCycle:	EndLoop
						)
					)
				)
				(= seconds (Random 2 6))
			)
			(
				(herEyes
					setCycle:	CycleTo 0 -1
				)
				(= seconds (Random 2 6))
			)
			(
				(self init:)
			)
		)
	)
)
(instance sEveAngry of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(herEyes
					setLoop:		lAngryEyes,
					setCel:		0,
					setCycle:	EndLoop
				)
				(herMouth
					setLoop:		lEveFrown,
					setCel:		0,
					setCycle:	EndLoop ,
				)
				(= seconds (Random 4 9))
			)
			(
				(herEyes
					setCycle:	BegLoop self,
				)
				(herMouth
					setCycle:	BegLoop self,
				)
			)
			(
				NULL
			)
			(
				(self dispose:)
			)
		)
	)
)
(instance sWink&Pucker of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(eyeLeft
					setLoop:		lLeftBlink,
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(
				(herMouth
					setLoop:		lKiss,
					setCel:		0,
					setCycle:	EndLoop
				)
				(eyeLeft
					setCycle:	BegLoop
				)
				(= seconds 2)
			)
			(
				(herMouth
					setCycle:	BegLoop self
				)
			)
			(
				(self dispose:)
			)
		)
	)
)



(instance sEveHappy of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(herEyes
					init:			,
					setLoop:		lLeftBlink,
					setCel:		0,
					setCycle:	BegLoop
				)
				(herMouth
					setLoop:		lEveSmile,
					setCel:		0,
					setCycle:	EndLoop
				)
				(= seconds (Random 4 9))
			)
			(
				(herMouth
					setCycle:	BegLoop self
				)
			)
			(
				(self dispose:)
			)
		)
	)
)
(instance sEveEatsApple of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(theMusic
					send:		6 mMUTE FALSE,
				)
				(herEyes	dispose:)
				(herMouth
					setLoop:		lEveSmile,
					setCel:		0,
					setCycle:	EndLoop
				)
				(= seconds 3)
			)
			(
			 	(Print "Eve takes your apple, and with a sweet, innocent look,
					raises it to her mouth.
					She takes a small bite,
					then lowers her arm as a sexy smile comes across her face.
					She winks, then, almost imperceptibly,
					she lies back slightly, sliding deeper into the warm water,
					and chews delicately.")		
				(ego put: iApple)
				(= seconds 3)
			)
			(
				(Print "Eve gently slips her sensuous tongue around and around
					the lucky apple.
					Tiny drops of the apple's juice glistening on her lips.
					She's really turning you on!")
				(Print "(You had no idea fruit could be so exciting.)" LOW)
				(= seconds 3)
			)
			(
				(Print "After a few bites of your apple,
					Eve slowly slides out of the hot tub,
					her steamy, naked body glistening in the moonlight.
					Barely covering her tremendous assets with the towel,
					she tosses you a towel,
					smiles and motions for you to follow her." HIGH)
				(curRoom newRoom: 380)
			)
		)
	)
)
(instance sButton of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(aSpaButton
					setCel:		0,
					setCycle:	EndLoop self,
				)
			)
			(
				(if (Btst fJetsOff)
					(Print "You slyly brush the spa bubbler control switch.")
					(JetsOff)
				else
					(Print "Because you are such a wimp,
						you turn the bubblers back on.")
					(JetsOn)
				)
				(aSpaButton
					setCycle:	BegLoop self,
				)
			)
			(
				(aSpaButton stopUpd:)
				(if (!= (curRoom script?) sEveHappy)
					(curRoom setScript:	sEveHappy)
				)
				(self dispose:)
			)
		)
	)
)



;******************
;*** PROCEDURES ***
;******************

(procedure (JetsOn)
	(chestBubbles
		init:,
		cycleSpeed:	howFast,
		setCycle:	Forward
	)
	(leftRim
		init:,
		cycleSpeed:	howFast,
		setCycle:	Forward
	)
	(bubble1
		init:,
		cycleSpeed:	howFast,
		setCycle:	Forward
	)
	(bubble2
		init:,
		cycleSpeed:	howFast,
		setCycle:	Forward
	)
	(bubble3
		init:,
		cycleSpeed:	howFast,
		setCycle:	Forward
	)
	(smallBubble
		init:,
		cycleSpeed:	howFast,
		setCycle:	Forward
	)
)

(procedure (JetsOff)
	(chestBubbles	dispose:)
	(leftRim			dispose:)
	(bubble1			dispose:)
	(bubble2			dispose:)
	(bubble3			dispose:)
	(smallBubble	dispose:)
)


;************
;*** PROP ***
;************

(instance herEyes of Prop
	(properties
		view			vEveCloseup
		loop			lLookLeft
		cel			0
		x				eyesX
		y				eyesY
		z				eyesZ
		cycleSpeed	2
		description "her eyes"
		lookStr		"Her eyes are a deep loamy brown."
		sightAngle	180
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "You gently brush your fingertips across her lids.")
			)
			(else
				(curRoom doVerb: theVerb invItem)
			)
		)
	)
)
(instance eyeLeft of Prop
	(properties
		view			vEveCloseup
		loop			lLeftBlink
		cel			0
		x				eyesX
		y				eyesY
		z				eyesZ
		description "her eyes"
		lookStr		"Her eyes are a deep loamy brown."
		sightAngle	180
	)
	(method (doVerb theVerb invItem)
		(herEyes doVerb: theVerb invItem)
	)
)
(instance eyeRight of Prop
	(properties
		view			vEveCloseup
		loop			lRightBlink
		cel			0
		x				eyesX
		y				eyesY
		z				eyesZ
		cycleSpeed	2
		description "her eyes"
		lookStr		"Her eyes are a deep loamy brown."
		sightAngle	180
	)
	(method (doVerb theVerb invItem)
		(herEyes doVerb: theVerb invItem)
	)
)
(instance herMouth of Prop
	(properties
		view		vEveCloseup
		loop		lEveSmile
		cel		0
		x			mouthX
		y			mouthY
		description "her mouth"
		lookStr		"Her mouth drives you crazy."
		sightAngle	180
	)
	(method (doVerb theVerb invItem)
 		(curRoom doVerb: theVerb invItem)
	)
)

(instance aSpaButton of Prop
	(properties
		view			vTubBubbles
		loop			lButton
		x 				310
		y 				189
		z 				85
 		description "the button"
		lookStr 		"There's a control button built into the
						surface of the spa's decking."
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(self setScript: sButton)
				(sButton init:)
				(Btgl fJetsOff)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance chestBubbles of Prop
	(properties
		view			vTubBubbles
		loop			lChestBubbles
		cel			0
		x				119
		y				154
		description "the bubbles"
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "How you wish there was a switch to turn off those
					bubbles!")
			)
			(verbDo
				(Print "It feels good to run your hand through the bubbles.")
			)
			(verbTalk
				(Print "\"Buubbbbbbffeffosss,\" you say,
					attempting to speak underwater.")
			)
			(verbZipper
				(Print "Nah, do that when you're at home alone!")
			)
			(verbSmellTaste
				(Print "There's a whiff of chlorine in the air.")
			)
			(verbUse
				(Print "There's no need to rinse off any of your inventory
					in the water.")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)
(instance leftRim of Prop
	(properties
		view 			vTubBubbles
		loop			lLeftRim
		cel			1
		x				40
		y				134
		description "the bubbles"
	)
	(method (doVerb theVerb invItem)
		(chestBubbles doVerb: theVerb invItem)
	)
)
(instance smallBubble of Prop
	(properties
		view 			vTubBubbles
		loop	 		lSmallBubble
		cel			2
		x				264
		y				173
		description "the bubbles"
	)
	(method (doVerb theVerb invItem)
		(chestBubbles doVerb: theVerb invItem)
	)
)
(instance bubble1 of Prop
	(properties
		view			386
		loop			5
		cel			0
		x				243
		y				142
		description "the bubbles"
	)
	(method (doVerb theVerb invItem)
		(chestBubbles doVerb: theVerb invItem)
	)
)
(instance bubble2 of Prop
	(properties
		view			386
		loop			6
		cel			1
		x				285
		y				140
	  	description "the bubbles"
	)
	(method (doVerb theVerb invItem)
		(chestBubbles doVerb: theVerb invItem)
	)
)
(instance bubble3 of Prop
	(properties
		view			386
		loop			7
		cel			2
		x				256
		y				166
	  	description "the bubbles"
	)
	(method (doVerb theVerb invItem)
		(chestBubbles doVerb: theVerb invItem)
	)
)


;****************
;*** FEATURES ***
;****************

(instance fTowel of Feature
	(properties
		x				42
		y				70
		z				0
		heading		0
		nsLeft		0
		nsTop			51
		nsBottom		89
		nsRight		85
 		description	"her towel"
		sightAngle	 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Bset fKnowsEvesName)
				(Print "Her hand rests near a expensive fluffy bath towel,
					embroidered in a florid script with the name, \"Eve.\"")
			)
			(verbSmellTaste
				(Printf "%s towel has a clean, fresh aroma."
					(if (Btst fKnowsEvesName) {Eve's} else {Her}))
			)
			(verbDo
				(Print "You can't reach her bath towel, and you have no desire
					to leave the water at a time like this!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance fEveHead of Feature
	(properties
		x 158
		y 190
		z 0
		heading 0
		nsLeft 105
		nsTop 32
		nsBottom 110
		nsRight 211
 		description "her face"
		lookStr		"What a fabulous face!"
		sightAngle	180
	)
	(method (doVerb theVerb invItem)
		(curRoom doVerb: theVerb invItem &rest)
	)
)


(instance fEveArms of Feature
	(properties
		x 158
		y 116
		z 0
		heading 0
		nsLeft 9
		nsTop 96
		nsBottom 137
		nsRight 292
 		description "her body"
		sightAngle	180
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Printf "You can picture %s gentle arms winding their way
					around you."
					(if (Btst fKnowsEvesName) {Eve's} else {her}))
			)
			(verbDo
				(if (!= (curRoom script?) sEveHappy)
					(curRoom setScript: sEveHappy)
				)
				(curRoom doVerb: theVerb invItem &rest)
			)
			(else
				(curRoom doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance fBoobs of Feature
	(properties
		x 140
		y 185
		z 40
		heading 0
		nsLeft 80
		nsTop 125
		nsBottom 155
		nsRight 201
 		description "her breasts"
		sightAngle	180
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(if (Btst fJetsOff)
					(Print "What a good idea to turn off those bubblers!")
				else
					(Printf "Even through the bubbles, %s assets are
						outstanding."
						(if (Btst fKnowsEvesName) {Eve's} else {her}))
				)
			)
			(verbDo
				(Print "This is the part of the game that you've been waiting
					for, isn't it, Larry?")
				(if (!= (curRoom script?) sEveHappy)
					(curRoom setScript: sEveHappy)
				)
			)
			(verbZipper
				(Print "\"Hey, don't rush things!\" she says.")
				(if (!= (curRoom script?) sEveAngry)
					(curRoom setScript:	sEveAngry)
				)
			)
			(else
				(curRoom doVerb: theVerb invItem &rest)
			)
		)
	)
)
