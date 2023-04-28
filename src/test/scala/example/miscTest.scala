package example

import chisel3._
import chisel3.util._

object miscTest {
  class FifoRegisterSimp1(size: Int) extends Module {
    val io = IO(new Bundle {
      val enq = new WriterIO(size)
      val deq = new ReaderIO(size)
    })

    val empty :: full :: Nil = Enum(2) // 0.uint,1.uint
    val stateReg = RegInit(empty)
    val dataReg = RegInit(0.U(size.W))

    when(stateReg === empty) {
      stateReg := full
    }.otherwise {
      stateReg := empty
    }

    io.enq.full := (stateReg === full)
    io.deq.empty := (stateReg === empty)
    io.deq.dout := dataReg
  }

  class testFifoSimp extends utils.genVerilog(() => new FifoRegisterSimp1(1))

}
