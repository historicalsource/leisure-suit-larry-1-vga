	(method (doVerb theVerb invItem)
      (switch theVerb
			(verbLook	
				(Print "")
			)
			(verbTalk
				(Print "")
			)
         (verbDo 
				(Print "")
			)
			(verbZipper 
				(Print "")
			)
			(verbSmellTaste
				(Print "")
			)
			(verbUse
				(switch invItem
					(iHammer
						(Print "")
					)
					(else
						(super doVerb: theVerb invItem)
					)
				)
			)
			(else
            (super doVerb: theVerb invItem &rest)
			)
      )
   )
