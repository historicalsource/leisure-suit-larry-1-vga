;**
;**	Logics for Room 200 -- Taxi
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford &
;**
;**	Last Update:  June 21, 1991
;**

(script# 200)

(public
	rm200		0
)

(local
	destination
	lookedCabbie
	paidCabbie
  	destCount
	cabFare
	badgerTimer
	letsGo
)

(enum
	LOOK
	SNARL
	MOVE
	PULLOUT
	SPEEDUP
	CRUISE
	DRIVE
	SIGNAL
	SLOWDOWN
	WAITFARE
	WAITLEAVE
)

(define  BADGERTIME 400)
(define  windshieldX 158)

;(define	sCabDeparting			205)
;(define	mCab						206)

(define	pTaxi						200)
(define	vLarryInTaxi			201)
(define	vTaxiDriver				202)
	(define cFaceFront			0)
	(define cFaceLarry			8)
(define	vTaxiParts				203)
	(define lLeftLight			0)
	(define lRightLight			1)
	(define lLeftTurn				2)
	(define lRightTurn			3)
;	(define lTaxiLight			4)
	(define lDrunkDeath			6)
(define  vScenery					204)
	(define lWindshield			0)
	(define lLeftMirror			1)
	(define lRightMirror			2)

(instance rm200 of LLRoom
	(properties
		picture	200
;		style		DISSOLVE
	)
	(method (init)
		(LoadMany VIEW
			vLarryInTaxi
			vTaxiDriver
			vTaxiParts
			vScenery
		)
		(User canControl: FALSE)
		(theIconBar disable: walkIcon)
		(ego
			init:			,
			hide:,
			stopUpd:
		)
		(super init:)
		(theMusic2 fade:)
		(theMusic
			number:		mCab,
			vol:			127,
			loop:			-1,
			flags:		1,
			play:
		)
		(windshield
			cycleSpeed:	(+ howFast 5),
			init:			,
			cel:			(Random 0 4)
		)
		(driver
			cycleSpeed:	howFast,
			init:			,
		)
		(larry
			init:			,
			stopUpd:
		)
		(leftLight
			init:			,
			stopUpd:
		)
		(rightLight
			init:			,
			stopUpd:
		)
		(meter1
			init:			,
		)
		(meter2
			init:			,
		)
		(meter3
			init:			,
		)
		(meter4
			init:			,
		)
		(rightTurn
			cycleSpeed:	(+ howFast 3),
			setCycle:	Forward,
			init:			,
		)
		(trunk init:)
		(license init:)
		(meter init:)
		(if (ego has: iWine)
			(HandsOff)
			(curRoom setScript: sWineScript)
		else
			(HandsOff)
			(curRoom setScript: sRoomScript)
		)
		(Bclr fNoFawnInDisco)
			; After a drive, she'll be back
		(= badgerTimer BADGERTIME)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "You wonder if cab companies have full-time employees
					whose sole job is to make these floors sticky.")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
	(method (newRoom n)
		(theMusic fade:)
		(super newRoom: n)
	)
)

;
;	SCRIPTS
;

(instance sRoomScript of Script
	(method (doit)
		(super doit: &rest)
		(if (ego mover?)
			(if (< ((curRoom script?) state?) WAITFARE)
				(Print "\"Sorry, Mac, but my meter's runnin'.
					Where didja wanna go?\"")
				(ego setMotion: 0)
			else
				(if (not paidCabbie)
					(Bset fStiffedCabbie)
					(Print "\"Waddaya tryin' to do, peahead!
						You're not gonna stiff me, and get away with it!
						Step outside, Deadbeat!!\"")
				else
					(Points pRodeInCab ptsRodeInCab)
					(Print "Whew! You're glad to get out of here.")
				)
				(curRoom newRoom: destination)
			)
		)
		(if badgerTimer
			(-- badgerTimer)
			(if (== badgerTimer 1)
				(cond
					((< ((curRoom script?) state?) PULLOUT)
						(Print "\"So, wadda we gonna do, buddy?
							Ya just gonna sit here all night?
							Some guys gotta earn a livin', ya know!\"")
						(= badgerTimer BADGERTIME)
					)
					((== ((curRoom script?) state?) WAITFARE)
						(Print "\"So? Ya gonna pay up?
							Or does I have ta get rough wit' cha??\""
						)
						(= badgerTimer BADGERTIME)
					)
					((== ((curRoom script?) state?) WAITLEAVE)
						(Print "\"So? Ya gonna get outta my cab or not?
							Thanks for da business,
							but I didn't want ta make no career outta this!\"")
						(= badgerTimer BADGERTIME)
					)
				)
			)
		)
	)
	(method (changeState newState)
		(switchto (= state newState)
			(LOOK
				(driver
					setCycle: EndLoop self
				)
			)
			(SNARL
				(Print "The cabbie snarls a \"Where to, buddy?\"
					as he starts the meter rolling."
					#at:			85 90
					#time:		6
				)
				(meter4
					setCycle:	Forward
				)
				(rightTurn:
					setCycle:	0,
					setCel:		0,
					stopUpd:
				)
				(driver
					setCycle:	BegLoop driver
				)
				(= cycles 15)
			)
			(MOVE
				(HandsOn)
;				(taxiLight
;					cycleSpeed:	howFast,
;					setCycle: 	Forward,
;					init:			,
;				)
				(leftTurn
					cycleSpeed:	(+ howFast 3),
					setCycle:	Forward,
					init:,
				)
				(rightLight
					setCel:			0,
					stopUpd:
				)
				(leftLight
					setCel:			0,
					stopUpd:
				)
				(windshield
					setCycle:	Forward
				)
				(leftMirror
					cycleSpeed:	(+ howFast 5),
					setCycle:	Forward,
					init:,
				)
				(rightMirror
					setCycle:	Forward,
					cycleSpeed:	(+ howFast 5),
					init:,
				)
				(= cycles 60)
			)
			(PULLOUT
				(leftTurn
					setCycle:		0,
					setCel:			0,
					stopUpd:
				)
				(if
					(or
						destCount
						letsGo
					)
					(= cycles 1)
				)
			)
			(SPEEDUP
				(User canControl: FALSE)
				(theIconBar disable: walkIcon)
				(if (windshield cycleSpeed?)
					(windshield
						cycleSpeed: (- (windshield cycleSpeed?) 1),
					)
					(leftMirror
						cycleSpeed: (- (windshield cycleSpeed?) 1),
					)
					(rightMirror
						cycleSpeed: (- (windshield cycleSpeed?) 1),
					)
					(theMusic3
						send: 9 mPITCHBEND (* 1000 (- 8 (windshield cycleSpeed?)))
					)
					(-- state)
					(= cycles 10)
				else
					(= cycles 1)
				)
			)
			(CRUISE
				(if letsGo
					(= cycles 1)
				)
			)
			(DRIVE
				(if (!= destination prevRoomNum)
					(= seconds (Random 12 18))
				else
					(= seconds 4)
				)
			)
			(SIGNAL
				(rightTurn
					setCycle:	Forward
				)
				(leftLight
					setCel:		1,
					stopUpd:
				)
				(rightLight
					setCel:		1,
					stopUpd:
				)
				(= cycles 1)
			)
			(SLOWDOWN
				(if (< (windshield cycleSpeed?) 5)
					(windshield
						cycleSpeed: (+ (windshield cycleSpeed?) 1),
					)
					(leftMirror
						cycleSpeed: (+ (windshield cycleSpeed?) 1),
					)
					(rightMirror
						cycleSpeed: (+ (windshield cycleSpeed?) 1),
					)
					(theMusic3
						send: 9 mPITCHBEND (* 1000 (- 8 (windshield cycleSpeed?)))
					)
					(-- state)
					(= cycles 10)
				else
					(= cycles 1)
				)
  			)
			(WAITFARE
				(meter4
					setCycle: 	0,
					setCel:
					(if (== (meter4 cel?) 9)
						0
					else
						(meter4 cel?)
					)
				)
				(windshield
					setCycle:	0,
					stopUpd:
				)
				(leftMirror
					setCycle:	0,
					stopUpd:
				)
				(rightMirror
					setCycle:	0,
					stopUpd:
				)
;				(taxiLight
;					setCycle:	0,
;					stopUpd:
;				)
				(= badgerTimer BADGERTIME)
				(User canControl: TRUE)
				(theIconBar enable: walkIcon)
				(Printf "\"Well, here we are, buddy. That'll be $%d.00.\""
					cabFare)
			)
			(WAITLEAVE
				(= badgerTimer BADGERTIME)
				(Print "\"Thanks, buddy. Maybe I'll see ya again sometime!\"")
			)
		)
	)
)

(instance sWineScript of Script
	(method (changeState newState &tmp carSpeed)
		(switchto (= state newState)
			(
				(driver
					setCycle:	EndLoop self
				)
			)
			(
				(Print "The driver looks at you and says,
					\"Hey! What's that you got in your coat there?
					Is that wine?\"")
				(Print "Before you can move, the cabbie grabs your precious box
					of wine and guzzles it all down!
					Ohmigawd, Larry, hang on!!")
				(driver
					cel:		0
				)
;				(taxiLight setCycle: Forward)
				(rightTurn
					setCycle:	Forward
				)
				(windshield
					setCycle:	Forward
				)
				(leftMirror
					cycleSpeed:	(+ howFast 5),
					setCycle:	Forward,
					init:,
				)
				(rightMirror
					cycleSpeed:	(+ howFast 5),
					setCycle:	Forward,
					init:,
				)
				(meter4
					setCycle:	Forward
				)
				(= cycles 20)
			)
			(
				(= carSpeed (Random 0 3))
				(rightLight
					setCel:		0,
					stopUpd:
				)
				(leftLight
					setCel:		0,
					stopUpd:
				)
				(windshield
					x:
					(if (mod register 2)
						(if (Random 0 1)
							(- windshieldX 20)
						else
							(+ windshieldX 20)
						)
					else
						windshieldX
					),
					cycleSpeed: carSpeed,
				)
				(leftMirror
					cycleSpeed: carSpeed,
				)
				(rightMirror
					cycleSpeed: carSpeed,
				)
				(if (== register 5)
					(Print "Brace yourself, Larry.
						He's swerving all over the road!")
				)
				(if (< register 10)
					(-- state)
					(++ register)
					(= cycles (Random 5 10))
				else
					(windshield x: (- windshieldX 20))
					(= cycles 10)
				)
			)
			(
				(Print "It seems a bridge abutment has brought your evening
					in Lost Wages to a sudden, final, and complete halt!")
				(= seconds 3)
			)
			(
				(Death vTaxiParts lDrunkDeath TRUE)
				(Format @deathTitleStr "No air bag, eh?")
				(GameOver "Remember, Larry:
					\"Friends don't let cabbies drive drunk!\"")
			)
		)
	)
)

;
;	PROPS
;

(instance driver of Prop
	(properties
		view			202
		loop			0
		cel			0
		x				101
		y				173
		z				100
		priority		9
		signal		fixPriOn
		description "the driver"
	)
	(method (cue)
		(super cue:)
		(self stopUpd:)
	)
	(method (doVerb theVerb invItem &tmp tmpDest)
		(switch theVerb
			(verbLook
				(if
					(not lookedCabbie)
					(= lookedCabbie TRUE)
					(Print "You wish you hadn't. Mr. America this guy ain't.
						His strongest feature is his aroma!")
				else
					(Print "The cabbie wonders why you keep looking at him,
						but in the mirror he flashes you a coy little smile
						and a knowing wink.")
					(Print "(Your stomach turns, and not from the ride!)" LOW)
				)
			)
			(verbDo
				(Print "You can't reach the cabbie from your seat.
					Why not talk to him?")
			)
			(verbZipper
				(Print "Yuck! Your stomach turns at the thought.")
			)
			(verbSmellTaste
				(Print "This cab smells terrible!")
			)
			(verbTalk
				(cond
					((or
						(== ((curRoom script?) state?) SIGNAL)
						(== ((curRoom script?) state?) SLOWDOWN)
					 )
					 	(Print "\"We're there already;
							just sit tight, willya, Buddy?\"")
					)
					((== ((curRoom script?) state?) WAITFARE)
						(Print "\"So? Ya gonna pay up?
							Or does I have ta get rough wit' cha??\""
						)
						(= badgerTimer BADGERTIME)
					)
					((== ((curRoom script?) state?) WAITLEAVE)
						(Print "\"So? Ya gonna get outta my cab or not?
							Thanks for da business,
							but I didn't want ta make no career outta this!\"")
						(= badgerTimer BADGERTIME)
					)
					(else
						(switch destCount
							(0
								(if (not (Btst pRodeInCab))
									(Print "You ask the cabbie, \"Where's the action?\"")
									(Print "\"Lookin' for action, eh?
										Dis town is really full of it.
										We gotta disco full of foxes this time of night.
										Den dere's the casino,
										next to the all-night weddin' chapel.
										And we gotta lovely bar, but you've already been dere.
										Me, I like da convenience store next to da disco,
										but of course, that's just personal preference.\"")
								)
								(= destination -1)
								(theGame setCursor: ARROW_CURSOR TRUE)
								(Animate (cast elements?) FALSE)
								(while (< destination 1)
									(= destination
										(PrintD {\"Where to, Bub?\" snorts the cabbie.}
											#new
											#button	{_Disco_} 				600
											#button  {_Casino_}				300
											#button  {_Wedding Chapel_}	400
											#new
											#button  {Convenience Store}	500
											#button  {Lefty's Bar}			100
										)
									)
								)
								(theGame setCursor: ((theIconBar curIcon?) cursor?) TRUE)
								(+= cabFare (+ (Random 0 10) cabFareMin))
								(if (!= destination prevRoomNum)
									(Print "\"Ok. You got it, Mac!\"")
								else
									(Print "\"It won't take us long to get back dere,
										but I'll have to circle 'round da block.\"")
								)
								(if (< cabFareMin 40)
									(++ cabFareMin)
								)
								(= letsGo TRUE)
;								(if (== ((curRoom script?) state?) CRUISE)
;									(sRoomScript start: DRIVE)
;									(curRoom setScript: sRoomScript)
;								)
							)
							(1
								(Print "\"Uh, excuse me, Mr. Driver,\" you say,
									\"but I think I may wish to change my mind
									about my destination this evening.
									Would that be all right with you?\"")
								(theGame setCursor: ARROW_CURSOR TRUE)
								(Animate (cast elements?) FALSE)
								(= tmpDest
									(PrintD {\"Yeah, OK, but it'll cos' ja extra!\" the
										cabbie snarls.}
										#new
										#button	{_Disco_} 				600
										#button  {_Casino_}				300
										#button  {_Wedding Chapel_}	400
										#new
										#button  {Convenience Store}	500
										#button  {Lefty's Bar}			100
									)
								)
								(theGame setCursor: ((theIconBar curIcon?) cursor?) TRUE)
								(if (== tmpDest -1)
									(= tmpDest destination)
								)
								(if (== tmpDest destination)
									(Print "\"Geez, some guys jes' can't make up der minds!\"")
								else
									(= destination tmpDest)
									(+= cabFare cabFareMin)
									(if (== ((curRoom script?) state?) CRUISE)
										(sRoomScript start: DRIVE)
										(curRoom setScript: sRoomScript)
									)
								)
							)
							(else
								(Print "\"Uh, excuse me, Mr. Driver,\" you say,
									\"but I think I may wish to change my mind yet again
									about my destination this evening.
									Would that be too much trouble?\"")
								(Print "\"Nah, ya changed your min' already!
									I ain' gonna spen' all night driving you in circles!
									Next time, figgur out where ya wanna go before ya gets
									in da cab!\"")
							)
						)
						(if (== (sRoomScript state?) PULLOUT)
							(sRoomScript
								cue:
							)
						)
						(++ destCount)
					)
				)
			)
			(verbUse
				(switch invItem
					(iWallet
						(cond
							((== ((curRoom script?) state?) WAITFARE)
								(if (< larryDollars cabFare)
									(Print "Sheepishly, you apologize to the cabbie
										for not carrying enough money to pay for the
										ride.")
									(Print "\"Waddaya mean, `no money!!'\"
										snarls the driver.
										\"Step outsid'a ma office, chump!\"")
									(Bset fStiffedCabbie)
									(curRoom newRoom: destination)
								else
									(-= larryDollars cabFare)
									(= paidCabbie TRUE)
									(sRoomScript start: WAITLEAVE)
									(curRoom setScript: sRoomScript)
								)
							)
							((== ((curRoom script?) state?) WAITLEAVE)
								(Print "\"I appreciate your offer of a tip,
									but it's against my personal
									philosophy to accept gratuities!\"")
							)
							(else
								(Print "\"Why don'cha jes' wait'll we git there,
									buddy?\"")
							)
						)
					)
					(iBreathSpray
						(Print "You quietly and carefully spritz a little
							spray around, vainly trying to kill some of this odor!")
					)
					(iApple
						(Print "\"Nah, fruit gives me gas!\" he grunts.")
					)
					(iWatch
						(Print "\"Hey, hurry up,\" you cry,
							\"can't you see it's getting late?!\"")
						(Print "\"Time waits for no man!\" he replies cryptically.")
					)
					(iRing
						(Print "\"I got no use for a diamond ring,\" shouts the
							cabbie, \"especially one wit a stone like dat!
							Dat sucker sure looks fake to me!\"")
					)
					(iWhiskey
						(Print "\"No, thanks, buddy,\" replies the cabbie,
							\"but have you got any wine?\"")
					)
					(iRemoteControl
						(Print "\"What's that, a tricorder?\" he asks.
							\"Don't tell me you're one of dem science fiction
							freaks?\"")
					)
					(iLubber
						(Print "\"Well, ain't chu all growed up!\"")
					)
					(iCandy
						(Print "\"Thanks, buddy,\" says the cabbie,
							as he grabs your box of candy and proceeds to
							gobble down every piece!")
						(ego put: iCandy 0)
						(Print "(Bad move, Larry! You feel certain you're going
							to meet someone someday who would really enjoy
							a piece!)" LOW)
					)
					(iDiscoPass
						(Print "\"No, I don't want to go dancin' with you later,\"
							says the cabbie.")
					)
					(iGraffiti
						(Print "\"Not HIM again! Geez, does that guy get around.
							And I DO mean around!\"")
					)
					(iPocketKnife
						(Print "The air in here IS thick enough to cut!")
					)
					(iMagazine
						(Print "\"No, thanks,\" says the cabbie,
							\"I gotta subscription!\"")
					)
					(iHammer
						(Print "Don't hit the cabbie's head with your hammer;
							you might hurt it.")
						(Print "(The hammer, that is!)" LOW)
					)
					(iPills
						(Print "\"Hey, thanks!\" he cries.
							\"One of those and I'll be stuck in fifth gear all
							night.\"")
						(ego put: iPills 0)
						(Print "(Good idea, Larry! After all you had to go through
							to get those pills!)" LOW)
					)
					(iRibbon
						(Print "\"Wat a coinci-dince!
							I wuz jes talkin' 'bout ribbon like dat with dis
							stacked babe fare I hadda little while ago.
							She wuz tellin' me 'bout how she tied up some dumb dweeb
							and took all his dough.
							Boy, did we have a good laugh at that one!
							You shouldda been here!!\"")
						(Print "(Somehow you feel you were!)" LOW)
					)
					(else
						(Print "\"Here, take this!\" you offer.")
						(Print "\"Nah, I got no need for that!\" he replies.")
					)
				)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance larry of Prop
	(properties
		view			201
		loop			0
		cel			0
		x				204
		y				86
		z				14
		priority		10
		illegalBits	$0000
		signal		(| fixPriOn ignrAct)
		description "you"
		lookStr 		"It's not often you get to see the back of
						your own head, eh, Lar'?"
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "You scratch your head in amazement at the way this cab
					can still operate with such severe rear wheel camber!")
			)
			(verbTalk
				(Print "\"Talkin' to yourself, eh?\" smirks the cabbie.
					\"A guy in a job like this runs into lots o' weirdos!\"")
			)
			(else
				(Print "Leave yourself alone, Larry!")
			)
		)
	)
)

(instance windshield of Prop
	(properties
		view			204
		loop			0
		cel			0
		x				windshieldX
		y				85
		description "the street"
		lookStr
		"He sure drives fast, doesn't he?"
	)
)

(instance leftMirror of Prop
	(properties
		view			204
		loop			1
		cel			0
		x				52
		y				79
		detailLevel	1
		description "the mirror"
	)
)

(instance rightMirror of Prop
	(properties
		view			204
		loop			2
		cel			2
		x				266
		y				87
		detailLevel	1
		description "the mirror"
	)
)

(instance leftLight of View
	(properties
		view			203
		loop			0
		cel			1
		x				38
		y				114
		priority		14
		signal		fixPriOn
		description "the light"
	)
)

(instance rightLight of View
	(properties
		view			203
		loop			1
		cel			1
		x				278
		y				108
		priority		14
		signal		fixPriOn
		description "the light"
	)
)

(instance leftTurn of Prop
	(properties
		view			203
		loop			2
		cel			0
		x				45
		y				129
		priority		14
		signal		fixPriOn
		detailLevel	1
		description "the turn signal"
	)
)

(instance rightTurn of Prop
	(properties
		view			203
		loop			3
		cel			1
		x				275
		y				128
		priority		14
		signal		fixPriOn
		detailLevel	1
		description "the turn signal"
	)
)

(instance meter1 of Prop
	(properties
		view			203
		loop			5
		cel			0
		x				162
		y				62
		priority		14
		signal		fixPriOn
		description "the meter"
	)
	(method (doVerb theVerb invItem)
		(meter4 doVerb: theVerb invItem)
	)
)

(instance meter2 of Prop
	(properties
		view			203
		loop			5
		cel			0
		x				169
		y				62
		priority		14
		signal		fixPriOn
		description "the meter"
	)
	(method (doVerb theVerb invItem)
		(meter4 doVerb: theVerb invItem)
	)
)

(instance meter3 of Prop
	(properties
		view			203
		loop			5
		cel			0
		x				176
		y				62
		priority		14
		signal		fixPriOn
		description "the meter"
	)
	(method (doit)
		(super doit: &rest)
		(if (== (meter4 cel?) 9)
			(if (== (self cel?) 9)
				(self cel: 0)
				(if (== (meter2 cel?) 9)
					(meter2 cel: 0)
					(if (== (meter1 cel?) 9)
						(meter1 cel: 0)
					else
						(meter1 cel: (+ (meter1 cel?) 1))
					)
				else
					(meter2 cel: (+ (meter2 cel?) 1))
				)
			else
				(self cel: (+ (self cel?) 1))
			)
		)
	)
	(method (doVerb theVerb invItem)
		(meter4 doVerb: theVerb invItem)
	)
)

(instance meter4 of Prop
	(properties
		view			203
		loop			5
		cel			0
		x				184
		y				63
		priority		14
		signal		fixPriOn
		description "the meter"
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "It seems to be running a little fast,
					but then, this is an expensive town!")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)

)

;(instance taxiLight of Prop
;	(properties
;		view			203
;		loop			4
;		cel			0
;		x				154
;		y				8
;		priority		14
;		signal		(| ignrHrz fixPriOn)
;		detailLevel	2
;		description "the light"
;	)
;)

;new features
(instance meter of Feature
	(properties
		x 170
		y 86	;1 higher than street
		z 27
		heading 0
		nsLeft 154
		nsTop 53
		nsBottom 66
		nsRight 186
 		description "the taxicab's fare meter"
		lookStr "It seems to bear no relationship whatsoever
					to the rate he charges!"
		sightAngle 40
	)
)
(instance license of Feature
	(properties
		x 167
		y 109
		z 0
		heading 0
		nsLeft 140
		nsTop 95
		nsBottom 123
		nsRight 195
 		description "the cab's license plate"
		lookStr "Isn't that unusual? Most taxicabs don't have
					customized license plates.
					You wonder if there's any special significance
					to the driver."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "You don't really want to steal his license plate!")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance trunk of Feature
	(properties
		x 163
		y 100
		z 0
		heading 0
		nsLeft 96
		nsTop 71
		nsBottom 130
		nsRight 230
 		description "the trunk lid"
		lookStr "You wonder what the cabbie has hauled in
					there over the years!"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "You don't have the key to the trunk.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
