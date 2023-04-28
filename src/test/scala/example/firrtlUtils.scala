package example

import firrtl._
import firrtl.ir.Circuit
import firrtl.passes._
import firrtl.stage.{FirrtlMain, RunFirrtlTransformAnnotation}
import firrtl.transforms._
import org.scalatest.propspec.AnyPropSpec

import java.io.{BufferedWriter, File, FileWriter}

object firrtlUtils {

  // Example firrtl
  val input = ftlExample.adderSimp
  val parsedCircuit: Circuit = firrtl.Parser.parse(input)

  /*
  val state = CircuitState(parsedCircuit, UnknownForm)

  // Designate a series of transforms to be run in this order
  val transforms: Seq[Transform] =
    Seq(ToWorkingIR, ResolveKinds, InferTypes, new InferWidths)

  // Run transforms and capture final state
  val finalState = transforms.foldLeft(state) {
    (c: CircuitState, t: Transform) => t.runTransform(c)
  }

  // Emit output
  println(finalState.circuit.serialize)

   */
}

class firrtlUtils extends AnyPropSpec {
//  println(firrtlUtils.parsedCircuit)
  pprint.pprintln(firrtlUtils.parsedCircuit)

}

class verilogEmit extends AnyPropSpec {
  val parsedCircuit: Circuit = firrtl.Parser.parse(ftlExample.adderSimp)

  val vlg = RunFirrtlTransformAnnotation(new VerilogEmitter)
  val transforms: Seq[Transform] =
    Seq(ToWorkingIR, ResolveKinds, InferTypes, new InferWidths, vlg.transform)

  // Run transforms and capture final state
  val finalState =
    transforms.foldLeft(CircuitState(parsedCircuit, UnknownForm)) {
      (c: CircuitState, t: Transform) => t.runTransform(c)
    }

  // Emit output
  println(finalState.circuit.serialize)

  val vf = File.createTempFile("temp", ".fir")



  FirrtlMain.main("-i regress/rocket.fir".split(" "))
}

