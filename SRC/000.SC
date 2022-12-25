;**
;**	Logics for Room 000 -- Everywhere
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford
;**
;**	Last Update:  June 22, 1991
;**

(script# 0)

(include	menu.sh)

(procedure
	NormalEgo
	HandsOff
	HandsOn
	HaveMem
	SteppedOn
	Btst
	Bset
	Bclr
	Btgl
	GameOver
	Points
	Face
	DoStatus
	SteppedFullyOn
	Babble
;	LLDrawBox
	InitEgoHead
	LarryHours
	LarryMinutes
	LarrySeconds
	Death
	ObjInRoom
)

(public
	LSL1				0
	NormalEgo		1
	HandsOff			2
	HandsOn			3
	HaveMem			4
	SteppedOn		5
	Btst				6
	Bset				7
	Bclr				8
	Btgl				9
	GameOver			10
	Points			11
	Face				12
	DoStatus			13
	SteppedFullyOn	14
	Babble			15
;	LLDrawBox		16
	InitEgoHead		17
	LarryHours		18
	LarryMinutes	19
	LarrySeconds	20
	Death				21
	ObjInRoom		22
	spraySound		23
	deathIcon		24
	icon0				25
)


(instance egoObj of LLEgo
	(properties
		name			"ego"
		view			vLarryWalking
		illegalBits $8000
		sightAngle	180
		description	"you"
		lookStr		"You are wearing the latest in fashion --
			if you consider 1973 late!"
	)

	(method (doVerb theVerb invItem)
		(Cls)
		(switch theVerb
			(verbTalk
				(Print "You talk to yourself, and are amazed to discover you
					already know what you're going to say.")
			)
			(verbDo
				(if (Btst fWearingRubber)
					(Bclr fWearingRubber)
					(ego put: iLubber 0)
					(Points pRemovedRubber ptsRemovedRubber)
					(Print "Good idea. You carefully remove your lubber and
						dispose of it in a way that will not be described here.")
				else
					(Print "Carefully exploring your leisure suit,
						you find it stylish but empty.")
				)
			)
			(verbZipper
				(Print "Larry!
					The whole point of this game was to stop doing that!")
			)
			(verbSmellTaste
				(if
					(Btst fSmellsLikeDogPiss)
					(Print "\"Phew! You smell like a used fire hydrant!\"")
				else
					(if (Random 0 1)
						(Print "Sniffing your leisure suit thoroughly,
							you conclude there's nothing quite like the fragrance of
							fresh polyester! Ahhh!")
					else
						(Print "Licking your leisure suit leads you to the conclusion
							that polyester doesn't mean good taste!")
				 	)
				)
			)
			(verbUse
				(switch invItem
					(iWallet
						(Print "You pay yourself a few bucks, which you decide
							to keep in your wallet.")
					)
					(iBreathSpray
						(cond
							;see if there's a script running?
							((curRoom script?)
								(Print "Now?!")
							)
							((and (!= (ego view?) vLarryWalking)
									(!= (ego view?) vLarryStopping))
								(Print "Now?!")
							)
							(else
								(curRoom setScript: (ScriptID SPRAY 0))
							)
						)
					)
					(iWatch
						(Print "You don't need to wind your watch;
							it's self-winding.")
						(Print "(And it seems lately, you've been winding it
							plenty!)" LOW)
					)
					(iApple
						(Print "The apple tastes delicious. You devour it all.")
						(ego put: iApple 0)
					)
					(iRing
						(Print "Slipping the magic ring onto your finger gives
							you powers far beyond those of...")
						(Print "...Oops. Wrong game.")
					)
					(iWhiskey
						(ego put: iWhiskey 110)
						(= spraySeconds 10)
						(Print "You slam down the shot of cheap booze,
							then with a flourish, break the glass on the floor!")
						(Print "(Pretty dramatic, eh?)" LOW)
					)
					(iRemoteControl
						(Print "Although it appears to do nothing, aiming the
							remote control at your groin while rapidly pressing the
							channel selector does make you feel quite silly.")
					)
					(iRose
						(Print "You prick yourself with the rose.")
					)
					(iLubber
						(if (Btst fWearingRubber)
							(Bclr fWearingRubber)
							(ego put: iLubber 0)
							(Points pRemovedRubber ptsRemovedRubber)
							(Print "Good idea. You carefully remove your lubber and
								dispose of it in a way that will not be described here.")
						else
							(Print "This is hardly the place for that!")
						)
					)
					(iCandy
						(Print "The candy tastes so delicious, you consume it all.
							Now you're worried about your acne.")
						(ego put: iCandy 0)
					)
					(iPocketKnife
						(Print "Come on, Larry! Don't lose hope!
							There's no need to commit suicide...
							at least, not yet!")
					)
					(iWine
						(ego put: iWine 510)
						(= spraySeconds 15)
						(Print "You quickly down all the cheap wine,
							crumple the box, and toss it away.
							Your thirst is gone.")
						(Print "And your breath is revolting!" LOW)
					)
					(iHammer
						(Print "Pounding yourself with the hammer is ONE idea;
							it's just not a GOOD idea!")
					)
					(iPills
						(Print "Without thinking, you swallow the entire bottle
							of pills. \"Say, this stuff is ok!\" you think.")
						(Print "But, wait! Soon your breathing becomes heavy.
							Soon you become horny!
							Soon you must have relief!
							You've got to do something, and NOW!")
						(Print "You wonder about the location of that cute,
							little dog you met outside.")
						(Death vJail 0 1)
						(Format @deathTitleStr "You've always loved animals!")
						(GameOver "Following your conviction for bestiality,
							you spend the rest of your life in prison,
							turning big rocks into little ones!")
					)
					(iRibbon
						(Print "You wrap the pink ribbon carefully around your
							head, before deciding the \"Shirley Temple look\"
							just isn't your bag!")
					)
					(else
						(super doVerb: theVerb)
					)
				)
			)
			(else
				(super doVerb: theVerb)
			)
		)
	)
)

;***************************************
;	our ego's head
;***************************************

(instance egoHead of Head
	(properties
		view			vLarryStopping
		description	"your head"
	)
	(method (doVerb theVerb invItem)
		(Cls)
		(switch theVerb
			(verbLook
				(Print "It's a face only a mother could love!")
			)
			(verbDo
				(Print "You stick your finger deep into your ear until that
					itch goes away.  You hope no one is watching!")
			)
			(else
				(ego doVerb: theVerb invItem)
			)
		)
	)
)

;***************************************
; a sound used by Points procedure only
;***************************************

(instance pointsSound of Sound
	(properties
		number 		sScoredPoints
		flags			1
	)
)

;***************************************
; a sound used by breathSpray script
;***************************************

(instance spraySound of Sound
	(properties
		number 		sBreathSpray
		loop			1
		vol			127
		flags			1
	)
)


;***************************************
; the default grooper uses ego's current view.
; if he's stopped in his StopWalk view, it looks like he's standing
; on a turntable when you press a dir key.
; this instance puts him to his walking view before turning.
;***************************************

(instance stopGroop of GradualLooper
	(method (doit)
		(if (and
				(IsObject (ego cycler?))
				((ego cycler?) isKindOf: StopWalk)
			 )
			(ego view:	((ego cycler?) vWalking?))
		)
		(super doit: &rest)
	)
)

;***************************************
; an instance of cycling icon used by Babble
;***************************************

; cuz of problems with TalkIcon, i'm making it a DCIcon &
; redefining the init method to use RandCycle

(instance babbleIcon of DCIcon
	(method (init)
		((= cycler (Forward new:)) init: self) ;can have number after self for # of cycles
	)
)

; ******* EventHandlers *****************************************************

(instance ll1KDHandler	of EventHandler)	;just gets keyDown events
(instance ll1MDHandler 	of EventHandler)	;just gets mouse events
(instance ll1DirHandler of EventHandler)	;just gets direction events


; ******* the game *********************************************************

(instance LSL1 of Game
	(method (init &tmp foo num [str 5])

		; make our instance of grooper global
		(= sGrooper stopGroop)

		; we use bordered window
		(= systemWindow ll1Win)

		(= version {x.yyy})

		(super init:)

		; set up for the default feature doVerb thing
		(= doVerbCode ll1DoVerbCode)
		(= ftrInitializer ll1FtrInit)

		; initialize handlers
		((= keyDownHandler	ll1KDHandler) 	add:)
		((= mouseDownHandler	ll1MDHandler)	add:);;; cast features
		((= directionHandler	ll1DirHandler) add:)
		(= pMouse PseudoMouse)

		; make our instance of ego global
		((= ego egoObj)
			_head:		(= eHead egoHead),
		)

		((ego _head?) client: ego)


		; init disposable ll1
		((ScriptID LL1INIT 0) init:)
		(UnLoad SCRIPT LL1INIT)

		; set up some global sounds
		((= theMusic longSong)
			init:,
			owner: self,
			flags: 1,
		)
		((= theMusic2 longSong2)
			init:,
			owner: self,
			flags: 1,
		)
		((= theMusic3 longSong3)
			init:,
			owner: self,
			flags: 1,
		)

		((ScriptID INVENTORY 0) init:)	; set up inventory

		; set up icon bar
		((= theIconBar IconBar)
			invLoop:		0,
			add:
			icon0
			icon1
			icon2
			icon3
			icon6
			icon7
			icon4
			icon5
			icon8
			icon9,
			eachElementDo: #init:,
			eachElementDo: #highlightColor: colLBlue,
			curIcon:			icon0,
			useIconItem:	icon4,
			helpIconItem:	icon9,
			disable:,
		)
		(icon5 message:
			(if (HaveMouse)
				SHIFTTAB
			 else
			 	TAB
			)
		)

		(gcWin
			color:			0,	; text
			back:				colWindow,
		)

		; set up control panel
		(GameControls
			window:	gcWin,
			add:
				iconOk
				(detailSlider
					theObj:			theGame,
					selector: 		#detailLevel,
					topValue:		3,
					bottomValue		0,
					yourself?
				)
				(volumeSlider
					theObj:			theGame,
					selector: 		#masterVolume,
					topValue:		(if (> numVoices 1) 15 else 1), ; less range for pc
					bottomValue:	0,									  ; single-voice folks
					yourself?
				)
				(speedSlider
					theObj:			speedORama,
					selector: 		#doit,
					topValue:		0,
					bottomValue:	5,			;**	was 15
					yourself?
				)
				(iconSave
					theObj: 			theGame,
					selector: 		#save,
					yourself?
				)
				(iconRestore
					theObj: 			theGame,
					selector: 		#restore,
					yourself?
				)
				(iconRestart
					theObj: 			theGame,
					selector: 		#restart,
					yourself?
				)
				(iconQuit
					theObj:			theGame,
					selector: 		#quitGame,
					yourself?
				)
				(iconAbout
					theObj:	 		(ScriptID ABOUT 0),
					selector:		#doit,
					yourself?
				)
				iconHelp,

			eachElementDo:	#highlightColor:	colLBlue,
			eachElementDo:	#lowlightColor:	colWindow,
			helpIconItem:	iconHelp,
			curIcon:			iconSave
		)

		; set global restartRoom so speedtest knows where to go when it's done
		(if (GameIsRestarting)
			(= restartRoom 100)

;******	REMOVE BEFORE SHIPPING: this else is for testing
		else
			(= str 0)
			(= num 0)
			(= num
				(PrintD {Where to, Larry?}
					#edit	@str 5
					#new
					#button {Intro}	710
					#button {Lefty's} 100
					#button {Disco}	600
					#new
					#button {Casino}	300
					#button {Chapel}	400
					#button {Store}	500
				)
			)
			(if str
				(= num (ReadNumber @str))
			)
			(if (< num 100)
				(= num 100)
			)
			(= restartRoom num)
			(HandsOn)
			(theIconBar enable:)
;******	End testing else



;This else is for shipping
;**					else
;**	  					(= restartRoom 710)
;end shipping else



		)
		(theDefaultFeature init:)


		;THIS IS FOR TESTING
		; set logger path to empty so it will ask for drive:dir first time only
;**			(StrCpy @sysLogPath {})

		(self newRoom:	SPEEDTEST)

	);END LL1 INIT

	(method (pragmaFail &tmp theVerb theItem)
		(if (User canInput?)
			(= theVerb ((User curEvent?) message?))
			(if (IconBar curInvIcon?)
				(=	theItem ((IconBar curInvIcon?) description?))
			else
				(= theItem (theDefaultFeature description?))
			)
			(Cls)
			(switch theVerb
				(verbLook
					(Print "It's not much to look at.")
				)
				(verbTalk
					(Print "To whom were you trying to speak?")
				)
				(verbDo
					(Print "There's nothing to do there.")
				)
				(verbUse
					(switch theItem
						(((Inventory at: iRibbon) description?)
							(Print "You gaily display your bright pink ribbon
								for all to see.")
						)
						(((Inventory at: iPocketKnife) description?)
							(Print "Kill! Kill! Kill!")
							(Print "(Doesn't anybody ---- anymore?)" LOW)
						)
						(else
							(Printf "Try finding some place else to place %s."
								theItem)
						)
					)
				)
				(verbZipper
					(cond
						;see if there's a script running?
						((curRoom script?)
							(Print "Now?!")
						)
						((and (!= (ego view?) vLarryWalking)
								(!= (ego view?) vLarryStopping))
							(Print "Now?!")
						)
						(else
							(Print "You coyly turn your back and relieve yourself.
								You quickly feel much better.
								The hint of a smile crosses your lips.")
						)
					)
				)
				(verbSmellTaste
					(Print "You really don't want to put your face near that.")
				)
			)
		)
	)

	(method (quitGame)
		(babbleIcon
			view:			vQuitControls,
			loop:			0,
			cycleSpeed:	(* (+ howFast 1) 4), ; faster on slower machines
		)
		(theGame setCursor: ARROW_CURSOR TRUE)
		(Animate (cast elements?) FALSE)
		(super quitGame:
			(Print "No! Don't leave me all alone inside your computer!
				You can't imagine the things your CPU does to me when
				you're not watching!"
				#button:	{Stop Whining}		1
				#button:	{Oh, All Right}	0
				#icon:	babbleIcon
				#title:	{Don't give up. It's still early!}
			)
		)
	)

	(method (startRoom n)
		(if pMouse
			(pMouse stop:)
		)
		(Cls)
		((ScriptID DISP) doit: n)			;**	dump load-as-needed code
		(UnLoad SCRIPT DISP)




;**	REMOVE BEFORE SHIPPING:  check for frags
		(if (and
				(!= (- (MemoryInfo FreeHeap) 2) (MemoryInfo LargestPtr))
				(Print "Memory fragmented."
					#button:{Who cares} 0
					#button:{Debug} 1
				)
			 )
			(SetDebug)
		)
		(if debugOn
			(SetDebug)
		)



		; memory empty; load it back up.

		(if (OneOf n SIDEWALKROOMS)
			(ScriptID SIDEWALK)
		)


		(if (OneOf n CLOSEUP_ROOMS)
			(icon0
				loop: 	lExitIcon,
				cursor:	EXIT_CURSOR,
			)
		else
			(icon0
				loop: 	lWalkerIcon,
				cursor:	WALK_CURSOR,
			)
		)
		(if (== (theIconBar curIcon?) icon0)
			(theGame setCursor: (icon0 cursor?) TRUE)
		)

		(CueObj 
			client:		NULL, 
	 		theVerb:   	NULL,
			theInvItem:	NULL
		)
;**	REMOVE BEFORE SHIPPING:
		((ScriptID DEBUG 0) init:)



		;FINALLY, let the room load
		(super startRoom: n)

		(if (cast contains: ego)
			(if (and
					(ego normal?)
					(not ((ego cycler?) isKindOf: StopWalk))
				)
				; put stopwalker on him, use default big view
		 		(ego setCycle:	StopWalk vLarryStopping)
			)
			(if (not (ego looper?))
				(ego setLoop:	stopGroop)
			)
			; init ego's head at each new room so it's always in the cast
			(InitEgoHead (egoHead view?))
		)
	);END LL1 STARTROOM

	(method (handleEvent event &tmp saveIcon)
		(super handleEvent: event)
		(if (event claimed?) (return TRUE))
		(if (or (== curRoomNum 710)
					(== curRoomNum 720)
					(== curRoomNum 803))
			(return (event claimed: TRUE))
		else
			(switch (event type?)
				(keyDown
					(switch (event message?)
						; inventory was invoked from keyboard
						; but only if inventory icon is active
						(`#7 						; restore
							(theGame restore:)
							(theGame setCursor: ((theIconBar curIcon?) cursor?) TRUE)
							(event claimed: TRUE)
						)
						(`^q 						; quit
							(theGame quitGame:)
							(event claimed: TRUE)
						)
						(`+
							(if (> (theGame egoMoveSpeed?) 0)
								(theGame egoMoveSpeed: (- (theGame egoMoveSpeed?) 1))
								(if (User canControl?)
									(ego egoSpeed:	(theGame egoMoveSpeed?))
								)
							)
						)
						(`-
							(if (< (theGame egoMoveSpeed?) 15)
								(theGame egoMoveSpeed: (+ (theGame egoMoveSpeed?) 1))
								(if (User canControl?)
									(ego egoSpeed:	(theGame egoMoveSpeed?))
								)
							)
						)
						(`=
							(theGame egoMoveSpeed: larrySpeed)
							(if (User canControl?)
								(ego egoSpeed:	(theGame egoMoveSpeed?))
							)
						)
						(TAB
							(if (not (& (icon5 signal?) DISABLED))
								(Inventory showSelf: ego)
							)
						)
						(SHIFTTAB
							(if (not (& (icon5 signal?) DISABLED))
								(Inventory showSelf: ego)
							)
						)
						(`^c						;**	control panel
							(theGame setCursor: ARROW_CURSOR TRUE)
							(icon8 select:)
						)
						(`#2 						; volume
							(cond
								((theGame masterVolume:)
									(theGame masterVolume: 0)
								)
								((> numVoices 1)
									(theGame masterVolume: 15)
								)
								(else
									(theGame masterVolume: 1)
								)
							)
							(event claimed: TRUE)
						)
						(`#5 						; save
							(theGame save:)
							(theGame setCursor: ((theIconBar curIcon?) cursor?) TRUE)
							(event claimed: TRUE)
						)


;;**	REMOVE BEFORE SHIPPING:  so they don't accidentally restart the game
;						(`#9 						; restart
;							(theGame restart:)
;							(event claimed: TRUE)
;						)


;**	REMOVE BEFORE SHIPPING:
;						(`@L
;							(if (HaveMem $600)
;						  		((ScriptID LOGGER) doit: @sysLogPath 0)
;							else
;								(Print "Not Enough Memory!!")
;							)
;						)


					)
				)
			)
		)
	)	;*	END OF handleEvent

	(method (restart)
		(curRoom
			style:		IRISIN,
			drawPic: 	pBlackScreen
		)
		(cast eachElementDo: #hide:)
		(super restart:)
	)

	(method (doit)
		;** Update larry's watch **
		(if (!= oldSysTime (GetTime SYSTIME1))
			(= oldSysTime (GetTime SYSTIME1))
			(++ larryTime)
			(if (== (& larryTime $003C) $003C)
				(+= larryTime $0040)
				(&= larryTime $FFC0)
				(if (== (& larryTime $0F00) $0F00)
					(+= larryTime $1000)
					(&= larryTime $F03F)
					(if (== (& larryTime $D000) $D000)
						(= larryTime $1000)
					)
				)
			)

			;** check Larry's breath **
			(if
				(and
					(> spraySeconds 1)
					(!= curRoomNum 710)
					(!= curRoomNum 720)
					(!= curRoomNum 190)
					(not (Btst fEveHasApple))
				)
				(-- spraySeconds)
				(if (== spraySeconds 1)
					(= spraySeconds 0)
					(Bset fMouthSmellsBad)

					(if (not (Btst fEgoUsingSpray)) ;don't print if spraying
						(Cls)
						(switch (Random 1 5)
							(1
								(Print "Your mouth tastes like the inside of a
									motorman's glove!")
							)
							(2
								(Print "Your breath smells like floor sweepings
									from a rendering plant.")
							)
							(3
								(Print "Your breath is enough to take your breath
									away.")
							)
							(4
								(Print "Your mouth tastes like drippings from a
									marathon runner's sock.")
							)
							(5
								(Print "Now you know where old elephants go to
									die--your mouth!")
							)
						)
					)
				)
			)


			(if (and (< larryDollars 1)
						(not (Btst fBetInProgress)))
				(Cls)
				(Print "Oh, no! You are now penniless.
					You hock your leisure suit only to spend the rest of your
					life wandering from town to town, wondering about what
					\"might have been.\"")
				(Death vLarryKillingHimself lLarryMatchHead	)
				(Format @deathTitleStr "POOR Larry!")
				(GameOver  "The rest of your story is\n
								simply too sad to tell.\n
								Larry, it's time for a little\n
								\"R & R,\" -- Restart or Restore!")
			)
		)
		(super doit: &rest)
	)	;* END OF LL1 DOIT

)	;*	END OF LL1 INSTANCE

;*****************************
; code to set ego's speed called
; by speedSlider
;*****************************

(instance speedORama of Code
	(method (doit num)
		(if argc
			(theGame egoMoveSpeed: num)
			(if (User canControl?)
				(ego egoSpeed:	(theGame egoMoveSpeed?))
			)
		)
		(return (theGame egoMoveSpeed?))
	)
)

;******************************
; PROCEDURES
;******************************

(procedure (NormalEgo theLoop	theView swView &tmp stopView)
	(= stopView 0)
	(ego view: vLarryWalking)

	(if (> argc 0)
		(ego loop:	theLoop)
		(if (> argc 1)
			(ego view:	theView)
			(if (> argc 2)
				(= stopView swView)
			)
		)
	)

	(if (not stopView)
		(= stopView vLarryStopping)
	)

	(ego
		normal:			TRUE,
		moveHead:		TRUE,
		setLoop:			RELEASE,
		setLoop:			stopGroop,
		setPri:			RELEASE,
		setMotion:		FALSE,
		setCycle:		StopWalk stopView,
		setStep:			3 2,
		illegalBits:	$8000,
		ignoreActors:	FALSE,
		userSpeed:,
	)
)

(procedure (HandsOff &tmp saveIcon)
	(User
		canControl:	FALSE,
		canInput:	FALSE
	)
	(ego setMotion: 0)

	; save current icon to restore at end
	(= saveIcon (theIconBar curIcon?))

	; disable some icons so user doesn't screw us up
	(theIconBar disable:
		walkIcon
		lookIcon
		doIcon
		talkIcon
		zipperIcon
		smellTasteIcon
		useIcon
		inventoryIcon
	)

	; restore icon
	(theIconBar curIcon: saveIcon)

	; if no mouse, move the cursor out of the way, but save the initial
	; posn so HandsOn can restore it
	(if (not (HaveMouse))
		(= saveCursorX ((User curEvent?) x?))
		(= saveCursorY ((User curEvent?) y?))
		(theGame setCursor: waitCursor TRUE 310 180)
	else
		(theGame setCursor: waitCursor TRUE)
	)
	(if pMouse
		(pMouse stop:)
	)
)

(procedure (HandsOn)
	(User
		canControl: TRUE,
		canInput:	TRUE
	)

	; re-enable iconbar
	(theIconBar enable:
		walkIcon
		lookIcon
		doIcon
		talkIcon
		zipperIcon
		smellTasteIcon
		useIcon
		inventoryIcon
	)
	(if (not (theIconBar curInvIcon?))
		(theIconBar disable: useIcon)
	)
	; restore cursor xy posn if no mouse
	(if (not (HaveMouse))
		(theGame setCursor:
					((theIconBar curIcon?) cursor?)
					TRUE
					saveCursorX
					saveCursorY
		)
	else
		(theGame setCursor: ((theIconBar curIcon?) cursor?))
	)
	(ego userSpeed:)
)

(procedure (HaveMem howMuch)
	(return (u> (MemoryInfo FreeHeap) howMuch))
)

; return true if who's origin is on color
(procedure (SteppedOn who color)
	(if (& (who onControl: origin) color)
		(return TRUE)
	)
)

; like SteppedOn, but only true if who is completely on color
(procedure (SteppedFullyOn who color)
	(if (== (who onControl:) color)
		(return TRUE)
	)
)

(procedure (Btst flagEnum)
	(& [gameFlags (/ flagEnum 16)] (>> $8000 (mod flagEnum 16)))
)

(procedure (Bset flagEnum  &tmp oldState)
	(= oldState (Btst flagEnum))
	(|= [gameFlags (/ flagEnum 16)] (>> $8000 (mod flagEnum 16)))
	oldState
)

(procedure (Bclr flagEnum  &tmp oldState)
	(= oldState (Btst flagEnum))
	(&= [gameFlags (/ flagEnum 16)] (~ (>> $8000 (mod flagEnum 16))))
	oldState
)

(procedure (Btgl flagEnum)
	(if (Btst flagEnum)
		(Bclr flagEnum)
	else
		(Bset flagEnum)
	)
)

; when you die, print a text string plus a title bar string

(procedure (Death view loop cycler)
	(Load SOUND mDeath)
	(Load VIEW (= deathView view))
	(= deathLoop loop)
	(if (> argc 2)
		(= deathCycler cycler)
	)
)
(instance deathIcon of DCIcon
	(method (init)
		(if deathCycler
			((= cycler (EndLoop new:)) init: self) ;can have number after self for # of cycles
		else
			((= cycler (Forward new:)) init: self) ;can have number after self for # of cycles
		)
	)
)
(procedure (GameOver &tmp [str 80])
	(Format @str &rest)
	(sounds eachElementDo: #stop)
	(theMusic
		number:		mDeath,
		vol:			127,
		loop:			1,
		flags:		1,
		play:			,
	)
	(deathIcon
		view:			deathView,
		loop:			deathLoop,
		cycleSpeed:	(* (+ howFast 1) 4), ; faster on slower machines
	)
	(theGame setCursor: ARROW_CURSOR TRUE)
	(Animate (cast elements?) FALSE)
	(repeat
		(switch
			(Print @str
				#mode: 	teJustCenter
				#button: {Restore} 1
				#button: {Restart} 2
				#button: {____Quit____} 3
				#icon:	deathIcon
				#title:	@deathTitleStr
			)
			(1
				(theGame restore:)
			)
			(2
				(theGame restart:)
			)
			(3
				(= quit TRUE)
				(break)
			)
		)
	)
)

; Give the player points for a puzzle.
; If the second parameter exists, it is assumed to be a bit flag that must
; be clear to get points, at which time the flag is set
;
;(procedure (Points flag val &tmp theVal)
;	(if (> argc 1)
;		(if (Bset flag)	; Bset returns previous state of flag; if set, no pts
;			(= val 0)
;		else
;			(= theVal val)
; 			(pointsSound play:)
;		)
;	else
;		(= theVal flag)
; 		(pointsSound play:)
;	)
;	(theGame changeScore: theVal)
;)

; a procedure to help keep user from getting points more than once.
; example:
;	(Points bitFlag points)
;		bitFlag	= one of the global bit flags
;		points   = number of points to process

(procedure (Points flag val)
	(if (not (Btst flag))
		(theGame changeScore: val)
		(Bset flag)
		(pointsSound play:)
	)
)

; force 'who' to face either another object or an xy usage:
;	(Face who whoElse)
;	will turn who to face whoElse, cueing nobody

;	(Face who whoElse whoCares)
;	will turn who to face whoElse, cueing whoCares

;	(Face who x y)
;	will turn who to face pixel at x y

;	(Face who x y whoCares)
;	will turn who to face pixel at x y, cueing whoCares

(procedure (Face who theObjOrX theY whoCares
				&tmp theHeading lookX lookY whoToCue)
	(= whoToCue 0)
	(if (IsObject theObjOrX)
		(= lookX (theObjOrX x?))
		(= lookY (theObjOrX y?))
		(if (== argc 3)
			(= whoToCue theY)
		)
	else
		(= lookX theObjOrX)
		(= lookY theY)
		(if (== argc 4)
			(= whoToCue whoCares)
		)
	)

	(= theHeading (GetAngle (who x?) (who y?) lookX lookY))
	(who setHeading: theHeading (if (IsObject whoToCue) whoToCue))
)

; for no response
(instance theDefaultFeature of Feature
	(properties
		description "it"
	)
)

; show a talking icon in a dialog window:
;	(Babble theView "text")
; or (if you're using far text)
;	(Babble theView module# entry#)

(procedure (Babble theView msgS msgO &tmp [buffer 500])
	(if (u< msgS 1000)
		(GetFarText msgS msgO @buffer)
	else
		(StrCpy @buffer msgS)
	)

	(babbleIcon
		view:			theView,
;		cycler:		RandCycle,
		cycleSpeed:	(* (+ howFast 1) 4), ; faster on slower machines
;		iterate:		(* (StrLen @buffer) 6),
	)

	(if (u< msgS 1000)
		(Print @buffer &rest
			#icon:	babbleIcon 0 0
		)
	else
		(Print @buffer msgO &rest
			#icon:	babbleIcon 0 0
		)
	)
)


; user can pass color to be used for 256, & color to be used for 16
; usage:
;	(FindColor color256 color16)

;**	(procedure (FindColor col256 col16)
;**		; make sure the colors are within bounds
;**		(if (< col256 0)
;**			(= col256 0)
;**		)
;**		(if (> col256 255)
;**			(= col256 255)
;**		)
;**		(if (< col16 0)
;**			(= col16 0)
;**		)
;**		(if (> col16 15)
;**			(= col16 15)
;**		)
;**
;**		(return (if (Btst isVga) col256 else col16))
;**	)

; so ego can get a little head any time he wants to
(procedure (InitEgoHead headView &tmp hView)
	(= hView 0)

	(if argc
		(= hView headView)
	else
		(= hView vLarryStopping)
	)

	((= eHead egoHead)
		init:			ego,
		view:			hView,
		cycleSpeed:	24,
	)
)

;hours since player started game
(procedure (LarryHours &tmp hours)
	(= hours (>> larryTime 12))
	(return
		(if (> hours 9)
			(- hours 10)
		else
			(+ hours 2)
		)
	)
)
(procedure (LarryMinutes)
	(return
		(& (>> larryTime 6) %111111)
	)
)
(procedure (LarrySeconds)
	(return
		(& larryTime %111111)
	)
)

(procedure (ObjInRoom object theRoom &tmp theOwner)
	(if (> argc 1)
		(= theOwner theRoom)
	else
		(= theOwner curRoomNum)
	)
	(return
		(== ((inventory at: object) owner?) theOwner)
	)
)

;******************************
; main iconBar icons
;******************************

(instance icon0 of IconItem				;walk
	(properties
		view			vIcons
		loop			lIconWalk	
		cel			0
		maskView		vIcons
		maskLoop		14
		maskCel		1
		message		verbWalk
		cursor		WALK_CURSOR
		signal		(| RELVERIFY HIDEBAR)
		helpStr		"Use this icon to move your character."
	)
	(method (init)
		(self
			lowlightColor:		colLMagenta,
		)
		(super init:)
	)
)
(instance icon1 of IconItem				;	
	(properties
		view			vIcons
		loop			lIconLook
		cel			0
		maskView		vIcons
		maskLoop		14
		maskCel		1
		message		verbLook
		cursor		LOOK_CURSOR
		signal		(| RELVERIFY HIDEBAR)
		helpStr		"Use this icon to look at things."
	)
	(method (init)
		(self
			lowlightColor:		colLCyan,
		)
		(super init:)
	)
)
(instance icon2 of IconItem				;the hand
	(properties
		view			vIcons
		loop			lIconHand	
		cel			0
		maskView		vIcons
		maskLoop		14
		maskCel		0
		message		verbDo
		cursor		DO_CURSOR
		signal		(| RELVERIFY HIDEBAR)
		helpStr		"Use this icon to do things."
	)
	(method (init)
		(self
			lowlightColor:		colLGreen,
		)
		(super init:)
	)
)
(instance icon3 of IconItem				;talk	to
	(properties
		view			vIcons
		loop			lIconTalk	
		cel			0
		maskView		vIcons
		maskLoop		14
		maskCel		3
		message		verbTalk
		cursor		TALK_CURSOR
		signal		(| RELVERIFY HIDEBAR)
		helpStr		"Use this icon to talk to other characters."
	)
	(method (init)
		(self
			lowlightColor:		colLRed,
		)
		(super init:)
	)
)
(instance icon4 of IconItem				;use it, inventory window
	(properties
		view			vIcons
		loop			lIconInvItem	
		cel			0
		maskView		vIcons
		maskLoop		14
		maskCel		4
		message		verbUse
		cursor		ARROW_CURSOR
		signal		(| RELVERIFY HIDEBAR)
		helpStr		"Select this icon to use the current inventory object."
	)
	(method (init)
		(self
			lowlightColor:		colLGreen,
		)
		(super init:)
	)
)
(instance icon5 of IconItem				;inventory
	(properties
		view			vIcons
		loop			lIconInventory	
		cel			0
		maskView		vIcons
		maskLoop		14
		maskCel		2
		message		0
		signal		(| RELVERIFY IMMEDIATE HIDEBAR)
		cursor		ARROW_CURSOR
		helpStr		"Use this icon to bring up the inventory window."
	)
	(method (init)
		(self
			lowlightColor:		colYellow,
		)
		(super init:)
	)
	(method (select)
		(return
			(if (super select: &rest)
				(Inventory showSelf: ego)
			)
		)
	)
)
(instance icon6 of IconItem				;sex
	(properties
		view			vIcons
		loop			lIconZipper	
		cel			0
		maskView		vIcons
		maskLoop		14
		maskCel		0
		message		verbZipper
		cursor		ZIPPER_CURSOR
		signal		(| RELVERIFY HIDEBAR)
		helpStr		"Larry! You know what this is for!"
	)
	(method (init)
		(self
			lowlightColor:		colLYellow,
		)
		(super init:)
	)
)
(instance icon7 of IconItem				;taste & smell
	(properties
		view			vIcons
		loop			lIconTasteSmell	
		cel			0
		maskView		vIcons
		maskLoop		14
		maskCel		1
		message		verbSmellTaste
		cursor		SMELLTASTE_CURSOR
		signal		(| RELVERIFY HIDEBAR)
		helpStr		"This icon is for tasting and smelling."
	)
	(method (init)
		(self
			lowlightColor:		colLCyan,
		)
		(super init:)
	)
)
(instance icon8 of IconItem				;	control panel
	(properties
		view			vIcons
		loop			lIconControls	
		cel			0
		maskView		vIcons
		maskLoop		14
		maskCel		1
		message		CONTROL_PANEL
		cursor		ARROW_CURSOR
		signal		(| RELVERIFY IMMEDIATE HIDEBAR)
		helpStr		"This icon brings up the control panel."
	)
	(method (init)
		(self
			lowlightColor:		colLMagenta,
		)
		(super init:)
	)
	(method (select)
		(return
			(if (super select:)
				(theIconBar hide:)
				(GameControls show:)
			)
		)
	)
)
(instance icon9 of IconItem				;	question mark
	(properties
		view			vIcons
		loop			lIconHelp	
		cel			0
		maskView		vIcons
		maskLoop		14
		maskCel		0
		message		verbHelp
		cursor		HELP_CURSOR
		signal		(| RELVERIFY IMMEDIATE)
		helpStr		"This icon tells you about other icons."
	)
	(method (init)
		(self
			lowlightColor:		colLRed,
		)
		(super init:)
	)
)


;******************************
;	our default doverb code
;******************************

(instance ll1DoVerbCode of Code
	(method (doit theVerb theObj invNo &tmp objDesc itemDesc)
		(if (theObj description?)
			(= objDesc (theObj description?))
		else
			(= objDesc (theDefaultFeature description?))
		)
		(if (inventory at: invNo)
			(= itemDesc ((inventory at: invNo) description?))
		else
			(= itemDesc (theDefaultFeature description?))
		)
		(Cls)
		(if (theObj facingMe: ego) ; turn ego to look
			(switch theVerb
				(verbLook
					(if (theObj lookStr?)
						(Print (theObj lookStr?))
					else
						(Printf "Why, look! It's %s." objDesc)
					)
				)
				(verbTalk
					(Printf "You engage in a short, but personally meaningful
						conversation with %s." objDesc)
				)
				(verbDo
					(Printf "That feels exactly like %s." objDesc)
				)
				(verbUse
				 	(switch itemDesc
				 		(((inventory at: iRibbon) description?)
							(Printf "Perhaps %s is not into velveteen S_&_M."
								objDesc
							)
						)
				 		(((inventory at: iPocketKnife) description?)
							(Print "You're a lover, not a fighter!")
						)
						(else
							(Printf "It seems %s just doesn't work with %s."
								itemDesc
								objDesc
							)
						)
					)
				)
				(verbZipper
					(Printf "Larry! Don't do that to %s!" objDesc)
				)
				(verbSmellTaste
					(Printf "To you, %s has no distinctive smell or taste." objDesc)
				)
 			)
		)
	)
)

;***************************************
;	some	global sounds
;***************************************

(instance longSong  of Sound)
(instance longSong2 of Sound)
(instance longSong3 of Sound)


;******************************
;	our default feature initializer
;******************************

(instance ll1FtrInit of Code		; sets up defaults
	(method (doit theObj)
		; angle used by facingMe
		(if (== (theObj sightAngle?) ftrDefault)
			(theObj sightAngle: 90)
		)
		; maximum distance to get an object (for example.)
		; instance of Action or EventHandler with Actions
		(if (== (theObj actions?) ftrDefault)
			(theObj actions: 0)
		)
	)
)

;***************************************
; our system window.	using an instance so we
; can change the properties
;***************************************

;**	(class ShadowWindow kindof Window
;**		(properties
;**			theShadowWidth	5
;**			underBits 		0		; handle to saved visual map
;**			pUnderBits 		0		; handle to saved priority map
;**			lsTop				0
;**			lsLeft			0
;**			lsBottom			0
;**			lsRight			0
;**		)
;**		(method (open &tmp wMap)
;**			(SetPort 0)
;**	
;**			(= wMap VMAP)
;**			(if (!= priority -1)
;**				(|= wMap PMAP)
;**			)
;**			(= brTop 	top)
;**			(= brLeft 	left)
;**			(= brRight	(+ right theShadowWidth))
;**			(= brBottom (+ bottom theShadowWidth))
;**	
;**			(= underBits (Graph GSaveBits brTop brLeft brBottom brRight VMAP))
;**			(if (!= priority -1)
;**				(= pUnderBits (Graph GSaveBits brTop brLeft brBottom brRight PMAP))
;**			)
;**			(Graph GDrawLine 						; bottom line
;**				(+ 1 bottom)
;**				(+ 1 left)
;**				(+ 1 bottom)
;**				(+ 1 right)
;**				colLMagenta -1 -1
;**			)	
;**			(Graph GDrawLine 						; right line
;**				(+ 1 top)
;**				(+ 2 right)			
;**				(+ 1 bottom)
;**				(+ 2 right)
;**				colLMagenta -1 -1
;**			)	
;**			(Graph GDrawLine 						; bottom line2
;**				(+ 2 bottom)
;**				(+ 2 left)
;**				(+ 2 bottom)
;**				(+ 2 right)
;**				colVLBlue -1 -1
;**			)	
;**			(Graph GDrawLine 						; right line2
;**				(+ 2 top)
;**				(+ 3 right)			
;**				(+ 2 bottom)
;**				(+ 3 right)
;**				colVLBlue -1 -1
;**			)	
;**	
;**			(Graph GShowBits brTop brLeft brBottom brRight VMAP)
;**			;		(= type $81)
;**	
;**			(super open:)
;**		)
;**	
;**		(method (dispose)
;**			(super dispose:)
;**			(SetPort 0)
;**			(Graph GRestoreBits underBits)
;**			(Graph GRestoreBits pUnderBits)
;**			;(Graph GShowBits brTop brLeft brBottom brRight VMAP)
;**			(Graph GReAnimate brTop brLeft brBottom brRight)
;**		)
;**	)

(instance ll1Win of Window)
;(instance ll1Win of LLWindow)


;***************************************
;	the window that goes with GameControls.
;	defines are for window & for GameControl members.
;***************************************

; the 1st set are all used as property values, so they
; can't use stuff such as CelHigh, etc.

(define	GAP			3)
(define	SIDEGAP		4)
(define	BIGBOXTOP	GAP)
(define	BIGBOXLEFT	SIDEGAP)
(define	BUT1LEFT		(+ BIGBOXLEFT 4))
(define	BUT2LEFT		(+ BIGBOXLEFT 30))
(define	SAVETOP		(+ BIGBOXTOP 3))
(define	RESTOTOP		(+ BIGBOXTOP 23))
(define	RESTATOP		(+ BIGBOXTOP 43))
(define	QUITTOP		(+ BIGBOXTOP 63))
(define	ABOUTTOP		(+ BIGBOXTOP 83))
(define	PLAYTOP		(+ BIGBOXTOP 103))
(define	SLIDERTOP	37);;15)
(define	DETAILLEFT	67)
(define	SLIDDIST		40)
(define	VOLUMELEFT	(+ DETAILLEFT SLIDDIST))
(define	SPEEDLEFT	(+ VOLUMELEFT SLIDDIST))

; these guys are only used for kernel calls

(define	TITLETOP		(* GAP 2))
(define	TITLEWIDE	(CelWide vGameControls 0 5))
(define	BIGBOXHIGH	(CelHigh	vGameControls 1 1))
(define	BIGBOXWIDE	(CelWide	vGameControls 1 1))
(define	ICONHIGH		(CelHigh vGameControls 0 4))
(define	SLIDWIDE		(CelWide vGameControls 0 1))
(define	SLIDHIGH		(CelHigh vGameControls 0 1))
(define	WINWIDE		(+ DETAILLEFT SLIDWIDE (* SLIDDIST 2) SIDEGAP))
(define	WINHIGH		(+ BIGBOXHIGH (* GAP 2)));;(+ TITLEHIGH BIGBOXHIGH (* GAP 5)))
(define	WINTOP		(/ (- 200 WINHIGH) 2))
(define	WINLEFT		(/ (- 320 WINWIDE) 2))
(define	WINBOTTOM	(+ WINHIGH WINTOP))
(define	WINRIGHT		(+ WINWIDE WINLEFT))
(define	TITLELEFT
	(+ (/ (- (- WINWIDE (+ BIGBOXLEFT BIGBOXWIDE)) TITLEWIDE) 2) (+ BIGBOXLEFT BIGBOXWIDE))
)
(define	ARROWTOP		(+ SLIDERTOP 1))
(define	ARROW1LEFT	(+ DETAILLEFT 27))
(define	ARROW2LEFT	(+ VOLUMELEFT 28))
(define	SLIDTITTOP	(- SLIDERTOP (+ ICONHIGH GAP)))
(define	DETAILTLEFT	(- DETAILLEFT 4))
(define	VOLUMETLEFT	(- VOLUMELEFT 6))
(define	SPEEDTLEFT	(- SPEEDLEFT 1))

; for score 'window'
(define	SWINHIGH		13);;30)
(define	SWINLEFT		(+ 6 BIGBOXLEFT BIGBOXWIDE))
(define	SWINWIDE		(- WINWIDE (+ SWINLEFT SIDEGAP 2)))
(define	SWINTOP		(+ 9 SLIDERTOP SLIDHIGH))
(define	TEXTTOP		(+ SWINTOP 3))
(define	THEFONT		999)

(instance gcWin of Window
	(method (open
					&tmp savePort theBevelWid t l r b theColor theMaps
					topColor bottomColor rightColor leftColor	thePri i
					[str 15]	[rectPt 4]
				)
		(self
			top:			WINTOP,
			left:			WINLEFT,
			bottom:		WINBOTTOM,
			right:		WINRIGHT,
			priority:	15,
		)
		(super open:)

		;*******************************
		; title
		(DrawCel vGameControls 0 5 TITLELEFT TITLETOP 15)
		; big box around buttons
		(DrawCel vGameControls 1 1 BIGBOXLEFT BIGBOXTOP 15)
		; 1st arrow between sliders
		(DrawCel vGameControls 1 0 ARROW1LEFT ARROWTOP 15)
		; 2nd arrow between sliders
		(DrawCel vGameControls 1 0 ARROW2LEFT ARROWTOP 15)
		; icon over detail
		(DrawCel vGameControls 0 4 DETAILTLEFT SLIDTITTOP 15)
		; icon over volume
		(DrawCel vGameControls 0 3 VOLUMETLEFT SLIDTITTOP 15)
		; icon over speed
		(DrawCel vGameControls 0 2 SPEEDTLEFT SLIDTITTOP 15)

		; print score 
		(Format @str "Score: %d of %d" score possibleScore)
		(TextSize @rectPt @str THEFONT 0)
		(Display @str
			#p_font:		userFont
			#p_color:	colBlue
			#p_at:		(+ SWINLEFT (/ (- SWINWIDE [rectPt 3]) 2)) TEXTTOP
		)
	)
)

;******************************
; control panel members
;******************************

(instance detailSlider	of Slider
	(properties
		view			vGameControls
		loop			0
		cel 			1
		nsLeft		DETAILLEFT
		nsTop			SLIDERTOP
		sliderView	vGameControls
		sliderLoop	0
		sliderCel	0
		signal		FIXED_POSN
		helpStr		"Lower this if game play seems sluggish.
						Raise it to increase the amount of background animation."
		topValue		3
		bottomValue	0
	)
)
(instance volumeSlider	of Slider
	(properties
		view			vGameControls
		loop			0
		cel 			1
		nsLeft		VOLUMELEFT
		nsTop			SLIDERTOP
		sliderView	vGameControls
		sliderLoop	0
		sliderCel	0
		signal		FIXED_POSN
		helpStr		"This adjusts the volume on some sound boards and
						synthesizers."
		topValue		15
		bottomValue		0
	)
)
(instance speedSlider of Slider
	 (properties
		view			vGameControls
		loop			0
		cel 			1
		nsLeft		SPEEDLEFT
		nsTop			SLIDERTOP
		sliderView	vGameControls
		sliderLoop	0
		sliderCel	0
		signal		FIXED_POSN
		helpStr		"This adjusts Larry's speed,
						within the limits of your computer's capabilities."
		topValue		0
		bottomValue	15
	)
)

(instance iconSave of ControlIcon
	(properties
		view			vGameControls
		loop			2
		cel			0
		nsLeft		BUT1LEFT
		nsTop			SAVETOP
		message		ABOUT_HELP
		signal		(| RELVERIFY IMMEDIATE FIXED_POSN VICON HIDEBAR)
		helpStr		"Use this to save the current state of your game.
						When you later select Restore,
						everything will be exactly as it is now."
	)
)
(instance iconRestore of ControlIcon
	(properties
		view			vGameControls
		loop			3
		cel			0
		nsLeft		BUT1LEFT
		nsTop			RESTOTOP
		message		ABOUT_HELP
		signal		(| RELVERIFY IMMEDIATE FIXED_POSN VICON HIDEBAR)
		helpStr		"This restores a game you saved earlier."
	)
)
(instance iconRestart of ControlIcon
	(properties
		view			vGameControls
		loop			4
		cel			0
		nsLeft		BUT1LEFT
		nsTop			RESTATOP
		message		ABOUT_HELP
		signal		(| RELVERIFY IMMEDIATE FIXED_POSN VICON HIDEBAR)
		helpStr		"Use this to restart the game from the very beginning."
	)
)
(instance iconQuit of ControlIcon
	(properties
		view			vGameControls
		loop			5
		cel			0
		nsLeft		BUT1LEFT
		nsTop			QUITTOP
		message		ABOUT_HELP
		signal		(| RELVERIFY IMMEDIATE FIXED_POSN HIDEBAR VICON)
		helpStr		"Use this to leave the game."
	)
)
(instance iconAbout of ControlIcon		; halfdome
	(properties
		view			vGameControls
		loop			6
		cel			0
		nsLeft		BUT1LEFT
		nsTop			ABOUTTOP
		message		ABOUT_HELP
		signal		(| RELVERIFY IMMEDIATE FIXED_POSN HIDEBAR VICON)
		helpStr		"Here's where you learn more than you care to know
						about this game."
	)
;	(method (doit)
;		(super doit: &rest)
;		(if
;			(not (HaveMem $1a90))
;			(Print
;				"Sorry, you don't have enough memory to
;				to see the about screen at the moment."
;			)
;		else
;		)
;
;	)
)
(instance iconHelp	of IconItem		;
	(properties
		view			vGameControls
		loop			7
		cel			0
		nsLeft		BUT2LEFT
		nsTop			ABOUTTOP
		signal		(| RELVERIFY IMMEDIATE FIXED_POSN VICON)
		type			userEvent
		message		verbHelp
		cursor		HELP_CURSOR
		helpStr		"This helps explain the other icons."
	)
)
(instance iconOk	of IconItem				; actually sez 'Play'
	(properties
		view			vGameControls
		loop			8
		cel			0
		nsLeft		BUT1LEFT
		nsTop			PLAYTOP
		message		ABOUT_HELP
		cursor		ARROW_CURSOR
		signal		(| RELVERIFY IMMEDIATE FIXED_POSN HIDEBAR VICON)
		helpStr		"Use this to exit this menu and resume game play."
	)
)


;
;  Class person of Actor
;
;			basically complains if ego is near him
;

(class Person of Actor
	(properties
		complained		0
	)
	(method (doit)
		(super doit:)
		(cond
			((Btst fToiletPaperOnShoe)
				(if (< (self distanceTo: ego) 30)
					(Bclr fToiletPaperOnShoe)
					(Cls)
					(Print "\"Hey, dork! You've got toilet paper stuck to your
								shoe!\"")
					(Print "You nonchalantly scrape it off.")
				)
			)
			((Btst fSmellsLikeDogPiss)
				(if (< (self distanceTo: ego) 30)
					(Bclr fSmellsLikeDogPiss)
					(Cls)
					(Print "\"Phew! You smell like a used fire hydrant!\"")
				)
			)
			((and	(not spraySeconds) 		;why?
					(Btst fMouthSmellsBad)
					(not complained)
		 			(< (self distanceTo: ego) 30))
				(self complained: TRUE)
				(Cls)
				(Print "\"Eeeyow! Do something about that breath of yours!\"")
			)
		)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Cls)
				(Print "You'd better not. You might get your face slapped!")
			)
			(verbUse
				(Printf "\"I have no use for %s!\" says %s."
					((Inventory at: invItem) description?)
					description
				)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

