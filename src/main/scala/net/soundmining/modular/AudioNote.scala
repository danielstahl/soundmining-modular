package net.soundmining.modular

import net.soundmining.modular.ModularInstrument.{AudioInstrument, ControlInstrument}
import net.soundmining.modular.ModularSynth.{amModulate, bandPassFilter, bandRejectFilter, highPassFilter, lineControl, lowPassFilter, panning, pulseOsc, ringModulate, sawOsc, staticControl, triangleOsc}
import net.soundmining.synth.Instrument.TAIL_ACTION
import net.soundmining.synth.SuperColliderClient
import net.soundmining.synth.Utils.absoluteTimeToMillis

import scala.collection.mutable

case class AudioNote() {
  val audioInstruments = mutable.Stack[AudioInstrument]()

  def saw(freq: Double, amp: ControlInstrument): AudioNote = {
    audioInstruments.push(sawOsc(amp, staticControl(freq)).addAction(TAIL_ACTION))
    this
  }

  def triangle(freq: Double, amp: ControlInstrument): AudioNote = {
    audioInstruments.push(triangleOsc(amp, staticControl(freq)).addAction(TAIL_ACTION))
    this
  }

  def pulse(freq: Double, amp: ControlInstrument): AudioNote = {
    audioInstruments.push(pulseOsc(amp, staticControl(freq)).addAction(TAIL_ACTION))
    this
  }

  def ring(modulatorFreq: Double): AudioNote = {
    audioInstruments.push(ringModulate(audioInstruments.pop(), staticControl(modulatorFreq)).addAction(TAIL_ACTION))
    this
  }

  def frequencyShift(modulator: ControlInstrument): AudioNote = {
    audioInstruments.push(ModularSynth.frequencyShift(audioInstruments.pop(), modulator).addAction(TAIL_ACTION))
    this
  }

  def bitCrushing(amount: ControlInstrument): AudioNote = {
    audioInstruments.push(ModularSynth.bitCrushing(audioInstruments.pop(), amount).addAction(TAIL_ACTION))
    this
  }

  def am(modulatorFreq: Double): AudioNote = {
    audioInstruments.push(amModulate(audioInstruments.pop(), staticControl(modulatorFreq)).addAction(TAIL_ACTION))
    this
  }

  def bandReject(freq: Double, rq: Double): AudioNote = {
    audioInstruments.push(bandRejectFilter(audioInstruments.pop(), staticControl(freq), staticControl(rq))
      .addAction(TAIL_ACTION))
    this
  }

  def bandPass(freq: Double, rq: Double): AudioNote = {
    audioInstruments.push(bandPassFilter(audioInstruments.pop(), staticControl(freq), staticControl(rq))
      .addAction(TAIL_ACTION))
    this
  }

  def highPass(freq: Double): AudioNote = {
    audioInstruments.push(highPassFilter(audioInstruments.pop(), staticControl(freq))
      .addAction(TAIL_ACTION))
    this
  }

  def lowPass(freq: Double): AudioNote = {
    audioInstruments.push(lowPassFilter(audioInstruments.pop(), staticControl(freq))
      .addAction(TAIL_ACTION))
    this
  }

  def xfade(pan: ControlInstrument): AudioNote = {
    audioInstruments.push(ModularSynth.xfade(audioInstruments.pop(), audioInstruments.pop(), pan)
      .addAction(TAIL_ACTION))
    this
  }

  def pan(panPosition: Double): AudioNote =
    pan(staticControl(panPosition))

  def pan(startPan: Double, endPan: Double): AudioNote =
    pan(lineControl(startPan, endPan))

  def pan(panPosition: ControlInstrument): AudioNote = {
    audioInstruments.push(panning(audioInstruments.pop(), panPosition)
      .addAction(TAIL_ACTION)
      .withNrOfChannels(2))
    this
  }

  def getRealOutputBus(outputBus: Int, numberOfOutputBuses: Int): Int =
    if(numberOfOutputBuses > 2) (outputBus % numberOfOutputBuses) + 2
    else outputBus % numberOfOutputBuses

  def play(start: Double, dur: Double, output: Int = 0, realOutput: Boolean = true)(implicit client: SuperColliderClient): Unit = {
    val out = if(realOutput) getRealOutputBus(output, AudioNote.numberOfOutputBuses) else output
    val audioInstrument = audioInstruments.pop()
    audioInstrument.getOutputBus.staticBus(out)
    val graph = audioInstrument.buildGraph(start, dur, audioInstrument.graph(Seq()))
    client.send(client.newBundle(absoluteTimeToMillis(start), graph))
  }
}

object AudioNote {
  val numberOfOutputBuses: Int = 2
}
