;**
;**	Logics for Room ABOUT -- The "about" screen
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford & Al Lowe
;**
;**	Last Update:  July 1, 1991
;**

(script#	ABOUT)

(public
	aboutCode	0
)

(local
	hours
)

(instance aboutCode of Code
	(method (doit &tmp [string 200])
		((ScriptID 0 24)
			view:			vAbout,
			loop:			0,
			cycleSpeed:	(* (+ howFast 1) 4), ; faster on slower machines
		)
		(Print "
 			\"Leisure Suit Larry 1:\n
			in the Land of the Lounge Lizards\"\n
			\n
			Executive Producer\n
			Ken Williams\n
			\n
			Creative Director\n
			Bill Davis\n
			\n
			Director\n
			Al Lowe\n
			\n
			Producer\n
			Stuart Moulder
			"
			#font			smallFont
			#mode			teJustCenter
			#width		200
			#title		{Sierra Presents}
		)
		(Print "
			Game Designer\n
			Al Lowe\n
			\n
			Art Designer\n
			William Skirvin\n
			\n
			Lead Programmer\n
			Oliver Brelsford\n
			\n
			Composer\n
			Chris Braymen\n
			"
			#font			smallFont
			#mode			teJustCenter
			#icon			(ScriptID 0 24)
			#width		200
			#title		{Sierra Presents}
		)
		(Print "
			Animators\n
			Barry Smith, Jennifer Shontz,\n
			Roger Hardy Jr., William D. Skirvin,\n
			Eric Apel, Russ Truelove,\n
			and Desie Hartman\n
			\n
			Background Artists\n
			William D. Skirvin, Jay Allan Friedmann,\n
			Jennifer Shontz, Jane Cardinal,\n
			and Maurice Morgan\n
			\n
			Programmers\n
			Oliver Brelsford, Mike Larsen\n
			and Al Lowe\n
			\n
			Witty Text and Documentation\n
			Josh Mandel
			"
			#font			smallFont
			#mode			teJustCenter
			#width		200
			#title		{Sierra Presents}
		)
		(Print "
			Music Director\n
			Mark Seibert\n
			\n
			Sound Effects\n
			Chris Braymen, Mark Seibert\n
			and Orpheus Hanley\n
			\n
			Theme Song Composed by\n
			Al Lowe\n
			\n
			Some tunes written by dead guys\n
			(and you know who you are!)
			"
			#font			smallFont
			#mode			teJustCenter
			#width		200
			#title		{Sierra Presents}
		)
		(Print (Format @string "
			Written Exclusively in S.C.I.\n
			\"Sierra's Creative Interpreter\"\n
			\n
			System Programmers\n
			J. Mark Hood, Jeff Stephenson, Bob Heitman\n
			Pablo Ghenis, Dan Foy, Larry Scott,\n
			Mark Wilden, Eric Hart, and Chris Smith\n
 			\n
			Quality Assurance Team Leaders\n
			Mike Pickhinke and Keri Cooper\n
 			\n
			Version %s,
			July 4, 1991
			\n
			"
			version
			)
			#font			smallFont
			#mode			teJustCenter
			#width		222
			#title		{Sierra Presents}
		)
		(Print "
 			Having fun?\n
			Want to have more fun?\n
			\n
 			For another \"real good time,\"\n
			just dial Sierra On-Line's\n
			Order Desk at 800 326-6654\n
			International customers\n
			call 209 683-4468\n
			\n
			They have lots more games available\n
			and all of them are better than this!\n
			"
			#font			smallFont
			#mode			teJustCenter
			#width		222
			#title		{A Paid Commercial Announcement}
		)
		(Print
			(cond
				((LarryHours)
					(Format @string
						"How could you play for %d hour%s,
						%d minute%s and %d second%s and
						only score %d point%s?!"
						(LarryHours)
						(if (== (LarryHours) 1) {} else {s})
						(LarryMinutes)
						(if (== (LarryMinutes) 1) {} else {s})
						(LarrySeconds)
						(if (== (LarrySeconds) 1) {} else {s})
						score
						(if (== score 1) {} else {s})
					)
				)
				(score
					(Format @string
						"You've been playing this game
						for %d minute%s and %d second%s and
						you've only scored %d point%s?!"
						(LarryMinutes)
						(if (== (LarryMinutes) 1) {} else {s})
						(LarrySeconds)
						(if (== (LarrySeconds) 1) {} else {s})
						score
						(if (== score 1) {} else {s})
					)
				)
				(else
					(Format @string
						"You've been playing this game
						for %d minute%s and %d second%s and
						you haven't scored a single point?"
						(LarryMinutes)
						(if (== (LarryMinutes) 1) {} else {s})
						(LarrySeconds)
						(if (== (LarrySeconds) 1) {} else {s})
					)
				)
			)
			#font			smallFont
			#title		{You've Only Just Begun}
		)
		(Print "Get a life!"
			#font			smallFont
		)
	)
)

