package example

import example.ALU.Alu
import Chisel.{is, switch}
import chisel3._
import chisel3.stage.ChiselStage
import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._
import org.scalatest.propspec.AnyPropSpec

object ALU {
  val w = Wire(UInt())
  val cond = Bool()
  w := 0.U
  when(cond) {
    w := 3.U
  }

  class Alu extends Module {
    val io = IO(new Bundle {
      val a = Input(UInt(16.W))
      val b = Input(UInt(16.W))
      val fn = Input(UInt(2.W))
      val y = Output(UInt(16.W))
    })
    // some default value is needed
    io.y := 0.U
    // The ALU selection
    switch(io.fn) {
      is(0.U) {
        io.y := io.a + io.b
      }
      is(1.U) {
        io.y := io.a - io.b
      }
      is(2.U) {
        io.y := io.a | io.b
      }
      is(3.U) {
        io.y := io.a & io.b
      }
    }
  }

}

// switch becomes when
class testAlu extends utils.genFirrtl(() => new Alu)

/*circuit Alu :
  module Alu :
    input clock : Clock
    input reset : UInt<1>
    output io : { flip a : UInt<16>, flip b : UInt<16>, flip fn : UInt<2>, y : UInt<16>}

    io.y <= UInt<1>("h0") @[ALU.scala 29:10]
    node _T = eq(UInt<1>("h0"), io.fn) @[ALU.scala 31:19]
    when _T : @[ALU.scala 31:19]
      node _io_y_T = add(io.a, io.b) @[ALU.scala 33:22]
      node _io_y_T_1 = tail(_io_y_T, 1) @[ALU.scala 33:22]
      io.y <= _io_y_T_1 @[ALU.scala 33:14]
    else :
      node _T_1 = eq(UInt<1>("h1"), io.fn) @[ALU.scala 31:19]
      when _T_1 : @[ALU.scala 31:19]
        node _io_y_T_2 = sub(io.a, io.b) @[ALU.scala 36:22]
        node _io_y_T_3 = tail(_io_y_T_2, 1) @[ALU.scala 36:22]
        io.y <= _io_y_T_3 @[ALU.scala 36:14]
      else :
        node _T_2 = eq(UInt<2>("h2"), io.fn) @[ALU.scala 31:19]
        when _T_2 : @[ALU.scala 31:19]
          node _io_y_T_4 = or(io.a, io.b) @[ALU.scala 39:22]
          io.y <= _io_y_T_4 @[ALU.scala 39:14]
        else :
          node _T_3 = eq(UInt<2>("h3"), io.fn) @[ALU.scala 31:19]
          when _T_3 : @[ALU.scala 31:19]
            node _io_y_T_5 = and(io.a, io.b) @[ALU.scala 42:22]
            io.y <= _io_y_T_5 @[ALU.scala 42:14]
*/

