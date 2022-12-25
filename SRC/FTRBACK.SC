(script# FEATURE)

;; Features are the basic responsive object
;; PicView, View, Prop, Actor, and Ego all inherit from this class.
;;
;; Classes
;;	CueObj
;;	Feature

(class CueObj of RootObj
	(properties
		client 0
		theVerb 0
		userCI 0
		userCC 0
	)
	(methods cue)
	(method (cue)
		(client doVerb:theVerb)
;		(User canControl:userCC, canInput:userCI)
	)
)

(class	Feature of Object
	(properties
		x					0
		y					0
		z					0
		heading			0
		noun				0 	; noun that this object will respond to from user input.
		nsTop				0	; rectangle defining this objects boundaries.
		nsLeft			0	;
		nsBottom			0	;
		nsRight			0	;
		description		0	; noun used to describe this object in print messages
		sightAngle		ftrDefault	; angle used by facingMe
		actions			ftrDefault	; instance of Action or EventHandler with Actions
		onMeCheck		ftrDefault	; if filled, the control color used by onMe:	or a Polygon
		lookStr			0
	)


	(methods
		init
		handleEvent
		doVerb				; check all proximity tests and respond.

		notFacing			; if ego isn't facing me (TurnIfSaid hook)

		;; boolean methods
		facingMe				; is ego facing this object?
		isNotHidden			; method defining what hidden means for this object
		onMe					; method defining what constitutes being on
	)


	(method (init theInitializer)
		(cond
			((and argc theInitializer)
				(self perform:theInitializer)
			)
			(ftrInitializer
				(self perform:ftrInitializer)
			)
;			(else		; Game didn't supply initializer -- use Sierra defaults
;				(self perform:dftFtrInit)
;			)
		)

		;; if object isKindof:View
		;; won't compile since actor.sc is compiled after
		(if (self respondsTo:#underBits)
			(cast add: self) ;;this is here so that Views etc can inherit this code
		else
			(features add: self)
		)
	)

	(method (handleEvent event &tmp useMsg)
		(cond
			((event claimed?)
				(return TRUE)
			)
			((not description?)
				(return FALSE)
			)
			((and
					(== (event type?) userEvent)
					(self onMe: event)
					(self isNotHidden:)
				)
				(if (and
						theIconBar
						(== (event message?) verbUse)
						inventory
					)
					(if (theIconBar curInvIcon?)
						(if (not
								(and
									actions
									(actions
										doVerb:
											(event message?)
									 		(inventory indexOf:(theIconBar curInvIcon?))
									)
								)
							)
							(self
							 	doVerb:
							 		(event message?)
							 		(inventory indexOf:(theIconBar curInvIcon?))
							)
						)
					)
				else
					(if (not
						 	(and
						 		actions
						 		(actions
						 			doVerb:
						 				(event message?)
						 		 		(inventory indexOf:(theIconBar curInvIcon?))
						 		)
						 	)
						)
						(self doVerb: (event message?))
					)
				)
				(event claimed: TRUE)
			)
		)
		(return (event claimed?))
	)

	(method (notFacing &tmp event)
		(CueObj
			client:	self,
			theVerb:	verbLook,
;			userCI: 	(User input?),
;			userCC: 	(User controls?)
		)
		(ego
			setMotion:	0,
			setHeading:
				(GetAngle (ego x?) (ego y?)  x?  y?)
				CueObj
		)
	)

	;; verb responses

	(method (doVerb theVerb)
	  	(return
			(
				(if doVerbCode
					doVerbCode
				else
					dftDoVerb
				)
				doit:	theVerb self &rest
			)

		)
	)

	;; Boolean Methods

	(method (facingMe act &tmp theActor theAngle)
		(= theActor (if argc act else ego))
		(= theAngle
			(Abs
				(-
					(GetAngle (theActor x?) (theActor y?) x y)
					(theActor heading?)
				)
			)
		)
		(if (> theAngle 180)
			(= theAngle (- 360 theAngle))
		)
		(if (<= 	theAngle	sightAngle)
			(return TRUE)
		else
			(self notFacing:)
			(return FALSE)
		)
	)

	(method (isNotHidden)
		(return TRUE)
;		(if (& signal actorHidden)
;			(self notInFar:)
;			(return FALSE)
;		else
;			(return TRUE)
;		)
	)

	(method (onMe theObjOrX theY &tmp oX oY)

		;; do we really want this much code?
		;; your comments welcome.

		(if (IsObject theObjOrX)
			(= oX	(theObjOrX x?))
			(= oY	(theObjOrX y?))
		else
			(= oX	theObjOrX)
			(= oY	theY)
		)
		(return
			(if (IsObject onMeCheck)
				(AvoidPath oX oY onMeCheck)
			else
				(and
					(<= nsLeft oX nsRight)
					(<= nsTop  oY nsBottom)
					(if (!= onMeCheck ftrDefault)
						(& onMeCheck (OnControl CMAP oX oY))
					else
						TRUE
					)
				)
			)
		)
	)

	(method (dispose)
		(features delete: self)
		(super dispose:)
	)

)



;(instance dftFtrInit of Code		; sets up defaults
;	(method (doit theObj)
;		; angle used by facingMe
;		(if (== (theObj sightAngle?) ftrDefault)
;			(theObj sightAngle: 90)
;		)
;		; maximum distance to get an object (for example.)
;		(if (== (theObj closeRangeDist?) ftrDefault)
;			(theObj closeRangeDist: 50)
;		)
;		; maximum distance to see an object	(for example.)
;		(if (== (theObj longRangeDist?) ftrDefault)
;			(theObj longRangeDist: 100)
;		)
;		; verb message corresponding to a shift click.
;		(if (== (theObj shiftClick?) ftrDefault)
;			; don't do checks if mousedOn.
;			(theObj shiftClick: (| NOCHECKMOUSE verbLook))
;		)
;		; verb message corresponding to a control click.
;;		(if (== (theObj contClick?) ftrDefault)
;;			(theObj contClick: verbGet)
;;		)
;		; instance of Action or EventHandler with Actions
;		(if (== (theObj actions?) ftrDefault)
;			(theObj actions: 0)
;		)
;		; if filled, the control color used by onMe:
;		(if (== (theObj control?) ftrDefault)
;			(theObj control: 0)
;		)
;		; Bit-mapped verb checks (4 bits per verb)
;		(if (== (theObj verbChecks1?) ftrDefault)
;			(theObj verbChecks1: $bbb5)
;		)
;		; Bit-mapped verb checks (4 bits per verb)
;		(if (== (theObj verbChecks2?) ftrDefault)
;			(theObj verbChecks2: $bbbb)
;		)
;		; Bit-mapped verb checks (4 bits per verb)
;		(if (== (theObj verbChecks3?) ftrDefault)
;			(theObj verbChecks3: $bbbb)
;		)
;	)
;)


;(instance moveInCloseScript of Script
;	(define thisVerb register)
;	;; use caller property as a holder for the target feature
;	;; then disable the normal cueing of a script
;	(method (changeState newState)
;		(switchto (= state newState)
;			(
;;				(User canControl:FALSE, canInput:FALSE)
;				(if theIconBar
;					(theIconBar state:(| (theIconBar state?) DISABLED))
;				)
;				(ego setMotion:PolyPath
;					(+ (caller x?) (SinMult  (caller heading?) (caller closeRangeDist?)))
;					(+ (caller y?)	(CosMult  (caller heading?) (caller closeRangeDist?)))
;					self
;				)
;			)
;			(
;				(caller doVerb:thisVerb)
;				(= caller 0)
;;				(User canControl:TRUE, canInput:TRUE)
;				(if theIconBar
;					(theIconBar state:(| (theIconBar state?) ENABLED))
;				)
;				(self dispose:)
;			)
;		)
;	); changeState
;
;); moveInCloseScript


(instance dftDoVerb of Code
	(method (doit theVerb theObj invNo &tmp objDesc)
		(= objDesc (theObj description?))
		(switch theVerb
			(verbLook
				(if (theObj lookStr?)
					(Print (theObj lookStr?))
				else
					(Printf "If you've seen one %s, you've seen 'em all." objDesc)
				)
			)
			(verbUse
				(Printf "Your %s won't work like that with your %s."
					((inventory at: invNo) description?)
					objDesc)
			)

;			(verbClose
;				(Printf "You cannot close the %s." objDesc)
;			)
;			(verbSmell
;				(Printf "The %s has no smell." objDesc)
;			)
;			(verbMove
;				(Printf "You cannot move the %s." objDesc)
;			)
;			(verbEat
;				(Printf "You wouldn't want to eat the %s." objDesc)
;			)
;			(verbGet
;				(Printf "You cannot get the %s." objDesc)
;			)
;			(verbClimb
;				(Printf "You can't climb the %s." objDesc)
;			)
			(verbTalk
				(Printf "The %s has nothing to say." objDesc)
			)
 		)
	)
)
