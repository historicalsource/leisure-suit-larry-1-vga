;**
;**	Logics for Room 250 -- Blackjack
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford &
;**
;**	Last Update:  June 11, 1991
;**

;[R250 V809 XY 95/173] what are rules?  dealer had a jack up, and
; an ace in the hole.  I played my hand,
; then he turned his cards over to
; reveal he had blackjack.
; Isn't that wrong?
;                    I don't know

(script# 250)

(public
	rm250		0
)


(enum
	DEALER
	EGOHAND
	SPLITHAND
)


;(define vJail						103)

(define pBJParts					250)
(define vBJParts					250)
	(define lDouble			0)
	(define lSplit				1)
	(define lSurrender		2)
	(define lOdds				3)
	(define lStand				4)
	(define lCashout			5)
	(define lDeal				6)
	(define lHitMe				7)
	(define lIncrease			8)
	(define lDecrease			9)
	(define lChangeBet		10)
(define vCards						251)
;(define vJail						103)

(define	sCardOn					250)
(define	sBJlose					251)
(define	sBJwin					252)
(define  sBlackJack				253)
;(define	sButtonPress			260)
;(define	sCashOut					266)


(define cardX 61)
(define dealerY 25)
(define splitY 66)
(define egoY 105)
(define cardGap 33)

(define betAt 120 11)		;275 90
(define dollarsAt 165 11)	;275 60
(define displayBack colVDGreen)
(define displayColor colBlack)
(define houseLimit 10000)
(define minBet 10)
(define lCardBack 4)
(define cCardBack 4)
(define tickDelay	7)

(local
	dealerTotal			;dealer's current card count
	egoTotal				;ego's current card count
	splitTotal			;split's current card count
	cardNum				;number of card in deck
	[dealerCards 6]	;dealer's cards
	[egoCards 7]		;ego's cards
	[splitCards 6]		;split hand's cards
	dealersAces			;number of dealer's aces
	egosAces				;number of ego's aces
	splitsAces			;number of split hand's aces
	egoBusted			;ego busted flag
	egoCardInc			;used to index cards in array and place ego's cards
	splitCardInc		;used to index split hand cards
	handOver				;current hand is over
	dealerCardInc		;used to place dealer's cards
	dealersCard			;index to dealer's card
	bet					;current amount being bet
	status1				;displays bet amount
	status2				;ego's cash on hand
	[str 80]				;holds bet status string
	[str1 80]			;holds machineDollars status string
	dealersFirstCard	;remember this card for display purposes
	dealerShowCard		;dealers second card
	egoDoublingDown	;usually 1, 2 if doubling bet on 10 or 11
	splitDoublingDown	;usually 1, 2 if doubling bet on 10 or 11
	egosFirstCard		;remembered for AskSplit
	egosSecondCard		;added for splitting
	splitting			;TRUE or FALSE
	egoHandDone			;TRUE or FALSE for splitting
	splitBusted			;TRUE or FALSE
	machineDollars		;larry's money
	dontBlink			;so buttons know if not to blink
	brokeHouse

;for debugging, holds card values I want

;	5 10 5
;	cardDeck = [4 50 16 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1]

;	give ego jack, jack, 10, ace
;	cardDeck = [23 45 10 43 13 0 0 0 0  -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1]

;	5 10 5 ace
;	cardDeck = [4 50 16 49 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1]

;	splits and doubles both hands
;	cardDeck = [4 45 16 49 27 27 38 38 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1]

;	get a split of fours and bust and double down on split hand
;	cardDeck = [3 50 15 47 46 46 5 38 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1]

;	I want to split aces and get two BJ's
;	cardDeck = [39 50 26 48 47 43 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1]

;	six-card city, splits aces
;	cardDeck = [39 50 26 48 40 40 27 27 14 14 1 1 1 1 -1 -1 -1 -1 -1 -1]

;	to double down with an ace
;	cardDeck = [39 50 8 48 25 40 27 27 14 14 1 1 1 1 -1 -1 -1 -1 -1 -1]

;	to double down on splits ace
;	cardDeck = [39 50 26 48 25 23 27 27 14 14 1 1 1 1 -1 -1 -1 -1 -1 -1]

;	normal card deck, take out for shippng version
;	cardDeck = [ 0  1  2  3  4  5  6  7  8  9
;					10 11 12 13 14 15 16 17 18 19
;					20 21 22 23 24 25 26 27 28 29
;					30 31 32 33 34 35 36 37 38 39
;					40 41 42 43 44 45 46 47 48 49 51
;				  ]
;	cardNumber			; index to cardDeck
)

(procedure
	BackToRoom
	GetCard
	ShowDealerCard
	GetValue
	ShowCard
	AskDoubleDown
	Commit
	AskInsurance
	AskSplit
	ChangeDollars
	BlinkIt
	BetSize
	HandOver
)

(instance rm250 of LLRoom
	(properties
		picture	pBJParts
		style		HSHUTTER
	)

	(method (init)
		(LoadMany VIEW
			vBJParts
			vCards
		)
		(LoadMany SOUND
			sCardOn
			sBJlose
			sBJwin
			sCashOut
			sButtonPress
		)
		(theMusic3
			loop:			1,
			vol:			127,
			flags:		1,
		)
		(theMusic2
			loop:			1,
			vol:			127,
			flags:		1,
		)
		(ego
			init:		,
			z:			1000,
			hide:		,
		)
		(super init:)
		(theDouble init:, stopUpd:)
		(theSplit init:, stopUpd:)
		(theDeal init:, stopUpd:)
		(hitMe init:, stopUpd:)
		(stand init:, stopUpd:)
		(changeBet init:, stopUpd:)
		(surrender init:, stopUpd:)
		(odds init:, stopUpd:)
		(cashout init:, stopUpd:)
		(increase init:, stopUpd:)
		(decrease init:, stopUpd:)
		(= bet minBet)
		(= machineDollars larryDollars)
		(= status1
			(Display (Format @str "$%d" bet)
				p_at: betAt
				p_color: displayColor
				p_save:
			)
		)
		(= status2
			(Display (Format @str1 "$%d" machineDollars)
				p_at: dollarsAt
				p_color: displayColor
				p_save:
			)
		)
		(= handOver TRUE)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Cls)
				(Print "The numbers at the top of the video blackjack machine
					indicate the size of your current bet and the total
					amount of credit you have in the machine.")
				(Print "For help, just look at the various buttons.")
			)
			(verbTalk
				(Cls)
				(Print "\"Come on, dammit, pay off!\"")
			)
         (verbDo
				(Cls)
				(Print "Click on a button instead of where you just clicked.")
			)
			(verbZipper
				(Cls)
				(Print "\"Screw you, machine!\" you shout,
					never one to be intimidated by mere machinery.")
			)
			(verbSmellTaste
				(Cls)
				(Print "You gently kiss your machine to bring good luck.")
			)
			(verbUse
				(switch invItem
					(iPocketKnife
						(Cls)
						(Print "You carefully slip the pocketknife into the hole
							in the back of the machine, twist the odds screw,
							and hope no one saw you.")
					)
					(iHammer
						(Cls)
						(Print "Good idea! You smash the video blackjack machine
							with your hammer, causing hundreds of dollars to
							spill out, which you gladly catch and put in your
							pockets.")
						(Print "Bad idea! The security guard handles you
							indelicately as he escorts you to the local jail!")
						(Death vJail 0 1)
						(Format @deathTitleStr "...maybe with good behavior?")
						(GameOver "Next time, Larry,
							be a little more careful with the equipment!")
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
			(= status1
				(Display (Format @str "$%d " bet)
					p_at: betAt
					p_color: displayColor
					p_save:
				)
			)
			(= status2
				(Display (Format @str1 "$%d " machineDollars)
					p_at: dollarsAt
					p_color: displayColor
					p_save:
				)
			)
		)
		(cond
			(script)
			((and (>= machineDollars houseLimit)
			 		(== handOver TRUE))
				(Cls)
				(Print "Congratulations, Larry! You've broken the house limit.")
				(BackToRoom)
			)
			((ego mover?)
				(BackToRoom)
			)
		)
		(super doit:)
	)
)

(instance card of View
	(properties
		view vCards
		loop lCardBack
		cel cCardBack
		priority 14
		signal fixPriOn
	)
)

(instance deck of List)


(instance theDeal of View
   (properties
		x 29	;219
		y 184	;184
      z 0
      heading 0
      view vBJParts
      loop lDeal
      description "the Deal button"
		lookStr		"Click the Hand here to begin a new hand of blackjack."
   )
	(method (doVerb theVerb)
		(switch theVerb
			(verbDo
				(curRoom setScript: dealEm)
			)
			(else
				(super doVerb: theVerb &rest)
			)
		)
	)
)

(instance dealEm of Script
	(method (changeState newState &tmp i)
		(switch (= state newState)
			(0
				(Cls)
				(cond
					((== handOver FALSE)
						(Print "Please wait until this hand is over.")
						(self dispose:)
					)
					((== bet 0)
						(Print "You bet zero dollars, receive zero cards,
							and win zero dollars and zero cents.")
						(Print "You can play all night like this!")
						(self dispose:)
					)
					((> bet machineDollars)
						(Print "You can't bet more than you have.")
						(self dispose:)
					)
					(else
						(BlinkIt theDeal)
						(self cue:)
					)
				)
			)
			(1
				(HandsOff)
				(deck release:)
				(for ((= i 0)) (< i 52) ((++ i))
;					(= [cardDeck i] i)
					(deck addToEnd: i)
				)
				(for ((= i 0)) (< i 6) ((++ i))
					(if (IsObject: [dealerCards i])
						([dealerCards i] dispose:)
					)
					(if (IsObject: [egoCards i])
						([egoCards i] dispose:)
					)
					(if (IsObject: [splitCards i])
						([splitCards i] dispose:)
					)
				)
				(= egoTotal 0)
				(= egosAces 0)
				(= egoBusted FALSE)
				(= splitTotal 0)
				(= splitsAces 0)
				(= splitBusted FALSE)
				(= dealerTotal 0)
				(= dealersAces 0)
				(= egoCardInc 0)
				(= splitCardInc 1)
				(= dealerCardInc 0)
				(= dealersCard 0)
				(= egoDoublingDown 1)
				(= splitDoublingDown 1)
				(= handOver FALSE)
				(= egoHandDone FALSE)
				(= splitting FALSE)

				(= cardNum (GetCard))
				(= egosFirstCard (mod cardNum 13))
				(+= egoTotal (GetValue EGOHAND))
				(ShowCard EGOHAND egoCardInc)
				(= cycles 10)
			)
			(2

				(= cardNum (GetCard))
				(= dealersFirstCard cardNum)
				(+= dealerTotal (GetValue DEALER))
				((= [dealerCards dealersCard] (Clone card))
					view: vCards,
					x:	cardX,
					y: dealerY,
					init:
				)
				(theMusic3
					number:		sCardOn,
					play:,
				)
				(= cycles 10)
			)
			(3
				(++ egoCardInc)
				(= cardNum (GetCard));; added for debug
				(= egosSecondCard (mod cardNum 13)) ;; added for splitting
				(+= egoTotal (GetValue EGOHAND))
				(ShowCard EGOHAND egoCardInc)
				(= cycles 10)
			)
			(4

				(++ dealerCardInc)
				(++ dealersCard)
				(= cardNum (GetCard))
				(= dealerShowCard cardNum)	;; so can be checked for ace below
				(+= dealerTotal (GetValue DEALER))
				(ShowCard DEALER dealersCard)
				(= cycles 1)

			)
			(5
				(if (== egoTotal 21)
					(Cls)
					(Print "You have Blackjack!")
					(ShowDealerCard)
					(if (== dealerTotal 21)
						(Print "Unfortunately, so does the dealer, so this hand
							is a push.")
						(HandOver)
						(self dispose:)
					else
						(+= machineDollars (/ bet 2))	 ;;(added to in egoWins)
						(HandOver)
						(curRoom setScript: egoWins)
					)
				else
					(self cue:)
				)
			)
			(6
				(if (== (mod dealerShowCard 13) 0)	; does dealer have ace showing
					(if (AskInsurance)
						(if (== dealerTotal 21)
							(ShowDealerCard)
							(ChangeDollars 0)
							(Cls)
							(Print "The dealer has Blackjack!")
							(HandOver)
							(self dispose:)
						else
							(ChangeDollars (- (/ bet 2)))
							(self cue:)
						)
					else
						(if (== dealerTotal 21)
							(ShowDealerCard)
							(Cls)
							(Print "The dealer has Blackjack!")
							(ChangeDollars (- bet))
							(HandOver)
							(self dispose:)
						else
							(self cue:)
						)
					)
				else
					(self cue:)
				)
			)
			(7
				(self start: 0)
				(if (== egosSecondCard egosFirstCard)	; check if ego has a pair
					(if (and (<= (* 2 bet) machineDollars)
								(not splitting))
						;new stuff, let him double-down if he wants to
						;if he does, restart this script here
						(HandsOn)
						(self dispose:)
						;
;						(= splitting (AskSplit))
					)
					(if (== splitting TRUE)
						(if (== egosFirstCard 0)
							(= egosAces 1)
							(= splitsAces 1)
							(= egoTotal 11)
							(= splitTotal 11)
						else
							(= egoTotal (/ egoTotal 2))
							(= splitTotal egoTotal)
						)
						(= [splitCards 0] (Clone [egoCards 1]))
						([egoCards 1] dispose:)
						([splitCards 0]
							x:	cardX,
							y: splitY,
							init:,
						)
						(= cardNum (GetCard)) ;critical addition
						(= egosSecondCard (mod cardNum 13)) ;; added for splitting
						(+= egoTotal (GetValue EGOHAND))
						(ShowCard EGOHAND egoCardInc)
						(self cue:)
					else
						(self cue:)
					)
				else
					(self cue:)
				)
			)
			(8
				(if (== egoTotal 21)			; is normal hand a Blackjack?
					(Cls)
					(Print "Your first hand has Blackjack!")
					(= cardNum (GetCard))	; give split hand another card
					(+= splitTotal (GetValue SPLITHAND))
					(ShowCard SPLITHAND splitCardInc)
					(Animate (cast elements?) FALSE)
					; check if split hand is a Blackjack
					(if (== splitTotal 21)
						(Cls)
						(Print "Your split hand has Blackjack!")
						(curRoom setScript: splitStands)
						(self dispose:)
					else
						(HandsOn)
						(= egoHandDone TRUE)
						(self dispose:)
					)
				else
					(self cue:)
				)
			)
			(9
				(if (== egoTotal 22)
					(= egoTotal 12)
					(-- egosAces)
				)
				; check if split hand is a Blackjack
				(if (== splitTotal 21)
					(Cls)
					(Print "Your split hand has Blackjack!")
					(curRoom setScript: splitStands)
					(self dispose:)
				else
					(HandsOn)
					(self dispose:)
				)
			)
		)
	)
)

(instance hitMe of View
   (properties
		x 100	;284
		y 186	;185
      z 0
      heading 0
      view vBJParts
      loop lHitMe
      description "the Hit button"
		lookStr		"Click the Hand here to get another card."
   )
	(method (doVerb theVerb)
		(switch theVerb
			(verbDo
				(if
					(and
						splitting
						egoHandDone
					)
					(curRoom setScript: splitGetsHit)
				else
					(curRoom setScript: egoGetsHit)
				)
			)
			(else
				(super doVerb: theVerb &rest)
			)
		)
	)
)

(instance egoGetsHit of Script
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(++ egoCardInc) ;; used to place card view
				(cond
					((== handOver TRUE)
						(Cls)
						(Print "First, click on Deal to start a new hand.")
						(HandsOn)
						(self dispose:)
					)
					((and (not splitting)
							(== egoBusted TRUE))
						(Cls)
						(Print "You busted.")
						(HandsOn)
						(self dispose:)
					)
					(else
						(BlinkIt hitMe)
						(= cardNum (GetCard))
						(+= egoTotal (GetValue EGOHAND))
						(ShowCard EGOHAND egoCardInc)
						(= cycles 1)
					)
				)
			)
			(1
				(if (> egoTotal 21)
					(if egosAces
						(-- egosAces)
						(-= egoTotal 10)
					else
						(if (not splitting)
							(Cls)
							(Print "You busted!")
							(ShowDealerCard)
							(HandsOff) ;NEW
							(self setScript: dealerWins)
							(self dispose:)
						else
							(= egoHandDone TRUE)
							(Cls)
							(Print "Your first hand busted.")
							(= egoBusted TRUE)
							(= cardNum (GetCard))
							(+= splitTotal (GetValue SPLITHAND))
							(ShowCard SPLITHAND splitCardInc)
							(Animate (cast elements?) FALSE)
							(= egoHandDone TRUE)
							(HandsOn)
							(self dispose:)
						)
					)
				)
				(self cue:)
			)
			(2
				;; check if ego has 6 cards
				(if (and	(== egoBusted FALSE)
							(== egoCardInc 5))
					(Cls)
					(Print "You have six cards whose total is not over 21!")
					(if (not splitting)
						(ShowDealerCard)
						(curRoom setScript: egoWins)
						(self dispose:)
					else
						(= egoHandDone TRUE)
						(= cardNum (GetCard))
						(+= splitTotal (GetValue SPLITHAND))
						(ShowCard SPLITHAND splitCardInc)
						(HandsOn)
						(self dispose:)
					)
				else
					(HandsOn)
					(self dispose:)
				)
			)
		)
	)
)

(instance splitGetsHit of Script
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(++ splitCardInc) ;; used to place card view
				(cond
					(	(== handOver TRUE)
						(Cls)
						(Print "First, click on Deal to start a new hand.")
						(HandsOn)
						(self dispose:)
					)
					(	(== splitBusted TRUE)
						(Cls)
						(Print "You busted.")
						(HandsOn)
						(self dispose:)
					)
					(	else
						(BlinkIt hitMe)
						(= cardNum (GetCard))
						(+= splitTotal (GetValue SPLITHAND))
						(ShowCard SPLITHAND splitCardInc)
						(= cycles 1)
					)
				)
			)
			(1
				(if (> splitTotal 21)
					(if splitsAces
						(-- splitsAces)
						(-= splitTotal 10)
					else
						(Cls)
						(Print "Your split hand busted!")
						(= splitBusted TRUE)
						(ShowDealerCard)
						(curRoom setScript: splitStands)
						(self dispose:)
					)
				)
				(self cue:)
			)
			(2
				;; check if split has 6 cards
				(if (and	(== splitBusted FALSE)
							(== splitCardInc 5))
					(Cls)
					(Print "Your split hand has six cards!")
					(ShowDealerCard)
					(curRoom setScript: splitStands)
					(self dispose:)
				else
					(HandsOn)
					(self dispose:)
				)
			)
		)
	)
)

(instance stand of View
   (properties
		x 219		;100
		y 184		;186
      z 0
      heading 0
      view vBJParts
      loop lStand
      description "the Stand button"
		lookStr		"Click the Hand here when you want no more cards."
   )
	(method (doVerb theVerb)
		(switch theVerb
			(verbDo
				(if splitting
					(if (not egoHandDone)
						(BlinkIt stand)
						(= dontBlink TRUE)
						(=	egoHandDone TRUE)
						(= cardNum (GetCard)) ;critical addition
						(+= splitTotal (GetValue SPLITHAND))
						(ShowCard SPLITHAND splitCardInc)
						(Animate (cast elements?) FALSE)
						;check if splitTotal BJ
						(if (and	(== splitTotal 21)
									(== splitCardInc 1))
							(Cls)
							(Print "Your split hand has Blackjack!")
							(ShowDealerCard)
							(curRoom setScript: splitStands)
						)
					else
						(curRoom setScript: splitStands)
					)
				else
					(curRoom setScript: egoStands)
				)
			)
			(else
				(super doVerb: theVerb &rest)
			)
		)
	)
)

(instance egoStands of Script
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(if (== handOver TRUE)
					(Cls)
					(Print "First, click on Deal to start a new hand.")
					(HandsOn)
					(self dispose:)
				else
					(if (not egoDoublingDown)
						(BlinkIt stand)
					)
					(ShowDealerCard)
					(if
						(and
							(> egoTotal 21)
							egosAces
						)
						(-- egosAces)
						(-= egoTotal 10)
					)
				)
				(self cue:)
			)
			(1
				(++ dealerCardInc)
				(++ dealersCard)
				(if (or	(< dealerTotal 17)
							(and	dealersAces
									(== dealerTotal 17)))
					(= cardNum (GetCard))
					(+= dealerTotal (GetValue DEALER))
					(ShowCard DEALER dealerCardInc)
					(self changeState: 1)
				else
					(= cycles 1)
				)
			)
			(2
				(if (> dealerTotal 21)
					(if dealersAces
						(-- dealersAces)
						(-= dealerTotal 10)
						(-- dealerCardInc)
						(self changeState: 1)
					else
						(= cycles 1)
					)
				)
				(= cycles 1)
			)
			(3
				(cond
					(	(> egoTotal 21)
						(Cls)
						(HandsOff)
						(Print "You busted.")
						(curRoom setScript: dealerWins)
					)
					(	(> dealerTotal 21)
						(Cls)
						(Print "The dealer busted!")
						(self setScript: egoWins)
						(self dispose:)
					)
					(	(== dealerTotal egoTotal)
						(Cls)
						(Print "It's a push.")
					)
					(	(> dealerTotal egoTotal)
						(HandsOff)	;NEW
						(self setScript: dealerWins)
						(self dispose:)
					)
					(	else
						(self setScript: egoWins)
						(self dispose:)
					)
				)
				(HandOver)
				(self dispose:)
			)
		)
	)
)

(instance splitStands of Script
	(method (changeState newState)
		(switch (= state newState)
			(0
				(HandsOff)
				(if (== handOver TRUE)
					(= dontBlink FALSE)
					(Cls)
					(Print "First, click on Deal to start a new hand.")
					(HandsOn)
					(self dispose:)
				else
					(if (not dontBlink)
						(BlinkIt stand)
						(= dontBlink FALSE)
					)
					(ShowDealerCard)
					(self cue:)
				)
			)
			(1
				(++ dealerCardInc)
				(++ dealersCard)
				(if (or	(< dealerTotal 17)
							(and	dealersAces
									(== dealerTotal 17)))
					(= cardNum (GetCard))
					(+= dealerTotal (GetValue DEALER))
					(ShowCard DEALER dealerCardInc)
					(self changeState: 1)
				else
					(= cycles 1)
				)
			)
			(2
				(if (> dealerTotal 21)
					(if dealersAces
						(-- dealersAces)
						(-= dealerTotal 10)
						(-- dealerCardInc)
						(self changeState: 1)
					else
						(self cue:)
					)
				)
				(self cue:)
			)
			(3
				(cond
					(	(> dealerTotal 21)
						(Cls)
						(Print "The dealer busted!")
						(if (not egoBusted)
							(Cls)
							(Print "Your first hand wins!")
							(ChangeDollars (* bet egoDoublingDown))
						)
						(if (not splitBusted)
							(Cls)
							(Print "Your split hand wins!")
							(ChangeDollars (* bet splitDoublingDown))
						)
						(HandOver)
						(self dispose:)
					)
					(	(and
							(== egoCardInc 5)
							(not egoBusted)
						)
						(Cls)
						(Print "Your first hand wins!")
						(ChangeDollars (* bet egoDoublingDown))
					)
					(	(== dealerTotal egoTotal)
						(Cls)
						(Print "Your first hand is a push.")
					)
					(	(> dealerTotal egoTotal)
						(Cls)
			 			(Print "The dealer's hand beats your first hand.")
			 			(ChangeDollars (- (* bet egoDoublingDown)))
					)
					(	(and
							(== egoTotal 21)
							(== egoCardInc 1)
						)
						(Cls)
						(Print "Your first hand wins!")
						(ChangeDollars (+ bet (/ bet 2)))
					)
					(	else
						(if (not egoBusted)
							(Cls)
							(Print "Your first hand wins!")
							(ChangeDollars (* bet egoDoublingDown))
						else
							(Cls)
							(Print "Your first hand loses.")
							(ChangeDollars (- (* bet egoDoublingDown)))
						)
					)
				)
				(self cue:)
			)
			(4
				(cond
					(	(and
							(== splitCardInc 5)
							(not splitBusted)
						)
						(Cls)
						(Print "Your split hand wins!")
						(ChangeDollars (* bet splitDoublingDown))
					)
					(	(== dealerTotal splitTotal)
						(Cls)
						(Print "Your split hand is a push.")
					)
					(	(> dealerTotal splitTotal)
						(Cls)
			 			(Print "The dealer beats your split hand.")
			 			(ChangeDollars (- (* bet splitDoublingDown)))
						(HandOver)
						(self dispose:)
					)
					(	(and
							(== splitTotal 21)
							(== splitCardInc 1)
						)
						(Cls)
						(Print "Your split hand wins!")
						(ChangeDollars (+ bet (/ bet 2)))
					)
					(	else
						(if (not splitBusted)
							(Cls)
							(Print "Your split hand wins!")
							(ChangeDollars (* bet splitDoublingDown))
						else
							(Cls)
							(Print "Your split hand loses.")
							(ChangeDollars (- (* bet splitDoublingDown)))
						)
					)
				)
				(self cue:)
			)
			(5
				(HandOver)
				(self dispose:)
			)
		)
	)
)

(instance egoWins of Script
	(method (changeState newState)
		(switch (= state newState)
			(0
;				(Printf "send to changeDollars: %d" (* bet egoDoublingDown))
				(Cls)
				(Printf "You win $%d."
					(ChangeDollars (* bet egoDoublingDown))
					#time 	3
					#dispose
				)
				(HandOver)
				(self dispose:)
			)
		)
	)
)

(instance dealerWins of Script
	(method (changeState newState)
		(switch (= state newState)
			(0
				(Cls)
			 	(ChangeDollars (- (* bet egoDoublingDown)))
			 	(Print "The dealer wins!"
					#time 	3
					#dispose
				)
				(HandOver)
				(self dispose:)
			)
		)
	)
)

(instance increase of View
   (properties
      x 158
      y 161
      view vBJParts
      loop lIncrease
      description "the Increase button"
		lookStr 		"Click the Hand here to increase your bet.
						This button repeats automatically
						if you hold it down."
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
	(method (handleEvent evt &tmp event ticks doDelay)
		(if (self onMe: evt)
			(cond
				((or (< 0 (evt modifiers?) 5)
					  (!= ((theIconBar curIcon?) message?) verbDo))
					(super handleEvent: evt)
				)
				((== handOver FALSE)
					(Cls)
		  			(Print "You must wait until this hand is over.")
					(return (evt claimed: TRUE))
				)
				(else
;					(BlinkIt self)
					(switch (evt type?)
						(mouseDown
							(while (!= ((= event (Event new:))	type?) mouseUp)
								(= ticks (GetTime))
								(if doDelay		;; delay
									(while (<  (GetTime)  (+ tickDelay ticks)))
								)
								(= doDelay TRUE)
								(event localize:)
								(cond
									((== bet houseLimit)
										(Cls)
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
										(= status1 (Display (Format @str "$%d" bet)
												p_at: betAt
												p_color: displayColor
												p_save:
											)
										)
										(Cls)
										(Print "You can't bet more than you have.")
										(break)
									)
									((< bet 0)
										(Cls)
										(Print "You must bet more than that!")
									)
									((mod bet 2)
										(Cls)
										(Print "Please bet an even number of dollars
											(2, 4, 6, etc.).
 											This machine doesn't pay half-dollars.")
										(-- bet)
										(Display "" p_restore: status1)
										(= status1 (Display (Format @str "$%d" bet)
												p_at: betAt
												p_color: displayColor
												p_save:
											)
										)
									)
									(else
										(+= bet (BetSize))
										(Display "" p_restore: status1)
										(= status1 (Display (Format @str "$%d" bet)
												p_at: betAt
												p_color: displayColor
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
										(Cls)
										(Print "This machine has a $10,000.00 limit.")
									)
									((< machineDollars (+ (BetSize) bet 1))
										(if (mod machineDollars 2)
											(= bet (- machineDollars 1))
										else
											(= bet machineDollars)
										)
										(Display "" p_restore: status1)
										(= status1
											(Display (Format @str "$%d" bet)
												p_at: betAt
												p_color: displayColor
												p_save:
											)
										)
										(Cls)
										(Print "You can't bet more than you have.")
									)
									((< bet 0)
										(Cls)
										(Print "You must bet more than that!")
									)
									((mod bet 2)
										(Cls)
										(Print "Please bet an even number of dollars
											(2, 4, 6, etc.).
 											This machine doesn't pay half-dollars.")
										(-- bet)
										(Display "" p_restore: status1)
										(= status1 (Display (Format @str "$%d" bet)
												p_at: betAt
												p_color: displayColor
												p_save:
											)
										)
									)
									(else
										(+= bet (BetSize))
										(Display "" p_restore: status1)
										(= status1 (Display (Format @str "$%d" bet)
												p_at: betAt
												p_color: displayColor
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
)

(instance decrease of View
   (properties
      x 157
      y 186
      view vBJParts
      loop lDecrease
      description "the Decrease button"
		lookStr 		"Click the Hand here to decrease your bet.
						This button repeats automatically
						if you hold it down."
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
		(if (self onMe: evt)
			(cond
				((or (< 0 (evt modifiers?) 4)
					  (!= ((theIconBar curIcon?) message?) verbDo))
					(super handleEvent: evt)
				)
				((== handOver FALSE)
					(Cls)
		  			(Print "Wait until this hand is over.")
					(return (evt claimed: TRUE))
				)
				(else
;					(BlinkIt self)
					(switch (evt type?)
						(mouseDown
							(while (!= ((= event (Event new:))	type?) mouseUp)
								(= ticks (GetTime))
								;; delay
								(while (< (GetTime) (+ tickDelay ticks)))
								(event localize:)
								(if (> bet (+ minBet 1))
									(-= bet (BetSize))
									(Display "" p_restore: status1)
									(= status1 (Display (Format @str "$%d" bet)
											p_at: betAt
											p_color: displayColor
											p_save:
										)
									)
								else
									(Cls)
									(Printf "The minimum bet in this machine is $%d.00."
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
								(if (> bet (+ minBet 1))
									(-= bet (BetSize))
									(Display "" p_restore: status1)
									(= status1 (Display (Format @str "$%d" bet)
											p_at: betAt
											p_color: displayColor
											p_save:
										)
									)
								else
									(Cls)
									(Printf "The minimum bet in this machine is $%d.00."
										minBet)
								)
								(return (evt claimed: TRUE))
							)
						)
					)
				)
			)
		)
	)
)

(instance changeBet of View
   (properties
		x 157
		y 174
      z 0
      heading 0
      view vBJParts
      loop lChangeBet
      description "the Change Bet button"
		lookStr		"Click the two arrows to increase or decrease your bet."
   )
)

(instance surrender of View
   (properties
		x 27
		y 76
      z 0
      heading 0
      view vBJParts
      loop lSurrender
      description "the Surrender button"
		lookStr		"Click the Hand here to surrender your cards
						and have half your bet refunded."
   )
	(method (doVerb theVerb)
		(switch theVerb
			(verbDo
				(curRoom setScript: surrenderScript)
			)
			(else
				(super doVerb: theVerb &rest)
			)
		)
	)
)

(instance theDouble of View
   (properties
		x 291
		y 72
      z 0
      heading 0
      view vBJParts
      loop lDouble
      description "the Double Down button"
		lookStr		"Click the Hand only here when you have two cards totaling
						10 or 11. You will automatically receive only one more card
						and double your bet."
   )
	(method (doVerb theVerb)
		(switch theVerb
			(verbDo
				(cond
					((> (+ (Commit) bet)
						 (if (== (mod machineDollars 2) 0) machineDollars else (- machineDollars 1)))
						(Cls)
						(Print "You're a little short of funds at the moment, Larry.")
					)
					((== handOver TRUE)
						(Cls)
						(Print "First, click on Deal to start a new hand.")
					)
					(splitting
						(Print "You can't double down when splitting!")
					)
					;check if ego total 10 or 11
					((and (< 9 (- egoTotal (* egosAces 10)) 12)
							(!= egoHandDone TRUE))
						(BlinkIt self)
						(= egoDoublingDown 2)
						(++ egoCardInc)
						(= cardNum (GetCard))
						(+= egoTotal (GetValue EGOHAND))
						(ShowCard EGOHAND egoCardInc)
						(curRoom setScript: egoStands)
					)
					(else
						(Cls)
						(Print "You may only double when your hand
							contains two cards totaling 10 or 11.")
					)
				)
			)
			(else
				(super doVerb: theVerb &rest)
			)
		)
	)
)
(instance theSplit of View
   (properties
		x 292
		y 104
      z 0
      heading 0
      view vBJParts
      loop lSplit
      description "the Split button"
		lookStr		"Click the Hand here when you have only two matching cards
						and you want to split them into two hands,
						doubling your bet."
   )
	(method (doVerb theVerb)
		(switch theVerb
			(verbDo
				(if (and (== egosSecondCard egosFirstCard)
							(not handOver)
							(not splitting))
					(BlinkIt theSplit)
					(HandsOff)
					(= splitting TRUE)
					(dealEm start: 7)
					(curRoom setScript: dealEm)
				else
					(self doVerb: verbLook)
				)
			)
			(else
				(super doVerb: theVerb &rest)
			)
		)
	)
)

(instance surrenderScript of Script
	(method (changeState newState)
		(switch (= state newState)
			(0
				(cond
					((== handOver TRUE)
						(Cls)
						(Print "First, click on Deal to start a new hand.")
					)
					((and (== egoCardInc 1)
							(not splitting))
						(BlinkIt surrender)
						(Cls)
						(Print "You surrender your cards and are refunded half of
							your bet.")
						(ChangeDollars (- (/ bet 2)))
						(HandOver)
					)
					(else
						(Cls)
						(Print "It's too late now!")
					)
				)
				(self dispose:)
			)
		)
	)
)

(instance odds of View
   (properties
		x 26
		y 106
      z 0
      heading 0
      view vBJParts
      loop lOdds
      description "the Odds button"
		lookStr		"Click the Hand here to learn this machine's current odds."
	)
	(method (doVerb theVerb)
		(switch theVerb
			(verbDo
				(BlinkIt odds)
				(Cls)
				(Print "This machine has a $10.00 minimum bet and a
					$10,000.00 limit.\n\n
 					Bets must be a multiple of $2.00.\n\n
					Dealer must hit 16, and must stand on 17,
					unless he has an ace.\n\n
					Blackjack pays 3 to 2 odds.\n\n
					Any six cards under 22 wins."
					WIDE
					#title {HOUSE RULES}
				)
				(Print "Insurance bets are offered whenever the dealer shows an ace.
 					They are half your bet, and pay 2 to 1 odds.\n\n
					You may split a pair or surrender whenever you have only two
					cards.\n\n
					You may double down if your cards total 10 or 11 points."
					WIDE
					#title {HOUSE RULES}
				)
			)
			(else
				(super doVerb: theVerb &rest)
			)
		)
	)
)

(instance cashout of View
   (properties
      x 284	;29
      y 185	;184
      z 0
      heading 0
      view vBJParts
      loop lCashout
      description "the Cash Out button"
		lookStr		"Click the Hand here to stop playing this machine.
						Your winnings will be given to you automatically."
	)
	(method (doVerb theVerb)
		(switch theVerb
			(verbDo
				(if (== handOver FALSE)
					(Cls)
					(Print "Wait until this hand is over.")
				else
					(BlinkIt cashout)
					(BackToRoom)
				)
			)
			(	else
				(super doVerb: theVerb &rest)
			)
		)
	)
)

(procedure (BackToRoom)
	(theMusic2
		number:	sCashOut,
		play:
	)
	(Cls)
	(if
		brokeHouse
		(Print "This machine only contains $10,000.00;
			trust us, we'll mail you the rest."
		)
	)
	(Printf
		"You pocket your $%d.00 from the tray
		and wish you had won more."
		machineDollars
	)
	(ego
		z:	 	0,
		hide:
	)
	(deck dispose:)
	(= larryDollars machineDollars)
	(cast eachElementDo: #dispose:)
	(Cls)
	(curRoom newRoom: 310)
)

;for debugging
;(procedure (GetCard &tmp num temp)
;	(if
;		(== [cardDeck cardNumber] -1)
;		(= temp (Random 0 (- (deck size?) 1)))
;	else
;		(= temp [cardDeck cardNumber])
;	)
;	(deck delete:	(= num
;							(deck at: temp)
;						)
;	)
;	(++ cardNumber)
;	(return num)
;)

(procedure (GetCard &tmp num)
	(if debugging	;this one let's you input numbers
		(= num (GetNumber {card #}))
		(return num)
	else
		(= num (Random 0 51))	;this is the normal one
		(if (== (deck at: num) -1)
			(= num (GetCard))
		else
			(deck delete: (deck at: num))
			(deck addAfter: (- num 1) -1)
			(return num)
		)
	)
)

;put this one back in for shipping version, no, obsolete
;(procedure (GetCard &tmp num)
;	(deck delete:	(= num (deck at: (Random 0 (- (deck size?) 1)))))
;	(return num)
;)

(procedure (ShowDealerCard)
	([dealerCards 0]
		loop: (/ dealersFirstCard 13),
		cel: (mod dealersFirstCard 13)
	)
	(Animate (cast elements?) FALSE)
)

(procedure (GetValue whoseHand)
	(cond
		( 	(== (mod cardNum 13) 0)
			(switch whoseHand
				(0
					(++ dealersAces)
				)
				(1
					(++ egosAces)
				)
				(2
					(++ splitsAces)
				)
			)
			11
		)
		(	(< (mod cardNum 13) 9)
			(+ (mod cardNum 13) 1)
		)
		(	else
			10
		)
	)
)


(procedure (ShowCard whoseHand whoseCard)
	(theMusic3
		number:		sCardOn,
		play:,
	)
	(switch whoseHand
		(0
			((= [dealerCards dealersCard] (Clone card))
				loop: (/ cardNum 13),
				cel: (mod cardNum 13),
				x: (+ cardX (* whoseCard cardGap)),
				y: dealerY,
				init:,
			)
		)
		(1
			((= [egoCards egoCardInc] (Clone card))
				loop: (/ cardNum 13),
				cel: (mod cardNum 13),
				x: (+ cardX (* whoseCard cardGap)),
				y: egoY,
				init:,
			)
		)
		(2
			((= [splitCards splitCardInc] (Clone card))
				loop: (/ cardNum 13),
				cel: (mod cardNum 13),
				x: (+ cardX (* whoseCard cardGap)),
				y: splitY,
				init:,
			)
		)
	)
)

;(procedure (AskDoubleDown)
;	(if
;		(>	(+ (Commit) bet)
;			(if (== (mod machineDollars 2) 0) machineDollars else (- machineDollars 1)))
;		(return 1)
;	else
;		(return
;			(Print "Do you want to double down?"
;				#button: 	{Yes} 2
;				#button: 	{No} 1
;;				#window: SysWindow
;			)
;		)
;	)
;)

(procedure (Commit)
	(return
		(+
			(* bet egoDoublingDown)
			(if splitting
				(* bet splitDoublingDown)
			else
				0
			)
		)
	)
)

(procedure (AskInsurance)
	(HandsOn)
	(if
		(>=
			(- (+ (Commit) (/ bet 2)) 1)
			(if (== (mod machineDollars 2) 0)
				machineDollars
			else
				(- machineDollars 1)
			)
		)
		(return FALSE)
	else
		(Cls)
		(theGame setCursor: ARROW_CURSOR TRUE)
		(Animate (cast elements?) FALSE)
		(return
			(Print "Do you want insurance?"
				#button: {Yes} TRUE
				#button: {No} FALSE
			)
		)
		(theGame setCursor: ((theIconBar curIcon?) cursor?) TRUE)
	)
	(HandsOff)
)

;(procedure (AskSplit)
;	(HandsOn)
;	(return
;		(Print "Do you want to split?"
;			#button: {Yes} TRUE
;			#button: {No} FALSE
;;			#window: SysWindow
;		)
;	)
;	(HandsOff)
;)

(procedure (ChangeDollars amount)
	(if (> amount 0)
		(if (or (and (== egoTotal 21)
						 (== egoCardInc 1))
				  (and (== splitTotal 21)
				  		 (== splitCardInc 1)))
			(soundFX
				number:		sBlackJack,
				play:
			)
		else
			(soundFX
				number:		sBJwin,
				play:
			)
		)
	)
	(if (< amount 0)
		(soundFX
			number:		sBJlose,
			play:
		)
	)
	(if (> amount 0)
		(if (< (+ machineDollars amount) 0)
			(= machineDollars houseLimit)
			(= brokeHouse TRUE)
		else
			(+= machineDollars amount)
		)
	else
		(+= machineDollars amount)
	)
	(if (< machineDollars 0)
		(= machineDollars 0)
	)
	(if (> machineDollars houseLimit)
		(= machineDollars houseLimit)
		(= brokeHouse TRUE)
	)
	(Display "" p_restore: status2)
	(= status2 (Display (Format @str1 "$%d " machineDollars)
			p_at: dollarsAt
			p_color: displayColor
			p_save:
		)
	)
	(if (and (== egoTotal 21)
		 		 (== egoCardInc 1))
		(return (+ amount (/ amount 2)))
	else
		(return amount)
	)
)

(procedure (BlinkIt button)
	(soundFX
		number:	sButtonPress,
		play:
	)
	(button
		setCel:	1
	)
	(Animate (cast elements?) FALSE)
	(button
		setCel:	0
	)
	(Animate (cast elements?) FALSE)
)

(procedure (BetSize &tmp theSize)
	(= theSize (/ (+ bet 100) 50))
	(if (mod theSize 2)
		(return (- theSize 1))
	else
		(return theSize)
	)
)

(procedure (HandOver)
	(= handOver TRUE)
	(HandsOn)
	(if (>= bet machineDollars)
 		(if (mod bet 2)
			(= bet (- machineDollars 1))
		else
			(= bet machineDollars)
		)
		(if (< bet 10)
			(= bet 10)
		)
		(Display "" p_restore: status1)
		(= status1 (Display (Format @str "$%d" bet)
				p_at: betAt
				p_color: displayColor
				p_save:
			)
		)
	)
)

(instance soundFX of Sound
	(properties
		flags		1
	)
)
