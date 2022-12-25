;**
;**	Logics for Room 505 -- QuikiMart Exterior	Pay Telephone
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Mike Larsen
;**
;**	Last Update:  July 1, 1991
;**

(script# 505)

(public
	rm505		0
)

(local
	ajax = [5 5 5 8 0 3 9 0 0 0]
	sexline = [5 5 5 6 9 6 9 0 0 0]
	sierra = [2 0 9 6 8 3 8 9 8 9]
	sierra2 = [8 0 0 3 2 6 6 6 5 4]
	index
	dialTimer
	phoneEntry = [0 0 0 0 0 0 0 0 0 0]
	phoneTime
	touchTone
	tmpFont
)

(procedure
	DialNumber
	ResetPhone
	CheckAjax
	CheckSexline
	CheckSierra
	CheckSierra2
)

;**		gYourPart 			170 ;	17 chars	;
;**		gVideo 				181 ;	38 chars	 ;
;**		gYourClothing		211 ;	17 chars	  ;
;**		gTheirName 			221 ;	13 chars		;
;**		gTheirPart 			230 ;	17 chars		 ;
;**		gTheirClothing		240 ;	17 chars		 ;
;**		gObject 				250 ; 17 chars		;
;**		gRoom 				260 ;	17 chars	  ;
;**		gActivity 			270 ;	37 chars	 ;
;**		gLastByte			290 ;			   ;


(define DIALDELAY 800)

(define BASETONE 490)

(define	sPhoneDialTone			490)
(define	sPhone1					491)
(define	sPhone2					492)
(define	sPhone3					493)
(define	sPhone4					494)
(define	sPhone5					495)
(define	sPhone6					496)
(define	sPhone7					497)
(define	sPhone8					498)
(define	sPhone9					499)
(define	sPhone0					508)
(define	sPhoneAsterisk			500)
(define	sPhonePoundSign		501)
(define	sPhoneBusy 				502)
(define	sPhoneRinging 			503)
(define	sPhoneAnswered			505)
(define	sPhoneHangUp			506)
(define	sWrongNumber			509)



(define pPhoneKeypad				515)
(define	vPhoneKeypad		515)
	(define	lZero				0)
	(define	lOne				1)
	(define	lTwo				2)
	(define	lThree			3)
	(define	lFour				4)
	(define	lFive				5)
	(define	lSix				6)
	(define	lSeven			7)
	(define	lEight			8)
	(define	lNine				9)
	(define	lAsterisk		10)
	(define	lPound			11)

	(define	lSign				0)
	(define	lPhone			1)
		(define	cOnHook	0)
		(define	cOffHook	1)


(instance rm505 of LLRoom
	(properties
		picture	515
		lookStr	"You are using the Quiki-Mart's one and only telephone."
	)
	(method (init)
		(LoadMany PICTURE	pPhoneKeypad)
		(LoadMany VIEW		vPhoneKeypad)
		(LoadMany SOUND
			sPhoneDialTone
			sPhone1
			sPhone2
			sPhone3
			sPhone4
			sPhone5
			sPhone6
			sPhone7
			sPhone8
			sPhone9
			sPhone0
			sPhoneAsterisk
			sPhonePoundSign
			sPhoneBusy
			sPhoneRinging
			sPhoneAnswered
			sPhoneHangUp
			sWrongNumber
		)
		(ego
			init:			,
			x:				(+ (ego x?)	1000)
		)
		(one init:)
		(two init:)
		(three init:)
		(four init:)
		(five init:)
		(six init:)
		(seven init:)
		(eight init:)
		(nine init:)
		(zero init:)
		(star init:)
		(pound init:)

		(if
			(Btst fPhoneRinging)
			(Bclr fPhoneRinging)
			(HandsOff)
			(curRoom setScript: sSurveyResponse)
		else
			(sfxDialTone
				play: ,
				hold: 10,
			)
		)

		(super init:)

	)
	(method (doit)
		(super doit: &rest)
	   (if
			(== (curRoom script?) 0)
			(++ dialTimer)
		)
		(if
			(> dialTimer DIALDELAY)
			(sfxDialTone stop:)
			(sfxWrongNumber play:)
			(Print "Please hang up and try again. This is a recording.")
			(ResetPhone)
			(= dialTimer 0)
			(sfxDialTone
				play:	,
				hold: 10,
			)
		)
		(if (ego mover?)
			(curRoom setScript: sBackToRoom)
		)
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbTalk
				(Print "Please dial the party to whom you wish to speak with.")
			)
			(verbDo
				(Print "Just press a button or walk away.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

;***************
;*** SCRIPTS ***
;***************

(instance sBackToRoom of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(HandsOff)
				(ego setMotion: 0)
				(ResetPhone)
				(= cycles 5)
			)
			(
				(HandsOn)
				(curRoom newRoom: 500)
			)
		)
	)
)

(instance sCallSexline of Script
	(method (changeState newState &tmp [tmpStr 200])
		(switchto (= state newState)
			(
				(ResetPhone)
				(if (Btst pDialedSurvey)
					(sfxDialTone stop:)
					(sfxPhoneBusy play:)
					(Print "Hmmm... it's busy.")
					(sfxPhoneBusy stop:)
					(sfxDialTone
						play:	,
						hold: 10,
					)
					(HandsOn)
					(self dispose:)
				else
					(sfxPhoneRinging
						loop:		3,
						play:		self,
					)
					(= gYourPart 		0)
					(= gVideo 			0)
					(= gYourClothing	0)
					(= gTheirName 		0)
					(= gTheirPart 		0)
					(= gTheirClothing	0)
					(= gObject 			0)
					(= gRoom 			0)
					(= gActivity 		0)
				)
			)
			(
				(sfxPhoneAnswered play)
				(Print "A sensuous female voice answers the telephone after the
					third ring."
				)
				(Points pDialedSurvey ptsDialedSurvey)
				(Print "\"Hello,
					welcome to the National Quiki-Sex Survey Hot Line.
 					Please answer the following questions.
					I may have something wonderful waiting for you!!\""
				)
				(= cycles 20)
			)
			(
				(Print "\"First, a few questions about you...\"")
				(= seconds 3)
			)
			(
				(Print "\"What is your name?\"")
				(= seconds 3)
			)
			(
				(Print "\"My name is Larry; Larry Laffer,\" you respond.")
				(= seconds 3)
			)
			(
				(= tmpFont (DEdit font?))
				(DEdit font: monoFont)
				(Print "\"Oooh,\" she sighs, \"what a sweet name!\"")


				;**	NOTE:  the funny-looking (+ 17 7) in all the edit strings
				;**	below is because Dedit automatically gives you a window 
				;**	2/3 as wide as what you ask for.  Since monoFont is safe
				;**	at the correct string length, we add on half-again to
				;**	fool Dedit to give us what we really want!


				(while ( > 2 (StrLen @gYourPart))
					(PrintD
						#text:		{\"So tell me, Larry:
										what is the best part of your body?\"}
						#new:
						#edit:		@gYourPart (+ 17 7)
					)
					;**	(+ 17 8) because #edit only gives you 2/3 of the
					;**	number you ask for!
				)
				(= seconds 3)
			)
			(
				(Printf "\"Oh, I so love a man with a manly, yet urbane, %s!\""
					@gYourPart
				)
				(= seconds 3)
			)
			(
				(while ( > 2 (StrLen @gVideo))
					(PrintD
						#text:		{\"And Larry,
										what is your favorite X-rated video?\"}
						#new:
						#edit:		@gVideo (+ 38 13)
					)
				)
				(= seconds 3)
			)
			(
				(Print "\"Mmmmmm, mine too!\" she says, knowingly.
					\"I just love that part with the two women!\"")
				(while ( > 2 (StrLen @gYourClothing))
					(PrintD
						#text:		{\"Now tell me your favorite article
										of clothing.\"}
						#new:
						#edit:		@gYourClothing	(+ 17 7)
					)
				)
				(= seconds 3)
			)
			(
				(Print "\"Ahhh, and I can just picture you in it, too!\"
					she moans.")
				(Print "\"Now, a few questions about your favorite lover.\"")
				(while ( > 2 (StrLen @gTheirName))
					(PrintD
						#text:		{\"Larry, what is your favorite sex partner's
										first name?\"}
						#new:
						#edit:		@gTheirName 	(+ 13 5)
					)
				)
				(= seconds 3)
			)
			(
				(Format @tmpStr {\"What's the best part of %s's anatomy?\"}
					@gTheirName)
				(while ( > 2 (StrLen @gTheirPart))
					(PrintD
						#text:		@tmpStr
						#new:
						#edit:		@gTheirPart (+ 17 7)
					)
				)
				(= seconds 3)
			)
			(
				(Format @tmpStr {\"What is %s's sexiest article of clothing?\"}
					@gTheirName)
				(while ( > 2 (StrLen @gTheirClothing))
					(PrintD
						#text:		@tmpStr
						#new:
						#edit:		@gTheirClothing	(+ 17 7)
					)
				)
				(= seconds 3)
			)
			(
				(Format @tmpStr {\"What's %s's favorite object?\"} @gTheirName)
				(while ( > 2 (StrLen @gObject))
					(PrintD
						#text:		@tmpStr
						#new:
						#edit:		@gObject (+ 17 7)
					)
				)
				(= seconds 3)
			)
			(
				(while ( > 2 (StrLen @gRoom))
					(PrintD
						#text:		{\"And where do you two most enjoy
										making love?\"}
						#new:
						#edit:		@gRoom (+ 17 7)
					)
				)
				(= seconds 3)
			)
			(
				(Print "\"Yeah, me too!\"")
				(Format @tmpStr {\"And, finally,
					what do you and %s like to do when you're together?\"}
					@gTheirName)
				(while ( > 2 (StrLen @gActivity))
					(PrintD
						#text:		@tmpStr
						#new:
						#edit:		@gActivity (+ 37 13)
					)
				)
				(DEdit font: tmpFont)
				(= seconds 3)
			)
			(
				(Print "\"Thanks for participating. Your prize is...\"")
				(sfxPhoneHangUp play:)
				(sfxDialTone
					play:	,
					hold: 10,
				)
				(Print "....")
				(Print "Hum. Looks like they hung up on you.
					Oh, well. It was probably a bummer prize anyway!"
				)
				(Bset fSurveyCalled)
				(= cycles 10)
			)
			(
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

(instance sSurveyResponse of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(sfxDialTone stop:)
				(Bclr fSurveyCalled)
				(= seconds 2)
			)
			(
				(Print "You pick up the telephone, and hear a familiar voice.")
				(= seconds 3)
			)
			(
				(Printf "\"Hello, Larry! This is %s.
					I was just sitting here in the %s,
					wearing your %s and thinking about you
					(if you know what I mean!).\""
					@gTheirName
					@gRoom
					@gYourClothing
					HIGH
				)
				(= seconds 3)
			)
			(
				(Printf "\"Why don't you forget about this silly game,
 					and come over to my place?
					I'll slip into my %s, and we'll curl up in front of the
					fireplace and I'll stick in our copy of \"%s!\"\""
					@gTheirClothing
					@gVideo
					HIGH
				)
				(= seconds 3)
			)
			(
				(Printf "\"You know your %s has always turned me on!
					So bring along a %s and come play with my %s!\""
					@gYourPart
					@gObject
					@gTheirPart
				)
				(= seconds 3)
			)
			(
				(Printf "\"Pretty soon, we'll both get excited and we'll
					%s like we always do!\""
					@gActivity
				)
				(= seconds 3)
			)
			(
				(Print "\"Bye, Larry! See you soon?\"")
				(sfxPhoneHangUp play:)
				(sfxDialTone
					play:	,
					hold: 10,
				)
				(= seconds 3)
			)
			(
				(Bset fLarryPanting)
				(HandsOn)
				(self dispose:)
				(curRoom newRoom: 500)
			)
		)
	)
)

(instance sCallAjax of Script
	(method (changeState newState)
		(switchto (= state newState)
			(;1
				(ResetPhone)
				(if
					debugging
					(Bset pHeardLiquorStoreNumber)
				)
				(cond
					((and
						(Btst pHeardLiquorStoreNumber)
						(not (Btst pDialedAjax)))

						(Points pDialedAjax ptsDialedAjax)
						(sfxPhoneRinging
							loop:		3,
							play:		self
						)
					)
					((or
						(not (Btst pHeardLiquorStoreNumber))
						(Btst pDialedAjax))

						(sfxPhoneBusy play:)
						(Print "Hmmm... Ajax Liquor Store's line is busy.")
						(sfxPhoneBusy stop:)
						(sfxDialTone
							play:	,
							hold: 10,
						)
						(HandsOn)
						(self dispose:)
					)
				)
			)
			(;2
				(sfxPhoneAnswered play:)
				(Print "\"Hello. Ajax Liquor Store. We deliver!\"")
				(= cycles 1)
			)
			(;3
				(Print "\"I'd like to purchase some fine wine,\" you say.")
				(= seconds 3)
			)
			(;4
				(Print "\"Hey, ya called the right guy!\" he says.
					\"Where didja want it delivered?\"")
				(= seconds 3)
			)
			(;5
				(Print "\"To the Casino Hotel's Honeymoon Suite,\" you respond.
					\"And step on it!\"")
				(= seconds 3)
			)
			(;6
				(Print "\"Ok. Consider it done.
					I'll send one of my hottest young men.
					Hey, thanks for calling!\"")
				(Print "And with that, he hangs up.")
				(sfxPhoneHangUp play:)
				(= seconds 3)
			)
			(;7
				(sfxDialTone
					play:	,
					hold: 10,
				)
				(Print "(What did he mean, \"hottest?\")" LOW)
				(Bset fOrderedWine)
				(= cycles 1)
			)
			(;8
				(HandsOn)
				(self dispose:)
			)
		)
	)
)
(instance sCallSierra of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(Points pDialedSierra ptsDialedSierra)
				(ResetPhone)
				(sfxPhoneRinging
					loop:		2,
					play:		self
				)
			)
			(
				(sfxPhoneAnswered play:)
				(Print "\"Hello,\" a pleasant voice responds.
					\"Sierra On-Line.\"")
				(= seconds 2)
			)
			(
				(Print "\"We're not open at night,
					but if you'd call during business hours
					(and stop using this pretend telephone)
 					we would be happy to sell you a copy of any of Al Lowe's 
					other games!\"")
				(= seconds 2)
			)
			(
				(Print "\"We also have many lines of GOOD games you should
					consider. Just dial Sierra's Order Desk at 800 326-6654
					(International customers call 209 683-4468).\"")
				(= seconds 2)
			)
			(
				(Print "\"At the same time, be sure to inquire about our 
					full line of Leisure Suit Larry novelty items.\"")
				(= seconds 2)
			)
			(
				(Print "\"Thank you for calling Sierra On-Line,
					and for your purchase of
					`Leisure Suit Larry in the Land of the Lounge Lizards.'
					Goodbye.\""
				)
				(sfxPhoneHangUp play:)
				(= seconds 2)
			)
			(
				(sfxDialTone
					play:	,
					hold: 10,
				)
				(Print "Such blatant commercialism turns you on.
					You make a mental note to purchase one of those great,
					Al Lowe games!")
				(HandsOn)
				(self dispose:)
			)
		)
	)
)

;**************
;*** SOUNDS	***
;**************

(instance sfxPhone of Sound
	(properties
		loop			1
		vol			127
		flags			1
	)
)
(instance sfxDialTone of Sound
	(properties
		number		sPhoneDialTone
		loop			-1
		vol			127
		flags			1
	)
)
(instance sfxPhoneAsterisk of Sound
	(properties
		number		sPhoneAsterisk
		loop			1
		vol			127
		flags			1
	)
)
(instance sfxPhonePoundSign of Sound
	(properties
		number		sPhonePoundSign
		loop			1
		vol			127
		flags			1
	)
)
(instance sfxPhone0 of Sound
	(properties
		number		sPhone0
		loop			1
		vol			127
	)
)
(instance sfxPhoneBusy of Sound
	(properties
		number		sPhoneBusy
		loop			-1
		vol			127
		flags			1
	)
)
(instance sfxPhoneRinging of Sound
	(properties
		number		sPhoneRinging
		flags			1
	)
	(method (check)
		(DoSound UpdateCues self)
		(if signal
			(= prevSignal signal)
			(= signal 0)
			(if (> (self loop?) 1)
				(self
					loop: (- (self loop?) 1),
				)
				(DoSound PlaySound self 0)
			else
				(if (IsObject client)
					(client cue: self)
				)
			)
		)
	)
)
(instance sfxPhoneAnswered of Sound
	(properties
		number		sPhoneAnswered
		loop			1
		flags			1
	)
)
(instance sfxPhoneHangUp of Sound
	(properties
		number		sPhoneHangUp
		loop			1
		flags			1
	)
)
(instance sfxWrongNumber of Sound
	(properties
		number		sWrongNumber
		loop			1
		flags			1
	)
)


;###############################
;###		TELEPHONE STUFF	  ###
;###############################

(procedure (DialNumber number)
	;play sound
	(if 
		(not
			(and
				(== number 1)
				(not index)
			)
		)
		(= [phoneEntry index] number)
		(++ index)
		(if (or
				(== index 7)
				(== index 10)
			)
			(cond
				((CheckAjax)
					(HandsOff)
					(curRoom setScript: sCallAjax)
				)
				((CheckSexline)
					(HandsOff)
					(curRoom setScript: sCallSexline)
				)
				((== (CheckSierra) 11)
					(HandsOff)
					(curRoom setScript: sCallSierra)
				)
				((== (CheckSierra2) 11)
					(HandsOff)
					(curRoom setScript: sCallSierra)
				)
				((and
						(== (CheckSierra) 8)
						(<= index 7)
					)
					NULL
				)
				((and
						(== (CheckSierra2) 8)
						(<= index 7)
					)
					NULL
				)
				(else
					(ResetPhone)
					(sfxWrongNumber play:)
					(Print "\"I'm sorry, but you have reached a number that is
						disconnected or no longer in service.
						Please hang up and dial again.\"")
					(Print "\"This is a recording.\"")
					(sfxDialTone
						play:	,
						hold: 10,
					)
				)
			)
		)
	)
)

(procedure (ResetPhone &tmp i)
	(for ((= i 0))
		(< i 10)
		((++ i))
		(= [phoneEntry i] 0)
		(= index 0)
	)
)
(procedure (CheckAjax &tmp i)
	(for
		((= i 0))
		(and (<= i 7) (== [phoneEntry i] [ajax i]))
		((++ i))
	)
	(if
		(== i 8)
		(return 8)
	else
		(return FALSE)
	)
)
(procedure (CheckSexline &tmp i)
	(for
		((= i 0))
		(and (<= i 7) (== [phoneEntry i] [sexline i]))
		((++ i))
	)
	(if
		(== i 8)
		(return 8)
	else
		(return FALSE)
	)
)
(procedure (CheckSierra &tmp i)
	(for
		((= i 0))
		(and (<= i 10) (== [phoneEntry i] [sierra i]))
		((++ i))
	)
	(cond
		((== i 10)
			(return 11)
		)
		((== i 7)
			(return 8)
		)
		(else
			(return FALSE)
		)
	)
)
(procedure (CheckSierra2 &tmp i)
	(for
		((= i 0))
		(and (<= i 10) (== [phoneEntry i] [sierra2 i]))
		((++ i))
	)
	(cond
		((== i 10)
			(return 11)
		)
		((== i 7)
			(return 8)
		)
		(else
			(return FALSE)
		)
	)
)

;******************************
;*** TELEPHONE BUTTON PROPS ***
;******************************


;**	This script handles the button pushing for every button.  It
;**	tells the DialNumber procedure which button, and kills
;**	the dialtone (in case it was playing)

(instance sButton of Script
	(method (changeState newState)
		(switchto (= state newState)
			(
				(HandsOff)
				(sfxDialTone stop:)
				(sfxPhone
					number:
					(cond
						((== client zero)
							sPhone0
						)
						(else
							(+ BASETONE (client loop?))
						)
					),
					play:
				)
				(client setCel: 1)
				(= dialTimer 0)
				(= cycles 4)
			)
			(
				(client setCel: 0)
				(= cycles 1)
			)
			(
				(HandsOn)
				(DialNumber (client loop?))
				(= cycles 1)
			)
			(
				(= touchTone 0)
				(self dispose:)
			)
		)
	)
)


(instance one of Prop
	(properties
		view	vPhoneKeypad
		loop	lOne
		cel	0
		x 		110
		y 		71
 		description "the button"
		lookStr 		"Thoroughly inspecting the touchtone telephone,
							you conclude that it's just like any other telephone."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(self setScript: sButton)
			)
			(verbTalk
				(Print "Please dial the party to whom you wish to speak with.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance two of Prop
	(properties
		view		vPhoneKeypad
		loop		lTwo
		cel		0
		x 			135
		y 			71
		z 			0
		heading	0
 		description "the button"
		lookStr 		"Thoroughly inspecting the touchtone telephone,
							you conclude that it's just like any other telephone."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(self setScript: sButton)
			)
			(else
				(one doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance three of Prop
	(properties
		view		vPhoneKeypad
		loop		lThree
		cel		0
		x 			161
		y 			71
		z 			0
		heading	0
 		description "the button"
		lookStr 		"Thoroughly inspecting the touchtone telephone,
							you conclude that it's just like any other telephone."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(self setScript: sButton)
			)
			(else
				(one doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance four of Prop
	(properties
		view		vPhoneKeypad
		loop		lFour
		cel		0
		x 			114
		y 			91
		z			0
		heading	0
 		description "the button"
		lookStr 		"Thoroughly inspecting the touchtone telephone,
							you conclude that it's just like any other telephone."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(self setScript: sButton)
			)
			(else
				(one doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance five of Prop
	(properties
		view		vPhoneKeypad
		loop		lFive
		cel		0
		x			137
		y			91
		z			0
		heading	0
 		description "the button"
		lookStr 		"Thoroughly inspecting the touchtone telephone,
							you conclude that it's just like any other telephone."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(self setScript: sButton)
			)
			(else
				(one doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance six of Prop
	(properties
		view		vPhoneKeypad
		loop		lSix
		cel		0
		x			162
		y			91
		z			0
		heading	0
 		description "the button"
		lookStr 		"Thoroughly inspecting the touchtone telephone,
							you conclude that it's just like any other telephone."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(self setScript: sButton)
			)
			(else
				(one doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance seven of Prop
	(properties
		view		vPhoneKeypad
		loop		lSeven
		cel		0
		x			116
		y			111
		z			0
		heading	0
 		description "the button"
		lookStr 		"Thoroughly inspecting the touchtone telephone,
							you conclude that it's just like any other telephone."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(self setScript: sButton)
			)
			(else
				(one doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance eight of Prop
	(properties
		view		vPhoneKeypad
		loop		lEight
		cel		0
		x			140
		y			111
		z			0
		heading	0
 		description "the button"
		lookStr 		"Thoroughly inspecting the touchtone telephone,
							you conclude that it's just like any other telephone."
		sightAngle 	40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(self setScript: sButton)
			)
			(else
				(one doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance nine of Prop
	(properties
		view		vPhoneKeypad
		loop		lNine
		cel		0
		x			164
		y			112
		z			0
		heading	0
 		description "the button"
		lookStr 		"Thoroughly inspecting the touchtone telephone,
							you conclude that it's just like any other telephone."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(self setScript: sButton)
			)
			(else
				(one doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance zero of Prop
	(properties
		view		vPhoneKeypad
		loop		lZero
		cel		0
		x			140
		y			132
		z			0
		heading 0
 		description "the button"
		lookStr 		"Thoroughly inspecting the touchtone telephone,
							you conclude that it's just like any other telephone."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(self setScript: sButton)
			)
			(else
				(one doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance star of Prop
	(properties
		view		vPhoneKeypad
		loop		lAsterisk
		cel		0
		x			118
		y			132
		z			0
		heading	0
 		description "the button"
		lookStr 		"Thoroughly inspecting the touchtone telephone,
							you conclude that it's just like any other telephone."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(= touchTone -1)
				(self setScript: sButton)
				(sfxPhoneAsterisk play:)
			)
			(one
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)
(instance pound of Prop
	(properties
		view		vPhoneKeypad
		loop		lPound
		cel		0
		x			164
		y			130
		z			0
		heading	0
 		description "the button"
		lookStr 		"Thoroughly inspecting the touchtone telephone,
							you conclude that it's just like any other telephone."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbDo
				(= touchTone -2)
				(self setScript: sButton)
				(sfxPhonePoundSign play:)
			)
			(verbTalk
				(Print "Please dial the party to whom you wish to speak with.")
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

