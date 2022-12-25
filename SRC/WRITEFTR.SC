(script# WRITEFTR)
;;;;
;;;;	WRITEFTR.SC
;;;;	(c) Sierra On-Line, Inc, 1990
;;;;
;;;;	Author: J.Mark Hood
;;;;
;;;;	Simple C.A.S.E. Tools for positioning and adding response toCarlos Escobar
;;;;	Features, PicViews, Views, Props, Actors and their subclasses.
;;;;
;;;;	Classes:
;;;;		WriteCode
;;;;		CreateObject
;;;;
;;;; also several internal procedures


(local
	[nameString	50]
	[descString 50]
	[lookString 100]
	[vDoStr		100]
	[vTalkStr	100]
	[vItemStr	15]
	[vUseStr		100]
	toScreen	=	FALSE
	drawNSRect = TRUE
	inited	=	FALSE
	theType 	=	FALSE
)

(procedure
	DoFeature
	DoView
	DoProperties
	DoDoVerb
	Logit
	LongInput
)
(enum
	exitPrint
	makeFeature
	makePicView
	makeView
	makeProp
	makeActor
)


(class WriteCode of RootObj  ; get check

	(methods
		doit
		writeList
	)

	(method (doit theObj &tmp [buffer 400] [vlcOrNsStr 40])

		; Format page 1
		(if (theObj isMemberOf: Feature)
			(Format @vlcOrNsStr
				"\09\09nsLeft\09\09%d\r
				 \09\09nsTop\09\09\09%d\r
				 \09\09nsBottom\09\09%d\r
				 \09\09nsRight\09\09%d\r"
				(theObj nsLeft?)
				(theObj nsTop?)
				(theObj nsBottom?)
				(theObj nsRight?)
			)
		else
			(Format @vlcOrNsStr
				"\09\09view\09\09\09\09%d\r
				 \09\09loop\09\09\09\09%d\r
				 \09\09cel\09\09\09\09%d\r"
				 (theObj view?)
				 (theObj loop?)
				 (theObj cel?)
			)
		)
		(Format @buffer
			"(instance %s of %s\r
				\09(properties\r
				\09\09x\09\09\09\09%d\r
				\09\09y\09\09\09\09%d\r
				\09\09z\09\09\09\09%d\r
				\09\09heading\09\09%d\r
				%s
				\09\09description\09\"%s\"\r
				\09\09lookStr\09\09\"%s\"\r
				\09\09sightAngle\09%d\r
				\09)\r
			"
			@nameString
			(if (== theType makePicView)
				(PicView name?)
			else
				((theObj superClass?) name?)
			)
			(theObj x?)
			(theObj y?)
			(theObj z?)
			(theObj heading?)
			@vlcOrNsStr
			@descString
			@lookString
			(theObj sightAngle?)
		)

		; Display page 1
		(if toScreen
			(Print @buffer #font:999 #title:{Feature Writer V1.0})
		)

		; Write page 1
		(Logit @buffer)

		; Format page 2
		(Format @buffer
			"\09(method (doVerb theVerb invItem)\r
				\09\09(switch theVerb\r
			"
		)
		(Logit @buffer)

		(if [vDoStr 0]
			(Format @buffer
			  	"\09\09\09(verbDo\r
				\09\09\09\09(Print \"%s\")\r
				\09\09\09)\r
				"
				@vDoStr
			)
			(Logit @buffer)
		)
		(if [vTalkStr 0]
			(Format @buffer
				"\09\09\09(verbTalk\r
				\09\09\09\09(Print \"%s\")\r
				\09\09\09)\r
				"
				@vTalkStr
			)
			(Logit @buffer)
		)
		(GetInput @vItemStr 30 {verbUse inventory item?})
		(if [vItemStr 0]
			(Format @buffer
				"\09\09\09(verbUse\r
				\09\09\09\09(switch invItem\r
				"
			)
			(Logit @buffer)
			(while [vItemStr 0]
				(= vUseStr 0)
				(LongInput @vUseStr {verbUse string?})
				(Format @buffer
					"\09\09\09\09\09(%s\r
					\09\09\09\09\09\09(Print \"%s\")\r
					\09\09\09\09\09)\r
					"
					@vItemStr
					@vUseStr
				)
				(Logit @buffer)
				(= vItemStr 0)
				(GetInput @vItemStr 30 {verbUse inventory item?})
			)
			(Format @buffer
				"\09\09\09\09\09(else\r
				\09\09\09\09\09\09(super doVerb: theVerb invItem &rest)\r
				\09\09\09\09\09)\r
				\09\09\09\09)\r
				\09\09\09)\r
				"
			)
			(Logit @buffer)
		)

		(Format @buffer
			"\09\09\09(else\r
			\09\09\09\09(super doVerb: theVerb invItem &rest)\r
			\09\09\09)\r
			\09\09)\r
			\09)\r
			)\r
			"
		)
		(Logit @buffer)

		(if (theObj isMemberOf: Feature)
			(theObj dispose:)
		else
			(theObj addToPic:)
		)
		(File close:)
		(DisposeScript FILE)
	)

;; although currently not used
;; this is a useful method for writing out the cast, features list etc.
	(method (writeList theList)
		(theList eachElementDo: #perform: self)
		(CreateObject doit:)
		(DisposeScript WRITEFTR)
	); writeObjects
)


(class CreateObject	of RootObj
	(methods
		doit
	)
	(method (doit &tmp [thePath 15] theObj event)
		(if (not inited)
			(= thePath 0)

			(Format @thePath "%d.fea" (curRoom curPic?))	; default "pic#.fea"
			(if (not (GetInput @thePath 30 {Enter path and filename}))
				(return)
			else
				(Format @sysLogPath @thePath)
				(switch (Print "Outline Features?"
						#title:{Feature Writer V1.0}
						#button: {YES}	1
						#button: {NO}	2
					)
					(1 (= drawNSRect TRUE))
					(2 (= drawNSRect FALSE))
				)
				(switch (Print "Display code to screen? (but not doVerb)"
						#title:{Feature Writer V1.0}
						#button: {NO}	1
						#button: {YES}	2
					)
					(1 (= toScreen FALSE))
					(2 (= toScreen TRUE))
				)
				(= inited TRUE)
			)
		)
		(= theType
			(Print  "Class?"
				#title:	{Feature Writer V1.0}
				#button:	{Feature}	makeFeature
				#button:	{PicView}	makePicView
				#button:	{View}		makeView
				#button:	{Prop}		makeProp
				#button:	{Actor}		makeActor
			)
		)
		(if (not theType) (return))
		(= theObj
			(
				(switch theType
					(makeFeature
						Feature
					)
					(makePicView
						View
					)
					(makeView
						View
					)
					(makeProp
						Prop
					)
					(makeActor
						Actor
					)
				)
				new:
			)
		)
		(= nameString 0)
		(GetInput @nameString 30 {Name?})
		(StrCpy @descString @nameString)
		(DoProperties theObj)
		(if (== theType makeFeature)
			(DoFeature theObj)
		else	; something with a view, loop and cel
			(DoView theObj)
		)
		(if (Print  "Z property"
		 	#title:	{Feature Writer V1.0}
		 	#button:	{NO}	FALSE
		 	#button:	{YES}	TRUE
			)
			(Print "Please	click the mouse on the object's projection onto
				the ground")
			(while (!= ((= event (Event new:)) type?) mouseDown)
				(event dispose:)
			)
			(GlobalToLocal event)
			(theObj z:(- (event y?)	(theObj y?)))
			(theObj y:(event y?))
			(event dispose:)
		)
		(DoDoVerb)
		(WriteCode doit: theObj)
	)
)

(procedure (DoFeature obj &tmp event theX theY theTop theLeft theBottom theRight oldCursor)
	(= oldCursor theCursor)
	(theGame setCursor: 999)
	(Print "Click left mouse button on top left corner.")
	(while (!= ((= event (Event new:)) type?) mouseDown) (event dispose:))
	(GlobalToLocal event)
	(= theTop (event y?))
	(= theLeft (event x?))
	(Print "Click left mouse button on bottom right corner.")
	(while (!= ((= event (Event new:)) type?)	mouseDown) (event dispose:))
	(GlobalToLocal event)
	(= theBottom 	(event y?))
	(= theRight 	(event x?))
	(= theX	(+ (/ (- theRight theLeft) 2) theLeft))
	(= theY	(+ (/ (- theBottom theTop) 2) theTop))
	(obj
		x:				theX,
		y:				theY,
		nsLeft:		theLeft,
		nsTop:		theTop,
		nsBottom:	theBottom,
		nsRight:		theRight,
	)
	(if drawNSRect
		(Graph GDrawLine theTop    theLeft 	 	theTop 		theRight 	VMAP vWHITE)
		(Graph GDrawLine theBottom theLeft 		theBottom	theRight 	VMAP vWHITE)
		(Graph GDrawLine theTop		theLeft 	  	theBottom 	theLeft 		VMAP vWHITE)
		(Graph GDrawLine theTop		theRight   	theBottom 	theRight 	VMAP vWHITE)
		(Graph GShowBits theTop 	theLeft 		(+ theBottom 1)	(+ theRight 1)	VMAP)
	)
 	(event dispose:)
	(theGame setCursor: oldCursor)
)



(procedure (DoView obj &tmp event)
	(obj
		view:			(GetNumber {View?} (curRoom curPic?)),
		loop:			(GetNumber {Loop?} 0),
		cel:			(GetNumber {Cel?} 0),
		signal:		(| fixPriOn ignrAct),
		priority:	15,
		init:
	)
	(if (obj respondsTo:#illegalBits) (obj illegalBits:0))
	(while (!= ((= event (Event new:)) type?) mouseDown)
		(GlobalToLocal event)
		(obj posn:(event x?) (event y?))
		(Animate (cast elements?) FALSE)
		(event dispose:)
	)
 	(event dispose:)
)

(procedure (DoProperties obj)
	(obj
		sightAngle:	(GetNumber {sight angle?} 40),
		description:(GetInput @descString 50 {description?}),
		lookStr:		(LongInput @lookString {lookStr?})
	)
)

(procedure (DoDoVerb &tmp [itemStr 20])
	(LongInput 	@vDoStr 		{verbDo?})
	(LongInput	@vTalkStr	{verbTalk?})
)

(procedure (Logit what)
	(File
		name:	 @sysLogPath,
		writeString: what,
		close:
	)
)

(procedure (LongInput where title &tmp [aString 25])
	(StrAt where 0 0)
	(while TRUE
		(= aString 0)
		(GetInput @aString 50 title)
		(if (not [aString 0])
			(break)
		else
			(if (StrLen where)
				(StrCat where {\r\t\t\t\t\t})
			)
			(StrCat where @aString)
		)
	)
	(return (StrLen where))
)
