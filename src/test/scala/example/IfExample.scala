package example

import chisel3._

class IfExample extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(16.W))
    val b = Input(UInt(16.W))
    val y = Output(UInt(16.W))
  })

  when(io.a === io.b) {
    io.y := io.a + io.b

  } otherwise {
    io.y := io.a - io.b
  }
}

class IfUninit extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(16.W))
    val b = Input(UInt(16.W))
    val y = Output(UInt(16.W))
  })

  when(io.a === io.b) {
//    io.y := io.a + io.b

  } otherwise {
    io.y := io.a - io.b
  }
}
//gen verilog will err : [module IfUninit]  Reference io is not fully initialized.
//but gen firrtl is fine
class testIfUnin extends utils.genFirrtl(() => new IfUninit)

/*
circuit IfExample :
  module IfExample :
    input clock : Clock
    input reset : UInt<1>
    output io : { flip a : UInt<16>, flip b : UInt<16>, y : UInt<16>}

    node _T = eq(io.a, io.b) @[IfExample.scala 19:13]
    when _T : @[IfExample.scala 19:23]
      node _io_y_T = add(io.a, io.b) @[IfExample.scala 20:18]
      node _io_y_T_1 = tail(_io_y_T, 1) @[IfExample.scala 20:18]
      io.y <= _io_y_T_1 @[IfExample.scala 20:10]
    else :
      node _io_y_T_2 = sub(io.a, io.b) @[IfExample.scala 23:18]
      node _io_y_T_3 = tail(_io_y_T_2, 1) @[IfExample.scala 23:18]
      io.y <= _io_y_T_3 @[IfExample.scala 23:10]
 */
