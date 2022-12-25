;**
;**	Logics for Room 371 -- Blow-Up Doll Closeup
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford
;**
;**	Last Update:  June 21, 1991
;**


(script# 371)

(public
	rm371		0
)

(procedure
	InitDollFeas
)

(local triedOnce = 0)

(define	pBlowUpDoll				375)

(instance rm371 of LLRoom
	(properties
		picture	pBlowUpDoll
	)
	(method (init)
		(InitDollFeas)
		(ego 
			init:,
			z:		1000,
			hide:
		)
		(super init:)
	)
	(method (doit)
		(super doit: &rest)
		(if 
			(ego mover?)
			(HandsOff)
			(curRoom newRoom: 370)
		)
	)
)


;**********************************************
;** Feature stuff for pic 375 inflated doll **
;**********************************************

(instance herMouth of Feature
	(properties
		x 156
		y 81
		z 0
		heading 0
		nsLeft 142
		nsTop 71
		nsBottom 91
		nsRight 171
 		description "her mouth"
		lookStr 		"Her mouth reminds you of something."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Printf "%s" (self lookStr?))
			)
			(else
				(doll doVerb: theVerb invItem)
			)
		)
	)
)
(instance fRightNipple of Feature
	(properties
		x 156
		y 172
		z 0
		heading 0
		nsLeft 144
		nsTop 166
		nsBottom 179
		nsRight 169
 		description "her right nipple"
		lookStr		"You used this to inflate half of her."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Printf "%s" (self lookStr?))
			)
			(verbSmellTaste
				(Print "If you inflate her any more, she'll pop!")
			)
			(else
				(doll doVerb: theVerb invItem)
			)
		)
	)
)
(instance fLeftNipple of Feature
	(properties
		x 250
		y 136
		z 0
		heading 0
		nsLeft 238
		nsTop 126
		nsBottom 141
		nsRight 262
 		description "her left nipple"
		lookStr		"You used this to inflate half of her."
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Printf "%s" (self lookStr?))
			)
			(verbSmellTaste
				(Print "If you inflate her any more, she'll pop!")
			)
			(else
				(doll doVerb: theVerb invItem)
			)
		)
	)
)
(instance fRightTit of Feature
	(properties
		x 157
		y 164
		z 0
		heading 0
		nsLeft 122
		nsTop 140
		nsBottom 189
		nsRight 193
 		description "that latex breast"
		lookStr 		"At this particular moment,
						you feel her right breast is the best."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Printf "%s" (self lookStr?))
			)
			(verbDo
				(Print "She feels so life-like--for latex!")
			)
			(else
				(doll doVerb: theVerb invItem)
			)
		)
	)
)
(instance fLeftTit of Feature
	(properties
		x 239
		y 135
		z 0
		heading 0
		nsLeft 199
		nsTop 114
		nsBottom 162
		nsRight 279
 		description "that latex breast"
		lookStr 		"At this particular moment,
						you feel her left breast is the best."
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Printf "%s" (self lookStr?))
			)
			(verbDo
				(Print "She feels so life-like--for latex!")
			)
			(else
				(doll doVerb: theVerb invItem)
			)
		)
	)
)
(instance doll of Feature
	(properties
		x 162
		y 30
		z 0
		heading 0
		nsLeft 39
		nsTop 0
		nsBottom 189
		nsRight 285
 		description "the doll"
		sightAngle 40
	)
	(method (doVerb theVerb invItem)
		(switch theVerb
			(verbLook
				(Print "There's nothing more for you to see!")
			)
			(verbDo
				(Print "You're not sure, but you think she just smiled!")
			)
			(verbZipper
				(if triedOnce
					(Points pDidDoll ptsDidDoll)
					(Print "All right, you asked for it.
						In fact, you've asked for it twice!
						Here we go...")
					(Print "Selecting your own personal favorite from the three
						available openings, you shyly try out the doll.
						This has to be the kinkiest thing you've done yet, Larry.")
					(Print "You gradually increase your tempo as you lose your
						inhibitions (not to mention your self-esteem).
						Faster and faster you go, until suddenly, there's a loud,
						flatulent sound...")
					(HandsOff)
					(curRoom newRoom: 370)
				else
					(= triedOnce 1)
					(Print "Geez, Larry. Do we have to?")
				)
			)
			(verbTalk
				(Print "Foreplay is not really necessary with inanimate objects.")
				(Print "(Besides, you find it difficult to establish a meaningful
					relationship with latex!)" LOW)
			)
			(verbSmellTaste
				(Print "Foreplay is not really necessary with inanimate objects.")
				(Print "(Besides, you find it difficult to establish a meaningful
					relationship with latex!)" LOW)
			)
			(verbUse
				(switch invItem
					(iGraffiti
						(Print "\"Ken was here, too!\"")	
					)
					(iPocketKnife
						(Print "Oops! Bad idea, Larry!")
						(HandsOff)
						(curRoom newRoom: 370)
					)
					(else
						(Print "That's not the item she expects you to use!")
					)
				)
			)
			(else
				(super doVerb: theVerb invItem &rest)
			)
		)
	)
)

(procedure (InitDollFeas)
	(doll init:)
	(herMouth init:)
	(fLeftNipple init:)
	(fRightNipple init:)
	(fLeftTit init:)
	(fRightTit init:)
)
