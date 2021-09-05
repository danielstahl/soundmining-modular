package net.soundmining.sound

import net.soundmining.modular.ModularInstrument.ControlInstrument
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
    def playMono(rate: Double, volume: Double): SoundNotePlayer = {
      soundNote.playMono(soundPlay.start, soundPlay.end, rate, soundPlay.amp(volume * masterVolume))
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

    def highPass(filterFreq: Double): SoundNotePlayer = {
      soundNote.highPass(staticControl(filterFreq))
      this
    }

    def lowPass(filterFreq: Double): SoundNotePlayer = {
      soundNote.lowPass(staticControl(filterFreq))
      this
    }

    def bandPass(filterFreq: Double, rq: Double): SoundNotePlayer = {
      soundNote.bandPass(staticControl(filterFreq), staticControl(rq))
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
    def play(startTime: Double, outputBus: Int): Unit =
      soundNote.play(startTime, getRealOutputBus(outputBus))
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
