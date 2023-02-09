package net.soundmining.modular

import net.soundmining.modular.ModularInstrument.{AudioInstrument, ControlInstrument}
import net.soundmining.modular.ModularSynth._
import net.soundmining.synth.Instrument.TAIL_ACTION
import net.soundmining.synth.{SuperColliderClient, SuperColliderScore}
import net.soundmining.synth.Utils.absoluteTimeToMillis

import scala.collection.mutable

case class SynthPlayer(soundPlays: Map[String, SoundPlay], masterVolume: Double = 1.0, numberOfOutputBuses: Int = 2, bufferedPlayback: Boolean = false)(implicit val client: SuperColliderClient = SuperColliderClient()) {

  val superColliderScore: SuperColliderScore = SuperColliderScore()

  def init(): Unit = {
    BufNumAllocator.reset()
    superColliderScore.reset()
    soundPlays.values.foreach(_.init)
  }

  def initScore(): Unit = {
    superColliderScore.reset()
    soundPlays.values.foreach(_.initScore(superColliderScore))
  }

  def stop(): Unit = {
    soundPlays.values.foreach(_.stop)
    BufNumAllocator.reset()
    superColliderScore.reset()
  }

  def getRealOutputBus(outputBus: Int): Int =
    if(numberOfOutputBuses > 2) (outputBus % numberOfOutputBuses) + 2
    else outputBus % numberOfOutputBuses

  def apply(): SynthNote = SynthNote(None, this)

  def apply(soundName: String): SynthNote = SynthNote(soundPlays.get(soundName), this)
}

case class AudioStack() {
  private val audioStack = mutable.Stack[AudioInstrument]()
  var duration: Option[Double] = None

  def push(instrument: AudioInstrument, dur: Double): Unit = {
    audioStack.push(instrument)
    duration = duration.orElse(Some(0.0)).map(oldDur => math.max(oldDur, dur))
  }

  def push(instrument: AudioInstrument): Unit =
    audioStack.push(instrument)

  def mapInstrument(func: AudioInstrument => AudioInstrument): Unit =
    audioStack.push(func(audioStack.pop()))

  def foreachInstrument(func: AudioInstrument => Unit): Unit =
    func(audioStack.pop())

  def popInstrument(): AudioInstrument =
    audioStack.pop()
}

case class SynthNote(soundPlay: Option[SoundPlay] = None, synthPlayer: SynthPlayer) {
  private val audioStack = AudioStack()

  def playMono(start: Double, end: Double, rate: Double, volume: Double): SynthNote = {
    soundPlay.foreach {
      sound =>
        audioStack.push(
          monoPlayBuffer(sound.bufNum, rate, start, end, sound.amp(volume * synthPlayer.masterVolume)).addAction(TAIL_ACTION),
          sound.duration(rate))
    }
    this
  }

  def playMono(rate: Double, volume: Double): SynthNote = {
    soundPlay.foreach {
      sound =>
        playMono(sound.start, sound.end, rate, volume)
    }
    this
  }

  def playLeft(start: Double, end: Double, rate: Double, volume: Double): SynthNote = {
    soundPlay.foreach {
      sound =>
        audioStack.push(
          left(stereoPlayBuffer(sound.bufNum, rate, start, end, sound.amp(volume * synthPlayer.masterVolume)),
            staticControl(1))
            .addAction(TAIL_ACTION),
          sound.duration(rate))
    }
    this
  }

  def playLeft(rate: Double, volume: Double): SynthNote = {
    soundPlay.foreach {
      sound =>
        playLeft(sound.start, sound.end, rate, volume)
    }
    this
  }

  def playRight(start: Double, end: Double, rate: Double, volume: Double): SynthNote = {
    soundPlay.foreach {
      sound =>
        audioStack.push(
          right(stereoPlayBuffer(sound.bufNum, rate, start, end, sound.amp(volume * synthPlayer.masterVolume)),
            staticControl(1))
            .addAction(TAIL_ACTION),
          sound.duration(rate))
    }
    this
  }

  def playRight(rate: Double, volume: Double): SynthNote = {
    soundPlay.foreach {
      sound =>
        playRight(sound.start, sound.end, rate, volume)
    }
    this
  }

  def expand(): SynthNote = {
    audioStack.push(ModularSynth.expand(audioStack.popInstrument(), audioStack.popInstrument())
      .addAction(TAIL_ACTION))
    this
  }

  def mix(amp: ControlInstrument): SynthNote = {
    audioStack.push(ModularSynth.mix(audioStack.popInstrument(), audioStack.popInstrument(), amp).addAction(TAIL_ACTION))
    this
  }

  def saw(freq: ControlInstrument, amp: ControlInstrument): SynthNote = {
    audioStack.push(sawOsc(amp, freq).addAction(TAIL_ACTION))
    this
  }

  def triangle(freq: ControlInstrument, amp: ControlInstrument): SynthNote = {
    audioStack.push(triangleOsc(amp, freq).addAction(TAIL_ACTION))
    this
  }

  def pulse(freq: ControlInstrument, amp: ControlInstrument): SynthNote = {
    audioStack.push(pulseOsc(amp, freq).addAction(TAIL_ACTION))
    this
  }

  def sine(freq: ControlInstrument, amp: ControlInstrument): SynthNote = {
    audioStack.push(sineOsc(amp, freq).addAction(TAIL_ACTION))
    this
  }

  def whiteNoise(amp: ControlInstrument, optionalDur: Option[Double] = None): SynthNote = {
    if(optionalDur.isDefined) audioStack.push(whiteNoiseOsc(amp).withDur(optionalDur.get).addAction(TAIL_ACTION))
    else audioStack.push(whiteNoiseOsc(amp).addAction(TAIL_ACTION))
    this
  }

  def pinkNoise(amp: ControlInstrument, optionalDur: Option[Double] = None): SynthNote = {
    if(optionalDur.isDefined) audioStack.push(pinkNoiseOsc(amp).withDur(optionalDur.get).addAction(TAIL_ACTION))
    else audioStack.push(pinkNoiseOsc(amp).addAction(TAIL_ACTION))
    this
  }

  def dust(amp: ControlInstrument, freq: ControlInstrument): SynthNote = {
    audioStack.push(dustOsc(amp, freq).addAction(TAIL_ACTION))
    this
  }

  def sineFm(carrierFreq: ControlInstrument, modulatorFreq: ControlInstrument, modulatorAmount: ControlInstrument, amp: ControlInstrument): SynthNote = {
    audioStack.push(fmSineModulate(carrierFreq, sineOsc(modulatorAmount, modulatorFreq), amp).addAction(TAIL_ACTION))
    this
  }

  def sineFm(carrierFreq: ControlInstrument, amp: ControlInstrument): SynthNote = {
    audioStack.push(fmSineModulate(carrierFreq, audioStack.popInstrument(), amp).addAction(TAIL_ACTION))
    this
  }

  def pulseFm(carrierFreq: ControlInstrument, modulatorFreq: ControlInstrument, modulatorAmount: ControlInstrument, amp: ControlInstrument): SynthNote = {
    audioStack.push(fmPulseModulate(carrierFreq, sineOsc(modulatorAmount, modulatorFreq), amp).addAction(TAIL_ACTION))
    this
  }

  def pulseFm(carrierFreq: ControlInstrument, amp: ControlInstrument): SynthNote = {
    audioStack.push(fmPulseModulate(carrierFreq, audioStack.popInstrument(), amp).addAction(TAIL_ACTION))
    this
  }

  def bankOfOsc(freqs: Seq[Double], amps: Seq[Double], phases: Seq[Double]): SynthNote = {
    audioStack.push(ModularSynth.bankOfOsc(freqs, amps, phases))
    this
  }

  def sawFm(carrierFreq: ControlInstrument, modulatorFreq: ControlInstrument, modulatorAmount: ControlInstrument, amp: ControlInstrument): SynthNote = {
    audioStack.push(fmSawModulate(carrierFreq, sineOsc(modulatorAmount, modulatorFreq), amp).addAction(TAIL_ACTION))
    this
  }

  def sawFm(carrierFreq: ControlInstrument, amp: ControlInstrument): SynthNote = {
    audioStack.push(fmSawModulate(carrierFreq, audioStack.popInstrument(), amp).addAction(TAIL_ACTION))
    this
  }

  def triangleFm(carrierFreq: ControlInstrument, modulatorFreq: ControlInstrument, modulatorAmount: ControlInstrument, amp: ControlInstrument): SynthNote = {
    audioStack.push(fmTriangleModulate(carrierFreq, sineOsc(modulatorAmount, modulatorFreq), amp).addAction(TAIL_ACTION))
    this
  }

  def triangleFm(carrierFreq: ControlInstrument, amp: ControlInstrument): SynthNote = {
    audioStack.push(fmTriangleModulate(carrierFreq, audioStack.popInstrument(), amp).addAction(TAIL_ACTION))
    this
  }

  def audioAmplitude(attackTime: Double = 0.01, releaseTime: Double = 0.01): ControlInstrument =
    soundAmplitudeControl(audioStack.popInstrument(), attackTime, releaseTime).addAction(TAIL_ACTION)

  def sineWithAudioAmplitude(freq: ControlInstrument, attackTime: Double = 0.01, releaseTime: Double = 0.01): SynthNote = {
    sine(audioAmplitude(attackTime, releaseTime), freq)
    this
  }


  def highPass(filterFreq: ControlInstrument): SynthNote = {
    audioStack.mapInstrument {
      audio => highPassFilter(audio, filterFreq).addAction(TAIL_ACTION)
    }
    this
  }

  def lowPass(filterFreq: ControlInstrument): SynthNote = {
    audioStack.mapInstrument {
      audio => lowPassFilter(audio, filterFreq).addAction(TAIL_ACTION)
    }
    this
  }

  def bandPass(filterFreq: ControlInstrument, rqBus: ControlInstrument): SynthNote = {
    audioStack.mapInstrument {
      audio => bandPassFilter(audio, filterFreq, rqBus).addAction(TAIL_ACTION)
    }
    this
  }

  def bandReject(filterFreq: ControlInstrument, rqBus: ControlInstrument): SynthNote = {
    audioStack.mapInstrument {
      audio => bandRejectFilter(audio, filterFreq, rqBus).addAction(TAIL_ACTION)
    }
    this
  }

  def moogFilter(filterFreq: ControlInstrument, gain: ControlInstrument): SynthNote = {
    audioStack.mapInstrument {
      audio => ModularSynth.moogFilter(audio, filterFreq, gain).addAction(TAIL_ACTION)
    }
    this
  }

  def resonantFilter(filterFreq: ControlInstrument, decay: ControlInstrument): SynthNote = {
    audioStack.mapInstrument {
      audio => ModularSynth.resonantFilter(audio, filterFreq, decay).addAction(TAIL_ACTION)
    }
    this
  }

  def ring(modularFreq: ControlInstrument): SynthNote = {
    audioStack.mapInstrument {
      audio => ringModulate(audio, modularFreq).addAction(TAIL_ACTION)
    }
    this
  }

  def bankOfResonators(freqs: Seq[Double], amps: Seq[Double], ringTimes: Seq[Double]): SynthNote = {
    audioStack.mapInstrument {
      audio => ModularSynth.bankOfResonators(audio, freqs, amps, ringTimes).addAction(TAIL_ACTION)
    }
    this
  }

  def am(modularFreq: ControlInstrument): SynthNote = {
    audioStack.mapInstrument {
      audio => amModulate(audio, modularFreq).addAction(TAIL_ACTION)
    }
    this
  }

  def frequencyShift(modularFreq: ControlInstrument): SynthNote = {
    audioStack.mapInstrument {
      audio => ModularSynth.frequencyShift(audio, modularFreq).addAction(TAIL_ACTION)
    }
    this
  }

  def xfade(pan: ControlInstrument): SynthNote = {
    audioStack.push(ModularSynth.xfade(audioStack.popInstrument(), audioStack.popInstrument(), pan).addAction(TAIL_ACTION))
    this
  }

  def bitCrushing(amount: ControlInstrument): SynthNote = {
    audioStack.mapInstrument {
      audio => ModularSynth.bitCrushing(audio, amount).addAction(TAIL_ACTION)
    }
    this
  }

  def monoComb(delayTime: Double, decayTime: Double, amp: ControlInstrument): SynthNote = {
    audioStack.mapInstrument {
      audio => ModularSynth.monoComb(audio, amp, delayTime, decayTime).addAction(TAIL_ACTION)
    }
    this
  }

  def monoAllpass(delayTime: Double, decayTime: Double, amp: ControlInstrument): SynthNote = {
    audioStack.mapInstrument {
      audio => ModularSynth.monoAllpass(audio, amp, delayTime, decayTime).addAction(TAIL_ACTION)
    }
    this
  }

  def monoDelay(delayTime: Double, amp: ControlInstrument): SynthNote = {
    audioStack.mapInstrument {
      audio => ModularSynth.monoDelay(audio, amp, delayTime).addAction(TAIL_ACTION)
    }
    this
  }

  def monoVolume(amp: ControlInstrument): SynthNote = {
    audioStack.mapInstrument {
      audio => ModularSynth.monoVolume(audio, amp).addAction(TAIL_ACTION)
    }
    this
  }

  def stereoVolume(amp: ControlInstrument): SynthNote = {
    audioStack.mapInstrument {
      audio => ModularSynth.stereoVolume(audio, amp).addAction(TAIL_ACTION)
    }
    this
  }

  def pan(panPosition: ControlInstrument): SynthNote = {
    audioStack.mapInstrument {
      audio => panning(audio, panPosition)
        .addAction(TAIL_ACTION)
    }
    this
  }

  private def superColliderPlayback(startTime: Double, duration: Double, outputBus: Int = 0, realOutput: Boolean = true): Unit = {
    val out = if(realOutput) synthPlayer.getRealOutputBus(outputBus) else outputBus
    audioStack.foreachInstrument {
      audio =>
        audio.getOutputBus.staticBus(out)
        val graph = audio.buildGraph(startTime, duration, audio.graph(Seq()))
        if(synthPlayer.bufferedPlayback) {
          synthPlayer.client.playBundle(absoluteTimeToMillis(startTime), graph)
        } else {
          synthPlayer.client.send(synthPlayer.client.newBundle(absoluteTimeToMillis(startTime), graph))
        }
    }
  }

  private def writeToScore(startTime: Double, duration: Double, outputBus: Int = 0): Unit = {
    val out = outputBus
    audioStack.foreachInstrument {
      audio =>
        audio.getOutputBus.staticBus(out)
        audio.buildGraph(startTime, duration, audio.graph(Seq()))
          .map(SuperColliderClient.newSynthRaw)
          .foreach(message => synthPlayer.superColliderScore.addMessage(startTime, message))
    }
  }

  def playWithDuration(startTime: Double, duration: Double, outputBus: Int = 0, realOutput: Boolean = true, shouldWriteToScore: Boolean = false): Unit = {
    if(shouldWriteToScore) writeToScore(startTime, duration, outputBus)
    else superColliderPlayback(startTime, duration, outputBus, realOutput)
  }

  def play(startTime: Double, outputBus: Int = 0, realOutput: Boolean = true, shouldWriteToScore: Boolean = false): Unit =
    playWithDuration(startTime, audioStack.duration.get, outputBus, realOutput, shouldWriteToScore)
}