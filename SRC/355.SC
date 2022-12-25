;**
;**	Logics for Room 355 -- Faith Closeup
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford 
;**
;**	Last Update:  June 21, 1991
;**


(script# 355)

(public
	rm355		0
)

(local
	lookCount
	talkCount
	breathLineTimer
)

(define	pFaithCloseup			355)
(define	vFaithCloseup			355)
	(define lBlink					0)
	(define lFrown					1)
	(define lPout					2)
;	(define lTalk					3)
	(define lMouthNormal			4)
	(define lSmile					5)

(define	mPills					352)

(instance rm355 of LLRoom
	(properties
		picture	pFaithCloseup
	)
	(method (init)
		(LoadMany VIEW
			vFaithCloseup
		)
		(LoadMany SOUND
			mPills
		)
		(ego
			init:,
			hide:
		)
		(super init)
		(herEyes
			init:,
			cycleSpeed:	larrySpeed,
			setScript:	sBlink
		)
		(herMouth
			init:,
			cycleSpeed:	larrySpeed,
		)
		(if (and	(not spraySeconds) ;why?
					(Btst fMouthSmellsBad))
			(= breathLineTimer 60)
			(herMouth setScript: sFrown)
		)
		(hair init:)
		(theBreasts init:)
		(faithF init:)
		(neck init:)
		(necklace init:)
		(face init:)
	)
	(method (doit)
		(super doit: &rest)
		(if breathLineTimer
			(-- breathLineTimer)
			(if (== breathLineTimer 1)
;				(herMouth setScript: sTalk)
				(Print "\"Bad breath's one thing, Mister;
					but, you could knock a buzzard off a manure truck!\""
					LOW
				)
			)
		)
		(cond
			(script)
			((ego mover?)
				(curRoom newRoom: 350)
		 	)
		)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "She is a knockout!
					But that pair of pistols slung around her hips brings
					a sense of foreboding to your gut.")
			)
			(verbUse
				(switch invItem
					(iBreathSpray
						(= breathLineTimer 0)
						(herEyes setScript: sFlap)
					)
					(else
						(super doVerb: theVerb invItem)
					)
				)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

;
;	SCRIPTS
;

(instance sFaithLeaves of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(theMusic
					number:		mPills,
					play:,
				)
				(herEyes
					setScript:		sFlap,
				)
				(herMouth setScript: sSmile NULL 1)
				(ego put: iPills 350)
				(Points pGavePills ptsGavePills)
				(= seconds 2)
			)
			(
				(herEyes setCycle: EndLoop self)
			)
			(
				(Print "Faith only gives the bottle of Spanish Fly a quick glance
					before saying, \"Boy, thanks!
					How did you know I love this stuff?!\""
				)
				(Print "She tosses back a few pills, then a few more,
					finally gulping down the whole bottle!
					Her breathing becomes faster and faster.
					She begins to pant!"
				)
				(herEyes
					setScript:		sFlap,
				)
				(= seconds 4)
			)
			(
				(herEyes setCycle: EndLoop self)
			)
			(
				(Print "She's really getting turned on!")
				(Print "(Looks like tonight's your lucky night, Larry.)" LOW)
				(= seconds 4)
			)
			(
				(curRoom newRoom 350)
			)
		)
	)
)

(instance sFrown of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(herMouth
					setLoop:		lPout,
					setCel:		0,
				)
				(herEyes
					setScript:	0,
					setLoop:		lFrown,
					setCel:		0,
				)
				(= seconds 5)
			)
			(
				(herMouth
					setLoop:		lMouthNormal,
					setCel:		0,
				)
				(herEyes
					setLoop:		lBlink,
					setCel:		0,
					setScript:	sBlink,
				)
				(self dispose:)
			)
		)
	)
)

(instance sSmile of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(if
					(!= (curRoom script?) sFaithLeaves)
					(herEyes
						setLoop:		lBlink,
						setCel:		0,
						setScript:	sBlink,
					)
				)
				(herMouth
					setLoop:		lSmile,
					setCel:		0,
				)
				(= seconds register)
			)
			(
				(herMouth
					setLoop:		lMouthNormal,
					setCel:		0,
				)
				(if
					(> register 3)
					(self dispose:)
				else
					(self init:)
				)
			)
		)
	)
)

;(instance sTalk of Script
;	(method (changeState newState)
;		(switchto (= state newState)
;			(
;				(herEyes
;					setLoop:		lBlink,
;					setCel:		0,
;					setScript:	sBlink,
;				)
;				(herMouth
;					setLoop:		lTalk,
;					setCel:		0,
;					setCycle:	Forward
;				)
;				(= seconds 3)
;			)
;			(
;				(herMouth
;					setLoop:		lMouthNormal,
;					setCel:		0,
;				)
;				(self dispose:)
;			)
;		)
;	)
;)

(instance sFlap of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(herEyes
					setLoop:		lBlink,
					setCycle:	Forward
				)
				(= seconds 5)
			)
			(
				(if
					(== (curRoom script?) sFaithLeaves)
					(self init:)
				else
					(herEyes setCycle: BegLoop self)
				)
			)
			(
				(herEyes	setScript: sBlink)
				(self dispose:)
			)
		)
	)
)

(instance sBlink of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= seconds (Random 2 4))
			)
			(
				(herEyes
					setCycle:	BegLoop self
				)
			)
			(
				(self init:)
			)
		)
	)
)

;
;	Closeup Props
;

(instance herEyes of Prop
	(properties
		view			vFaithCloseup
		loop			lBlink
		x				122
		y				71
		z				28
		signal		ignrAct
		description "her eyes"
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "Her eyes are as sensuous as her lips.")
			)
			(else
				(faithF doVerb: theVerb invItem)
			)
		)
	)
)

(instance herMouth of Prop
	(properties
		view			vFaithCloseup
		loop			lMouthNormal
		x				128
		y				71
		signal		ignrAct
		description "her mouth"
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "Her lips are moist and inviting, but then,
					you also felt that way about Mr. Bergman,
					your junior high school math instructor!")
			)
			(verbSmellTaste
				(herMouth setScript: sSmile NULL 5)
			)
			(else
				(faithF doVerb: theVerb invItem)
			)
		)
	)
)


;
;	Closeup Features
;

(instance face of Feature
	(properties
		x 131
		y 53
		z 0
		heading 0
		nsLeft 105
		nsTop 37
		nsBottom 81
		nsRight 157
 		description "her face"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "She's the loveliest woman you've even been THIS close to,
					Larry.")
			)
			(else
				(faithF doVerb: theVerb invItem)
			)
		)
	)
)
(instance neck of Feature
	(properties
		x 152
		y 54
		z 0
		heading 0
		nsLeft 131
		nsTop 82
		nsBottom 114
		nsRight 174
 		description "her neck"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "You long to get closer to her beautiful long neck.")
			)
			(verbSmellTaste
;				(herMouth setScript: sTalk)
				(Print "\"Oh, DON'T do that!\" she says,
					\"I have a job to do here!\""
					LOW
				)
				(herMouth setScript: sSmile NULL 5)
			)
			(else
				(faithF doVerb: theVerb invItem)
			)
		)
	)
)
(instance necklace of Feature
	(properties
		x 151
		y 55
		z 0
		heading 0
		nsLeft 129
		nsTop 115
		nsBottom 141
		nsRight 174
 		description "her necklace"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "You wonder just how religious this girl could be,
					wearing an outfit like that.")
			)
			(verbDo
				(Print "Upon touching the cross, you feel a sudden inspiration.")
				(Print "\"What is your name?\" you ask.")
				(Print "\"Faith,\" she replies.")
				(Print "Hmmm. Unusual name.")
				(herMouth setScript: sSmile NULL 5)
			)
			(else
				(faithF doVerb: theVerb invItem)
			)
		)
	)
)

(instance hair of Feature
	(properties
		x 151
		y 52
		z 0
		heading 0
		nsLeft 83
		nsTop 2
		nsBottom 103
		nsRight 219
 		description "her hair"
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "Her hair is thick and beautiful,
					falling in cascades around her delicate shoulders.")
			)
			(verbDo
				(Print "\"I'd love to run my fingers through your hair,\"
					you say.")
				(Print "\"From the looks of your hairline, you'd love to run
					your fingers through ANY hair!\"")
			)
			(verbSmellTaste
				(Print "\"Your hair smells wonderful,\" you offer,
					assuming it's her shampoo you're smelling.
					\"What's your brand?\"")
				(Print "\"Smith and Wesson!\"")
			)
			(else
				(faithF doVerb: theVerb invItem)
			)
		)
	)
)

(instance theBreasts of Feature
	(properties
		x 146
		y 170
		z 0
		heading 0
		nsLeft 77
		nsTop 152
		nsBottom 189
		nsRight 215
 		description "her breasts"
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "What a pair of pistols!")
			)
			(verbDo
				(Print "That pair of pistols she's wearing aren't the only
					38's she's packing!")
			)
			(verbSmellTaste
				(Print "You can only imagine!")
			)
			(else
				(faithF doVerb: theVerb invItem)
			)
		)
	)
)

(instance faithF of Feature
	(properties
		x 166
		y 24
		z 0
		heading 0
		nsLeft 74
		nsTop 2
		nsBottom 189
		nsRight 259
 		description "Faith"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(++ lookCount)
				(switch lookCount
					(1
						(Print "She is a knockout!
							But that pair of pistols slung around her hips brings
							a sense of foreboding to your gut.")
					)
					(2
						(Print "Her uniform reveals she's built like
							a brick shipyard.")
					)
					(else
						(Print "\"Stop staring there,\" she says softly.")
						(herMouth setScript: sSmile NULL 5)
					)
				)
			)
			(verbDo
				(Print "\"You know, I'm supposed to be working!\""
					LOW
				)
			)
			(verbTalk
				(++ talkCount)
				(switch talkCount
					(1
						(Print "\"Hi ya, Baby. I think I loves ya!!\"")
						(herMouth setScript: sSmile NULL 5)
						(Print "\"May I help you in some way, Sir?\"
							she responds in a pleasant, but businesslike voice."
							LOW
						)
					)
					(2
						(Print "\"My name is Larry; Larry Laffer,\" you offer.")
						(Print "\"Say, Larry,\" she says with a smile.
							\"Is that a roll of dimes in your pocket,
							or are you just glad to see me?\""
							LOW
						)
						(herMouth setScript: sSmile NULL 5)
					)
					(3
						(Print "\"Hey, sweetheart. What's your sign?\"
							you ask her.")
						(Print "\"`EXIT,\" she replies. \"Try using it!\""
							LOW
						)
					)
					(4
						(Print "\"What's your name, beautiful?\" you ask.")
						(Print "\"Faith,\" she replies."
							LOW
						)
						(Print "Hmmm. Unusual name.")
					)
					(5
						(Print "\"I'm only in town for one night,\" you try again.
							\"How about we get together when you get off duty?\"")
						(Print "She smiles and says,
							\"Oh, I could never be unfaithful to my boyfriend.\""
							LOW
						)
						(Print "(Larry, it seems this one's gonna require
							outside assistance.)" LOW)
						(herMouth setScript: sSmile NULL 5)
					)
					(6
						(Print "\"When do you get off?\" you ask her.")
						(Print "\"When I'm with someone romantic!\"
							she replies."
							LOW
						)
					)
					(7
						(Print "\"Don't sit here all night,\" you plead.
							\"Why don't you come with me?\"")
						(Print "\"Perhaps,\" she says,
							\"but right now, I'm working.\""
							LOW
						)
					)
					(else
						(Print "You're a man of the world, Larry.
							Perhaps some sort of medical stimulant would help.")
					)
				)
			)
			(verbZipper
				(Print "\"Try it, and I'll blow your head off.\"")
				(Print "(She is, of course,
					referring to the use of her pistols, Larry!)" LOW)
				(herMouth setScript: sFrown)
			)
			(verbSmellTaste
				(Print "There's an aroma of authority in the air!")
			)
			(verbUse
				(switch invItem
					(iWallet
						(Print "\"Really? I'm not that kind of girl.\"")
						(herMouth setScript: sFrown)
					)
					(iWatch
						(Print "\"So, what time do you get off?\" you ask her.")
						(Print "\"Usually after I get home following work,\"
							she replies.")
					)
					(iApple
						(Print "\"Oh, no, thanks.
							It is against regulations for security guards to
							eat while on duty.\"")
					)
					(iRing
						(Print "\"I'm sorry,\" Faith replies,
							\"but you don't know me that well!\"")
					)
					(iWhiskey
						(Print "\"Oh, no, thanks.
							It is against regulations for security guards to
							drink while on duty.\"")
					)
					(iRemoteControl
						(Print "You click the remote at the security guard.
							As you expected, it fails to turn her on.")
						(Print "\"Apparently your unit isn't functioning
							properly,\" she says smugly.")
					)
					(iRose
						(Print "\"Oh, no, thanks.
							It is against regulations for security guards to
							accept common, ordinary, gifts while on duty.\"")
					)
					(iLubber
						(Print "\"Yes, good idea,\" she smiles,
							caressing that pair of loaded weapons,
							\"I try to always pack a little protection myself!\"")
					)
					(iCandy
						(Print "\"Oh, no, thanks.
							It is against regulations for security guards to
							eat while on duty,\" she says.
							\"Besides, I have to watch my girlish figure.\"")
						(Print "\"That's a duty I'd be glad to volunteer for!\"
							you think.")
					)
					(iDiscoPass
						(Print "She peers closely at your disco pass.
							\"Say, exactly what's going on here?
							This isn't an authorized disco-dancing-type club
							admittance authorization voucher, 7325B/11\" she says.
							\"I'm going to confiscate this on the grounds!\"")
						(ego put: iDiscoPass)
					)
					(iPocketKnife
						(Print "\"An impressive weapon, Buddy!\"
							she whispers huskily,
							\"but you must learn how to thrust it properly!\"")
					)
					(iWine
						(Print "\"Oh, no, thanks.
							It is against regulations for security guards to
							drink while on duty.\"")
					)
					(iMagazine
						(Print "Faith has her own \"Jugs,\" Larry!")
					)
					(iHammer
						(Print "\"You'd better not threaten me, Buddy!
							My weapons are bigger than yours!\"")
					)
					(iPills
						(HandsOff)
						(curRoom setScript: sFaithLeaves)
					)
					(else
						(Printf "\"I'm sorry, Sir, but I'm on duty right now,
							and even if I were off-duty, I'd still have no
							use whatsoever for %s!\""
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

