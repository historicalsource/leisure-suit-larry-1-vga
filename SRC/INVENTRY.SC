;**
;**	Logics for INVENTRY.SC -- Everywhere
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford &
;**
;**	Last Update:  June 20, 1991
;**


;**	All inventory views are in v900.
;**	First three inventory views are in L0, additional ones in L2.
;**	The last cel in L0 is Larry looking at centerfold of magazine.
;**	Loops 1 and 3 are "SHARE"d loops, so the system code will think
;**	they're duplicates of loop0 and loop2.

;**	The text that describes them is contained in series of files
;**	beginning with 850.sc.  Each of those files contains the text that
;**	results from the interaction between two inventory items; i.e.,
;**	clicking the Wallet on the Watch would print the 3rd message in
;**	file 850.sc

;**	All the cursors are in the same order as the inventory equates in
;**	game.sh, i.e., iBreathSpray is 1, so breath spray cursor is 1.cur.


(script# INVENTORY)

(public
	invCode 0
)


(define	INVENTORY_START 			850)


;**		iWallet								;**	from game.sh
;**		iBreathSpray
;**		iWatch
;**		iApple
;**		iRing
;**		iWhiskey
;**		iRemoteControl
;**		iRose
;**		iLubber
;**		iCandy
;**		iDiscoPass
;**		iPocketKnife
;**		iWine
;**		iMagazine
;**		iHammer
;**		iPills
;**		iRibbon
;**		iGraffiti

;(define	vLL1inventory			900)

	(define	cWallet				0)
	(define	cBreathSpray		1)
	(define	cWatch				2)

	(define	cApple				0)
	(define	cRing					1)
	(define	cWhiskey				2)
	(define	cRemoteControl		3)
	(define	cRose					4)
	(define	cLubber				5)
	(define	cCandy				6)
	(define	cDiscoPass			7)
	(define	cPocketKnife		8)
	(define	cWine					9)
	(define	cMagazine			10)
	(define	cHammer				11)
	(define	cPills				12)
	(define	cRibbon				13)
	(define	cGraffiti			14)
	(define	cLarryOglingCenterfold 15)



(instance invCode of Code					; set up inventory
	(method (init &tmp i)
		(Inventory
			init:,								; init to set global 'inventory'
			add:
				wallet
				breathSpray
				watch
				apple
				ring
				whiskey
			 	remoteControl
				rose
				lubber
				candy
				discoPass
				pocketKnife
				wine
				magazine
				hammer
				pills
				ribbon
				graffiti

				;**	add new inventory items above this line

				invLook
				invHand
				invSelect
				invHelp
				ok
				,

			eachElementDo: #highlightColor:	colLBlue,
			eachElementDo: #lowlightColor:	colDBlue,
			eachElementDo: #init:,
			window:			invWin,
			helpIconItem:	invHelp,
			selectIcon:		invSelect,
			okButton:		ok
		)

		; you start off with a wallet, breathspray and a watch
		(watch			owner: ego)
		(wallet			owner: ego)
		(breathSpray	owner: ego)

		(invWin
			color:			0,			  ; text
			back:				colWindow, ; inventory window
			topBordColor:	colWindow,
			lftBordColor:	colWindow,
			rgtBordColor:	colWindow,
			botBordColor:	colWindow,
			insideColor:	colDBlue,
			topBordColor2:	colMagenta,
			lftBordColor2:	colMagenta,
			botBordColor2:	colLMagenta,
			rgtBordColor2:	colLMagenta,
		)
	)
)

;***************************************
; our inventory window. using an instance
; so we can change the properties
;***************************************

(instance invWin of InsetWindow
	(properties
		topBordHgt		28
		botBordHgt		5
	)
	(method (open)
		(invLook nsLeft: (- (/ (- (self right?) (self left?)) 2) 68))
		(super open:)
	)
)



;******************************
;	icons for inventory window
;******************************

(instance invLook	of IconItem		;
	(properties
		view				vInventory
		loop				lIconInvLook	
		cel				0
		signal			(| RELVERIFY FIXED_POSN)
		cursor			LOOK_CURSOR
		type				userEvent
		message			verbLook
		helpStr			"To look more closely at an object,
							first click here, then click on the item."
		nsLeft			0
		nsTop				0
	)
	(method (init)
		(self
			lowlightColor:		colLCyan,
		)
		(super init:)
	)
)
(instance invHand	of IconItem		;
	(properties
		view				vInventory
		loop				lIconInvHand	
		cel				0
		signal			RELVERIFY
		cursor			DO_CURSOR
		type				userEvent
		message			verbDo
		helpStr			"Use this when you want to do something to an item."
	)
	(method (init)
		(self
			lowlightColor:		colLGreen,
		)
		(super init:)
	)
)
(instance invSelect	of IconItem		;
	(properties
	  	view				vInventory
		loop				lIconInvArrow
		cel				0
		signal			RELVERIFY
		type				userEvent
		cursor			ARROW_CURSOR
		helpStr			"To use an item in the game, first click here,
							then click on the item, then click on OK."
	)
	(method (init)
		(self
			lowlightColor:		colLMagenta,
		)
		(super init:)
	)
)
(instance invHelp	of IconItem		;
	(properties
		view				vInventory
		loop				lIconInvHelp	
		cel				0
		signal			RELVERIFY
		type				userEvent
		message			verbHelp
		cursor			HELP_CURSOR
		helpStr			"This icon tells you about other icons."
	)
	(method (init)
		(self
			lowlightColor:		colLRed,
		)
		(super init:)
	)
)
(instance ok	of IconItem		;
	(properties
		view				vInventory
		loop				lIconInvOK	
		cel				0
		signal			(| HIDEBAR IMMEDIATE RELVERIFY)
		cursor			ARROW_CURSOR
		helpStr			"Click here to close this window and return to the game."
	)
	(method (init)
		(self
			lowlightColor:		colLYellow,
		)
		(super init:)
	)
)

(class LLinvItem of InvItem
	(properties
		signal		IMMEDIATE
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print (+ INVENTORY_START (Inventory indexOf: self))
					(Inventory indexOf: self)
					#icon:	vLL1inventory loop cel
				)
				(UnLoad TEXT (+ INVENTORY_START (Inventory indexOf: self)))
			)
			(verbDo
				(Printf "You might try using %s on yourself." (self description?))
			)
			(verbUse
				(if (!= (Inventory indexOf: self) invItem)
					(Print (+ INVENTORY_START invItem) (Inventory indexOf: self))
				else
					(Print "Click the OK button to close this window.")
				)
				(UnLoad TEXT (+ INVENTORY_START invItem))
			)
		)
	)
)

;******************************
;	inventory items
;******************************

(instance wallet of LLinvItem
	(properties
		view			vLL1inventory
		loop			0
		cel			cWallet
		cursor		iWallet
		description "your wallet"
	)
	(method (doVerb theVerb invItem &tmp choice [str 100])
		(switch theVerb
			(verbLook
			 	(if (or	(== curRoomNum 250)
							(== curRoomNum 260))
					(Print "Your wallet is mostly empty since you stuck all your
						money into this machine!")
				else
					(Print
						(Format @str "Your wallet contains some wrinkled
							business cards, lots of notes, some credit cards
							(which won't work in this game), and $%d.00 in cash."
							larryDollars
						)
						#icon: view loop cel
					)
				)
			)
			(verbDo
				(switch (Print "You reach in your wallet and grab the..."
							#icon: view loop cel
							#button	{Credit Cards}		0
							#button	{Business Cards}	1
							#button	{Notes}				2
							)
					(0
						(Print "What a shame!
							Not one single business in Lost Wages accepts
							credit cards from the First National Bank of Iraq.")
					)
					(1
						(Print "The only business card you have reads,
							\"Sierra On-Line, Inc. (209) 683-8989\"")
					)
					(2
						(Print "\"Remember to buy more Al Lowe games!\"")
					)
				)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance breathSpray of LLinvItem
	(properties
		view			vLL1inventory
		loop			0
		cel			cBreathSpray
		cursor		iBreathSpray
		description "your breath spray"
	)
)

(instance watch of LLinvItem
	(properties
		view			vLL1inventory
		loop			0
		cel			cWatch
		cursor		iWatch
		description "your wrist watch"
	)
	(method (doVerb theVerb invItem &tmp [str 100])
		(switch theVerb
			(verbLook
				(Print (Format @str "Gee, a genuine BowlX!\n\n
		 				At least the time is accurate\n -- it's %02d:%02d."
		 				(>> larryTime 12) (& (>> larryTime 6) %111111))
					#icon: view loop cel
				)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance apple of LLinvItem
	(properties
		view			vLL1inventory
		loop			2
		cel			cApple
		cursor		iApple
		description "the apple"
		owner			300
	)
)

(instance ring of LLinvItem
	(properties
		view			vLL1inventory
		loop			2
		cel			cRing
		cursor		iRing
		description "the diamond ring"
		owner			130
	)
)

(instance whiskey of LLinvItem
	(properties
		view			vLL1inventory
		loop			2
		cel			cWhiskey
		cursor		iWhiskey
		description	"your glass of whiskey"
		owner			110
	)
)

(instance remoteControl of LLinvItem
	(properties
		view			vLL1inventory
		loop			2
		cel			cRemoteControl
		cursor		iRemoteControl
		description	"your remote control"
		owner			120
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(Print "You press the button on the remote control and...")
				(Print "...nothing happens.")
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance rose of LLinvItem
	(properties
		view			vLL1inventory
		loop			2
		cel			cRose
		cursor		iRose
		description "the rose"
		owner			120
	)
)

(instance lubber of LLinvItem
	(properties
		view			vLL1inventory
		loop			2
		cel			cLubber
		cursor		iLubber
		description "your prophylactic"
		owner			510
	)
)

(instance candy of LLinvItem
	(properties
		view			vLL1inventory
		loop			2
		cel			cCandy
		cursor		iCandy
		description	"the box of candy"
		owner			150
	)
)

(instance discoPass of LLinvItem
	(properties
		view			vLL1inventory
		loop			2
		cel			cDiscoPass
		cursor		iDiscoPass
		description	"the disco pass"
		owner			330
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "The passcard reads:\n\n
					\"Lost Wages Disco\n\n
					Admit One Member and Guest\n\n
					Remember: every Monday\n
					is Ladies Night!\""
					#mode:	teJustCenter
					#icon: 	view loop cel
				)
			)
			(else
				(super doVerb: theVerb invItem)
			)
		)
	)
)

(instance pocketKnife of LLinvItem
	(properties
		view			vLL1inventory
		loop			2
		cel			cPocketKnife
		cursor		iPocketKnife
		description "your pocketknife"
		owner			500
	)
)

(instance wine of LLinvItem
	(properties
		view			vLL1inventory
		loop			2
		cel			cWine
		cursor		iWine
		description "your wine"
		owner			510
	)
)

(instance magazine of LLinvItem
	(properties
		view			vLL1inventory
		loop			2
		cel			cMagazine
		cursor		iMagazine
		description	"your magazine"
		owner			510
	)
	(method (doVerb theVerb invItem)
		(if (or	(== theVerb verbLook)
					(== theVerb verbDo))
			(Print "Well, well.
				It looks like this month's issue is filled with intelligent,
				literary articles laden with redeeming social value.
				For example, here's an article about how professional
				window washers secure themselves to the sides of tall buildings
				with a rope tied around their waist...")
			(Print "Nah! Who wants to read that crap?
				You quickly flip through the pages until you discover...")
			(Points pReadMagazine ptsReadMagazine)
			(Print "Whoaaah! Nice centerfold this month!!"
				#icon: 	view loop cLarryOglingCenterfold
			)
		else
			(super doVerb: theVerb invItem)
		)
	)
)

(instance hammer of LLinvItem
	(properties
		view			vLL1inventory
		loop			2
		cel			cHammer
		cursor		iHammer
		description "Lefty's hammer"
		owner			160
	)
)

(instance pills of LLinvItem
	(properties
		view			vLL1inventory
		loop			2
		cel			cPills
		cursor		iPills
		description "the bottle of pills"
		owner			160
	)
)

(instance ribbon of LLinvItem
	(properties
		view			vLL1inventory
		loop			2
		cel			cRibbon
		cursor		iRibbon
		description	"the ribbon"
		owner			390
	)
)

(instance graffiti of LLinvItem
	(properties
		view			vLL1inventory
		loop			2
		cel			cGraffiti
		cursor		iGraffiti
		description "the password"
		owner			130
	)
)
