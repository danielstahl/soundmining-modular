package net.soundmining.modular

import net.soundmining.synth.Instrument._
import net.soundmining.synth.BusAllocator
import java.util.UUID

/**
  * A development of the Instrument that can  build a graph of
  * instruments and bus arguments are the actual class that generates
  * the bus.
  *
  * val percControl = perc(....)
  * val staticControl = static(440)
  * val triangle = triangle(staticControl, percControl)
  *
  * When you build the triangle you will need the start and
  * end time because you need to know when the buses will will
  * be used.
  *
  * You also need to recursively build all instruments and send them
  * to SC in the correct order. E.g the controllers before the oscillators.
  *
  * Because a controller can be used several times you need to know if
  * a instrument have been generated before. For that reason all instances
  * of a instrument need an id that is unique. UUID.randomUUID().toString?
  *
  * All modular instruments have one output (and one output only). Either
  * to a control bus or a audio bus.
  */
object ModularInstrument {
  var DEBUG = false

  trait Bus {
    def busAllocator: BusAllocator
    var busValue: Option[Int] = None

    def dynamicBus(startTime: Double, endTime: Double, nrOfChannels: Int = 1): Integer = {
      if(busValue.isEmpty) {
        val allocatedBuses = busAllocator.allocate(nrOfChannels, startTime, endTime)
        this.busValue = Option(allocatedBuses.head)
        busValue.foreach {
          busVal =>
            if(DEBUG) {
              println(s"Allocated ${this.getClass.getSimpleName} ch $busVal $nrOfChannels channels from $startTime to $endTime")
            }

        }
      }
      busValue.get
    }

    def staticBus(busValue: Int): Int = {
      this.busValue = Option(busValue)
      this.busValue.get
    }
  }

  class AudioBus extends Bus {
    def busAllocator: BusAllocator = BusAllocator.audio
  }

  class ControlBus extends Bus {
    def busAllocator: BusAllocator = BusAllocator.control
  }

  trait ModularInstrument {
    type SelfType <: ModularInstrument
    type OutputBusType <: Bus
    type OutputInstrumentType <: ModularInstrument

    def self(): SelfType

    val id: String = UUID.randomUUID().toString
    val instrumentName: String

    val nrOfChannels: Int

    var addAction: AddAction = HEAD_ACTION

    def addAction(value: AddAction): SelfType = {
      addAction = value
      self()
    }

    var nodeId: Node = SOURCE

    def nodeId(value: Node): SelfType = {
      nodeId = value
      self()
    }

    var optionalDur: Option[Double] = None

    def withDur(value: Double): SelfType = {
      optionalDur = Option(value)
      self()
    }

    /**
      * return this instruments output bus
      */
    def getOutputBus: OutputBusType

    def withOutput(output: OutputInstrumentType): SelfType

    /**
      * Build a flattened graph of this instrument and it's children
      */
    def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument]

    /**
      * Prepend this instrument builder to the flattened graph. Makes sure that it gets
      * built as early as possible
      */
    def prependToGraph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      if (parent.exists(instrument => instrument.id == this.id)) {
        parent
      } else {
        this +: parent
      }

    /**
      * Append this instrument builder to the flattened graph. Also makes sure
      * that its not added if it's already there.
      */
    def appendToGraph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] =
      if (parent.exists(instrument => instrument.id == this.id)) {
        parent
      } else {
        parent :+ this
      }

    /**
      * Builds a flattened graph of instrument builders
      */
    def buildGraph(startTime: Double, duration: Double, flatGraph: Seq[ModularInstrument]): Seq[Seq[Any]] =
      flatGraph.map(instrument => instrument.build(startTime, duration))
        .filter(graph => graph.nonEmpty)

    def internalBuild(startTime: Double, duration: Double): Seq[Any]

    var instrumentIsBuilt = false
    /**
      * Builds this instrument
      */
    def build(startTime: Double, duration: Double): Seq[Any] = {
      if(!instrumentIsBuilt) {
        val finalDuration = optionalDur.getOrElse(duration)

        val graph = Seq(
          instrumentName,
          Integer.valueOf(-1), addAction.action, nodeId.nodeId,
          "out", getOutputBus.dynamicBus(startTime, startTime + finalDuration, nrOfChannels),
          "dur", finalDuration
        ) ++ internalBuild(startTime, finalDuration)
        instrumentIsBuilt = true
        graph
      } else {
        Seq()
      }

    }
  }

  trait AudioInstrument extends ModularInstrument {
    override type OutputBusType = AudioBus
    override type OutputInstrumentType = AudioInstrument
    var bus = new AudioBus
    override def getOutputBus: AudioBus = bus

    def withOutput(output: AudioInstrument): SelfType = {
      this.bus = output.getOutputBus
      self()
    }
  }

  trait ControlInstrument extends ModularInstrument {
    override type OutputBusType = ControlBus
    override type OutputInstrumentType = ControlInstrument

    var bus = new ControlBus
    override def getOutputBus: ControlBus = bus

    val nrOfChannels: Int = 1

    override def withOutput(output: ControlInstrument): SelfType = {
      this.bus = output.getOutputBus
      self()
    }
  }

  abstract class StaticAudioBusInstrument extends AudioInstrument {
    override val instrumentName: String = "NONE"

    override def graph(parent: Seq[ModularInstrument]): Seq[ModularInstrument] = parent

    override def internalBuild(startTime: Double, duration: Double): Seq[Any] = Seq.empty
  }


  class StaticMonoAudioBusInstrument extends StaticAudioBusInstrument {
    override type SelfType = StaticMonoAudioBusInstrument

    override def self(): SelfType = this

    override val nrOfChannels: Int = 1
  }

  class StaticStereoAudioBusInstrument extends StaticAudioBusInstrument {
    override type SelfType = StaticStereoAudioBusInstrument

    override def self(): SelfType = this

    override val nrOfChannels: Int = 2
  }
}