;**
;**	Logics for Room 710 -- Scrolling intro screens
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford &
;**
;**	Last Update:  June 17, 1991
;**


(script# 710)

(public
	rm710		0
)

(define pTitleScreen 	700)
(define pCreditScreen 	710)
;(define mThemeSong		700)		;**	700 	Titles

(instance rm710 of LLRoom
	(properties
		picture	pTitleScreen
	)
	(method (init)
		(theGame setCursor: NON_CURSOR TRUE 350 185)
		(Load PICTURE
			pCreditScreen
		)
		(theIconBar disable:)
		(super init:)
		(theMusic
			number:		mThemeSong,
			loop:			-1,
			vol:			127,
			flags:		1,
			play:,
		)
		;(define	mThemeSong				700)		;**	700 	Titles
		(keyDownHandler addToFront: self)
		(mouseDownHandler addToFront: self)
		(curRoom
			style:		SCROLLUP,
			setScript: 	sRoom NULL 710
		)
	)
	(method (newRoom n)
		(keyDownHandler delete: self)
		(mouseDownHandler delete: self)
		(super newRoom: n)
	)
	(method (handleEvent event)
		(if (event type?)
			(event claimed: TRUE)
			(curRoom newRoom: 720)
		)
	)
)

(instance sRoom of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(= seconds 5)
			)
			(
				(curRoom
					drawPic:		register,
				)
				(if (== register 710)
					(= register 700)
					(curRoom style: SCROLLDOWN)
					(Display "Copyright 1987, 1991 by Sierra On-Line, Inc."
						p_width	318
						p_at		1 178
						p_font		smallFont
						p_mode		teJustCenter
						p_color	colWhite
					)
				else
					(= register 710)
					(curRoom style: SCROLLUP)
				)
				(-- state)
				(= seconds 10)
			)
		)
	)
)
