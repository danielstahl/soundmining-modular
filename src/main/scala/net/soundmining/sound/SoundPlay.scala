package net.soundmining.sound

import net.soundmining.modular.ModularInstrument.{AudioInstrument, ControlInstrument, StaticAudioBusInstrument}
import net.soundmining.modular.ModularSynth.{lineControl, staticControl}
import net.soundmining.synth.SuperColliderClient
import net.soundmining.synth.SuperColliderClient.allocRead

object BufNumAllocator {
  var bufNum = 0

  def reset(): Unit = {
    bufNum = 0
  }

  def next(): Int = {
    val nextBufNum = bufNum
    bufNum = bufNum + 1
    nextBufNum
  }
}

case class SoundPlay(soundPath: String, start: Double, end: Double,
                     amp: Double => ControlInstrument = amp => staticControl(amp),
                     spectrumFrequencies: Seq[Double] = Seq.empty, peakTimes: Seq[Double] = Seq.empty) {

  def duration(rate: Double): Double = math.abs((end - start) / rate)

  def absoluteTime(time: Double, rate: Double): Double = math.abs((time - start) / rate)

  def absolutePeakTimes(rate: Double): Seq[Double] = {
    absoluteTimes(peakTimes, rate)
  }

  def relativePeakTimes(rate: Double): Seq[Double] =
    relativeTimes(peakTimes, rate)

  def absoluteTimes(times: Seq[Double], rate: Double): Seq[Double] =
    times.map(time => absoluteTime(time, rate))
      .prepended(0.0).appended(absoluteTime(end, rate))

  def relativeTimes(times: Seq[Double], rate: Double): Seq[Double] = {
    val absTimes = absoluteTimes(times, rate)
    absTimes.zipWithIndex.tail.map {
      case (time, i) => time - absTimes(i - 1)
    }
  }

  var bufNum: Int = _

  def init(implicit client: SuperColliderClient): Unit = {
    bufNum = BufNumAllocator.next()
    client.send(allocRead(bufNum, soundPath))
  }
}

case class SoundPlays(soundPlays: Map[String, SoundPlay], masterVolume: Double = 1.0, numberOfOutputBuses: Int = 2)(implicit val client: SuperColliderClient = SuperColliderClient()) {

  def init: Unit = {
    BufNumAllocator.reset()
    soundPlays.values.foreach(_.init)
  }

  case class SoundNotePlayer(soundPlay: SoundPlay, soundNote: SoundNote) {

    def playMono(start: Double, end: Double, rate: Double, volume: Double): SoundNotePlayer = {
      soundNote.playMono(start, end, rate, soundPlay.amp(volume * masterVolume))
      this
    }

    def playMono(rate: Double, volume: Double): SoundNotePlayer = {
      playMono(soundPlay.start, soundPlay.end, rate, volume)
      this
    }

    def apply(func: SoundNote => Unit): SoundNotePlayer = {
      func(soundNote)
      this
    }

    def sine(freq: Double, attackTime: Double = 0.01, releaseTime: Double = 0.01): SoundNotePlayer = {
      soundNote.sine(freq, attackTime, releaseTime)
      this
    }

    def sineFm(carrier: Double, modulator: Double, modulatorAmount: Double, attackTime: Double = 0.01, releaseTime: Double = 0.01): SoundNotePlayer = {
      soundNote.sineFm(carrier, modulator, modulatorAmount, attackTime, releaseTime)
      this
    }

    def noise(attackTime: Double = 0.01, releaseTime: Double = 0.01): SoundNotePlayer = {
      soundNote.noise(attackTime, releaseTime)
      this
    }

    def ring(modulator: Double): SoundNotePlayer = {
      soundNote.ring(staticControl(modulator))
      this
    }

    def ring(startModulator: Double, endModulator: Double): SoundNotePlayer = {
      soundNote.ring(lineControl(startModulator, endModulator))
      this
    }

    def am(modulator: Double): SoundNotePlayer = {
      soundNote.am(staticControl(modulator))
      this
    }

    def am(startModulator: Double, endModulator: Double): SoundNotePlayer = {
      soundNote.am(lineControl(startModulator, endModulator))
      this
    }

    def frequencyShift(modularFreq: ControlInstrument): SoundNotePlayer = {
      soundNote.frequencyShift(modularFreq)
      this
    }

    def bitCrushing(amount: ControlInstrument): SoundNotePlayer = {
      soundNote.bitCrushing(amount)
      this
    }

    def highPass(filterFreq: Double): SoundNotePlayer = {
      highPass(staticControl(filterFreq))
    }

    def highPass(startFreq: Double, endFreq: Double): SoundNotePlayer = {
      highPass(lineControl(startFreq, endFreq))
    }

    def highPass(filterFreq: ControlInstrument): SoundNotePlayer = {
      soundNote.highPass(filterFreq)
      this
    }

    def lowPass(filterFreq: Double): SoundNotePlayer = {
      lowPass(staticControl(filterFreq))
    }

    def lowPass(startFreq: Double, endFreq: Double): SoundNotePlayer = {
      lowPass(lineControl(startFreq, endFreq))
    }

    def lowPass(filterFreq: ControlInstrument): SoundNotePlayer = {
      soundNote.lowPass(filterFreq)
      this
    }

    def bandPass(filterFreq: Double, rq: Double): SoundNotePlayer = {
      soundNote.bandPass(staticControl(filterFreq), staticControl(rq))
      this
    }

    def bandReject(filterFreq: Double, rq: Double): SoundNotePlayer = {
      soundNote.bandReject(staticControl(filterFreq), staticControl(rq))
      this
    }

    def pan(panControl: () => ControlInstrument): SoundNotePlayer = {
      soundNote.pan(panControl())
      this
    }

    def pan(pos: Double): SoundNotePlayer = {
      pan(() => staticControl(pos))
      this
    }

    def pan(startPos: Double, endPos: Double): SoundNotePlayer = {
      pan(() => lineControl(startPos, endPos))
      this
    }

    def splay(spread: ControlInstrument, center: ControlInstrument): SoundNotePlayer = {
      soundNote.splayPan(spread, center)
      this
    }

    def splay(spread: Double, startCenter: Double, endCenter: Double): SoundNotePlayer = {
      splay(staticControl(spread), lineControl(startCenter, endCenter))
      this
    }

    def splay(spread: Double, center: Double): SoundNotePlayer = {
      splay(staticControl(spread), staticControl(center))
      this
    }

    def play(startTime: Double, outputBus: Int, realOutput: Boolean = true): Unit = {
      val out = if(realOutput) getRealOutputBus(outputBus) else outputBus
      soundNote.play(startTime, out)
    }

    def play(startTime: Double, output: AudioInstrument): Unit = {
      val out = output.getOutputBus.busValue.get
      soundNote.play(startTime, out)
    }
  }

  def mono(name:String): SoundNotePlayer = {
    val soundPlay = soundPlays(name)
    SoundNotePlayer(soundPlay, MonoSoundNote(bufNum = soundPlay.bufNum))
  }

  def getRealOutputBus(outputBus: Int): Int =
    if(numberOfOutputBuses > 2) (outputBus % numberOfOutputBuses) + 2
    else outputBus % numberOfOutputBuses

  def apply(name: String): SoundPlay = soundPlays(name)
}
