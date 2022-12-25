; SMF make file for Larry 1

;       m = MT32
;       a = adlib/soundblaster
;       t = tandy
;       j = CMS
;       s = single voice
;       x = sample

;       - = no sound for this device
;       ? = sound for this device not yet done


;(101.snd        ;sNuclearBlowup         Play:10         m?????
;     (seq\VDXplode.mid)
;)

;(102.snd        ;mDogPissing            Play:10         ma---?
;     (seq\weewee.mid)
;)

;(105.snd        ;sNuclearHiss           Play:10         m?????
;     (seq\VDBurn.mid)
;)

;(110.snd        ;mBarInterior           PlayBed:50      m????-  Loop
;     (seq\leftys.mid)
;)

(111.snd        ;sDrinking              Play:10         m?????
     (seq\Glug.mid)
)

(112.snd        ;sMugOnBar              Play:10         m?????
     (seq\MugSlam.mid)
)

;(113.snd        ;sBartenderSwoosh       Play:10         ma----
;     (seq\barzip.mid)
;)
;
;(114.snd        ;sPantsDown             Play:10         ma----
;     (seq\PantsDn.mid)
;)
;
;(115.snd        ;sPantsUp               Play:10         ma----
;     (seq\PantsUp.mid)
;)

;(116.snd        ;sRecordStart           Play:10         m?----
;                ;Play until sound done
;    (seq\RecStart.mid)
;)
;
;(117.snd        ;sRecordNoise           Play:10         m?----  Loop
;                ;Loop while playing music selection
;     (seq\Tracking.mid)
;)
;
;(118.snd        ;sRecordDone            Play:10         m?----
;                ;play after music is over
;                ;play until sound done
;     (seq\RecEnd.mid)
;)

(119.snd        ;sPeepholeSlider        Play:10         m?????
     (seq\Peephole.mid)
)

;(130.snd        ;sDripping              Play:5          ma---?
;                ;loop while in bathroom
;     (seq\Drip.mid)
;     (smp\Drip.smp[at])
;)

;(132.snd        ;sNewspaperShuffle      Play:10         ma---?
;                ;each page turn
;     (seq\Newsp.mid)
;)

;(133.snd        ;sFart                  Play:10         matjs?
;                ;after text "Boldly go"
;     (seq\Fart.mid)
;)

;(134.snd        ;sToiletFlush           Play:10         ma---?
;     (seq\toilet.mid)
;     (smp\flush.smp[at])
;)

;rm 160
;(160.snd        ;sFalling               Play:10         matjs-
;     (seq\Fall.mid)
;)

;(161.snd        ;sLandInTrash           Play:10         m?????
;     (seq\TrshLand.mid)
;)

;(163.snd        ;sOutOfTrash            Play:10         m?????
;     (seq\TrshOut.mid)
;)

;(164.snd        ;sSplatOnPavement       Play:10         m?????
;     (seq\Splat.mid)
;)

;(165.snd        ;mTrapeze               Play:10         m????-  Loop
;                ;Play while EGO is leaning out from fire escape
;     (seq\Trapeze.mid)
;)

;(167.snd        ;sGlassBreaking         Play:10         m?????
;     (seq\GlassBrk.mid)
;)

;rm170
;(170.snd        ;mDarkAlley             PlayBed:50      m????-  Loop
;                ;Play until EGO stops fighting with himself
;    (seq\Alley.mid)
;)
;
;(171.snd        ;sFight                 Play:10         m?????
;     (seq\Fight.mid)
;)
;
;(172.snd        ;sDizzy                 Play:10         m?????
;                ;Play after EGO stops fighting with himself but
;                ;is still standing.
;     (seq\Dizzy.mid)
;)
;
;(173.snd        ;sBounce                Play:10         m?????
;                ;play when ego hits the ground after fight
;     (seq\Bounce.mid)
;)

;(200.snd        ;sYellTaxi              Play:10         matjs-
;    (seq\YoTaxi.mid)
;)

;(202.snd        ;sCabTires              Play:10         m?????
;    (seq\Screech.mid)
;)

;(203.snd        ;sCabDoorOpen           Play:10         m?????
;    (seq\CabDoorO.mid)
;)
;
;(204.snd        ;sCabDoorClose          Play:10         m?????
;    (seq\CabDoorC.mid)
;)

;(205.snd        ;sCabDeparting          PlayBed:10      m?---?      Loop
;                ;Send pitch bend to channel 1 according to cab speed.
;                ;Bring volume down to 90 when scene changes from outside view
;                ;to inside cab view.
;     (seq\CabMotor.mid)
;)

;(206.snd        ;mCab                   PlayBed:50      m????-     Loop
;                ;Play while inside cab
;     (seq\Taxi.mid)
;)

;(310.snd        ;mCasino                PlayBed:50      m????-     Loop
;     (seq\hotel.mid)
;)

(321.snd        ;sDrumFill1             PlayBed:10      m?????
     (seq\dfill1.mid)
     (smp\fill1.smp[at])
)

(322.snd        ;sDrumFill2             PlayBed:10      m?????
     (seq\dfill2.mid)
     (smp\fill2.smp[at])
)

(323.snd        ;sDrumFill3             PlayBed:10      m?????
     (seq\dfill3.mid)
     (smp\fill3.smp[at])
)

(324.snd        ;sDrumFill4             PlayBed:10      m?????
     (seq\dfill4.mid)
     (smp\fill4.smp[at])
)

(325.snd        ;sDrumFill5             PlayBed:10      m?????
     (seq\dfill5.mid)
     (smp\fill5.smp[at])
)

;(326.snd       ;sDrumFill6             PlayBed:10      ??????
;     (seq\dfill6.mid)
;)

(382.snd        ;sCatCall               Play:20         matjs-
                ;higher priority than bubbles
     (seq\catcall.mid)
)

(490.snd        ;sPhoneDialTone         Play:10         m?---?
     (seq\DialTone.mid)
)

;(491.snd        ;sPhone1                Play:10         matjs-
;     (seq\TT1.mid)
;)
;
;(492.snd        ;sPhone2                Play:10         matjs-
;     (seq\TT2.mid)
;)
;
;(493.snd        ;sPhone3                Play:10         matjs-
;     (seq\TT3.mid)
;)
;
;(494.snd        ;sPhone4                Play:10         matjs-
;     (seq\TT4.mid)
;)
;
;(495.snd        ;sPhone5                Play:10         matjs-
;     (seq\TT5.mid)
;)
;
;(496.snd        ;sPhone6                Play:10         matjs-
;     (seq\TT6.mid)
;)
;
;(497.snd        ;sPhone7                Play:10         matjs-
;     (seq\TT7.mid)
;)
;
;(498.snd        ;sPhone8                Play:10         matjs-
;     (seq\TT8.mid)
;)
;
;(499.snd        ;sPhone9                Play:10         matjs-
;     (seq\TT9.mid)
;)
;
;(500.snd        ;sPhoneAsterisk         Play:10         matjs-
;     (seq\TTStar.mid)
;)
;
;(501.snd        ;sPhonePoundSign        Play:10         matjs-
;     (seq\TTPound.mid)
;)

(502.snd        ;sPhoneBusy             Play:10         m?tjs?    Loop
     (seq\BusySig.mid)
)

(503.snd        ;sPhoneRinging          Play:10         m?tjs?    Loop
     (seq\DialRing.mid)
)

(504.snd        ;sPhoneRingingHere      Play:10         m?tjs?    Loop
     (seq\TeleRing.mid)
)

(505.snd        ;sPhoneAnswered         Play:10         m?tjs?
     (seq\ClickAns.mid)
)

(506.snd        ;sPhoneHangUp           Play:10         m?tjs?
     (seq\ClickHng.mid)
)

(507.snd        ;sPhoneHangUpHere       Play:10         m?---?
     (seq\OnHook.mid)
)

;(510.snd        ;mQuikiMartInterior     PlayBed:50      m????-    Loop
;                ;Stop sound on dialog "Hey everybody"
;                ;Short pause after "What a pervert", then restart sound
;                ;from the beginning.
;     (seq\TJB.mid)
;)

;(511.snd        ;mHeadsOut              Play:10         matjs-
;     (seq\HeadsOut.mid)
;)
;
;(513.snd        ;mHeadsIn               Play:10         matjs-
;     (seq\HeadsIn.mid)
;)

(700.snd        ;mThemeSong             PlayBed:50      m????-
     (seq\ll1theme.mid)
)

;(800.snd        ;sCitySounds            Play:20         m?---?
;                ;Alters reverb mode
;                ;***Reverb removed due to noise while setting ***
;     (seq\Traffic.mid)
;)

(801.snd        ;sDoorOpen              Play:10         m?????
     (seq\DoorO1.mid)
)

(802.snd        ;sDoorClose             Play:10         m?????
     (seq\DoorC1.mid)
)

(803.snd        ;sNeonBuzz              Play:10          m?---?
     (seq\Neon.mid)
)

;(810.snd        ;sCarHonk               Play:10          m????-
;      (seq\Honk.mid)
;)
;
;(811.snd        ;sCarThump              Play:10          m?????
;      (seq\CarThud.mid)
;)
;
;(812.snd        ;sCarLeaving            Play:10          m?????
;      (seq\SpeedAwy.mid)
;)

;(820.snd        ;sBreathSpray           Play:10          m?---?
;      (seq\Spray.mid)
;)
;
;(821.snd        ;sScoredPoints          Play:10         matjs?
;     (seq\Points.mid)
;)

;*****************************************************
;* The following are sounds for the demo only
;*****************************************************

(900.snd        ;mDemoSongOpen          PlayBed:50      matjs-
     (seq\ll1dthem.mid)
)

(901.snd        ;mDemoSongClose         PlayBed:50      matjs-
                ;Cue 10 for ending title screen
     (seq\ll1dend.mid)
)

(902.snd        ;mDemoBarInterior       PlayBed:50      matjs-
                ;Bring volume down to 80 while in bathroom
     (seq\leftysd.mid)
)

(903.snd        ;sType                  Play:10         matj-?
                ;for each letter typed
     (seq\type.mid)
     (smp\type.smp[at])
)

(904.snd        ;sMouse                 Play:10         matj-?
     (seq\mouseclk.mid)
)

(905.snd        ;sBackSpace             Play:10         matj-?
                ;for each backspace
     (seq\backspc.mid)
     (smp\backsp.smp[at])
)

(906.snd        ;sDemoSongPC            Play:10         matjs-
     (seq\pctheme.mid)
)


