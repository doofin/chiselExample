package example

import chisel3._
import chisel3.stage.ChiselStage
import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec
import chisel3.experimental.BundleLiterals._
import org.scalatest.propspec.AnyPropSpec

class Adder() extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(32.W))
    val b = Input(UInt(32.W))
    val y = Output(UInt(32.W))
  })

  io.y := io.a + io.b
}

class AdderCall extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(32.W))
    val b = Input(UInt(32.W))
    val y = Output(UInt(32.W))
  })

  val m1 = Module(new Adder())

  m1.io.a := io.a
  m1.io.b := io.b

  io.y := m1.io.y

}

class DoubleAdder extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(32.W))
    val b = Input(UInt(32.W))
    val c = Input(UInt(32.W))
    val d = Input(UInt(32.W))

    val y = Output(UInt(32.W))
  })

  val m1 = Module(new Adder())
  val m2 = Module(new Adder())

  m1.io.a := io.a
  m1.io.b := io.b
  m2.io.a := io.c
  m2.io.b := io.d

  io.y := m1.io.y + m2.io.y
}
class testDoubleAdder extends utils.genVerilog(() => new DoubleAdder)
/*
circuit DoubleAdder :
  module Adder :
    input clock : Clock
    input reset : UInt<1>
    output io : { flip a : UInt<32>, flip b : UInt<32>, y : UInt<32>}

    node _io_y_T = add(io.a, io.b) @[doubleAdder.scala 19:16]
    node _io_y_T_1 = tail(_io_y_T, 1) @[doubleAdder.scala 19:16]
    io.y <= _io_y_T_1 @[doubleAdder.scala 19:8]

  module DoubleAdder :
    input clock : Clock
    input reset : UInt<1>
    output io : { flip a : UInt<32>, flip b : UInt<32>, flip c : UInt<32>, flip d : UInt<32>, y : UInt<32>}

    inst m1 of Adder @[doubleAdder.scala 34:18]
    m1.clock <= clock
    m1.reset <= reset
    inst m2 of Adder @[doubleAdder.scala 35:18]
    m2.clock <= clock
    m2.reset <= reset
    m1.io.a <= io.a @[doubleAdder.scala 36:11]
    m1.io.b <= io.b @[doubleAdder.scala 37:11]
    m2.io.a <= io.c @[doubleAdder.scala 38:11]
    m2.io.b <= io.d @[doubleAdder.scala 39:11]
    node _io_y_T = add(m1.io.y, m2.io.y) @[doubleAdder.scala 41:19]
    node _io_y_T_1 = tail(_io_y_T, 1) @[doubleAdder.scala 41:19]
    io.y <= _io_y_T_1 @[doubleAdder.scala 41:8]
 */
