package example

import chisel3._
import chisel3.stage.ChiselStage
import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._
import org.scalatest.propspec.AnyPropSpec

//import pprint._
class adder() extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(32.W))
    val b = Input(UInt(32.W))
    val y = Output(UInt(32.W))
    val y2 = Output(UInt(32.W))
  })

  //  io.y := io.a + io.b
  //   io.y := io.a & io.b
  (if ((1 to 10).sum == 10) io.y else io.y2) := io.a & io.b
}

class useradder extends Module {
  val n = Module(new adder())
  n.io.a := 1.U
  n.io.b := 1.U
  //  var n=1
  //  n=2
}

class adderSpec extends AnyPropSpec {
  property("GCD should elaborate") {
    //    ChiselStage.
    val ChirrtlR = ChiselStage.emitChirrtl {
      //      new adder()
      new useradder
    }
    println(ChirrtlR.toString)
    /* circuit adder :
      module adder :
        input clock : Clock
        input reset : UInt<1>
        output io : { flip a : UInt<32>, flip b : UInt<32>, y : UInt<32>}

        node _io_y_T = add(io.a, io.b) @[AdderOld.scala 88:16]
        node _io_y_T_1 = tail(_io_y_T, 1) @[AdderOld.scala 88:16]
        io.y <= _io_y_T_1 @[AdderOld.scala 88:8]
        */
    val firrtlR = ChiselStage.emitFirrtl {
      new adder()
    }
    println("firrtlR")
    println(firrtlR.toString)
  }
}

/*
firrtlR

circuit adder :
module adder :
input clock : Clock
input reset : UInt<1>
output io : { flip a : UInt<32>, flip b : UInt<32>, y : UInt<32>}

node _io_y_T = add(io.a, io.b) @[AdderOld.scala 88:16]
node _io_y_T_1 = tail(_io_y_T, 1) @[AdderOld.scala 88:16]
io.y <= _io_y_T_1 @[AdderOld.scala 88:8]
*/

/*circuit useradder :
  module adder :
    input clock : Clock
    input reset : Reset
    output io : { flip a : UInt<32>, flip b : UInt<32>, y : UInt<32>}

    node _io_y_T = add(io.a, io.b) @[AdderOld.scala 89:16]
    node _io_y_T_1 = tail(_io_y_T, 1) @[AdderOld.scala 89:16]
    io.y <= _io_y_T_1 @[AdderOld.scala 89:8]
    node _io_y_T_2 = and(io.a, io.b) @[AdderOld.scala 90:17]
    io.y <= _io_y_T_2 @[AdderOld.scala 90:9]

  module useradder :
    input clock : Clock
    input reset : UInt<1>

    inst n of adder @[AdderOld.scala 94:17]
    n.clock <= clock
    n.reset <= reset
    n.io.a <= UInt<1>("h1") @[AdderOld.scala 95:9]
    n.io.b <= UInt<1>("h1") @[AdderOld.scala 96:9]
*/
