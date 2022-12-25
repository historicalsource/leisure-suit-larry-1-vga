;**
;**	Logics for Room 813 -- Breath Spray
;**
;**	LSL1:  Leisure Suit Larry in the Land of the Lounge Lizards
;**				Copyright 1987, 1991
;**				by Al Lowe
;**
;**	Programmed by Oliver Brelsford &
;**
;**	Last Update:  May 4, 1991
;**


(script# SPRAY)

(public
	egoSprays	0
)

(local
	sprayCount
)

(instance egoSprays of Script
	(enum
		begin
		psssft
		spray
		countOff
		end
	)
	(method (dispose)
		(super dispose:)
		(Bclr	fEgoUsingSpray)
		(DisposeScript SPRAY)
	)
	(method (changeState newState)
		(switchto (= state newState)
			(
				(Bset fEgoUsingSpray)
				(HandsOff)
				(if (!= (ego loop?) 1)
					(ego setHeading: 90)
				)
				(= cycles 3)
			)
			(
				(ego
					egoSpeed:,
					view: 		vLarrySpraying,
					loop:			(if (not (ego loop?)) 0 else 1),
					cel:			0,
					x:				(if (!= (ego loop?) 1) (ego x?) else (- (ego x?) 1)),
					setCycle: 	CycleTo 3 1 self
				)
			)
			(
				((ScriptID 0 23) play:)
				(ego
					setCycle:	CycleTo 1 -1 self
				)
			)
			(
				(ego
					setCycle:	CycleTo 4 1 self
				)
			)
			(
				((ScriptID 0 23) play:)
				(= cycles 1)
			)
			(
				(Print "Ahhhh." HIGH)
				(ego setCycle: EndLoop self)
			)
			(
				(= seconds 2)
			)
			(
				(++ sprayCounter)
				(= spraySeconds BREATHLIMIT)
				(Bclr fMouthSmellsBad)
				(if (Btst fNearPerson)
					(Bclr fNearPerson)
					(Print "\"It's about time!!\"")
				)
				(if (> sprayCounter SPRAYSINCAN)
					(Print "Since your breath spray is now empty,
						you toss it away.")
					(ego put: iBreathSpray 510)
					(= sprayCounter 0)
				)
				(ego 	x:	(if (!= (ego loop?) 1) (ego x?) else (+ (ego x?) 1)))
				(NormalEgo)
				(HandsOn)
				(self dispose:)
			)
		)
	)
)


