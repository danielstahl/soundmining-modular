package net.soundmining.sound

import net.soundmining.modular.ModularInstrument._
import net.soundmining.modular.ModularSynth._
import net.soundmining.synth.Instrument._
import net.soundmining.synth.SuperColliderClient
import net.soundmining.synth.Utils._

case class Audio(audio: AudioInstrument, dur: Double)

abstract class SoundNote(bufNum: Integer = 0) {
    type SelfType <: SoundNote
    def self(): SelfType
    var audio: Option[Audio] = None

    def playLeft(start: Double, end: Double, rate: Double, amp: ControlInstrument): SelfType = {
        audio = Some(
            Audio(
                audio = left(stereoPlayBuffer(bufNum, rate, start, end, amp).withNrOfChannels(2).addAction(TAIL_ACTION), staticControl(1.0)).addAction(TAIL_ACTION),
                dur = math.abs((end - start) / rate)))
        self()    
    }

    def playRight(start: Double, end: Double, rate: Double, amp: ControlInstrument): SelfType = {
        audio = Some(
            Audio(
                audio = right(stereoPlayBuffer(bufNum, rate, start, end, amp).withNrOfChannels(2).addAction(TAIL_ACTION), staticControl(1.0)).addAction(TAIL_ACTION),
                dur = math.abs((end - start) / rate)))
        self()    
    }

    def playMono(start: Double, end: Double, rate: Double, amp: ControlInstrument): SelfType = {
        audio = Some(
            Audio(
                audio = monoPlayBuffer(bufNum, rate, start, end, amp).withNrOfChannels(1).addAction(TAIL_ACTION),
                dur = math.abs((end - start) / rate)
            )
        )
        self()
    }

    def highPass(filterFreq: ControlInstrument): SelfType = {
        audio = audio.map {
            case Audio(audioInstrument, dur) => 
                Audio(highPassFilter(audioInstrument, filterFreq).addAction(TAIL_ACTION), dur)
        }
        self()
    }

    def lowPass(filterFreq: ControlInstrument): SelfType = {
        audio = audio.map {
            case Audio(audioInstrument, dur) => 
                Audio(lowPassFilter(audioInstrument, filterFreq).addAction(TAIL_ACTION), dur)
        }
        self()
    }

    def bandPass(filterFreq: ControlInstrument, rqBus: ControlInstrument): SelfType = {
        audio = audio.map {
            case Audio(audioInstrument, dur) =>
                Audio(bandPassFilter(audioInstrument, filterFreq, rqBus).addAction(TAIL_ACTION), dur)
        }
        self()
    }

    def ring(modularFreq: ControlInstrument): SelfType = {
        audio = audio.map {
            case Audio(audioInstrument, dur) => 
                Audio(ringModulate(audioInstrument, modularFreq).addAction(TAIL_ACTION), dur)
        }
        self()
    }

    def pan(panPosition: ControlInstrument): SelfType = {
        audio = audio.map {
            case Audio(audioInstrument, dur) =>
                Audio(panning(audioInstrument, panPosition).addAction(TAIL_ACTION).withNrOfChannels(2), dur)
        }
        self()
    }

    def sine(freq: Double, attackTime: Double = 0.01, releaseTime: Double = 0.01): SelfType = {
        audio = audio.map {
            case Audio(audioInstrument, dur) =>
                Audio(
                    audio = sineOsc(soundAmplitudeControl(audioInstrument, attackTime, releaseTime).addAction(TAIL_ACTION), staticControl(freq)).addAction(TAIL_ACTION),
                    dur = dur)
        }
        self()
    }

    def noise(attackTime: Double = 0.01, releaseTime: Double = 0.01): SelfType = {
        audio = audio.map {
            case Audio(audioInstrument, dur) =>
                Audio(
                    audio = whiteNoiseOsc(soundAmplitudeControl(audioInstrument, attackTime, releaseTime).addAction(TAIL_ACTION)).addAction(TAIL_ACTION),
                    dur = dur)
        }
        self()
    }

    def sineFm(carrierFreq: Double, modulatorFreq: Double, modulatorAmount: Double, attackTime: Double = 0.01, releaseTime: Double = 0.01): SelfType = {
        audio = audio.map {
            case Audio(audioInstrument, dur) =>
                Audio(
                    audio = fmSineModulate(
                        staticControl(carrierFreq),
                        sineOsc(staticControl(modulatorAmount), staticControl(modulatorFreq)),
                        soundAmplitudeControl(audioInstrument, attackTime, releaseTime).addAction(TAIL_ACTION)).addAction(TAIL_ACTION),
                    dur = dur
                )
        }
        self()
    }

    def play(startTime: Double, outputBus: Int = 0)(implicit client: SuperColliderClient) = {
        audio.foreach {
            case Audio(audioInstrument, dur) =>
                audioInstrument.getOutputBus.staticBus(outputBus)
                val graph = audioInstrument.buildGraph(startTime, dur, audioInstrument.graph(Seq()))
                
                client.send(client.newBundle(absoluteTimeToMillis(startTime), graph))
        }
    }
}

case class MonoSoundNote(bufNum: Int = 0) extends SoundNote(bufNum) {
    override type SelfType = MonoSoundNote
    override def self(): SelfType = this
}

case class StereoSoundNote(bufNum: Int = 0) extends SoundNote(bufNum) {
    override type SelfType = StereoSoundNote
    override def self(): SelfType = this
    var leftNote: MonoSoundNote = MonoSoundNote(bufNum)
    var rightNote: MonoSoundNote = MonoSoundNote(bufNum)
    
    def left(func: (MonoSoundNote) => Unit): SelfType = {
        func(leftNote)
        self()
    }
        
    def right(func: (MonoSoundNote) => Unit): SelfType = {
        func(rightNote)  
        self()
    }
        
    def expandAudio(): SelfType = {
        (leftNote.audio, rightNote.audio) match {
            case (Some(left), Some(right)) => 
            this.audio = Some(Audio(
                audio = expand(left.audio, right.audio).addAction(TAIL_ACTION).withNrOfChannels(2),
                dur = math.max(left.dur, right.dur)))
            case _ => 
        }
        self()
    }  

    def mixAudio(amp: ControlInstrument): SelfType = {
        (leftNote.audio, rightNote.audio) match {
            case (Some(left), Some(right)) => 
                this.audio = Some(Audio(
                    audio = mix(left.audio, right.audio, amp).addAction(TAIL_ACTION),
                    dur = math.max(left.dur, right.dur)))
            case (Some(left), None) => this.audio = Some(left)
            case (None, Some(right)) => this.audio = Some(right)
            case _ => 
        }
        self()
    }
        
}