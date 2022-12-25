;**
;**	Logics for Room 615 -- Fawn Closeup
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Mike Larsen
;**
;**	Last Update:  June 19, 1991
;**


(script#		615)

(public
	rm615		0
)

(local
	looked = 0
	talked = 0
	moneyTimer = 0
)

(define	pFawnCloseup			615)
	(define	vFawnCloseup		615)
		(define	lLeftEye			0)
		(define	lRightEye		1)
		(define	lAngryEyes		2)
		(define	lFawnSmile		3)
		(define	lFawnFrown		4)
		(define	lFawnPucker		5)

(instance rm615 of Room
	(properties
		picture		615
	)
	(method (init)
		(ego
			init:	,
			hide:
		)
		(Bset fLookedAtFawn)
		(herMouth
			cycleSpeed:	(+ 2 howFast),
			init:
		)
		(rightEye
			cycleSpeed:	(+ 2 howFast),
			init:
		)
		(leftEye
			cycleSpeed:	(+ 2 howFast),
			init:
		)
		(angryEyes
			cycleSpeed:	(+ 2 howFast),
			init:
		)
		(fawnHead 	init:)
		(fawnBody 	init:)
		(fawnNeck 	init:)
		(super 		init:)
		(if
			debugging
			(Bset pGaveFawnRose)
			(Bset pGaveFawnCandy)
			(Bset pGaveFawnDiamond)
			(Bset fDancedWithFawn)
		)
	)
	(method (doit)
		(super doit: &rest)
		(cond
			((and	(Btst pGaveFawnRose)
					(Btst pGaveFawnCandy)
					(Btst pGaveFawnDiamond)
					(not (Btst fDancedWithFawn)))
				(Print "\"So, Fawn, have I given you enough presents that you
					would dance with me?\"")
				(Points pDanceWithFawn ptsDanceWithFawn)
				(Print "\"But, of course, Larry,\" Fawn replies,
					\"You should have asked sooner!\"")
				(Bset fReadyToDance)
				(HandsOff)
				(curRoom newRoom: 610)
			)
			((and (Btst pGaveFawnRose)
					(Btst pGaveFawnCandy)
					(Btst pGaveFawnDiamond)
					(Btst fDancedWithFawn)
					(not (Btst fFawnAskedForBucks)))
				(= moneyTimer 400)
				(Bset fFawnAskedForBucks)
				(Print "\"Oooh, you're so sweet. I just LOVE you!
						Larry, I want you to make wild, passionate love to me!\""
				)
				(Print "(All right, Larry. At last, you're gonna get lucky!!)")
				(Print "She continues, \"But first, we must get married.
					I could never make love to a man unless he's my husband.
					If you would loan me two hundred dollars,
					I'd rent the Honeymoon Suite at the Casino Hotel for us.
					Then, after we're married, we'll celebrate there.\""
				)
			)
			((== moneyTimer 1)
				(-- moneyTimer)
				(Print "\"You know, Larry, a swinging guy like you must be 
					loaded with dough,\" says Fawn with a tempting smile. 
					\"Why don't you give me a couple hundred so we can blow
					this joint?\"")
			)
			(moneyTimer 
				(-- moneyTimer)
			)
			((ego mover?)
				(HandsOff)
				(curRoom newRoom: 610)
			)
		)
	)
)

;***************
;*** SCRIPTS ***
;***************

(instance sSmile of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(angryEyes dispose:)
				(herMouth
					setLoop:			lFawnSmile,
					setCycle:		EndLoop self
				)
			)
			(
				(rightEye
					setCycle:		BegLoop
				)
				(leftEye
					setCycle:		BegLoop self
				)
			)
			(
				(= seconds 10)
			)
			(
				(herMouth
					setCycle:		BegLoop self
				)
			)
			(
				(self dispose:)
			)
		)
	)
)
(instance sFrown of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(angryEyes
					init:				,
					cycleSpeed:		howFast,
					setCel:			0,
					setCycle:		EndLoop
				)
				(herMouth
					setLoop:			lFawnFrown,
					setCel:			0,
					setCycle:		EndLoop self
				)
			)
			(
				(= seconds 10)
			)
			(
				(herMouth
					setCycle:		BegLoop self
				)
				(angryEyes
					setCycle:		BegLoop
				)
			)
			(
				(angryEyes dispose:)
				(self dispose:)
			)
		)
	)
)
(instance sWink of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(angryEyes dispose:)
				(leftEye
					setCel:			0,
					setCycle:		BegLoop
				)
				(herMouth
					setLoop:			lFawnPucker,
					setCel:			0,
					setCycle:		BegLoop self
				)
			)
			(
				(self dispose:)
			)
		)
	)
)

;*************
;*** PROPS ***
;*************

(instance herMouth of Prop
	(properties
		view		vFawnCloseup
		loop		lFawnSmile
		cel		0
		x			174
		y			72
		signal	ignrAct
	)
	(method (doVerb theVerb invItem)
		(fawnHead doVerb: theVerb invItem)
	)
)
(instance rightEye of Prop
	(properties
		view		vFawnCloseup
		loop 		lRightEye
		cel		0
		x			178
		y			47
		cycleSpeed 1
		signal	ignrAct
	)
	(method (doVerb theVerb invItem)
		(fawnHead doVerb: theVerb invItem)
	)
)
(instance leftEye of Prop
	(properties
		view		vFawnCloseup
		loop 		lLeftEye
		cel		0
		x			178
		y			47
		cycleSpeed 1
		signal	ignrAct
	)
	(method (doVerb theVerb invItem)
		(fawnHead doVerb: theVerb invItem)
	)
)
(instance angryEyes of Prop
	(properties
		view		vFawnCloseup
		loop		lAngryEyes
		cel		0
		x			178
		y			47
		lookStr	"Her lips are moist and inviting, but remember:
					you also thought that about old Mrs. Bradley,
					your homeroom teacher!"
		signal	ignrAct
	)
	(method (doVerb theVerb invItem)
		(fawnHead doVerb: theVerb invItem)
	)
)

;****************
;*** FEATURES ***
;****************


(instance fawnBody of Feature
	(properties
		x 157
		y 155
		z 0
		heading 0
		nsLeft 75
		nsTop 122
		nsBottom 189
		nsRight 240
 		description "her"
		lookStr 		"\"Please,\" she says coyly, \"stop staring there.\""
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(switch (++ looked)
					(1
						(Print "What a knock-out!
							She has bright blue eyes and a dazzling smile.
							Your eyes roam her body, and what you see, you like.
							She's sensational!!")
						(Print "(Larry, this MUST be love!)" LOW)
					)
					(2
						(Print "Those beautiful eyes tell you this doll requires
							more than your usual smooth talk.")
					)
					(else
						(if (!= (curRoom script?) sSmile)
							(curRoom setScript:	sSmile)
						)
						(Print "\"Please,\" she says coyly,
							\"stop staring there.\"")
					)
				)
			)
			(verbTalk
				(switch (++ talked)
					(1
						(Points pTalkedToFawn ptsTalkedToFawn)
						(Print "\"Hi ya, Baby. What say you and me get it on?\"")
						(Print "\"Get lost, creep.\"")
						(if (!= (curRoom script?) sFrown)
							(curRoom setScript:	sFrown)
						)
					)
					(2
						(Print "\"Hey, sweetheart. What's your sign?\"
							you ask her.")
						(Print "\"Octagonal,\" she replies. \"Like in `STOP!'\"")
						(if (!= (curRoom script?) sFrown)
							(curRoom setScript:	sFrown)
						)
					)
					(3
						(Print "\"My name is Larry,\" you offer. \"Larry Laffer.\"")
						(Print "\"Say, Larry,\" she chuckles, glancing downward,
							\"is that a pez dispenser in your pocket,
							or are you just lonely?\"")
						(if (!= (curRoom script?) sSmile)
							(curRoom setScript:	sSmile)
						)
					)
					(4
						(Print "\"What's your name, beautiful?\" you ask.")
						(Print "\"Fawn,\" she replies.")
						(Print "Somehow you knew that.")
						(if (!= (curRoom script?) sSmile)
							(curRoom setScript:	sSmile)
						)
					)
					(else
						(if
							(Btst fFawnAskedForBucks)
							(Print "\"So, how about it, Larry?\"
								Fawn says in her petite voice.
								\"May I have a measly two hundred bucks, or not?\"")
						else
							(Print "\"I'd really like to get to know you better,\"
								you say. \"What kind of girl are you, anyway?\"")
							(Print "She smiles and says,
								\"I'm just a girl who can't say no...")
							(Print "(All right, Larry; this is what you've
								been waiting for!)" LOW)
							(Print "\"...to nice presents,\" she concludes.")
							(Print "(Oops.)" LOW)
							(if (!= (curRoom script?) sSmile)
								(curRoom setScript:	sSmile)
							)
						)
					)
				)
			)
			(verbDo
				(if
					(Btst fDancedWithFawn)
					(Print
						"\"Not again. That trip through the ceiling
						gave me a headache!\""
					)
				else
					(Bset fReadyToDance)
					(if (!= (curRoom script?) sSmile)
						(curRoom setScript:	sSmile)
					)
					(Print "You screw up your courage and ask,
						\"What would you say to a spin around the dance
						floor with the nearest thing to John Travolta, baby?\"")
					(Points pDanceWithFawn ptsDanceWithFawn)
					(Print "\"Well, okay, I guess,\" she replies,
						thinking to herself, \"near as I'm gonna get!\"")
					(HandsOff)
					(curRoom newRoom: 610)
				)
			)
			(verbSmellTaste
				(Print "Fawn smells of expensive perfume--probably a gift
					from some admirer.")
				(if (!= (curRoom script?) sWink)
					(curRoom setScript:	sWink)
				)
			)
			(verbZipper
				(Print "\"Something might be arranged in that department,\"
					says Fawn, coyly.")
				(if (!= (curRoom script?) sWink)
					(curRoom setScript:	sWink)
				)
			)
			(verbUse
				(switch invItem

					;**	First, handle all the big stuff

					(iWallet
						(cond
							((not (Btst fFawnAskedForBucks))
								(Print "\"Well, really.
									I'm not that kind of girl!!\"")
								(if (!= (curRoom script?) sFrown)
									(curRoom setScript:	sFrown)
								)
							)
							((and (Btst fFawnAskedForBucks)
									(<= larryDollars 200)
								)
								(= moneyTimer 0)
								(Printf "\"Uh, gosh, Fawn,\" you mumble.
									\"I've only got $%d.00 bucks on me!\""
									larryDollars
								)
								(Print "\"I'm sorry,
									but I'll need at least two hundred dollars,\"
									she says with a coy smile.
									\"You know it'll be worth it!\"")
								(Print "It appears Fawn has caught you a
									\"little short,\" Larry.
									Better try the casino,
									and hope for a run of good luck!")
								(Print "You fervently hope she'll wait for your
									return.")
								(Print "Conveniently, she offers,
									\"I'll wait right here until you return.\"")
								(Print "Gee, Larry, this is working out swell.")
							)
							((and (Btst fFawnAskedForBucks)
									(> larryDollars 200))
								(Points pGaveFawnBucks ptsGaveFawnBucks)
								(Print "\"Ooh you're so sweet. I just love money.\"")
								(Print "\"Thanks, Larry. You won't regret this.
									Meet me at the Marriage Chapel near the
									Casino in a few minutes.
									I'll get us a room.
									Hurry, Larry, I can hardly wait!\"")
								(Bset fReadyToMarry)
								(Bset fFawnLeaves)
								(if
									(!= (curRoom script?) sSmile)
									(curRoom setScript:	sSmile)
								)
								(-= larryDollars 200)
								(HandsOff)
								(curRoom newRoom: 610)
							)
						)
					)

					;**	Here's the stuff Fawn desires!:

					(iRose
						(Print "\"Ooh you're so sweet. I just love roses.\"")
						(ego put: iRose)
						(if (!= (curRoom script?) sSmile)
							(curRoom setScript:	sSmile)
						)
						(Points pGaveFawnRose ptsGaveFawnRose)
					)
					(iCandy
						(Print "\"Ooh you're so sweet. I just love Candy.\"")
						(ego put: iCandy)
						(if (!= (curRoom script?) sSmile)
							(curRoom setScript:	sSmile)
						)
						(Points pGaveFawnCandy ptsGaveFawnCandy)
					)
					(iRing
						(Print "\"Ooh you're so sweet. I just love diamonds.\"")
						(ego put: iRing)
						(if (!= (curRoom script?) sSmile)
							(curRoom setScript:	sSmile)
						)
						(Points pGaveFawnDiamond ptsGaveFawnDiamond)
					)

					;**	Lastly, do all the lame answers:

					(iBreathSpray
						(Print "\"Are you implying I have bad breath?\" she says
							angrily.")
						(if (!= (curRoom script?) sFrown)
							(curRoom setScript:	sFrown)
						)
					)
					(iWatch
						(Print "It IS getting late, Larry.
							And you're sure she's soooo lonely!")
					)
					(iRemoteControl
						(Print "\"Why, Larry!
							What a cute little clicker you've got.\"")
						(if (!= (curRoom script?) sSmile)
							(curRoom setScript:	sSmile)
						)
					)
					(iLubber
						(Print "You're rushing things, Larry. Calm down.")
					)
					(iDiscoPass
						(Print "She'd probably dance with you if you'd offer
							your hand.")
					)
					(iPocketKnife
						(Print "It would be smart for you not to let Fawn know
							you're carrying a concealed weapon!")
					)
					(iMagazine
						(Print "\"I have no interest in naked women--or
							window washing!\" Fawn says.")
					)
					(iHammer
						(Print "\"Oooh, I just LOVE to play with manly tools!\"")
						(if (!= (curRoom script?) sSmile)
							(curRoom setScript:	sSmile)
						)
					)
					(iGraffiti
						(Print "\"Ken? Never heard of him,\" she says.
							Then her expression changes.
							\"Say... what's he like?
							How much does he make?
							Is he married?\"")
						(if (!= (curRoom script?) sSmile)
							(curRoom setScript:	sSmile)
						)
					)
					(else
						(Printf "\"Oh, %s is sweet, but I'd like something
							special... from a guy like you!\""
							((Inventory at: invItem) description?))
;						(Print "\"Oh, that's sweet, but I'd like something
;							special... from a guy like you!\""
;						)
					)
				)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance fawnHead of Feature
	(properties
		x 149
		y 56
		z 0
		heading 0
		nsLeft 88
		nsTop 12
		nsBottom 101
		nsRight 210
 		description "her head"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "Her mind is stuck in the present(s).")
			)
			(else
				(fawnBody doVerb: theVerb invItem)
			)
		)
	)
)

(instance fawnNeck of Feature
	(properties
		x 172
		y 111
		z 0
		heading 0
		nsLeft 136
		nsTop 102
		nsBottom 120
		nsRight 208
 		description "her neck"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "A long and lovely neck perfectly fills the distance
					between her head and her shoulders!")
			)
			(verbDo
				(Print "\"Ooooh, that feels nice!\"")
			)
			(else
				(fawnBody doVerb: theVerb invItem)
			)
		)
	)
)

