package example

import chisel3.Module
import chisel3.stage.ChiselStage
import org.scalatest.propspec.AnyPropSpec

object utils {
  class genFirrtl(m: () => Module) extends AnyPropSpec {
    property("GCD should elaborate") {

      val firrtlR = ChiselStage.emitChirrtl {
        m()
      }
      println("firrtl :")
      println(firrtlR)
    }
  }

  class genVerilog(m: () => Module) extends AnyPropSpec {
    property("GCD should elaborate") {

      val firrtlR = ChiselStage.emitVerilog {
        m()
      }
      println("verilog :")
      println(firrtlR)
    }
  }
}

class parseFtl extends AnyPropSpec {
  val parsed = firrtl.Parser.parse(ftlExample.adder)
  val parsed2 = firrtl.Parser.parse(ftlExample.adderSimp)
  println(parsed2)

}
