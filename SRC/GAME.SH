;**
;**	GAME.SH -- Global defines
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford & Mike Larsen
;**
;**	Last Update:  June 21, 1991
;**


(include graphics.sh)
(include sound.sh)


; Some general defines
(define 	FORWARD 		1)
(define 	BACKWARD 	-1)
(define	RELEASE		-1)
(define	NON_CURSOR	900)
(define	cursorOff	NON_CURSOR)
(define 	mPAUSE 		$0000)

(define BREATHLIMIT		600) 				;seconds 'til Larry's breath goes bad
(define SPRAYSINCAN		9)   				;# of sprays in can of breathspray


; userEvent
(enum 100
	DRAW_ICON_BAR
)

(enum
	walkIcon
	lookIcon
	doIcon
	talkIcon
	zipperIcon
	smellTasteIcon
	useIcon
	inventoryIcon
	computerIcon
	aboutIcon
)

(enum 10
	verbZipper
	verbSmellTaste
)

(define	egoAction		userEvent)
(define	WALK				travelIcon)
(define	LOOK_AT			lookIcon)
(define	DO_IT				doIcon)
(define	USE_IT			useIcon)
(define	TALK_TO			talkIcon)
(define	SAVE_RESTORE	computerIcon)
(define	STOP				computerIcon)
(define	CONTROL_PANEL	computerIcon)
(define	ABOUT_HELP		aboutIcon)

; Generic cursor IDs
(define	WALK_CURSOR			100)
(define	LOOK_CURSOR			101)
(define	DO_CURSOR			102)
(define	TALK_CURSOR			103)
(define  ZIPPER_CURSOR		104)
(define	SMELLTASTE_CURSOR	105)
(define	EXIT_CURSOR			106)
(define	HELP_CURSOR			150)
(define	handCursor			900)

(define	lExitIcon			15)
(define	lWalkerIcon			0)

; ******* Regions & Locales *****************************************

(define SIDEWALK 700)

(define SIDEWALKROOMS
	100
	300
	400
	500
	600
)
(define	CLOSEUP_ROOMS
	505
	385
	355
	615
	200
	250
	260
	371
	; also
	; p155
)
; ******* pseudo system stuff ***************************************
(define	DEBUG			800)
(define	DISP			801)
;(define	COLOR			802) now included in LL1init
(define	SPEEDTEST	803)
(define	NOSEPICK		808)
(define	ABOUT			811)
(define 	SPRAY			812)
(define	LLROOM		813)
(define  INVENTORY	814)
(define	LLEGO			815)
(define  LL1INIT		816)

; Inventory objects
; this enum MUST be in same order as inventory add: in rm0!
(enum
	iWallet
	iBreathSpray
	iWatch
	iApple
	iRing
	iWhiskey
	iRemoteControl
	iRose
	iLubber
	iCandy
	iDiscoPass
	iPocketKnife
	iWine
	iMagazine
	iHammer
	iPills
	iRibbon
	iGraffiti
)


(define	HIGH		#at: -1 20)
(define	LOW		#at: -1 140)
(define	PRIGHT	#at: 120 30)
(define	PLEFT		#at: 15 30)
(define	BIG		#at: -1 19 #width: 280)
(define	WIDE		#at: 15 -1 #width: 280)
(define 	LastCel	255)

(extern
	StillDown		255 5
	NormalEgo		0 1
	HandsOff			0 2
	HandsOn			0 3
	HaveMem			0 4
	SteppedOn		0 5
	Btst				0 6
	Bset				0 7
	Bclr				0 8
	Btgl				0 9
	GameOver			0 10
	Points			0 11
	Face				0 12
	DoStatus			0 13
	SteppedFullyOn	0 14
	Babble			0 15
;	LLDrawBox		0 16
	InitEgoHead		0 17
	LarryHours		0 18
	LarryMinutes	0 19
	LarrySeconds	0 20
	Death				0 21
	ObjInRoom		0 22
	spraySound		0 23
	aboutCode		ABOUT 0
)

(global
	theMusic				100
	howFast				101
	(enum
		slow
		medium
		fast
	)
	theMusic2			102
	curReg				103	; used by DISP to know if we're changing regions (for RegPath)
	noCursor				104 = TRUE
	cursorType			105
	numColors			106	; number of colors available
	numVoices			107
	restartRoom			108
	dontSave				109
	sGrooper				110	; global to point at stopGrooper in rm0
	gameFlags			111	; bit flags start at 111 and end at 118.
	endGameFlags		118	; This gives a maximum of 128 (8*16) flags.

	;global flags
	(enum
		fAlleyWindowBroken	; larry broke window with pills
		fBetInProgress			; 9  so far, used in rm0 to not kill penniless ego
		fComeOnIn
		fDancedWithFawn
		fDollFlies				; after larry pops doll it flies away
		fEgoUsingSpray			; 6  egoSprays script in progress
		fEveHasApple			; larry gave eve the apple
		fEveKnowsLarry
		fFailedCeremony		; set when larry didn't have enough cash for wedding
		fFaithGone				; 10 Faith has left security desk
		fFawnAskedForBucks
		fWhereDidSheGo
		fFawnInRoom				; Fawn's in honeymoon suite, tested in rm340
		fFawnIsGone
		fFawnLeaves
		fHasClap					; 1  didn't use rubber with hooker

		fHookerWindowOpen		; is it open or shut?
		fJetsOff					; spa jets are off for returning from closeup of eve
		fKnowsEvesName
		fLarryPanting
		fMouthSmellsBad		; 3  next person Larry approaches will complain
		fNearPerson				; 2  set and cleared by 'person' objects, (hopefully)
		fNoFawnInDisco			; Fawn's not in the disco, used so far in 200.sc
		fOpenedDoor
		fOrderedWine			; larry called for wine delivery, set in 500, tested in 340
		fPentDoorsOpen			; in case he hits Faith's button, and then goes down instead of up
		fPhoneRinging			;phone rang in 500
		fPimpStandingAside	; 12
		fPimpWatchingTV		; as opposed to just TV on
		fPuncturedDoll			; larry popped the blow up doll
		fReadTaxiSign			; 13 outside leftys
		fReadyToDance

		fReadyToMarry			; larry is ready to marry fawn
		fReturningCall
		fRodeInCab				; for print message first time he gets in cab
		fScoredHooker			; for pimp comment & ?
		fSeenDeliveryBoyGag  ; tested in rm340,set in 390
		fSmellsLikeDogPiss	; 4  next person Larry approaches will complain
		fStiffedCabbie			; exited cab without paying
		fSurveyCalled				; called sex hotline survey
		fToiletPaperOnShoe	; 5  next person Larry approaches will comment
		fTurnedOnRadio
		fTVOn						; 11
		fWearingRubber			; 7  hasn't removed yet
		isVga						; flag for number of colors and such
		fLookedAtFawn			; returns true after looking at Fawn in close up
		fCredits
		nextFlag3

		pAnsweredPhone
		pAskedToSit
		pBeenInPenthouse		; first time up elevator to penthouse
		pBoughtApple
		pBoughtWhiskey
		pCutRibbon
		pDanceWithFawn
		pDialedAjax
		pDialedSierra
		pDialedSurvey
		pDidDoll
		pGaveApple
		pGaveFawnRose
		pGaveFawnDiamond
		pGaveFawnCandy
		pGaveFawnBucks

		pGavePills				; gave bottle of pills to Faith
		pGotCandy
		pGotDoll
		pGotHammer
		pGotKnife
		pGotLubber
		pGotMagazine
		pGotMarried
		pGotPass					; got disco pass in rm330
		pGotPills
		pGotPimpToWatchTV
		pGotRemote
		pGotRibbon
		pGotRing
		pGotRose
		pGotWine

		pHeardLiquorStoreNumber
		pInflatedDoll
		pKnowsPassword
		pLaidEve
		pPoopedInCan
		pReadMagazine
		pReadTelephone
		pRemovedRubber
		pRodeInCab
		pSatOnWhoopieCushion
		pScoredHooker
		pTalkedToFawn
		pTalkedToFlasher
		pTurnedOnTV
		pTurnedOnRadio
		pUsedPass

		pWornRubber

		fGotDizzy		;after first time in elevator
		fCaberetTgl		
		fBumTgl
		fApplemanTgl
	)
	saveCursorX			119	; -position of cursor when HandsOff is fired
	saveCursorY			120	;
	eHead					121	; global to point at egoHead in rm0
	ffRoom				122	; room number to go to if fast-forward is hit

	colBlack				123					;**	These vars get loaded by
	colGray1				124					;**	COLOR.SC during game init
	colGray2				125
	colGray3				126
	colGray4				127
	colGray5				128
	colWhite				129
	colDRed				130	;**	Don't change these numbers
	colLRed				131	;**	without changing 375.sc Credits
	colVLRed				132	;**	procedure!!
	colDYellow			133	;**
	colYellow			134	;**
	colLYellow			135	;**
	colVDGreen			136	;**
	colDGreen			137	;**
	colLGreen			138	;**
	colVLGreen			139	;**
	colDBlue				140	;**
	colBlue				141	;**
	colLBlue				142	;**
	colVLBlue			143	;**
	colMagenta			144	;**
	colLMagenta			145	;**
	colCyan				146	;**
	colLCyan				147	;**
	colWindow			148
	;	149
	;	150

	actorX				151 = 160 			; to save larry's position
	actorY				152 = 160 			; for slots and blackjack
	larryTime 			153
	oldSysTime			154
	spraySeconds		155 ; counts down seconds after using spray
	; 156
	sprayCounter		157 ; counts how many times spray-bottle is used
	larryDollars		158 ; bucks he's got
	debugging			159 ; alt-d will toggle this flag
	;	160
	floorTo				161 ; hotel floor to go to
	tvChannel			162 ; tv channel playing in pimp room
	;	163
	cabFareMin			164 = 10 ;minimum cab fare increases every ride
	deathView			165 = 807 	;default death icon view
	deathLoop			166 = 1		;default death icon loop
	theMusic3			167
	deathCycler			168
	larrySpeed			169 = 4

	gYourPart 			170 ;	17 chars	;
	gVideo 				181 ;	38 chars	 ;
	gYourClothing		211 ;	17 chars	  ;
	gTheirName 			221 ;	13 chars		;
	gTheirPart 			230 ;	17 chars		 ;		These are for the sex
	gTheirClothing		240 ;	17 chars		 ;		survey in room 505.
	gObject 				250 ; 17 chars		;
	gRoom 				260 ;	17 chars	  ;
	gActivity 			270 ;	37 chars	 ;
	gLastByte			290 ;			   ;

	giantFont			292
	monoFont				293
)
(define	deathTitleStr	gYourPart)

(define ptsReadMagazine				1)			;point value defines
(define ptsRemovedRubber			1)
(define ptsBoughtWhiskey			1)
(define ptsGotRemote					2)
(define ptsGotRose					1)
(define ptsGotKnife					5)
(define ptsGotRing					3)
(define ptsKnowsPassword			2)
(define ptsPoopedInCan				1)
(define ptsTurnedOnTV 				3)
(define ptsGotPimpToWatchTV		8)
(define ptsGotCandy					2)
(define ptsGotWine					1)
(define ptsGotMagazine				1)
(define ptsGotLubber					4)
(define ptsWornRubber				10)
(define ptsScoredHooker				11)
(define ptsRodeInCab					1)
(define ptsGotHammer					3)
(define ptsGotPills					8)
(define ptsGotMarried				12)
(define ptsTalkedToFlasher			1)
(define ptsGotDoll					5)
(define ptsDidDoll					8)
(define ptsInflatedDoll				5)
(define ptsGotPass					1)
(define ptsGavePills					5)
(define ptsBeenInPenthouse			5)
(define ptsHeardLiquorStoreNumber 1)
(define ptsCutRibbon					10)
(define ptsGotRibbon					3)
(define ptsBoughtApple				3)
(define ptsAskedToSit				1)
(define ptsSatOnWhoopieCushion	1)
(define ptsTalkedToFawn				1)
(define ptsGaveFawnBucks			7)
(define ptsGaveFawnRose				5)
(define ptsGaveFawnCandy			5)
(define ptsGaveFawnDiamond			5)
(define ptsLaidEve					25)
(define ptsUsedPass					5)
(define ptsDanceWithFawn			5)
(define ptsTurnedOnRadio			1)
(define ptsReadTelephone			1)
(define ptsDialedSurvey				2)
(define ptsDialedSierra				5)
(define ptsDialedAjax				5)
(define ptsAnsweredPhone			5)
(define ptsGaveApple					15)

; ******* Pseudo-procedures with no arguments ******************************

(define Cls if modelessDialog (modelessDialog dispose:))

; returns detailLevel so coders don't have to type so much stuff
(define Detail theGame detailLevel?)

(define objectVerbs verbDo verbUse verbZipper verbSmellTaste)
(define actorVerbs objectVerbs verbTalk)

(define turnEgo (ego setHeading: (GetAngle (ego x?) (ego y?) (self x?) (self y?)) self))
