package net.soundmining.modular

import net.soundmining.modular.ModularInstrument.ControlInstrument
import net.soundmining.modular.ModularSynth.staticControl
import net.soundmining.synth.{SuperColliderClient, SuperColliderScore}
import net.soundmining.synth.SuperColliderClient.{allocRead, freeBuffer}

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
                     peakTimes: Seq[Double] = Seq.empty) {

  def duration(rate: Double): Double = math.abs((end - start) / rate)

  def absoluteTime(time: Double, rate: Double): Double = math.abs((time - start) / rate)

  def absolutePeakTimes(rate: Double): Seq[Double] =
    absoluteTimes(peakTimes, rate)

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

  def initScore(superColliderScore: SuperColliderScore): Unit = {
    bufNum = BufNumAllocator.next()
    superColliderScore.addMessage(0, allocRead(bufNum, soundPath))
  }

  def stop(implicit client: SuperColliderClient): Unit =
    client.send(freeBuffer(bufNum))

}
