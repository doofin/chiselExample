package example

import chisel3._

class reg1 extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(16.W))
    val b = Input(UInt(16.W))
//    val c = Input(UInt(16.W))
    val y = Output(UInt(16.W))
  })

  val mReg = RegInit(0.U(16.W))
//  io.y := io.a + io.b + io.c
//  io.y := io.a + io.b
  io.y := mReg

  val wire1 = Wire(UInt(16.W))
  wire1 := 10.U
}

class testreg extends utils.genFirrtl(() => new reg1)

/*firrtl :
circuit reg1 :
  module reg1 :
    input clock : Clock
    input reset : UInt<1>
    output io : { flip a : UInt<16>, flip b : UInt<16>, y : UInt<16>}

    reg mReg : UInt<16>, clock with :
      reset => (reset, UInt<16>("h0")) @[regWire.scala 13:21]
    io.y <= mReg @[regWire.scala 16:8]
    wire wire1 : UInt<16> @[regWire.scala 18:19]
    wire1 <= UInt<4>("ha") @[regWire.scala 19:9]
*/

/* normal form
y=a+b+c

x0=a+b
y=x0+c
 */
