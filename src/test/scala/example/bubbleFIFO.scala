package example

import chisel3._
import chisel3.util._

class WriterIO(size: Int) extends Bundle {
  val write = Input(Bool())
  val full = Output(Bool())
  val din = Input(UInt(size.W))
}

class ReaderIO(size: Int) extends Bundle {
  val read = Input(Bool())
  val empty = Output(Bool())
  val dout = Output(UInt(size.W))
}

/*module FifoRegister :
  input clock : Clock
  input reset : UInt<1>
  output io : { flip io_i_2 : UInt<1>,  io_o_3 : UInt<1>, flip io_i_4 : UInt<1>, flip io_i_5 : UInt<1>,  io_o_6 : UInt<1>,  io_o_7 : UInt<1>}

  reg r_8 : UInt<1>, clock with :
          reset => (reset, UInt<1>("h0"))
  reg r_9 : UInt<1>, clock with :
          reset => (reset, UInt<1>("h1"))
  node g_1 = eq(r_8,UInt<1>("h0"))
  when g_1 :
    node g_3 = UInt<1>("h1")
    r_8 <= g_3
  else :
    node g_4 = UInt<1>("h0")
    r_8 <= g_4*/

class FifoRegister(size: Int) extends Module {
  val io = IO(new Bundle {
    val enq = new WriterIO(size)
    val deq = new ReaderIO(size)
  })

  val empty :: full :: Nil = Enum(2) // 0.uint,1.uint
//  val ee2 = Enum(200) //UInt<8>("hc7")
//  val full = Enum(200).last
//  val (empty, full) = (0.U, 1.U)
  val stateReg = RegInit(empty)
  val dataReg = RegInit(0.U(size.W))

  when(stateReg === empty) {
    when(io.enq.write) {
      stateReg := full
      dataReg := io.enq.din
    }
  }.elsewhen(stateReg === full) {
    when(io.deq.read) {
      stateReg := empty
      dataReg := 0.U // just to better see empty slots in the waveform
    }
  }.otherwise {
    // There should not be an otherwise state
  }

  io.enq.full := (stateReg === full)
  io.deq.empty := (stateReg === empty)
  io.deq.dout := dataReg
}

class testFifoRegister extends utils.genFirrtl(() => new FifoRegister(5))

/*new FifoRegister(5) :
circuit FifoRegister :
  module FifoRegister :
    input clock : Clock
    input reset : UInt<1>
    output io : { enq : { flip write : UInt<1>, full : UInt<1>, flip din : UInt<5>}, deq : { flip read : UInt<1>, empty : UInt<1>, dout : UInt<5>}}

    reg stateReg : UInt<1>, clock with :
      reset => (reset, UInt<1>("h0")) @[bubbleFIFO.scala 73:25]
    reg dataReg : UInt<5>, clock with :
      reset => (reset, UInt<5>("h0")) @[bubbleFIFO.scala 74:24]
    node _T = eq(stateReg, UInt<1>("h0")) @[bubbleFIFO.scala 76:17]
    when _T : @[bubbleFIFO.scala 76:28]
      when io.enq.write : @[bubbleFIFO.scala 77:24]
        stateReg <= UInt<1>("h1") @[bubbleFIFO.scala 78:16]
        dataReg <= io.enq.din @[bubbleFIFO.scala 79:15]
    else :
      node _T_1 = eq(stateReg, UInt<1>("h1")) @[bubbleFIFO.scala 81:23]
      when _T_1 : @[bubbleFIFO.scala 81:33]
        when io.deq.read : @[bubbleFIFO.scala 82:23]
          stateReg <= UInt<1>("h0") @[bubbleFIFO.scala 83:16]
          dataReg <= UInt<1>("h0") @[bubbleFIFO.scala 84:15]
      else :
        skip
    node _io_enq_full_T = eq(stateReg, UInt<1>("h1")) @[bubbleFIFO.scala 90:28]
    io.enq.full <= _io_enq_full_T @[bubbleFIFO.scala 90:15]
    node _io_deq_empty_T = eq(stateReg, UInt<1>("h0")) @[bubbleFIFO.scala 91:29]
    io.deq.empty <= _io_deq_empty_T @[bubbleFIFO.scala 91:16]
    io.deq.dout <= dataReg @[bubbleFIFO.scala 92:15]*/

/** This is a bubble FIFO. */
class BubbleFifo(size: Int, depth: Int) extends Module {
  val io = IO(new Bundle {
    val enq = new WriterIO(size)
    val deq = new ReaderIO(size)
  })

  val buffers = Array.fill(depth) { Module(new FifoRegister(size)) }
  for (i <- 0 until depth - 1) {
    buffers(i + 1).io.enq.din := buffers(i).io.deq.dout
    buffers(i + 1).io.enq.write := ~buffers(i).io.deq.empty
    buffers(i).io.deq.read := ~buffers(i + 1).io.enq.full
  }

  io.enq <> buffers(0).io.enq
  io.deq <> buffers(depth - 1).io.deq
}

class testBubbleFifo
    extends utils.genFirrtl(() => new BubbleFifo(1, 2)) //vlg ok
