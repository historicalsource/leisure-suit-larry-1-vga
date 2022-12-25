;**
;**	Logics for Room 260 -- Slots
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford
;**
;**	Last Update:  June 21, 1991
;**

(script#	260)

(public
	rm260	0
)


(define	houseLimit			10000)

(define	pSlotParts				260)
(define	vSlotParts				260)
	(define lJackpot				6)
	(define lCircles				7)

;(define	vJail						103)

;(define	sButtonPress			260)
(define	sWheelsSpinning		261)
(define	sWheelStopping			262)
(define	sWinSmall				263)
(define	sWinBig					264)
(define	sWinJackpot				265)
;(define	sCashOut					266)
(define  cashAt					165 34)
(define  betAt						105 36)

(define	minBet					10)
(define 	statusColor				colDRed)


(local
	bet
	status1
	status2
	[str 80]
	machineDollars
	[display1 7]
	blewIt
	overLimit
)

(procedure
	BackToRoom
	ChangeDollars
	BlinkIt
	BetSize
)

(instance rm260 of Room
	(properties
		picture	pSlotParts
		style		HSHUTTER
	)

	(method (init &tmp i)
		(Load SCRIPT
			RANDCYC
		)
		(Load VIEW
			vSlotParts
		)
		(LoadMany SOUND
			sButtonPress
			sWheelsSpinning
			sWheelStopping
			sWinSmall
			sWinBig
			sWinJackpot
			sCashOut
		)
		(theMusic2
			loop:			1,
			vol:			127,
			flags:		1,
		)
		(theMusic3
			loop:			1,
			vol:			127,
			flags:		1,
		)
		(ego
			init:		,
			z:			1000,
			hide:
		)
		(super init:)
		(playButton init:, stopUpd:)
		(increaseButton init:, stopUpd:)
		(decreaseButton init:, stopUpd:)
		(oddsButton init:, stopUpd:)
		(cashOutButton init:, stopUpd:)
		(wheelLeft
			cycleSpeed:	0,		;howFast,
			init:,
			stopUpd:
		)
		(wheelCenter
			cycleSpeed:	0,		;howFast,
			init:,
			stopUpd:
		)
		(wheelRight
			cycleSpeed:	0,		;howFast,
			init:,
			stopUpd:
		)
		(= bet minBet)
		(= machineDollars larryDollars)
		(= status1 (Display (Format @str "%d" bet)
				p_at: 	betAt
				p_color: statusColor
				p_save:
			)
		)
		(= status2 (Display (Format @str "%5d" machineDollars)
				p_at: cashAt
				p_color: statusColor
				p_save:
			)
		)
		(leftTank
			init:
		)
		(rightTank
			init:
		)
		(for ((= i 0)) (< i (+ 4 howFast)) ((++ i))
			((circle new:)
				moveSpeed:		howFast,
				init:,
				cel:				i,
				setPri:			i,
				x:					(Random 1 36),
				y:					(Random 71 249),
				observeBlocks:	leftTank,
				setMotion:		Wander,
			)
		)
		(for ((= i 12)) (> i (- 9 howFast)) ((-- i))
			((circle new:)
				moveSpeed:		howFast,
				init:,
				cel:				i,
				setPri:			i,
				x:					(Random 302 318),
				y:					(Random 66 249),
				observeBlocks:	rightTank,
				setMotion:		Wander,
			)
		)
		(stripe0
			cycleSpeed:		howFast,
			setPri:			14,
			init:,
			stopUpd:,
		)
		(stripe1
			cycleSpeed:		howFast,
			setPri:			14,
			init:,
			stopUpd:,
		)
		(stripe2
			cycleSpeed:		howFast,
			setPri:			14,
			init:,
			stopUpd:,
		)
		(stripe3
			cycleSpeed:		howFast,
			setPri:			14,
			init:,
			stopUpd:,
		)
		(stripe4
			cycleSpeed:		howFast,
			setPri:			14,
			init:,
			stopUpd:,
		)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "The numbers at the top of the slot machine
					indicate the size of your current bet and the total
					amount of credit you have in the machine.")
				(Print "For help, just Look at the various buttons.")
			)
			(verbTalk
				(Print "\"Come on, dammit, loosen up or I'll find another
					machine that will!\"")
			)
         (verbDo
				(Print "Click on a button instead of where you just clicked.")
			)
			(verbZipper
				(Print "Careful or you'll jam the coin slot!")
			)
			(verbSmellTaste
				(Print "You gently kiss your machine to bring good luck.")
			)
			(verbUse
				(switch invItem
					(iPocketKnife
						(Print "You carefully slip the pocketknife into that
							special little hole in the back of every slot machine,
							twist the odds screw, and hope no one saw you.")
						(Print "Now you'll SURELY get lucky, Larry!")
					)
					(iHammer
						(Print "Wildly wielding your left-handed hammer,
							you smash the slot machine into a zillion pieces.
							\"Why, of course no one here at the casino will
							care,\" you think.")
						(Print "(You think wrong!)" LOW)
						(Death vJail 0 1)
						(Format @deathTitleStr "You animal!")
						(GameOver "That's one way to \"beat the slots,\" Larry!
							Next time, be gentle with her!")
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
	(method (doit)
		(if (GameIsRestarting)
			(= status1 (Display (Format @str "%d" bet)
					p_at: 	betAt
					p_color: statusColor
					p_save:
				)
			)
			(= status2 (Display (Format @str "%5d" machineDollars)
					p_at: cashAt
					p_color: statusColor
					p_save:
				)
			)
		)
		(cond
			(script)
			((>= machineDollars houseLimit)
				(HandsOff)
				(curRoom setScript: sWin)
			)
			((ego mover?)
				(BackToRoom)
			)
		)
		(super doit:)
	)
)
(instance sWin of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(theMusic3
					number:		sWinJackpot,
					play:			self,
				)
				(Print "Congratulations, Larry! You have broken the house limit."
					#dispose:)
			)
			(
				(Cls)
				(if
					overLimit
					(Print "This machine only contains $10,000.00;
						trust us, we'll mail you the rest."
					)
				)
				(BackToRoom)
			)
		)
	)
)

(instance playButton of Prop
   (properties
      x 164
      y 189
      z 0
      heading 0
      view 260
      loop 3
      cel 0
      description "the Play button"
		lookStr		"Click the Hand here to begin play."
   )
	(method (doVerb theVerb)
		(switch theVerb
			(verbDo
				(cond
					((< machineDollars bet)
						(Print "You don't have enough money!")
					)
					((== bet 0)
						(Print "You insert zero dollars, and pretend
							to push the button.")
						(Print "You win zero dollars!")
						(Print "(You are so easily amused!)" LOW)
					)
					(else
						(BlinkIt playButton)
						(HandsOff)
						(curRoom setScript: spinEm)
					)
				)
			)
			(else
				(super doVerb: theVerb &rest)
			)
		)
	)
)

(instance spinEm of Script
	(method (changeState newState)
		(switch (= state newState)
			(0
				(theMusic2
					setLoop:		-1,
					number:		sWheelsSpinning,
					play:,
				)
				(HandsOff)
				(Display "" p_restore: status2)
				(= status2 (Display (Format @str "%5d" (-= machineDollars bet))
						p_at: 	cashAt
						p_color: statusColor
						p_save:
					)
				)
				(wheelLeft
					setCel: 		(Random 0 7),
					setCycle: 	Forward
				)
				(wheelCenter
					setCel: 		(Random 0 7),
					setCycle: 	Forward
				)
				(wheelRight
					setCel: 		(Random 0 7),
					setCycle: 	Forward
				)
				(= seconds 3)
			)
			(1
				(wheelLeft
					setCycle: CycleTo (if debugging 6 else (* 2 (Random 0 3))) 1 wheelLeft
				)
				(= seconds 2)
			)
			(2
				(wheelCenter
					setCycle: CycleTo (if debugging 6 else (* 2 (Random 0 3))) 1 wheelCenter
				)
				(= seconds 2)
			)
			(3
				(wheelRight
					setCycle: CycleTo (if debugging 6 else (* 2 (Random 0 3))) 1 self
				)
			)
			(4
				(= blewIt FALSE)
				(theMusic2
					number:		sWheelStopping,
					setLoop:		1,
					play:
				)
				(theMusic3
					setLoop:		-1,
				)
				(cond
					((== (wheelLeft cel?) 0)
						(if (== (wheelCenter cel?) 0)
							(if (== (wheelRight cel?) 0)
								(theMusic3
									number: 		sWinBig,
									play:,
								)
								(if 
									(> bet 8000)
									(ChangeDollars 10000)
								else
									(ChangeDollars (* 4 bet))
								)
								(= seconds 5)
								;4
							else
								(theMusic3
									number: 		sWinSmall,
									play:,
								)
								(ChangeDollars (* 2 bet))
								(= seconds 3)
								;2
							)
						else
							(theMusic3
								number:		sWinSmall,
								play:
							)
							(ChangeDollars bet)
							(= seconds 2)
							;1
						)
					)
					((== (wheelLeft cel?) (wheelCenter cel?) 2)
						(if (== (wheelRight cel?) 2)
							(theMusic3
								number: 		sWinBig,
								play:,
							)
							(if 
								(> bet 8000)
								(ChangeDollars 10000)
							else
								(ChangeDollars (* 4 bet))
							)
							(= seconds 5)
							;4
						else
							(theMusic3
								number: 		sWinSmall,
								play:,
							)
							(ChangeDollars (* 2 bet))
							(= seconds 3)
							;2
						)
					)
					((== (wheelLeft cel?) (wheelCenter cel?) 4)
						(if (== (wheelRight cel?) 4)
							(theMusic3
								number: 		sWinBig,
								play:,
							)
							(if
								(> bet 6400)
								(ChangeDollars 10000)
							else
								(ChangeDollars (* 5 bet))
							)
							(= seconds 6)
							;5
						else
							(theMusic3
								number: 		sWinSmall,
								play:,
							)
							(ChangeDollars (* 3 bet))
							(= seconds 4)
							;3
						)
					)
					((== (wheelLeft cel?) (wheelCenter cel?) (wheelRight cel?) 6)
						(theMusic3
							number: 		sWinBig,
							play:			self,
						)
						(jackpotSign
							cycleSpeed:	howFast,
							init:,
							setCycle:	Forward
						)
						(if
							(> bet 4500)
							(ChangeDollars 10000)
						else
							(ChangeDollars (* 7 bet))
						)
						(= seconds 8)
						;7
					)
					(else
						(= cycles 1)
						(= blewIt TRUE)
						;-1
					)
				)
				(if
					(not blewIt)
					(stripe0
						setCycle:	EndLoop stripe1
					)
				)
			)
			(5
				(stripe0
					setCel:		0,
					stopUpd:
				)
				(stripe1
					setCel:		0,
					stopUpd:
				)
				(stripe2
					setCel:		0,
					stopUpd:
				)
				(stripe3
					setCel:		0,
					stopUpd:
				)
				(stripe4
					setCel:		0,
					stopUpd:
				)
				(theMusic3
					setLoop:		1,
					stop:
				)
				(if (IsObject jackpotSign)
					(jackpotSign dispose:)
				)
				(if (> bet machineDollars)
					(if
						(> machineDollars 9)
						(= bet machineDollars)
					else
						(= bet 10)
					)
					(Display "" p_restore: status1)
					(= status1(Display (Format @str "%d" bet)
							p_at: 	betAt
							p_color: statusColor
							p_save:
						)
					)
				)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance increaseButton of Prop
   (properties
      x 146
      y 164
      z 0
      heading 0
      view 260
      loop 2
      cel 0
      description "the Increase button"
		lookStr 		"Click the Hand here to increase the amount of your bet."
   )
	(method (init)
		(super init:)
		(mouseDownHandler addToFront: self)
		(keyDownHandler addToFront: self)
	)
	(method (dispose)
		(mouseDownHandler delete: self)
		(keyDownHandler delete: self)
		(super dispose:)
	)
	(method (handleEvent evt &tmp event ticks)
		(if (and (self onMe: evt)
					(!= (curRoom script?) spinEm))
			(if (or
					(< 0 (evt modifiers?) 5)
					(!= ((theIconBar curIcon?) message?) verbDo)
				)
				 (super handleEvent: evt)
			else
;				(BlinkIt self)
				(switch (evt type?)
					(mouseDown
						(while (!= ((= event (Event new:))	type?) mouseUp)
							(= ticks (GetTime))
							;; delay
							(while (<  (GetTime)  (+ 10 ticks)))
							(event localize:)
							(cond
								((== bet houseLimit)
									(Print "This machine has a $10,000.00 limit.")
									(break)
								)
								((< machineDollars 10)
									(Print "You don't have any more money!")
									(break)
								)
								((< machineDollars (+ (BetSize) bet 1))
									(if (mod machineDollars 2)
										(= bet (- machineDollars 1))
									else
										(= bet machineDollars)
									)
									(Display "" p_restore: status1)
									(= status1 (Display (Format @str "%d" bet)
											p_at: betAt
											p_color: statusColor
											p_save:
										)
									)
									(Print "You don't have any more money!")
									(break)
								)
								(else
									(theMusic3
										number:		sButtonPress,
										play:
									)
									(+= bet (BetSize))
									(Display "" p_restore: status1)
									(= status1 (Display (Format @str "%d" bet)
											p_at: 	betAt
											p_color: statusColor
											p_save:
										)
									)
								)
							)
							(event dispose:)
						)
						(event dispose:)
						(return (evt claimed: TRUE))
					)
					(keyDown
						(if (== (evt message?) ENTER)
							(cond
								((== bet houseLimit)
									(Print "This machine has a $10,000.00 limit.")
								)

								((< machineDollars (+ (BetSize) bet 1))
									(if (mod machineDollars 2)
										(= bet (- machineDollars 1))
									else
										(= bet machineDollars)
									)
									(Display "" p_restore: status1)
									(= status1 (Display (Format @str "%d" bet)
											p_at: betAt
											p_color: statusColor
											p_save:
										)
									)
									(Print "You don't have any more money!")
								)
								(else
									(+= bet (BetSize))
									(theMusic3
										number:		sButtonPress,
										play:
									)
									(Display "" p_restore: status1)
									(= status1 (Display (Format @str "%d" bet)
											p_at: 	betAt
											p_color: statusColor
											p_save:
										)
									)
								)
							)
							(return (evt claimed: TRUE))
						)
					)
				)
			)
		)
	)
)

(instance decreaseButton of Prop
   (properties
      x 183
      y 164
      z 0
      heading 0
      view 260
      loop 5
      cel 0
      description "the Decrease button"
		lookStr		"Click the Hand here to decrease the amount of your bet."
   )
	(method (init)
		(super init:)
		(mouseDownHandler addToFront: self)
		(keyDownHandler addToFront: self)
	)
	(method (dispose)
		(mouseDownHandler delete: self)
		(keyDownHandler delete: self)
		(super dispose:)
	)
	(method (handleEvent evt &tmp event ticks)
		(if (and (self onMe: evt)
					(!= (curRoom script?) spinEm))
			(if (or
					(< 0 (evt modifiers?) 4)
					(!= ((theIconBar curIcon?) message?) verbDo)
				)
				 (super handleEvent: evt)
			else
;				(BlinkIt self)
				(switch (evt type?)
					(mouseDown
						(while (!= ((= event (Event new:))	type?) mouseUp)
							(= ticks (GetTime))
							;; delay
							(while (< (GetTime) (+ 10 ticks)))
							(event localize:)
							(if (> bet minBet)
								(theMusic3
									number:		sButtonPress,
									play:
								)
								(-= bet (BetSize))
								(if 
									(< bet minBet)
									(= bet minBet)
								)
								(Display "" p_restore: status1)
								(= status1 (Display (Format @str "%d" bet)
										p_at: 	betAt
										p_color: statusColor
										p_save:
									)
								)
							else
								(Printf "This machine's minimum bet is $%d.00."
									minBet)
								(break)
							)
							(event dispose:)
						)
						(event dispose:)
						(return (evt claimed: TRUE))
					)
					(keyDown
						(if (== (evt message?) ENTER)
							(if (> bet minBet)
								(-= bet (BetSize))
								(if 
									(< bet minBet)
									(= bet minBet)
								)
								(theMusic3
									number:		sButtonPress,
									play:
								)
								(Display "" p_restore: status1)
								(= status1 (Display (Format @str "%d" bet)
										p_at: 	betAt
										p_color: statusColor
										p_save:
									)
								)
							else
								(Printf "You can't bet less than $%d.00." minBet)
							)
							(return (evt claimed: TRUE))
						)
					)
				)
			)
		)
	)
)
(instance oddsButton of Prop
   (properties
      x 193
      y 145
      z 0
      heading 0
      view 260
      loop 4
      cel 0
      description "the Odds button"
		lookStr		"Click the Hand here to learn this machine's odds."
   )
	(method (cue)
		(super cue:)
		(Print  "PAYOFF\t\t\tRESULTS\n\n
					\t 1\t\tCherry\n
					\t 2\t\tCherry\tCherry\n
					\t 4\t\tCherry\tCherry\tCherry\n
					\t 2\t\tBell\t\t__Bell\n
					\t 4\t\tBell\t\t__Bell\t\t_Bell\n
					\t 3\t\tSeven\t___Seven\n
					\t 5\t\tSeven\t___Seven\t__Seven\n
					\t 7\t\tBar\t\t___Bar\t\t__Bar"
			#width		200
			#at			-1 30
		)
	)
	(method (doVerb theVerb)
		(switch theVerb
			(verbDo
				(theMusic3
					number:		sButtonPress,
					play:
				)
				(self setCycle: BegLoop self)
			)
			(else
				(super doVerb: theVerb &rest)
			)
		)
	)
)

(instance cashOutButton of Prop
   (properties
      x 135
      y 144
      z 0
      heading 0
      view 260
      loop 1
      cel 0
      description "the Cash Out button"
		lookStr		"Click the Hand here to stop playing.
						All the money you've deposited in the machine will
						be returned to you."
   )
	(method (cue)
		(super cue:)
		(BackToRoom)
	)
	(method (doVerb theVerb)
		(switch theVerb
			(verbDo
				(self setCycle: BegLoop self)
			)
			(else
				(super doVerb: theVerb &rest)
			)
		)
	)
)

(instance wheelLeft of Prop
   (properties
      x 115
      y 92
      z 0
      heading 0
      view 260
      loop 0
      cel 0
      description "the left tumbler"
		lookStr		"You hope this will soon match the other two."
   )
	(method (cue)
		(super cue:)
		(theMusic3
			number:		sWheelStopping,
			loop:			1,
			play:
		)
	)
)
(instance wheelCenter of Prop
   (properties
      x 159
      y 89
      z 0
      heading 0
      view 260
      loop 0
      cel 0
      description "the center tumbler"
		lookStr		"You hope this will soon match the other two."
   )
	(method (cue)
		(super cue:)
		(theMusic3
			number:		sWheelStopping,
			loop:			1,
			play:
		)
	)
)
(instance wheelRight of Prop
   (properties
      x 204
      y 88
      z 0
      heading 0
      view 260
      loop 0
      cel 0
      description "the right tumbler"
		lookStr		"You hope this will soon match the other two."
   )
)

(procedure (BackToRoom)
	(= larryDollars machineDollars)
	(theMusic2
		number:		sCashOut,
		loop:			1,
		play:
	)
	(curRoom  newRoom: 310)
)

(procedure (ChangeDollars amount)
;	(+= machineDollars amount)
;	(if (< machineDollars 0)
;		(= machineDollars 0)
;	)
	(if (> amount 0)
		(if (< (+ machineDollars amount) 0)
			(= machineDollars houseLimit)
		else
			(+= machineDollars amount)
		)
	else
		(= machineDollars houseLimit)
	)
	(if (< machineDollars 0)
		(= machineDollars 0)
	)
	(if (> machineDollars houseLimit)
		(= machineDollars houseLimit)
		(= overLimit TRUE)
	)
	(Display "" p_restore: status2)
	(= status2 (Display (Format @str "%5d" machineDollars)
			p_at: 	cashAt
			p_color: statusColor
			p_save:
		)
	)
)

(procedure (BlinkIt obj)
	(theMusic3
		number:		sButtonPress,
		play:
	)
	(obj setCel: 1)
	(Animate (cast elements?) FALSE)
	(obj setCel: 0)
	(Animate (cast elements?) FALSE)
	(obj stopUpd:)
)

(procedure (BetSize &tmp theSize)
	(= theSize (/ (+ bet 100) 50))
	(if (mod theSize 2)
		(return (- theSize 1))
	else
		(return theSize)
	)
)

(instance circle of Actor
	(method (doit)
		(super doit: &rest)
		(if (self isBlocked:)
			(self setMotion: Wander)
		)
	)
	(properties
		view			vSlotParts
		loop			lCircles
		signal		(| ignrAct fixedLoop fixedCel)
		xStep			1
		yStep			1
		illegalBits 0
		description	"those cute little circles"
		lookStr		"You must be bored to be staring at these things!"
	)
)

;
;	Blocks
;

(instance leftTank of Cage
	(properties
		top			70
		left			-10
		bottom		210
		right			47
	)
)

(instance rightTank of Cage
	(properties
		top			65
		left			281
		bottom		230
		right			339
	)
)

(instance jackpotSign of Prop
	(properties
		view			vSlotParts
		loop			lJackpot
		x				159
		y				84
		priority		15
		signal		(| ignrAct fixPriOn)
	)
)

(instance stripe0 of Prop
	(properties
		view			260
		loop			8
		cel			0
		x				129
		y				115
		detailLevel 2
		description	"the light"
		signal		ignrAct
	)
	(method (cue)
		(super cue:)
		(stripe4
			setCel:	0,
		)
		(self
			setCycle: EndLoop stripe1
		)
	)
)

(instance stripe1 of Prop
	(properties
		view			260
		loop			9
		cel			0
		x				147
		y				134
		detailLevel 2
		description	"the light"
		signal		ignrAct
	)
	(method (cue)
		(super cue:)
		(stripe0
			setCel:	0,
		)
		(self
			setCycle: EndLoop stripe2
		)
	)
)

(instance stripe2 of Prop
	(properties
		view			260
		loop			10
		cel			0
		x				163
		y				154
		detailLevel 2
		description	"the light"
		signal		ignrAct
	)
	(method (cue)
		(super cue:)
		(stripe1
			setCel:	0,
		)
		(self
			setCycle: EndLoop stripe3
		)
	)
)

(instance stripe3 of Prop
	(properties
		view			260
		loop			11
		cel			0
		x				180
		y				133
		detailLevel 2
		description	"the light"
		signal		ignrAct
	)
	(method (cue)
		(super cue:)
		(stripe2
			setCel:	0,
		)
		(self
			setCycle: EndLoop stripe4
		)
	)
)

(instance stripe4 of Prop
	(properties
		view			260
		loop			12
		cel			0
		x				195
		y				108
		detailLevel 2
		description	"the light"
		signal		ignrAct
	)
	(method (cue)
		(super cue:)
		(stripe3
			setCel:	0,
		)
		(self
			setCycle: EndLoop stripe0
		)
	)
)


