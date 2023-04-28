package example

import chisel3._

class adder1 extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(16.W))
    val b = Input(UInt(16.W))
//    val c = Input(UInt(16.W))
    val y = Output(UInt(6.W))
  })

//  io.y := io.a + io.b + io.c
  io.y := io.a + io.b
//  io.y := io.a + 1.U
}

class adder2 extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(1.W))
    val b = Input(UInt())
    //    val c = Input(UInt(16.W))
    val y = Output(UInt())
  })

  //  io.y := io.a + io.b + io.c
  //  io.y := io.a + io.b
  io.y := io.a + 1.U
}

class testadder extends utils.genVerilog(() => new adder1)

class testadder2 extends utils.genFirrtl(() => new adder2)

/*
circuit adder1 :
  module adder1 :
    input clock : Clock
    input reset : UInt<1>
    output io : { flip a : UInt<16>, flip b : UInt<16>, y : UInt<16>}

    node _io_y_T = add(io.a, io.b) @[adder.scala 12:16]
    node _io_y_T_1 = tail(_io_y_T, 1) @[adder.scala 12:16]
    io.y <= _io_y_T_1 @[adder.scala 12:8]

 */

/* normal form
y=a+b+c

x0=a+b
y=x0+c
 */
