;**
;**	Logics for Room 720 -- Trivia Quiz
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford
;**
;**	Last Update:  May 31, 1991
;**

(script# 720)

(public
	rm720		0
)

(procedure
	HighLight
	HighLightCopy
	MouseInRect
	SetFlgTrivia
	TstFlgTrivia
	LoadSetup
	SaveSetup
)

(define copyFile 719)
(define copyQuestions 40)

(define	FIRST_FILE				721)
(define	LAST_FILE				752)

(define	QUESTION_X				20)
(define	QUESTION_WIDTH			277)
(define	QUESTION_Y				12)

(define	NUMBERS_X				50)
(define	NUMBERS_WIDTH			12)

(define	ANSWERS_X				65)
(define	ANSWER_WIDTH			205)

;(define  pBlackScreen			720)

(define	sQuestionAppears		710)
(define	sCorrect					711)
(define	sWrong					712)

(enum												;**	RoomScript state values
	intro
	askAquestion
	gotAnAnswer
	heBlueIt
	sweepUp
	copyQuestion
	copyAnswer
	dumpSucker
	youRbright
	changeRooms
)

(local
	atY
	correct
	question		= 0
	hisAnswer	= 0
	theAnswer
	thisColor
	thisFile
	theKey
	[filesSeen	99] 			;**	Handle 3*16 files, numbered 141-188
	[string	 300]
	missedOne
	dQuestion
	[dLetter 4]
	[dAnswer 4]
)

(instance rm720 of LLRoom
	(properties
		picture	721
	)
	(method (init &tmp tryCount)
;		(= debugging TRUE)
		(LoadMany SOUND
			sQuestionAppears
			sCorrect
			sWrong
		)
		(LoadSetup)
;			(if debugging
;				(Printf "These numbers came off the disk: %7d %7d %7d"
;								[filesSeen 0] [filesSeen 1] [filesSeen 2] )
;			)
		(theMusic2
			number:		sQuestionAppears,
			loop:			1,
			vol:			127,
			flags:		1,
		)
		(theMusic3
			loop:			1,
			vol:			127,
			flags:		1,
		)

		(while (and	(< (++ tryCount) 1000)
						(TstFlgTrivia (- (= thisFile (Random FIRST_FILE LAST_FILE)) FIRST_FILE)))
		)
		(if (>= tryCount 1000)
			(= [filesSeen 0] 0)
			(= [filesSeen 1] 0)
			(= [filesSeen 2] 0)
			(= thisFile (Random FIRST_FILE LAST_FILE))
		)
		(SetFlgTrivia (- thisFile FIRST_FILE))
		(SaveSetup)
		(Load TEXT thisFile)

;			(if debugging
;				(Printf "We're working file #%d.
;					These numbers just went back to the disk: %7d %7d %7d"
;					thisFile			[filesSeen 0] [filesSeen 1] [filesSeen 2] )
;			)

		(super init:)

		(mouseDownHandler addToFront: self)
		(keyDownHandler addToFront: self)

		(HandsOn)
		(User
			canControl			FALSE,
		)
		(ego
			init:,
			hide:,
		)
		(self
			setScript: 	RoomScript
		)
		(theGame setCursor: ARROW_CURSOR TRUE 310 185)
	)

	(method (dispose)
		(mouseDownHandler delete: self)
		(keyDownHandler delete: self)
		(super dispose:)
	)
)

(instance RoomScript of Script
	(method (handleEvent event &tmp evMod [str 33])

		(if (and (== state youRbright)	;**	They've read the msg, now leave
					(not (event claimed)))
			(self cue)
		)

		(if (== (event type?) keyDown)	;**	Be nice and accept shortcuts here
			(= evMod (event modifiers?))
			(switch (event message?)
				(`#2
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
				(`^q ; quit
					(theGame quitGame:)
					(event claimed: TRUE)
				)
;**					(`#7 ; restore
;**						(theGame restore:)
;**						(event claimed: TRUE)
;**					)
				(`@x
					(if (& evMod ctrlDown)
						(event claimed: TRUE)
						(Print "CHEATER, CHEATER, CHEATER!!!")
						(Print "Oh well, O.K., since it's you,
							go on and play, but only this time!")
						(= question 10)
						(self changeState: (- copyQuestion 1))
					)
				)
			)
		)

		(if (or	(event claimed)
					(and
						(!= state askAquestion)
						(!= state copyQuestion)
					)
					(super handleEvent event))
			(return)
		)

		(switch (event type?)
			(mouseDown
				(cond
					((MouseInRect event 41  61 200  81)
						(= hisAnswer 1)
						(self cue)
					)
					((MouseInRect event 41 91 200 111)
						(= hisAnswer 2)
						(self cue)
					)
					((MouseInRect event 41 122 200 142)
						(= hisAnswer 3)
						(self cue)
					)
					((MouseInRect event 41 151 200 176)
						(= hisAnswer 4)
						(self cue)
					)
				)
			)

			(keyDown
				(= evMod (event modifiers?))
				(switch (event message?)
					(`a
						(= hisAnswer 1)
						(self cue)
					)
					(`b
						(= hisAnswer 2)
						(self cue)
					)
					(`c
						(= hisAnswer 3)
						(self cue)
					)
					(`d
						(= hisAnswer 4)
						(self cue)
					)
					(`A
						(= hisAnswer 1)
						(self cue)
					)
					(`B
						(= hisAnswer 2)
						(self cue)
					)
					(`C
						(= hisAnswer 3)
						(self cue)
					)
					(`D
						(= hisAnswer 4)
						(self cue)
					)
				)
			)
		)
	)
	(method (changeState newState &tmp age i [tmpString 200])
		(switch (= state newState)
			(intro	;0
				(Print "\"Leisure Suit Larry in the Land of the Lounge Lizards\"
					contains some elements of plot which may not be considered
					appropriate for some children."
					#title:			{Sturgeon General's Warning}
				)
				(= dQuestion
					(Display "Use the TAB key to select,\n
						then ENTER to continue."
						#p_at: 		1 156
						#p_color:	colVLBlue
						#p_mode:		teJustCenter
						#p_width:	318
						#p_font:		smallFont
						#p_save:
					)
				)
				(= age 0)
				(theGame setCursor: ARROW_CURSOR TRUE)
				(Animate (cast elements?) FALSE)
				(while (not age)
					(= age (PrintD {So, how old are you, anyway?}
							#new
							#button	{under 15}		15
							#button	{16 to 18}		16
							#button	{19 to 39}		19
							#new
							#button	{40 to 65}		40
							#button	{66 to 99}		66
							#button  {over 100} 		100
							#title	{No lying, please!}
						)
					)
				)
				(Display "" #p_restore: dQuestion)
				(switch age
					(15
						(theMusic	stop:)
						(theMusic3
							number:	sWrong,
							play:		,
						)
						(Print "Sorry, but this game can only be played by adults,
							or with an adult.
							Please find an adult, come back, and try again.")
						(= quit TRUE)
					)
					(100
						(theMusic	stop:)
						(theMusic3
							number:	sWrong,
							play:		,
						)
						(Print "Oh no, you're not!")
						(= quit TRUE)
					)
					(else
						(switch age
							(16
								(Print "Shouldn't you be doing your homework?")
							)
							(19
								(Print "Yeah, sure you are!")
							)
							(40
								(Print "Gee, that's funny; you look much older!")
							)
							(66
								(Print "\t\t\t\t__Warning!\n\n
									This game may be hazardous to your health!")
							)
						)
						(Print "To verify your age,
							please answer these five simple questions."
						)
						(= cycles 11)
					)
				)
			)
			(askAquestion	;1
				(theMusic2		play:)

				;**	Do the question.
				(GetFarText thisFile (* question 5) @string)
				(= theAnswer (- (StrAt @string 0) 48))
				(for ((= i 0)) (<= i (StrLen @string)) ((++ i))
					(StrAt @tmpString i (StrAt @string (+ i 1)))
				)
				(= thisColor					;**	pick a color, but not colLRed
					(switch question
						(0 	colLYellow	)
						(1		colVLGreen	)
						(2		colVLBlue	)
						(3		colLMagenta	)
						(else	colLCyan		)
					)
				)
				(= dQuestion
					(Display
						@tmpString
						#p_at: 		QUESTION_X QUESTION_Y
						#p_color:	thisColor
						#p_font:		userFont
						#p_width:	QUESTION_WIDTH
						#p_save:
					)
				)

				;**	Do all four answers.
				(= atY 62)
				(for ((= i 1)) (< i 5) ((++ i) (+= atY 30))
					;**	Display the number & a period...
					(Format @string "%c." (+ i 96))
					(= [dLetter (- i 1)]
						(Display
							@string
							#p_at: 		NUMBERS_X atY
							#p_color:	thisColor
							#p_font:		userFont
							#p_save:
						)
					)

					;**	then display the text of the answer.
					(GetFarText thisFile (+ (* question 5) i) @tmpString)
					(= [dAnswer (- i 1)]
						(Display
							@tmpString
							#p_at: 		ANSWERS_X atY
							#p_color:	thisColor
							#p_font:		userFont
							#p_width:	ANSWER_WIDTH
							#p_save:
						)
					)
				)
			)
			(gotAnAnswer	;2
				(if (or	(== hisAnswer theAnswer)
							(== theAnswer 0))
					(HighLight hisAnswer colLGreen)
					(theMusic3
						number:	sCorrect,
						play:		127,
;						fade:		,
					)
					(Print "Correct"
						#at 			200 34
						#time 		3
					)
					(++ correct)
					(++ state)					;**	skip boo-boo messages
				else
					(HighLight hisAnswer colVLRed)
					(theMusic3
						number:	sWrong,
						play:		,
					)
				)
				(= cycles 22)
			)
			(heBlueIt		;3
				(if (not missedOne)
					(++ missedOne)
					(if (== correct 4)
						(Print "Oops. You blew that one!\n\n
							But since you came so close,
							you get to play in spite of yourself!")
					else
						(Print "Oops. You blew that one!\n\n
							If you miss another, you can't possibly be old enough
							to play this game!")
					)
				else
					(Print "\t\t\t\t\t\t\t\tYOU'RE A KID!!\n\n
						Shame on you for trying to deceive a friendly little
						computer game like this!
						Come back and try again when you're older (or wiser)."
						BIG)
					(= quit TRUE)
				)
				(= cycles 22)
			)
			(sweepUp			;4					;**	Clear up text that's still there.
				(Cls)
				(Display "" #p_restore: dQuestion)
				(for ((= i 0)) (< i 4) ((++ i))
					(Display "" #p_restore: [dLetter i])
					(Display "" #p_restore: [dAnswer i])
				)
				(= hisAnswer 0)
				(if (< (++ question) 5)
					(= state (- askAquestion 1))
				)
				(= cycles 11)
			)


			;**	He got through the questions, now handle copy protection:

			(copyQuestion						;5
				(theMusic2		play:)
				(Print "And now a question that won't tax your memory.
					This question refers to the documentation that came with
					your copy of this game.")
				(= question (Random 0 (- copyQuestions 1)))
				;**	Do the question.
				(GetFarText copyFile (* question 5) @string)
				(= theAnswer (- (StrAt @string 0) 48))
				(for ((= i 0)) (<= i (StrLen @string)) ((++ i))
					(StrAt @tmpString i (StrAt @string (+ i 1)))
				)
				(= thisColor  colVLGreen)
				(= dQuestion
					(Display
						@tmpString
						#p_at: 		QUESTION_X QUESTION_Y
						#p_color:	thisColor
						#p_font:		userFont
						#p_width:	QUESTION_WIDTH
						#p_save:
					)
				)

				;**	Do all four answers.
				(= atY 62)
				(for ((= i 1)) (< i 5) ((++ i) (+= atY 30))
					;**	Display the number & a period...
					(Format @string "%c." (+ i 96))
					(= [dLetter (- i 1)]
						(Display
							@string
							#p_at: 		NUMBERS_X atY
							#p_color:	thisColor
							#p_font:		userFont
							#p_save:
						)
					)

					;**	then display the text of the answer.
					(GetFarText copyFile (+ (* question 5) i) @tmpString)
					(= [dAnswer (- i 1)]
						(Display
							@tmpString
							#p_at: 		ANSWERS_X atY
							#p_color:	thisColor
							#p_font:		userFont
							#p_width:	ANSWER_WIDTH
							#p_save:
						)
					)
				)
			)
			(copyAnswer							;6
				(if (== hisAnswer theAnswer)
					(HighLightCopy hisAnswer colLGreen)
					(theMusic3
						number:	sCorrect,
						play:		127,
					)
					(Print "Correct"
						#at 			200 34
						#time 		3
					)
					(++ state)					;**	skip boo-boo messages
				else
					(HighLightCopy hisAnswer colVLRed)
					(theMusic3
						number:	sWrong,
						play:		,
					)
				)
				(= cycles 22)
			)
			(dumpSucker							;7
				(Print "Oh, oh!
					You couldn't possibly miss that question and have a legal 
					copy of the game!"
					#title	{Yo, ho. Yo, ho!}
					#button	{Quit}	0
					#button	{Leave}	1
					#button	{OK}		4
					#button	{Done}	2
					#button	{Exit}	3
				)
				(= quit TRUE)
			)
			(youRbright							;8
				(Cls)
				(Display "" #p_restore: dQuestion)
				(for ((= i 0)) (< i 4) ((++ i))
					(Display "" #p_restore: [dLetter i])
					(Display "" #p_restore: [dAnswer i])
				)
				(Format @string "Thank you.\n\n
					And now, slip into your leisure suit and prepare to enter the
					\"Land of the Lounge Lizards\" with \"Leisure Suit Larry!\"")
				(Display @string
					#p_at: 		60 60
					#p_color:	colLYellow
					#p_font:		userFont
					#p_width:	230
				)
				(= seconds 6)
			)
			(changeRooms 						;9
				(curRoom newRoom: 100)
			)
		)
	)
)

(instance qaFile of File
	(properties
		name		"LARRY.DRV"
	)
)

(procedure (HighLight i c &tmp [tmpString 200])
	(= atY (+ 32 (* i 30)))
	(Format @string "%c." (+ i 96))
	(Display
		@string
		#p_at: 		NUMBERS_X atY
		#p_color:	c
		#p_font:		userFont
;**			#p_width:	NUMBERS_WIDTH
	)

	(GetFarText thisFile (+ (* question 5) i) @tmpString)
	(Display
		@tmpString
		#p_at:		ANSWERS_X atY
		#p_color:	c
		#p_font:		userFont
		#p_width:	ANSWER_WIDTH
	)
)

(procedure (HighLightCopy i c &tmp [tmpString 200])
	(= atY (+ 32 (* i 30)))
	(Format @string "%c." (+ i 96))
	(Display
		@string
		#p_at: 		NUMBERS_X atY
		#p_color:	c
		#p_font:		userFont
;**			#p_width:	NUMBERS_WIDTH
	)

	(GetFarText copyFile (+ (* question 5) i) @tmpString)
	(Display
		@tmpString
		#p_at:		ANSWERS_X atY
		#p_color:	c
		#p_font:		userFont
		#p_width:	ANSWER_WIDTH
	)
)

(procedure (MouseInRect event left top right bottom &tmp evX evY)
	(= evX (event x?))
	(= evY (event y?))
	(return (if (and	( > evX left)
							( > evY top)
							( < evX right)
							( < evY bottom))
			TRUE
		else
			FALSE
		)
	)
)

(procedure (SetFlgTrivia flag)
	(|= [filesSeen (/ flag 16)] (>> $8000 (mod flag 16)))
)
(procedure (TstFlgTrivia flag)
	(return (if (& [filesSeen (/ flag 16)] (>> $8000 (mod flag 16))) TRUE else FALSE))
)

(procedure (LoadSetup &tmp soundState [str 30])
	(if (!= (qaFile open: fRead) NULL)
		(qaFile read: @str 8)
		(= [filesSeen 0] (ReadNumber @str))
		(qaFile read: @str 8)
		(= [filesSeen 1] (ReadNumber @str))
		(qaFile
			read: @str 8,
			close:
		)
		(= [filesSeen 2] (ReadNumber @str))
	)
)
(procedure (SaveSetup &tmp [str 40])
	(if (!= (qaFile open: fTrunc) NULL)
		(Format @str
			"%8d%8d%8d"
			[filesSeen 0]
			[filesSeen 1]
			[filesSeen 2]
		)
		(qaFile
			writeString: @str,
			close:
		)
	)
)

