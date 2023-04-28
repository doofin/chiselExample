package example

import firrtl._
import firrtl.ir._

object ftlExample {
  val adder = """circuit adder1 :
            |  module adder1 :
            |    input clock : Clock
            |    input reset : UInt<1>
            |    output io : { flip a : UInt<16>, flip b : UInt<16>, y : UInt<16>}
            |
            |    node _io_y_T = add(io.a, io.b) @[adder.scala 12:16]
            |    node _io_y_T_1 = tail(_io_y_T, 1) @[adder.scala 12:16]
            |    io.y <= _io_y_T_1 @[adder.scala 12:8]
            |""".stripMargin

//  simplified
  val adderSimp =
    """circuit adder1 :
      |  module adder1 :
      |    input clock : Clock
      |    input reset : UInt<1>
      |    output io : { flip a : UInt<16>, flip b : UInt<16>, y : UInt<16>}
      |
      |    node _io_y_T = add(io.a, io.b) @[adder.scala 12:16]
      |    io.y <= _io_y_T @[adder.scala 12:8]
      |""".stripMargin

  val adderCirc=""
}

/*Circuit(
  info = ,
  modules = List(
    Module(
      info = ,
      name = "adder1",
      ports = List(
        Port(info = , name = "clock", direction = Input, tpe = ClockType),
        Port(info = , name = "reset", direction = Input, tpe = UIntType(width = IntWidth( = 1))),
        Port(
          info = ,
          name = "io",
          direction = Output,
          tpe = BundleType(
            fields = List(
              Field(name = "a", flip = Flip, tpe = UIntType(width = IntWidth( = 16))),
              Field(name = "b", flip = Flip, tpe = UIntType(width = IntWidth( = 16))),
              Field(name = "y", flip = Default, tpe = UIntType(width = IntWidth( = 16)))
            )
          )
        )
      ),
      body = Block(
        stmts = List(
          DefNode(
            info = FileInfo(escaped = "adder.scala 12:16"),
            name = "_io_y_T",
            value = DoPrim(
              op = add,
              args = List(
                SubField(
                  expr = Reference(
                    name = "io",
                    tpe = UnknownType,
                    kind = UnknownKind,
                    flow = UnknownFlow
                  ),
                  name = "a",
                  tpe = UnknownType,
                  flow = UnknownFlow
                ),
                SubField(
                  expr = Reference(
                    name = "io",
                    tpe = UnknownType,
                    kind = UnknownKind,
                    flow = UnknownFlow
                  ),
                  name = "b",
                  tpe = UnknownType,
                  flow = UnknownFlow
                )
              ),
              consts = List(),
              tpe = UnknownType
            )
          ),
          Connect(
            info = FileInfo(escaped = "adder.scala 12:8"),
            loc = SubField(
              expr = Reference(
                name = "io",
                tpe = UnknownType,
                kind = UnknownKind,
                flow = UnknownFlow
              ),
              name = "y",
              tpe = UnknownType,
              flow = UnknownFlow
            ),
            expr = Reference(
              name = "_io_y_T",
              tpe = UnknownType,
              kind = UnknownKind,
              flow = UnknownFlow
            )
          )
        )
      )
    )
  ),
  main = "adder1"
)*/