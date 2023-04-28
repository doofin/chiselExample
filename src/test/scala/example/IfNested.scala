package example

import chisel3._
/*class IfModNested(using parent: DependenciesInfo) extends UserModule {
    val a = newInput[16]("a")
    val b = newInput[16]("b")
    val c = newInput[16]("b")
    val y = newOutput[16]("y")

    y :== a - b
    If(a === b) {
      y :== a + b
      If(a === c) {
        y :== a * b
      }
    }
  }*/

class IfNested extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(16.W))
    val b = Input(UInt(16.W))
    val c = Input(UInt(16.W))
    val y = Output(UInt(16.W))
  })
  io.y := io.a - io.b
  when(io.a === io.b) {
    io.y := io.a + io.b
    when(io.a === io.c) {
      io.y := io.a * io.b

    }

  }
}
class testIfNested extends utils.genFirrtl(() => new IfNested)

class testIfNestedV extends utils.genVerilog(() => new IfNested)

/*
module IfNested :
  input clock : Clock
  input reset : UInt<1>
  output io : { flip a : UInt<16>, flip b : UInt<16>, flip c : UInt<16>, y : UInt<16>}

  node _io_y_T = sub(io.a, io.b) @[IfNested.scala 26:16]
  node _io_y_T_1 = tail(_io_y_T, 1) @[IfNested.scala 26:16]
  io.y <= _io_y_T_1 @[IfNested.scala 26:8]
  node _T = eq(io.a, io.b) @[IfNested.scala 27:13]
  when _T : @[IfNested.scala 27:23]
    node _io_y_T_2 = add(io.a, io.b) @[IfNested.scala 28:18]
    node _io_y_T_3 = tail(_io_y_T_2, 1) @[IfNested.scala 28:18]
    io.y <= _io_y_T_3 @[IfNested.scala 28:10]
    node _T_1 = eq(io.a, io.c) @[IfNested.scala 29:15]
    when _T_1 : @[IfNested.scala 29:25]
      node _io_y_T_4 = mul(io.a, io.b) @[IfNested.scala 30:20]
      io.y <= _io_y_T_4 @[IfNested.scala 30:12]
 */
