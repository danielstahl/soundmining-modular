package net.soundmining.modular

import ModularInstrument._
import net.soundmining.synth.Instrument._

object ModularSynth {
  
  def staticAudioBus(): StaticAudioBusInstrument =
    new StaticAudioBusInstrument()

  def staticControl(value: Double): StaticControl =
    new StaticControl().control(value)

  def percControl(startValue: Double, peakValue: Double, attackTime: Double, curve: Either[Double, EnvCurve]): PercControl =
    new PercControl().control(startValue, peakValue, attackTime, curve)

  def relativePercControl(startValue: Double, peakValue: Double, attackTime: Double, curve: Either[Double, EnvCurve]): PercControl =
    new RelativePercControl().control(startValue, peakValue, attackTime, curve)

  def threeBlockcontrol(startValue1: Double, len1: Double, startValue2: Double, len2: Double, startValue3: Double, len3: Double, endValue3: Double, curve: Either[Double, EnvCurve]): ThreeBlockControl =
    new ThreeBlockControl().control(startValue1, len1, startValue2, len2, startValue3, len3, endValue3, curve)

  def relativeThreeBlockcontrol(startValue1: Double, len1: Double, startValue2: Double, startValue3: Double, len3: Double, endValue3: Double, curve: Either[Double, EnvCurve]): RelativeThreeBlockControl =
    new RelativeThreeBlockControl().control(startValue1, len1, startValue2, startValue3, len3, endValue3, curve)

  def lineControl(startValue: Double, endValue: Double): LineControl =
    new LineControl().control(startValue, endValue)

  def xlineControl(startValue: Double, endValue: Double): XLineControl =
    new XLineControl().control(startValue, endValue)

  def sineControl(freqBus: ControlInstrument, minValue: Double, maxValue: Double): SineControl =
    new SineControl().control(freqBus, minValue, maxValue)

  def controlMix(in1Bus: ControlInstrument, in2Bus: ControlInstrument): ControlMix =
    new ControlMix().mix(in1Bus, in2Bus)

  def controlMultiply(in1Bus: ControlInstrument, in2Bus: ControlInstrument): ControlMultiply =
    new ControlMultiply().mix(in1Bus, in2Bus)

  def controlSum(in1Bus: ControlInstrument, in2Bus: ControlInstrument): ControlSum =
    new ControlSum().mix(in1Bus, in2Bus)

  def soundAmplitudeControl(inBus: AudioInstrument, attackTime: Double, releaseTime: Double): SoundAmplitudeControl =
    new SoundAmplitudeControl().control(inBus, attackTime, releaseTime)

  def panning(inBus: AudioInstrument, panBus: ControlInstrument): Panning =
    new Panning().pan(inBus, panBus)

  def splay(inBus: AudioInstrument, spreadBus: ControlInstrument, centerBus: ControlInstrument, level: Double = 1): Splay =
    new Splay().splay(inBus, spreadBus, centerBus, level)

  def monoDelay(inBus: AudioInstrument, ampBus: ControlInstrument, delayTime: Double, decayTime: Double): MonoDelay =
    new MonoDelay().delay(inBus, ampBus, delayTime, decayTime)

  def stereoDelay(inBus: AudioInstrument, ampBus: ControlInstrument, delayTime: Double, decayTime: Double): StereoDelay =
    new StereoDelay().delay(inBus, ampBus, delayTime, decayTime)

  def reverb(inBus: AudioInstrument, preDelay: Double, wet: Double, combDelay: Double, combDecay: Double, allpassDelay: Double, allpassDecay: Double): Reverb =
    new Reverb().reverb(inBus, preDelay, wet, combDelay, combDecay, allpassDelay, allpassDecay)

  def convolutionReverb(inBus: AudioInstrument, irLeftBus: Int, irRightBus: Int, amp: Double = 1.0, fftSize: Int = 2048): ConvolutionReverb =
    new ConvolutionReverb().reverb(inBus, irLeftBus, irRightBus, amp, fftSize)

  def xfade(in1Bus: AudioInstrument, in2Bus: AudioInstrument, xfadeBus: ControlInstrument): XFade =
    new XFade().xfade(in1Bus: AudioInstrument, in2Bus: AudioInstrument, xfadeBus: ControlInstrument)

  def left(inBus: AudioInstrument, ampBus: ControlInstrument): Left =
    new Left().oneChannel(inBus, ampBus)

  def right(inBus: AudioInstrument, ampBus: ControlInstrument): Right =
    new Right().oneChannel(inBus, ampBus)

  def mix(in1Bus: AudioInstrument, in2Bus: AudioInstrument, ampBus: ControlInstrument): Mix =
    new Mix().mix(in1Bus: AudioInstrument, in2Bus: AudioInstrument, ampBus)

  def expand(leftInBus: AudioInstrument, rightInBus: AudioInstrument): Expand = 
    new Expand().expand(leftInBus, rightInBus)

  def triangleOsc(ampBus: ControlInstrument, freqBus: ControlInstrument): TriangleOsc =
    new TriangleOsc().triangle(ampBus, freqBus)

  def pulseOsc(ampBus: ControlInstrument, freqBus: ControlInstrument): PulseOsc =
    new PulseOsc().pulse(ampBus, freqBus)

  def sawOsc(ampBus: ControlInstrument, freqBus: ControlInstrument): SawOsc =
    new SawOsc().saw(ampBus, freqBus)

  def sineOsc(ampBus: ControlInstrument, freqBus: ControlInstrument): SineOsc =
    new SineOsc().sine(ampBus, freqBus)

  def whiteNoiseOsc(ampBus: ControlInstrument): WhiteNoiseOsc =
    new WhiteNoiseOsc().whiteNoise(ampBus)

  def dustOsc(ampBus: ControlInstrument, freqBus: ControlInstrument): DustOsc =
    new DustOsc().dust(ampBus, freqBus)

  def moogFilter(inBus: AudioInstrument, freqBus: ControlInstrument, gainBus: ControlInstrument): MoogFilter =
    new MoogFilter().filter(inBus, freqBus, gainBus)

  def resonantFilter(inBus: AudioInstrument, freqBus: ControlInstrument, decayBus: ControlInstrument): ResonantFilter =
    new ResonantFilter().filter(inBus, freqBus, decayBus)

  def lowPassFilter(inBus: AudioInstrument, freqBus: ControlInstrument): LowPassFilter =
    new LowPassFilter().filter(inBus, freqBus)

  def highPassFilter(inBus: AudioInstrument, freqBus: ControlInstrument): HighPassFilter =
    new HighPassFilter().filter(inBus, freqBus)

  def bandPassFilter(inBus: AudioInstrument, freqBus: ControlInstrument, rqBus: ControlInstrument): BandPassFilter =
    new BandPassFilter().filter(inBus, freqBus, rqBus)

  def bandRejectFilter(inBus: AudioInstrument, freqBus: ControlInstrument, rqBus: ControlInstrument): BandRejectFilter =
    new BandRejectFilter().filter(inBus, freqBus, rqBus)

  def ringModulate(carrierBus: AudioInstrument, modulatorFreqBus: ControlInstrument): RingModulate =
    new RingModulate().modulate(carrierBus, modulatorFreqBus)

  def amModulate(carrierBus: AudioInstrument, modulatorFreqBus: ControlInstrument): AmModulate =
    new AmModulate().modulate(carrierBus, modulatorFreqBus)

  def fmSineModulate(carrierFreqBus: ControlInstrument, modulatorBus: AudioInstrument,
               ampBus: ControlInstrument): FmSineModulate =
    new FmSineModulate().modulate(carrierFreqBus, modulatorBus, ampBus)

  def fmPulseModulate(carrierFreqBus: ControlInstrument, modulatorBus: AudioInstrument,
                     ampBus: ControlInstrument): FmPulseModulate =
    new FmPulseModulate().modulate(carrierFreqBus, modulatorBus, ampBus)

  def fmSawModulate(carrierFreqBus: ControlInstrument, modulatorBus: AudioInstrument,
                      ampBus: ControlInstrument): FmSawModulate =
    new FmSawModulate().modulate(carrierFreqBus, modulatorBus, ampBus)

  def fmTriangleModulate(carrierFreqBus: ControlInstrument, modulatorBus: AudioInstrument,
                    ampBus: ControlInstrument): FmTriangleModulate =
    new FmTriangleModulate().modulate(carrierFreqBus, modulatorBus, ampBus)

 def monoPlayBuffer(bufNum: Int, rate: Double, start: Double, end: Double, ampBus: ControlInstrument): MonoPlayBuffer =
    new MonoPlayBuffer().playBuffer(bufNum, rate, start, end, ampBus)

  def stereoPlayBuffer(bufNum: Int, rate: Double, start: Double, end: Double, ampBus: ControlInstrument): StereoPlayBuffer =
    new StereoPlayBuffer().playBuffer(bufNum, rate, start, end, ampBus)

  class PercControl extends ControlInstrument {
    type SelfType = PercControl

    def self(): SelfType = this

    val instrumentName: String = "percControl"

    var attackTime: Double = _
    var curveValue: Either[Double, EnvCurve] = Left(-4f)
    var startValue: Double = _
    var peakValue: Double = _

    def control(startValue: Double, peakValue: Double, attackTime: Double, curve: Either[Double, EnvCurve]): SelfType = {
      this.startValue = startValue
      this.peakValue = peakValue
      this.attackTime = attackTime
      this.curveValue = curve
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      prependToGraph(parent)

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] =
      Seq(
        "startValue", startValue,
        "peakValue", peakValue,
        "attackTime", attackTime,
        "curve", curveValue match {
          case Left(floatValue) => floatValue
          case Right(constant) => constant.name
        })
  }

  class RelativePercControl extends PercControl {
    override def internalBuild(startTime: Double, duration: Double): Seq[Any] =
      Seq(
        "startValue", startValue,
        "peakValue", peakValue,
        "attackTime", attackTime * duration,
        "curve", curveValue match {
          case Left(floatValue) => floatValue
          case Right(constant) => constant.name
        })
  }

  class ThreeBlockControl extends ControlInstrument {
    type SelfType = ThreeBlockControl

    def self(): SelfType = this

    val instrumentName: String = "threeBlockControl"

    var startValue1: Double = _
    var len1: Double = _
    var startValue2: Double = _
    var len2: Double = _
    var startValue3: Double = _
    var len3: Double = _
    var endValue3: Double = _
    var curveValue: Either[Double, EnvCurve] = Left(-4f)

    def control(startValue1: Double, len1: Double, startValue2: Double, len2: Double, startValue3: Double, len3: Double, endValue3: Double, curve: Either[Double, EnvCurve]): SelfType = {
      this.startValue1 = startValue1
      this.len1 = len1
      this.startValue2 = startValue2
      this.len2 = len2
      this.startValue3 = startValue3
      this.len3 = len3
      this.endValue3 = endValue3
      this.curveValue = curve
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      prependToGraph(parent)

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] =
      Seq(
        "startValue1", startValue1,
        "len1", len1,
        "startValue2", startValue2,
        "len2", len2,
        "startValue3", startValue3,
        "len3", len3,
        "endValue3", endValue3,
        "curve", curveValue match {
          case Left(floatValue) => floatValue
          case Right(constant) => constant.name
        })
  }

  class RelativeThreeBlockControl extends ControlInstrument {
    type SelfType = RelativeThreeBlockControl

    def self(): SelfType = this

    val instrumentName: String = "threeBlockControl"

    var startValue1: Double = _
    var len1: Double = _
    var startValue2: Double = _
    var startValue3: Double = _
    var len3: Double = _
    var endValue3: Double = _
    var curveValue: Either[Double, EnvCurve] = Left(-4f)

    def control(startValue1: Double, len1: Double, startValue2: Double, startValue3: Double, len3: Double, endValue3: Double, curve: Either[Double, EnvCurve]): SelfType = {
      this.startValue1 = startValue1
      this.len1 = len1
      this.startValue2 = startValue2
      this.startValue3 = startValue3
      this.len3 = len3
      this.endValue3 = endValue3
      this.curveValue = curve
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      prependToGraph(parent)

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] =
      Seq(
        "startValue1", startValue1,
        "len1", len1 * duration,
        "startValue2", startValue2,
        "len2", (1 - len1 - len3) * duration,
        "startValue3", startValue3,
        "len3", len3 * duration,
        "endValue3", endValue3,
        "curve", curveValue match {
          case Left(floatValue) => floatValue
          case Right(constant) => constant.name
        })
  }

  class LineControl extends ControlInstrument {
    type SelfType = LineControl

    def self(): SelfType = this

    val instrumentName: String = "lineControl"

    var startValue: Double = _
    var endValue: Double = _

    def control(startValue: Double, endValue: Double): SelfType = {
      this.startValue = startValue
      this.endValue = endValue
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      prependToGraph(parent)

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] =
      Seq(
        "startValue", startValue,
        "endValue", endValue)
  }

  class XLineControl extends ControlInstrument {
    type SelfType = XLineControl

    def self(): SelfType = this

    val instrumentName: String = "xlineControl"

    var startValue: Double = _
    var endValue: Double = _

    def control(startValue: Double, endValue: Double): SelfType = {
      this.startValue = startValue
      this.endValue = endValue
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      prependToGraph(parent)

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] =
      Seq(
        "startValue", startValue,
        "endValue", endValue)
  }

  class StaticControl extends ControlInstrument {
    type SelfType = StaticControl

    def self(): SelfType = this

    val instrumentName: String = "staticControl"

    var value: Double = _

    def control(value: Double): SelfType = {
      this.value = value
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      prependToGraph(parent)

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] =
      Seq("value", value)
  }

  class SineControl extends ControlInstrument {
    type SelfType = SineControl

    def self(): SelfType = this

    val instrumentName: String = "sineControl"

    var freqBus: ControlInstrument = _
    var minValue: Double = _
    var maxValue: Double = _

    def control(freqBus: ControlInstrument, minValue: Double, maxValue: Double): SelfType = {
      this.freqBus = freqBus
      this.minValue = minValue
      this.maxValue = maxValue
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(freqBus.graph(parent))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] =
      Seq(
        "freqBus", freqBus.getOutputBus.dynamicBus(startTime, startTime + freqBus.optionalDur.getOrElse(duration)),
        "minValue", minValue,
        "maxValue", maxValue)
  }

  class ControlMix extends ControlInstrument {
    type SelfType = ControlMix

    def self(): SelfType = this

    val instrumentName: String = "controlMix"

    var in1Bus: ControlInstrument = _
    var in2Bus: ControlInstrument = _

    def mix(in1Bus: ControlInstrument, in2Bus: ControlInstrument): SelfType = {
      this.in1Bus = in1Bus
      this.in2Bus= in2Bus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(in1Bus.graph(in2Bus.graph(parent)))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] = {
      val durationFallback: Double = duration

      Seq(
        "in1", in1Bus.getOutputBus.dynamicBus(startTime,
            startTime + in1Bus.optionalDur.getOrElse(duration)),
        "in2", in2Bus.getOutputBus.dynamicBus(startTime,
            startTime + in2Bus.optionalDur.getOrElse(duration)))
    }
  }

  class ControlMultiply extends ControlInstrument {
    type SelfType = ControlMultiply

    def self(): SelfType = this

    val instrumentName: String = "controlMultiply"

    var in1Bus: ControlInstrument = _
    var in2Bus: ControlInstrument = _

    def mix(in1Bus: ControlInstrument, in2Bus: ControlInstrument): SelfType = {
      this.in1Bus = in1Bus
      this.in2Bus= in2Bus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(in1Bus.graph(in2Bus.graph(parent)))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] = {
      val durationFallback: Double = duration

      Seq(
        "in1", in1Bus.getOutputBus.dynamicBus(startTime,
          startTime + in1Bus.optionalDur.getOrElse(duration)),
        "in2", in2Bus.getOutputBus.dynamicBus(startTime,
          startTime + in2Bus.optionalDur.getOrElse(duration)))
    }
  }

  class ControlSum extends ControlInstrument {
    type SelfType = ControlSum

    def self(): SelfType = this

    val instrumentName: String = "controlSum"

    var in1Bus: ControlInstrument = _
    var in2Bus: ControlInstrument = _

    def mix(in1Bus: ControlInstrument, in2Bus: ControlInstrument): SelfType = {
      this.in1Bus = in1Bus
      this.in2Bus= in2Bus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(in1Bus.graph(in2Bus.graph(parent)))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] = {
      val durationFallback: Double = duration

      Seq(
        "in1", in1Bus.getOutputBus.dynamicBus(startTime,
          startTime + in1Bus.optionalDur.getOrElse(duration)),
        "in2", in2Bus.getOutputBus.dynamicBus(startTime,
          startTime + in2Bus.optionalDur.getOrElse(duration)))
    }
  }

  class SoundAmplitudeControl extends ControlInstrument {
    type SelfType = SoundAmplitudeControl

    override def self(): SelfType = this

    val instrumentName: String = "soundAmplitudeControl"

    var inBus: AudioInstrument = _
    var attackTime: Double = _
    var releaseTime: Double = _

    def control(inBus: AudioInstrument, attackTime: Double, releaseTime: Double): SelfType = {
      this.inBus = inBus
      this.attackTime = attackTime
      this.releaseTime = releaseTime
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(inBus.graph(parent))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] = {
      Seq(
        "in", inBus.getOutputBus.dynamicBus(startTime,
          startTime + inBus.optionalDur.getOrElse(duration)),
        "attackTime", attackTime,
        "releaseTime", releaseTime
      )
    }
  }

  class Panning extends AudioInstrument {
    type SelfType = Panning

    def self(): SelfType = this

    val instrumentName: String = "pan"

    var inBus: AudioInstrument = _
    var panBus: ControlInstrument = _

    def pan(inBus: AudioInstrument, panBus: ControlInstrument): SelfType = {
      this.inBus = inBus
      this.panBus = panBus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(inBus.graph(panBus.graph(parent)))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] = {
      Seq(
        "in", inBus.getOutputBus.dynamicBus(startTime,
            startTime + inBus.optionalDur.getOrElse(duration)),
        "panBus", panBus.getOutputBus.dynamicBus(startTime,
            startTime + panBus.optionalDur.getOrElse(duration)))
    }
  }

  class Splay extends AudioInstrument {
    type SelfType = Splay

    def self(): SelfType = this

    val instrumentName: String = "splay"

    var inBus: AudioInstrument = _
    var spreadBus: ControlInstrument = _
    var centerBus: ControlInstrument = _
    var level: Double = _

    def splay(inBus: AudioInstrument, spreadBus: ControlInstrument, centerBus: ControlInstrument, level: Double = 1): SelfType = {
      this.inBus = inBus
      this.spreadBus = spreadBus
      this.centerBus = centerBus
      this.level = level
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(inBus.graph(spreadBus.graph(centerBus.graph(parent))))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] = {
      Seq(
        "in", inBus.getOutputBus.dynamicBus(startTime,
          startTime + inBus.optionalDur.getOrElse(duration)),
        "spreadBus", spreadBus.getOutputBus.dynamicBus(startTime,
          startTime + spreadBus.optionalDur.getOrElse(duration)),
        "level", level,
        "centerBus", centerBus.getOutputBus.dynamicBus(startTime,
          startTime + centerBus.optionalDur.getOrElse(duration)))
    }
  }

  abstract class Delay extends AudioInstrument {
    var inBus: AudioInstrument = _
    var delaytime: Double = _
    var decaytime: Double = _
    var ampBus: ControlInstrument = _

    def delay(inBus: AudioInstrument, ampBus: ControlInstrument, delayTime: Double, decayTime: Double): SelfType = {
      this.inBus = inBus
      this.ampBus = ampBus
      this.delaytime = delayTime
      this.decaytime = decayTime
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(ampBus.graph(inBus.graph(parent)))

    def getInputBus(startTime: Double, durationFallback: Double): Int


    override def internalBuild(startTime: Double, duration: Double): Seq[Any] = {
      val durationFallback: Double = duration

      Seq("in", getInputBus(startTime, durationFallback),
          "ampBus", ampBus.getOutputBus.dynamicBus(startTime,
            startTime + ampBus.optionalDur.getOrElse(duration)),
          "delaytime", delaytime,
          "decaytime", decaytime)
    }
  }

  class MonoDelay extends Delay {
    override type SelfType = MonoDelay

    override def self(): SelfType = this

    override def getInputBus(startTime: Double, durationFallback: Double): Int =
      this.inBus.getOutputBus.dynamicBus(startTime,
        startTime + inBus.optionalDur.getOrElse(durationFallback))

    override val instrumentName: String = "monoDelay"
  }

  class StereoDelay extends Delay {
    override type SelfType = StereoDelay

    override def self(): SelfType = this

    override def getInputBus(startTime: Double, durationFallback: Double): Int =
      this.inBus.getOutputBus.dynamicBus(startTime,
        startTime + inBus.optionalDur.getOrElse(durationFallback), nrOfChannels = 2)

    override val instrumentName: String = "stereoDelay"
  }

  class Reverb extends AudioInstrument {
    override type SelfType = Reverb

    override def self(): SelfType = this

    override val instrumentName: String = "reverb"

    var inBus: AudioInstrument = _
    var predelay: Double = _
    var wet: Double = _
    var combdelay: Double = _
    var combdecay: Double = _
    var allpassdelay: Double = _
    var allpassdecay: Double = _

    def reverb(inBus: AudioInstrument, preDelay: Double, wet: Double, combDelay: Double, combDecay: Double, allpassDelay: Double, allpassDecay: Double): SelfType = {
      this.inBus = inBus
      this.predelay = preDelay
      this.wet = wet
      this.combdelay = combDelay
      this.combdecay = combDecay
      this.allpassdelay = allpassDelay
      this.allpassdecay = allpassDecay
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(inBus.graph(parent))

    def getInputBus(startTime: Double, durationFallback: Double): Int =
      this.inBus.getOutputBus.dynamicBus(startTime,
        startTime + inBus.optionalDur.getOrElse(durationFallback),
        2)

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] = {
      val durationFallback: Double = duration

      Seq("in", getInputBus(startTime, durationFallback),
        "predelay", predelay,
        "wet", wet,
        "combdelay", combdelay,
        "combdecay", combdecay,
        "allpassdelay", allpassdelay,
        "allpassdecay", allpassdecay)
    }
  }

  class ConvolutionReverb extends AudioInstrument {

    override type SelfType = ConvolutionReverb

    override def self(): SelfType = this

    override val instrumentName: String = "stereoConvolutionReverb"

    var inBus: AudioInstrument = _
    var irLeftBus: Int = _
    var irRightBus: Int = _
    var amp: Double = _
    var fftSize: Int = _

    def reverb(inBus: AudioInstrument, irLeftBus: Int, irRightBus: Int, amp: Double = 1.0, fftSize: Int = 2048): SelfType = {
      this.inBus = inBus
      this.irLeftBus = irLeftBus
      this.irRightBus = irRightBus
      this.amp = amp
      this.fftSize = fftSize
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(inBus.graph(parent))

    def getInputBus(startTime: Double, durationFallback: Double): Int =
      this.inBus.getOutputBus.dynamicBus(startTime,
        startTime + inBus.optionalDur.getOrElse(durationFallback),
        2)

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] = {
      val durationFallback: Double = duration

      Seq("in", getInputBus(startTime, durationFallback),
        "fftSize", fftSize,
        "irLeft", irLeftBus,
        "irRight", irRightBus,
        "amp", amp)
    }
  }

  class XFade extends AudioInstrument {
    type SelfType = XFade

    def self(): SelfType = this

    val instrumentName: String = "xfade"

    var in1Bus: AudioInstrument = _
    var in2Bus: AudioInstrument = _
    var xfadeBus: ControlInstrument = _

    def xfade(in1Bus: AudioInstrument, in2Bus: AudioInstrument, xfadeBus: ControlInstrument): SelfType = {
      this.in1Bus = in1Bus
      this.in2Bus= in2Bus
      this.xfadeBus = xfadeBus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(in1Bus.graph(in2Bus.graph(xfadeBus.graph(parent))))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] = {
      val durationFallback: Double = duration

      Seq(
        "in1", in1Bus.getOutputBus.dynamicBus(startTime,
            startTime + in1Bus.optionalDur.getOrElse(duration)),
        "in2", in2Bus.getOutputBus.dynamicBus(startTime,
            startTime + in2Bus.optionalDur.getOrElse(duration)),
        "xfadeBus", xfadeBus.getOutputBus.dynamicBus(startTime,
            startTime + xfadeBus.optionalDur.getOrElse(duration)))
    }
  }


  abstract class OneChannel extends AudioInstrument {
    var inBus: AudioInstrument = _
    var ampBus: ControlInstrument = _

    def oneChannel(inBus: AudioInstrument, ampBus: ControlInstrument): SelfType = {
      this.inBus = inBus
      this.ampBus = ampBus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(ampBus.graph(inBus.graph(parent)))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] = {
      val durationFallback: Double = duration

      Seq(
        "in", inBus.getOutputBus.dynamicBus(startTime,
            startTime + inBus.optionalDur.getOrElse(duration)),
        "ampBus", ampBus.getOutputBus.dynamicBus(startTime,
            startTime + ampBus.optionalDur.getOrElse(duration)))
    }
  }

  class Left extends OneChannel {
    type SelfType = Left
    def self(): SelfType = this
    val instrumentName: String = "left"
  }

  class Right extends OneChannel {
    type SelfType = Right
    def self(): SelfType = this
    val instrumentName: String = "right"
  }

  class Mix extends AudioInstrument {
    type SelfType = Mix

    def self(): SelfType = this

    val instrumentName: String = "mix"

    var in1Bus: AudioInstrument = _
    var in2Bus: AudioInstrument = _
    var ampBus: ControlInstrument = _

    def mix(in1Bus: AudioInstrument, in2Bus: AudioInstrument, ampBus: ControlInstrument): SelfType = {
      this.in1Bus = in1Bus
      this.in2Bus = in2Bus
      this.ampBus = ampBus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(ampBus.graph(in1Bus.graph(in2Bus.graph(parent))))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] = {
      val durationFallback: Double = duration

      Seq(
        "in1", in1Bus.getOutputBus.dynamicBus(startTime,
            startTime + in1Bus.optionalDur.getOrElse(duration)),
        "in2", 
          in2Bus.getOutputBus.dynamicBus(startTime,
            startTime + in2Bus.optionalDur.getOrElse(duration)),
        "ampBus", ampBus.getOutputBus.dynamicBus(startTime,
            startTime + ampBus.optionalDur.getOrElse(duration)))
    }
  }

  class Expand extends AudioInstrument {
    type SelfType = Expand

    def self(): SelfType = this

    val instrumentName: String = "expand"

    var leftInBus: AudioInstrument = _
    var rightInBus: AudioInstrument = _
    
    def expand(leftInBus: AudioInstrument, rightInBus: AudioInstrument): SelfType = {
      this.leftInBus = leftInBus
      this.rightInBus= rightInBus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(leftInBus.graph(rightInBus.graph(parent)))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] = {
      val durationFallback: Double = duration

      Seq(
        "leftIn", leftInBus.getOutputBus.dynamicBus(startTime,
            startTime + leftInBus.optionalDur.getOrElse(duration)),
        "rightIn", rightInBus.getOutputBus.dynamicBus(startTime,
            startTime + rightInBus.optionalDur.getOrElse(duration)))
    }
  }

  class TriangleOsc extends AudioInstrument {
    type SelfType = TriangleOsc

    def self(): SelfType = this

    val instrumentName: String = "triangleOsc"

    var ampBus: ControlInstrument = _
    var freqBus: ControlInstrument = _

    def triangle(ampBus: ControlInstrument, freqBus: ControlInstrument): SelfType = {
      this.ampBus = ampBus
      this.freqBus = freqBus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(ampBus.graph(freqBus.graph(parent)))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] =
      Seq(
        "freqBus", freqBus.getOutputBus.dynamicBus(startTime,
          startTime + freqBus.optionalDur.getOrElse(duration)),
        "ampBus", ampBus.getOutputBus.dynamicBus(startTime,
          startTime + ampBus.optionalDur.getOrElse(duration)))
  }

  class PulseOsc extends AudioInstrument {
    type SelfType = PulseOsc

    def self(): SelfType = this

    val instrumentName: String = "pulseOsc"

    var ampBus: ControlInstrument = _
    var freqBus: ControlInstrument = _

    def pulse(ampBus: ControlInstrument, freqBus: ControlInstrument): SelfType = {
      this.ampBus = ampBus
      this.freqBus = freqBus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(ampBus.graph(freqBus.graph(parent)))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] =
      Seq(
        "freqBus", freqBus.getOutputBus.dynamicBus(startTime,
          startTime + freqBus.optionalDur.getOrElse(duration)),
        "ampBus", ampBus.getOutputBus.dynamicBus(startTime,
          startTime + ampBus.optionalDur.getOrElse(duration)))
  }

  class SawOsc extends AudioInstrument {
    type SelfType = SawOsc

    def self(): SelfType = this

    val instrumentName: String = "sawOsc"

    var ampBus: ControlInstrument = _
    var freqBus: ControlInstrument = _

    def saw(ampBus: ControlInstrument, freqBus: ControlInstrument): SelfType = {
      this.ampBus = ampBus
      this.freqBus = freqBus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(ampBus.graph(freqBus.graph(parent)))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] =
      Seq(
        "freqBus", freqBus.getOutputBus.dynamicBus(startTime,
          startTime + freqBus.optionalDur.getOrElse(duration)),
        "ampBus", ampBus.getOutputBus.dynamicBus(startTime,
          startTime + ampBus.optionalDur.getOrElse(duration)))
  }

  class SineOsc extends AudioInstrument {
    type SelfType = SineOsc

    def self(): SelfType = this

    val instrumentName: String = "sineOsc"

    var ampBus: ControlInstrument = _
    var freqBus: ControlInstrument = _

    def sine(ampBus: ControlInstrument, freqBus: ControlInstrument): SelfType = {
      this.ampBus = ampBus
      this.freqBus = freqBus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(ampBus.graph(freqBus.graph(parent)))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] =
      Seq(
        "freqBus", freqBus.getOutputBus.dynamicBus(startTime,
          startTime + freqBus.optionalDur.getOrElse(duration)),
        "ampBus", ampBus.getOutputBus.dynamicBus(startTime,
          startTime + ampBus.optionalDur.getOrElse(duration)))
  }

  class WhiteNoiseOsc extends AudioInstrument {
    type SelfType = WhiteNoiseOsc

    def self(): SelfType = this

    val instrumentName: String = "whiteNoiseOsc"

    var ampBus: ControlInstrument = _

    def whiteNoise(ampBus: ControlInstrument): SelfType = {
      this.ampBus = ampBus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(ampBus.graph(parent))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] =
      Seq(
        "ampBus", ampBus.getOutputBus.dynamicBus(startTime,
          startTime + ampBus.optionalDur.getOrElse(duration)))
  }

  class DustOsc extends AudioInstrument {
    type SelfType = DustOsc

    def self(): SelfType = this

    val instrumentName: String = "dustOsc"

    var ampBus: ControlInstrument = _
    var freqBus: ControlInstrument = _

    def dust(ampBus: ControlInstrument, freqBus: ControlInstrument): SelfType = {
      this.ampBus = ampBus
      this.freqBus = freqBus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(ampBus.graph(freqBus.graph(parent)))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] =
      Seq(
        "freqBus", freqBus.getOutputBus.dynamicBus(startTime,
          startTime + freqBus.optionalDur.getOrElse(duration)),
        "ampBus", ampBus.getOutputBus.dynamicBus(startTime,
          startTime + ampBus.optionalDur.getOrElse(duration)))
  }

  class MoogFilter extends AudioInstrument {
    type SelfType = MoogFilter

    def self(): SelfType = this

    val instrumentName: String = "moogFilter"

    var inBus: AudioInstrument = _
    var freqBus: ControlInstrument = _
    var gainBus: ControlInstrument = _

    def filter(inBus: AudioInstrument, freqBus: ControlInstrument, gainBus: ControlInstrument): SelfType = {
      this.inBus = inBus
      this.freqBus = freqBus
      this.gainBus = gainBus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(inBus.graph(freqBus.graph(gainBus.graph(parent))))

    override def internalBuild(startTime: Double, duration: Double): Seq[Object] = {
      val durationFallback: Double = duration

      Seq(
        "in", inBus.getOutputBus.dynamicBus(startTime,
            startTime + inBus.optionalDur.getOrElse(duration)),
        "freqBus", freqBus.getOutputBus.dynamicBus(startTime,
            startTime + freqBus.optionalDur.getOrElse(duration)),
        "gainBus", gainBus.getOutputBus.dynamicBus(startTime,
            startTime + gainBus.optionalDur.getOrElse(duration)))
    }
  }

  class ResonantFilter extends AudioInstrument {
    type SelfType = ResonantFilter

    def self(): SelfType = this

    val instrumentName: String = "resonantFilter"

    var inBus: AudioInstrument = _
    var freqBus: ControlInstrument = _
    var decayBus: ControlInstrument = _

    def filter(inBus: AudioInstrument, freqBus: ControlInstrument, decayBus: ControlInstrument): SelfType = {
      this.inBus = inBus
      this.freqBus = freqBus
      this.decayBus = decayBus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(inBus.graph(freqBus.graph(decayBus.graph(parent))))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] = {
      val durationFallback: Double = duration

      Seq(
        "in", inBus.getOutputBus.dynamicBus(startTime,
            startTime + inBus.optionalDur.getOrElse(duration)),
        "freqBus", freqBus.getOutputBus.dynamicBus(startTime,
            startTime + freqBus.optionalDur.getOrElse(duration)),
        "decayBus", decayBus.getOutputBus.dynamicBus(startTime,
            startTime + decayBus.optionalDur.getOrElse(duration)))
    }
  }

  class HighPassFilter extends AudioInstrument {
    type SelfType = HighPassFilter

    def self(): SelfType = this

    val instrumentName: String = "highPassFilter"

    var inBus: AudioInstrument = _
    var freqBus: ControlInstrument = _

    def filter(inBus: AudioInstrument, freqBus: ControlInstrument): SelfType = {
      this.inBus = inBus
      this.freqBus = freqBus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(inBus.graph(freqBus.graph(parent)))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] = {
      val durationFallback: Double = duration

      Seq(
        "in", inBus.getOutputBus.dynamicBus(startTime,
            startTime + inBus.optionalDur.getOrElse(duration)),
        "freqBus", freqBus.getOutputBus.dynamicBus(startTime,
            startTime + freqBus.optionalDur.getOrElse(duration)))
    }
  }

  class LowPassFilter extends AudioInstrument {
    type SelfType = LowPassFilter

    def self(): SelfType = this

    val instrumentName: String = "lowPassFilter"

    var inBus: AudioInstrument = _
    var freqBus: ControlInstrument = _

    def filter(inBus: AudioInstrument, freqBus: ControlInstrument): SelfType = {
      this.inBus = inBus
      this.freqBus = freqBus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(inBus.graph(freqBus.graph(parent)))

    override def internalBuild(startTime: Double, duration: Double): Seq[Object] = {
      val durationFallback: Double = duration

      Seq(
        "in", inBus.getOutputBus.dynamicBus(startTime,
            startTime + inBus.optionalDur.getOrElse(duration)),
        "freqBus", freqBus.getOutputBus.dynamicBus(startTime,
            startTime + freqBus.optionalDur.getOrElse(duration)))
    }
  }

  class BandPassFilter extends AudioInstrument {
    type SelfType = BandPassFilter

    def self(): SelfType = this

    val instrumentName: String = "bandPassFilter"

    var inBus: AudioInstrument = _
    var freqBus: ControlInstrument = _
    var rqBus: ControlInstrument = _

    def filter(inBus: AudioInstrument, freqBus: ControlInstrument, rqBus: ControlInstrument): SelfType = {
      this.inBus = inBus
      this.freqBus = freqBus
      this.rqBus = rqBus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(inBus.graph(freqBus.graph(rqBus.graph(parent))))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] = {
      val durationFallback: Double = duration

      Seq(
        "in", inBus.getOutputBus.dynamicBus(startTime,
            startTime + inBus.optionalDur.getOrElse(duration)),
        "freqBus", freqBus.getOutputBus.dynamicBus(startTime,
            startTime + freqBus.optionalDur.getOrElse(duration)),
        "rqBus", rqBus.getOutputBus.dynamicBus(startTime,
            startTime + rqBus.optionalDur.getOrElse(duration)))
    }
  }

  class BandRejectFilter extends AudioInstrument {
    type SelfType = BandRejectFilter

    def self(): SelfType = this

    val instrumentName: String = "bandRejectFilter"

    var inBus: AudioInstrument = _
    var freqBus: ControlInstrument = _
    var rqBus: ControlInstrument = _

    def filter(inBus: AudioInstrument, freqBus: ControlInstrument, rqBus: ControlInstrument): SelfType = {
      this.inBus = inBus
      this.freqBus = freqBus
      this.rqBus = rqBus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(inBus.graph(freqBus.graph(rqBus.graph(parent))))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] = {
      val durationFallback: Double = duration

      Seq(
        "in", inBus.getOutputBus.dynamicBus(startTime,
          startTime + inBus.optionalDur.getOrElse(duration)),
        "freqBus", freqBus.getOutputBus.dynamicBus(startTime,
          startTime + freqBus.optionalDur.getOrElse(duration)),
        "rqBus", rqBus.getOutputBus.dynamicBus(startTime,
          startTime + rqBus.optionalDur.getOrElse(duration)))
    }
  }

  class RingModulate extends AudioInstrument {
    type SelfType = RingModulate

    def self(): SelfType = this

    val instrumentName: String = "ringModulate"

    var carrierBus: AudioInstrument = _
    var modulatorFreqBus: ControlInstrument = _

    def modulate(carrierBus: AudioInstrument, modulatorFreqBus: ControlInstrument): SelfType = {
      this.carrierBus = carrierBus
      this.modulatorFreqBus = modulatorFreqBus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(carrierBus.graph(modulatorFreqBus.graph(parent)))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] = {
      val durationFallback: Double = duration

      Seq(
        "carrierBus", carrierBus.getOutputBus.dynamicBus(startTime,
            startTime + carrierBus.optionalDur.getOrElse(duration)),
        "modulatorFreqBus", modulatorFreqBus.getOutputBus.dynamicBus(startTime,
            startTime + modulatorFreqBus.optionalDur.getOrElse(duration)))
    }
  }

  class AmModulate extends AudioInstrument {
    type SelfType = AmModulate

    def self(): SelfType = this

    val instrumentName: String = "amModulate"

    var carrierBus: AudioInstrument = _
    var modulatorFreqBus: ControlInstrument = _

    def modulate(carrierBus: AudioInstrument, modulatorFreqBus: ControlInstrument): SelfType = {
      this.carrierBus = carrierBus
      this.modulatorFreqBus = modulatorFreqBus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(carrierBus.graph(modulatorFreqBus.graph(parent)))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] = {
      val durationFallback: Double = duration

      Seq(
        "carrierBus", carrierBus.getOutputBus.dynamicBus(startTime,
            startTime + carrierBus.optionalDur.getOrElse(duration)),
        "modulatorFreqBus", modulatorFreqBus.getOutputBus.dynamicBus(startTime,
            startTime + modulatorFreqBus.optionalDur.getOrElse(duration)))
    }
  }

  abstract class FmModulate extends AudioInstrument {
    var carrierFreqBus: ControlInstrument = _
    var modulatorBus: AudioInstrument = _
    var ampBus: ControlInstrument = _

    def modulate(carrierFreqBus: ControlInstrument, modulatorBus: AudioInstrument,
                 ampBus: ControlInstrument): SelfType = {
      this.carrierFreqBus = carrierFreqBus
      this.modulatorBus = modulatorBus
      this.ampBus = ampBus
      self()
    }

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      appendToGraph(carrierFreqBus.graph(modulatorBus.graph(ampBus.graph(parent))))

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] = {
      val durationFallback: Double = duration

      Seq(
        "carrierFreqBus", carrierFreqBus.getOutputBus.dynamicBus(startTime,
            startTime + carrierFreqBus.optionalDur.getOrElse(duration)),
        "modulatorBus", modulatorBus.getOutputBus.dynamicBus(startTime,
            startTime + modulatorBus.optionalDur.getOrElse(duration)),
        "ampBus", ampBus.getOutputBus.dynamicBus(startTime,
            startTime + ampBus.optionalDur.getOrElse(duration)))
    }
  }

  class FmSineModulate extends FmModulate {
    type SelfType = FmSineModulate

    def self(): SelfType = this

    val instrumentName: String = "fmSineModulate"
  }

  class FmPulseModulate extends FmModulate {
    type SelfType = FmPulseModulate

    def self(): SelfType = this

    val instrumentName: String = "fmPulseModulate"
  }

  class FmSawModulate extends FmModulate {
    type SelfType = FmSawModulate

    def self(): SelfType = this

    val instrumentName: String = "fmSawModulate"
  }

  class FmTriangleModulate extends FmModulate {
    type SelfType = FmTriangleModulate

    def self(): SelfType = this

    val instrumentName: String = "fmTriangleModulate"
  }

  abstract class PlayBuffer extends AudioInstrument  {
      var bufNum: Int = _
      var rate: Double = _
      var start: Double = _
      var end: Double = _
      var ampBus: ControlInstrument = _

      def playBuffer(bufNum: Int, rate: Double, start: Double, end: Double, ampBus: ControlInstrument): SelfType = {
          this.bufNum = bufNum
          this.rate = rate
          this.start = start
          this.end = end
          this.ampBus = ampBus
          self()
      }

      override def graph(parent: Seq[ModularInstrument.ModularInstrument]): Seq[ModularInstrument.ModularInstrument] = 
        appendToGraph(ampBus.graph(parent))

      override def internalBuild(startTime: Double, duration: Double): Seq[Any] =  
        Seq(
            "bufNum", bufNum,
            "rate", rate,
            "start", start,
            "end", end,
            "ampBus", ampBus.getOutputBus.dynamicBus(startTime,
              startTime + ampBus.optionalDur.getOrElse(duration))
        )  
  }

  class MonoPlayBuffer extends PlayBuffer {
    type SelfType = MonoPlayBuffer
    def self(): SelfType = this
    val instrumentName: String = "monoPlayBuffer"
  }

  class StereoPlayBuffer extends PlayBuffer {
    type SelfType = StereoPlayBuffer
    def self(): SelfType = this
    val instrumentName: String = "stereoPlayBuffer"
  }
  
}
