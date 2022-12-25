;**
;**	Logics for Room 320 -- Cabaret
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford &
;**
;**	Last Update:  June 20, 1991
;**


(script# 320)

(public
	rm320		0
)

(procedure
	PrintJoke
	PrintLine
)

(local
	trickTimer
	mcOnStage
	poopied
)

(define MINTIME	1200)
(define MAXTIME	2000)

(define JOKEMAX					38)
(define yGap 2)
(define xGap 22)
(define sitting (== (ego view?) vLarryCushion))
(define tableX 296)
(define tableY 178)
(define drummerTalksAt #at: 15 80)

;(define	sFart						133)

;(define	mCanCan					320)
;(define	sDrumFill1				321)
(define	sDrumFill2				322)
(define	sDrumFill3				323)
(define	sDrumFill4				324)
(define	sDrumFill5				325)

(define	pCabaret					320)
(define	vCabaret					320)
(define	vLarryCushion			321)
	(define lSit					0)
	(define lFart					1)
	(define lLook					2)
(define	vComedian				322)
	(define lHead					0)
		(define cShut				7)
	(define lLeft					1)
	(define lRight					2)
(define	vDancers					323)
(define	vDrummer					324)
	(define lDrumfill				0)
	(define lBlink					1)
	(define lDrumHead				2)

(instance rm320 of LLRoom
	(properties
		picture	pCabaret
		south		330
	)
	(method (init &tmp i)
		(LoadMany VIEW
			vCabaret
		)
		(LoadMany SOUND
			sFart
			mCanCan
			sDrumFill1
			sDrumFill2
			sDrumFill3
			sDrumFill4
			sDrumFill5
		)
		(theMusic2
			setVol:		127,
			setLoop:		1,
			flags:		1,
		)
		(curRoom addObstacle:
			((Polygon new:)
				type: PBarredAccess,
				init:	0 189 0 0 319 0 319 189 252 189 252 179 271 172 296 172
						296 179 317 179 317 142 267 114 135 111 32 117 13 147 53 147
						53 156 2 156 2 189,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	19 189 19 178 43 168 74 168 97 178 97 189,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	148 189 148 176 167 165 195 165 219 176 219 189,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	39 133 103 133 103 138 83 138 83 143 39 143,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	100 144 172 144 172 156 100 156,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	170 123 243 123 243 135 219 135 219 138 193 138 193 135
						170 135,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	232 158 302 158 302 168 232 168,
				yourself
			)
			((Polygon new:)
				type: PBarredAccess,
				init:	226 142 298 142 298 150 226 150,
				yourself
			)
		)
		(ego
			init:		,
			x:			126,
			actions:	egoActions,
		)
		(if (!= prevRoomNum 330)
			(ego y: 180)
		)
		(if (Btst fCaberetTgl)						;do comedian
			(Bclr fCaberetTgl)
			(LoadMany VIEW
				vLarryCushion
				vComedian
				vDrummer
			)
			(= trickTimer (Random MINTIME MAXTIME))
			(= mcOnStage TRUE)
			(comedian
				cycleSpeed:	howFast,
				init:,
				setScript:	sComedian
			)
			(head
				cycleSpeed:	howFast,
				init:
			)
			(drummer
				cycleSpeed:	(+ howFast 1),
				init:
			)
			(for ((= i 1)) (<= i JOKEMAX) ((++ i))
				(jokes addToEnd: i)
			)
		else										;do dancers
			(Bset fCaberetTgl)
			(LoadMany VIEW
				vDancers
			)
			(theMusic
				number:		mCanCan,
				loop:			-1,
				vol:			127,
				flags:		1,
				play:,
			)
			(for ((= i 0)) (< i (+ (theGame detailLevel?) 2)) ((++ i))
				((dancer new:)
					cycleSpeed:		howFast,
					init:			,
					x:					(+ (dancer x?) (* i xGap)),
					y:					(- (dancer y?) (* i yGap)),
					setCycle:		Forward,
					approachVerbs:	actorVerbs verbLook,
					approachX:		(+ (dancer x?) (* i xGap)),
					approachY:		(- (dancer y?) (* i yGap)),
				)
			)
		)

		(super init:)

		(light1
			cycleSpeed:	howFast,
			setCycle:	Forward,
			init:,
		)
		(light2
			cycleSpeed:	howFast,
			setCycle:	Forward,
			init:,
		)
		(light3
			cycleSpeed:	howFast,
			setCycle:	Forward,
			init:,
		)
		(light4
			cycleSpeed:	howFast,
			setCycle:	Forward,
			init:,
		)
		(light5
			cycleSpeed:	howFast,
			setCycle:	Forward,
			init:,
		)
		(light6
			cycleSpeed:	howFast,
			setCycle:	Forward,
			init:,
		)
		(table1 init:)
		(table2 init:)
		(table3 init:)
		(table4 init:)
		(table5
			init:,
			approachVerbs:	objectVerbs
		)
		(table6 init:)
		(theStage init:)
	)
	(method (doit)
		(super doit: &rest)
		(if (> trickTimer 1)
			(-- trickTimer)
		)
		(cond
			(script)
			((and (ego mover?)
					sitting)
				(HandsOff)
				(curRoom setScript: sStand)
			)
		)
	)
	(method (dispose)
		(jokes dispose:)
		(super dispose:)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "A sign off in the corner reads, \"Welcome to
					The Lizard Lounge.\"")
				(Print "It looks like a great place to be from.")
				(Print "(You wonder where they keep the lizards.)" LOW)
			)
			(verbUse
				(switch invItem
					(iRose
						(ego put: iRose 0)
						(Print "You toss the rose on the stage.
 							It falls where you will not be able to retrieve it.")
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
	(method (newRoom n)
		(if (== (theMusic number?) mCanCan)
			(theMusic fade:)
		)
		(super newRoom: n)
	)
)

;
;	Classes
;

(class egoActions of Code
	(methods
		doVerb
	)
	(method (doVerb theVerb invItem)
		(return
			(switch theVerb
				(verbDo
					(if mcOnStage
						(Print "\"Bravo. Bravo!\" you cry.")
						(Print "\"Thank you, thank you!\"
							the comedian responds to your applause.")
					else
						(Print "The girls' smiles beam at your applause!")
					)
				)
			)
		)
	)
)

;
;	Scripts
;

(instance sComedian of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= seconds 3)
			)
			(
				(drummer
					setLoop:		lDrumfill,
					setCel:		0,
					stopUpd:,
				)
				(comedian
					setLoop:		(Random 1 2),
					setCycle:	Forward,
				)
				(head
					setCycle:	Forward
				)
				(= seconds 2)
			)
			(
				(comedian
					setLoop:		(Random 1 2),
				)
				(= seconds 2)
			)
			(
				(head
					setCel:		(if (Random 0 1) cShut),
					stopUpd:,
				)
				(comedian
					stopUpd:
				)
				(= cycles 1)
			)
			(
				(if (== trickTimer 1)
					(= trickTimer 0)
					(comedian setScript: sMagicTrick)
				else
					(if (== (jokes size?) 1)
						(Print "\"Well, that's all I know, folks,\" the comic says.
 							\"Come back for the next show,
							and you can hear 'em all again!\"")
						(self dispose:)
					else
						(PrintJoke)
						(= cycles 3)
					)
				)
			)
			(
				(theMusic2
				;321 and 322 used to be the only sampled drumfills
;					number:		(if (DoSound NumDACs) (Random 321 322) else (Random 321 325)),
					number:		(Random 321 325),
					play:		,
				)
				(drummer
					setCycle:	EndLoop self
				)
			)
			(
				(= seconds 1)
			)
			(
				(PrintLine)
				(self init:)
			)
		)
	)
)

(instance sMagicTrick of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(Print "\"And, now, ladi... uh, gentleman,\"
					announces the comic,
					\"we're proud to present the Magic portion of our act.\"")
				(Print "\"Let me pick someone at random from our audience...\"")
				(Print "\"How about you,\" he says looking directly at you.
 					\"Would you like to participate in a little
					experiment in telepathy?\"")
				(Print "(Of course he's looking at you!
					You're the only one here!)" LOW)
				(Print "You're game, so you answer, \"Sure.\"")
				(Print "\"Great!
					Now Paul, my drummer and musical conductor for lo these
					many years, will read your mind,\" says the comic.
 					\"Come on, Paul, show him your stuff!\"")
				(drummer
					setLoop:		lBlink,
				)
				(drummerHead
					cycleSpeed:	howFast,
					init:,
					setCycle:	Forward
				)
				(Print "\"I want you to concentrate on your billfold,\"
					Paul says. \"Clear your mind,
					and think solely about the contents of your wallet.\""
					drummerTalksAt
					#dispose
				)
				(= seconds 5)
			)
			(
				(Cls)
				(Print "Since clearing your mind is no problem for you, Larry,
 					you do so easily.")
				(Print "\"I'm getting a message... I see something...\""
					drummerTalksAt
					#dispose:
				)
				(= seconds 5)
			)
			(
				(Cls)
				(Printf "\"I've GOT IT!\" he exclaims.
					\"If you open your billfold you'll discover
					some wrinkled business cards, lots of notes,
 					some credit cards (which won't work in this game),
 					and exactly $%d.00 in cash!!\""
					larryDollars
					drummerTalksAt
					#dispose:
				)
				(= seconds 5)
			)
			(
				(Cls)
				(drummer
					setLoop:		lDrumfill,
				)
				(drummerHead
					dispose:
				)
				(Print "\"Ladi... uh, GENTLEMAN!\" shouts the comic.
 					\"PAUL THE MAGNIFICENT has done it again!!\"")
				(Print "Just to make sure,
					you take out your wallet and look inside...")
				((inventory at: iWallet) doVerb: verbLook)
				(comedian setScript: sComedian)
			)
		)
	)
)

(instance sSit of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(ego
					view:			vLarryCushion,
					normal:		FALSE,
					setLoop:		lSit,
					setCel:		0,
					setPri:		15,
					setCycle:	EndLoop self,
				)
			)
			(
				(if (and mcOnStage
							(not poopied))
					(theMusic3
						number:		sFart,
						loop:			1,
						vol:			127,
						flags:		1,
						play:,
					)
					(= poopied TRUE)
					(whoopyCushion
						cycleSpeed:	(+ howFast 1),
						init:,
						setCycle:	EndLoop self
					)
				else
					(HandsOn)
					(self dispose:)
				)
			)
			(
				(whoopyCushion
					z:			1000,
					dispose:
				)
				(ego
					setLoop:		lLook,
					setCel:		0,
					setCycle:	EndLoop self
				)
			)
			(
				(Print "At least the comic's whoopee cushion doesn't smell!")
				(Points pSatOnWhoopieCushion ptsSatOnWhoopieCushion)
				(ego
					setLoop:		lSit,
					setCel:		255,
				)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sStand of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(table5
					approachX:	tableX,
					approachY:	tableY,
				)
				(ego
					setPri:		14,
					setCycle:	BegLoop self
				)
			)
			(
				(HandsOn)
				(NormalEgo 1)
				(if (and (!= (CueObj client?) table5)
							((CueObj client?) approachX?))
					(ego setMotion: PolyPath
					((CueObj client?) approachX?)
					(+ (ego z?) ((CueObj client?) approachY))
					CueObj)
				else
					(ego setMotion: PolyPath ((User curEvent?) x?) ((User curEvent) y?))
				)
				(self dispose:)
			)
		)
	)
)

;
;	Props
;

(instance comedian of Person
	(properties
		view			vComedian
		loop			lLeft
		cel			0
		x				166
		y				70
		lookStr		"Evidently, HE thinks he's funny!"
		description "the comedian"
		signal		ignrAct
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbTalk
				(Print "You shout to the comedian, \"This ain't funny!\"")
				(Print "The comedian retorts, \"But you are!!\"")
			)
			(verbDo
				(Print "You whistle loudly at the comic.")
				(Print "\"Hey, buddy, will ya hold it down?\" says the comic.
 					\"I'm tryin' to earn a living up here!\"")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance head of Prop
	(properties
		view			vComedian
		loop			lHead
		z				26
		signal		ignrAct
		lookStr		"Evidently, HE thinks he's funny!"
		description "the comedian"
	)
	(method (init)
		(= y (comedian y?))
		(super init:)
	)
	(method (doit)
		(super doit: &rest)
		(if (== (comedian loop?) 2)
			(= x (+ (comedian x?) 5))
		else
			(= x (- (comedian x?) 1))
		)
	)
	(method (doVerb theVerb invItem)
		(comedian doVerb: theVerb invItem)
	)
)

(instance drummerHead of Prop
	(properties
		view			vDrummer
		loop			lDrumHead
		z				30
		signal		ignrAct
		lookStr		"It's a living!"
		description	"Paul, the drummer"

	)
	(method (init)
		(= y (drummer y?))
		(= x (drummer x?))
		(super init:)
	)
	(method (doVerb theVerb invItem)
		(drummer doVerb: theVerb invItem)
	)
)

(instance drummer of Prop
	(properties
		view			vDrummer
		loop			lDrumfill
		x				101
		y				70
		cycleSpeed	1
		description	"Paul, the drummer"
		lookStr		"It's a living!"
	)
	(method (cue)
		(super cue:)
		(Print "It's a living."
			drummerTalksAt
		)
		(drummerHead dispose:)
	)
	(method (doVerb theVerb invItem)
		(if (!= (comedian script?) sMagicTrick)
			(drummerHead
				cycleSpeed:	howFast,
				init:,
				setCycle:	BegLoop self
			)
		)
	)
)

(instance dancer of Actor
	(properties
		view			vDancers
		x				120
		y				76
		signal		ignrAct
		description "the dancer"
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "Upon closer examination, you discover these dancers
					may not be the \"girls\" you thought they were!")
			)
			(verbDo
				(Print "You whistle loudly at the girls.")
				(Print "You're not sure, but you suppose they enjoy it.")
			)
			(verbTalk
				(Print "You whistle loudly at the girls.")
				(Print "You're not sure, but you suppose they enjoy it.")
			)
			(verbZipper
				(Print "Right, sure!
					As if it would reach from way down here!")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance whoopyCushion of Prop
	(properties
		view			vLarryCushion
		loop			lFart
		x				299
		y				172
		priority		15
		signal		(| ignrAct fixPriOn)
	)
)

(instance theStage of Feature
	(properties
		x 156
		y 108
		z 31
		heading 0
		nsLeft 50
		nsTop 76
		nsBottom 98
		nsRight 262
 		description "the stage"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "The stage is only there as a \"supporting\" cast member!")
			)
			(verbDo
				(Print "Stay off the stage, Larry. You're no star!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance light1 of Prop
	(properties
		view			vCabaret
		loop			0
		cel			0
		x				70
		y				141
		z				15
		priority		10
		signal		(| fixPriOn ignrAct)
		description	"the candle in a glass"
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "You wonder who invented that little plastic net
					surrounding the candle's glass.")
				(Print "And, how come it doesn't melt?")
			)
			(verbDo
				(Print "You briefly hold your hand right above the candle flame,
					then reconsider as your hand begins to blister.")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)
(instance table1 of Feature
	(properties
		x 68
		y 122
		z 0
		heading 0
		nsLeft 51
		nsTop 109
		nsBottom 135
		nsRight 86
 		description "the table"
		sightAngle 40
		lookStr 		"What wonderful decor!
						You just love furniture with a sense of humor."
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "As you are about to sit down in that chair,
					you notice a small sign on the table that reads,
					\"Reserved.\"")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance light2 of Prop
	(properties
		view			vCabaret
		loop			2
		cel			0
		x				131
		y				153
		z				15
		priority		11
		signal		(| fixPriOn ignrAct)
		description	"the candle in a glass"
	)
	(method (doVerb theVerb invItem)
		(light1 doVerb: theVerb invItem)
	)
)
(instance table2 of Feature
	(properties
		x 136
		y 137
		z 0
		heading 0
		nsLeft 116
		nsTop 125
		nsBottom 149
		nsRight 156
 		description "the table"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(table1 doVerb: theVerb invItem)
	)
)

(instance light3 of Prop
	(properties
		view			vCabaret
		loop			4
		cel			0
		x				178
		y				178
		z				15
		priority		15
		signal		(| fixPriOn ignrAct)
		description	"the candle in a glass"
	)
	(method (doVerb theVerb invItem)
		(light1 doVerb: theVerb invItem)
	)
)
(instance table3 of Feature
	(properties
		x 182
		y 165
		z 0
		heading 0
		nsLeft 165
		nsTop 152
		nsBottom 178
		nsRight 200
 		description "the table"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(table1 doVerb: theVerb invItem)
	)
)

(instance light4 of Prop
	(properties
		view			vCabaret
		loop			3
		cel			0
		x				207
		y				137
		z				15
		priority		9
		illegalBits	$0000
		signal		(| fixPriOn ignrAct)
		description	"the candle in a glass"
	)
	(method (doVerb theVerb invItem)
		(light1 doVerb: theVerb invItem)
	)
)
(instance table4 of Feature
	(properties
		x 206
		y 121
		z 0
		heading 0
		nsLeft 188
		nsTop 111
		nsBottom 131
		nsRight 225
 		description "the table"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(table1 doVerb: theVerb invItem)
	)
)

(instance light5 of Prop
	(properties
		view			vCabaret
		loop			5
		cel			1
		x				283
		y				180
		z				15
		priority		15
		signal		(| fixPriOn ignrAct)
		description	"the candle in a glass"
	)
	(method (doVerb theVerb invItem)
		(light1 doVerb: theVerb invItem)
	)
)
(instance table5 of Feature
	(properties
		x 292
		y 166
		z 0
		heading 0
		nsLeft 266
		nsTop 149
		nsBottom 184
		nsRight 319
 		description "the table"
		sightAngle 40
		approachX	tableX
		approachY	tableY
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(table1 doVerb: verbLook)
			)
			(verbDo
				(if sitting
					(Print "You already are sitting.")
				else
					(self approachX: 0)
					(self approachY: 0)
					(HandsOff)
					(curRoom setScript: sSit)
				)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(instance light6 of Prop
	(properties
		view			vCabaret
		loop			1
		cel			1
		x				63
		y				187
		z				15
		priority		15
		signal		(| fixPriOn ignrAct)
		description	"the candle in a glass"
	)
	(method (doVerb theVerb invItem)
		(light1 doVerb: theVerb invItem)
	)
)
(instance table6 of Feature
	(properties
		x 61
		y 173
		z 0
		heading 0
		nsLeft 40
		nsTop 160
		nsBottom 187
		nsRight 82
 		description "the table"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(table1 doVerb: theVerb invItem)
	)
)

(instance jokes of List)

(procedure (PrintJoke &tmp joke)
	(jokes delete: (= joke (jokes at: (Random 1 (jokes size?)))))
;	(Printf "joke number: %d" joke)
	(switch joke
		(1
			(Print "\"I told my doctor my sex life was boring.
 				He said `You need to add the element of surprise.
 				Go right home this afternoon, grab your wife,
 				and immediately make love to her, no matter what she's doing.'
				So I did!\"")
			(Print "\"It was still the same old boring crap,
 				but her bridge club got a tremendous kick out of it!!\"")
		)
		(2
			(Print "\"You know how I can tell when my wife has an orgasm?\"")
			(Print "\"She drops her nail file!!\"")
		)
		(3
			(Print "\"My wife is so ugly...\"")
			(Print "\"How ugly is she?\" you shout.")
			(Print "\"...the picture of her in my wallet is an X-ray!\"")
		)
		(4
			(Print "\"I told my psychiatrist
				`Nobody pays any attention to me.'\"")
			(Print "\"He looks at me and says, `When did you get here?'\"")
		)
		(5
			(Print "\"Didja hear about the clumsy female lawyer?\"")
			(Print "\"She dropped her briefs!\"")
		)
		(6
			(Print "\"A man walks into a doctor's office with a frog on his head.
 						The doctor says, `May I help you?'\"")
			(Print "\"The frog says, `Yeah, Doc.
				Could you take this wart off my ass?'\"")
		)
		(7
			(Print "\"Did you hear about the new, chocolate-flavored,
				contraceptive, breakfast drink?\"")
			(Print "\"It's called Ovumteen.\"")
		)
		(8
			(Print "\"My secretary is not only horny, she's ambitious.
				Today she asked me for a salary on next week's advance.\"")
		)
		(9
			(Print "\"Down the street there's a new, live, gay, sex show.\"")
			(Print "\"It's called `Anus and Andy.'\"")
		)
		(10
			(Print "\"Have ya seen the new R-rated movie about midgets?
				It features full runtal nudity.\"")
		)
		(11
			(Print "\"My uncle is a hit man for the Mob. What a great job!
 				He only goes to work when it's time to knock off.\"")
		)
		(12
			(Print "\"When I got married I asked my bride,
				`Do you perform fellatio?'\"")
			(Print "\"She said, `I dunno. I guess it depends on the fella!'\"")
		)
		(13
			(Print "\"Have you seen the impotent flasher in front of the
				wedding chapel next door?\"")
			(Print "\"We call him our `public futility.'\"")
		)
		(14
			(Print "\"I hear the pimp at Lefty's won't let his girls perform
				oral sex.\"")
			(Print "\"They call him the `Headless Whoresman.'\"")
		)
		(15
			(Print "\"What do lesbians like most about their kind of sex?\"")
			(Print "\"Tastes great.\"")
			(Print "\"Less filling.\"")
			(Print "\"Tastes great!\"")
			(Print "\"Less filling!\"")
		)
		(16
			(Print "\"Why don't sharks attack divorce lawyers?\"")
			(Print "\"Professional courtesy.\"")
		)
		(17
			(Print "\"My wife and I have a new system:
				if she wants sex, she reaches over and strokes me once.\"")
			(Print "\"If she doesn't,
				she reaches over and strokes me 300 times.\"")
		)
		(18
			(Print "\"My son just got kicked out of his co-ed college
				boarding house.")
			(Print "\"He got caught spreading roomers.\"")
		)
		(19
			(Print "\"My daughter came home from school today and tells me,
 				`Eli Whitney invented the cotton gin.'\"")
			(Print "\"I said, `Who wants a fluffy martini?'\"")
		)
		(20
			(Print "\"The shop teacher asked the high school girl if she knew
				the difference between a screw, a nail and a bolt.
				She told him she'd never been bolted.\"")
		)
		(21
			(Print "\"Have you heard about the new urologist's training film?\"")
			(Print "\"It's called, `E.T., the Extra Testicle.'\"")
		)
		(22
			(Print "\"Have you heard about the new film about marching band
				musicians?\"")
			(Print "\"It's called, `Desperately Seeking Sousa.'\"")
		)
		(23
			(Print "\"My son has a problem with compulsive masturbation.
 				His psychiatrist says he's gone completely wacky.\"")
		)
		(24
			(Print "\"My grandpa the farmer,
				finally got himself one of those latex ladies.
 				I heard him singing about it the other day:
 				`The farmer in the doll, the farmer in the doll...'\"")
		)
		(25
			(Print "\"Our local inventor just came up with a new,
				vibrating tampon.\"")
			(Print "\"He says if women have to be miserable once a month,
 				they might as well enjoy it.\"")
		)
		(26
			(Print "\"My wife just loves going to the movies to watch
 				teenagers get laid for the first time.\"")
			(Print "\"Sometimes, she even watches the screen!\"")
		)
		(27
			(Print "\"I asked my wife,
 				`How come you never tell me when you're having an orgasm?'\"")
			(Print "\"She replied, `Because you're never around!'\"")
		)
		(28
			(Print "\"My wife just loves to experiment.
 				Last week she mixed speed with her Midol.\"")
			(Print "\"She had a period six times in one day!\"")
		)
		(29
			(Print "\"My wife is a lot like Halley's Comet.\"")
			(Print "\"They both come about every 76 years!\"")
		)
		(30
			(Print "\"My son is becoming a real computer whiz!\"")
			(Print "\"His teacher called him a `master byter.'\"")
		)
		(31
			(Print "\"My uncle the inventor
				crossed a cucumber with a Mexican jumping bean.\"")
			(Print "\"He calls it the world's first organic vibrator!\"")
		)
		(32
			(Print "\"Last week, I got arrested by a female cop.
 				She asked me, `Are you carrying a concealed weapon?'\"")
			(Print "\"I replied, `Keep frisking me like that,
				and I soon will be!'\"")
		)
		(33
			(Print "\"My best sexual encounter was in the South Sea Islands...\"")
			(Print "\"I pushed a volcano into a virgin!\"")
		)
		(34
			(Print "\"Didja hear about the copier company that bought out
 				a musical instrument maker?\"")
			(Print "\"They're marketing a new reproductive organ!\"")
		)
		(35
			(Print "\"I just flew in from Coarsegold...\"")
			(Print "\"...and, boy, are my arms tired!\"")
		)
		(36
			(Print "\"What do you call a Roman with hair between his teeth?\"")
			(Print "\"Gladiator.\"")
		)
		(37
			(Print "\"I always say that little things mean a lot!\"")
			(Print "\"On the other hand, never underestimate the power of
				big, jiggly things!\"")
		)
		(38
			(Print "\"Have you heard about that new radio station in town,
				KPMS?\"")
			(Print "\"Every month they play three weeks of blues and a week of
				ragtime!\"")
		)
	)
)

(procedure (PrintLine)
	(switch (Random 0 6)
		(0
			(Print "\"Whaddsa' matter, folks? Am I keeping you up?\"")
		)
		(1
			(Print "\"You know, folks,
				it just don't get much better than this!\"")
		)
		(2
			(Print "\"Waddaya out there, a painting?\"")
		)
		(3
			(Print "\"Tough crowd. Tough crowd.\"")
		)
		(4
			(Print "\"I shoulda never given up my day gig!\"")
		)
		(5
			(Print "\"Waddaya waitin' for folks? This IS the `A' stuff!\"")
		)
		(6
			(Print "\"For this I spent four years in college?\"")
		)
	)
)
