package bin.morelogic

import arc.func.Cons
import arc.scene.ui.Button
import arc.scene.ui.TextField
import arc.scene.ui.layout.Cell
import arc.scene.ui.layout.Table
import arc.util.Strings
import bin.morelogic.statement.StringOp
import mindustry.logic.LAssembler
import mindustry.logic.LCategory
import mindustry.logic.LExecutor.LInstruction
import mindustry.logic.LStatement
import kotlin.math.min

/**
 * 字符串拼接
 * TODO: 字符串的切片，插入，拆除，判断
 * @author bin
 */
class PrintBufferStatement : LStatement() {
	var op = StringOp.GetLength
	var args = arrayOf("dest", "a", "b", "c")

	override fun build(table: Table) = op.run { invoke(table) }

	public override fun field(table: Table?, value: String?, setter: Cons<String>?): Cell<TextField> =
		super.field(table, value, setter)

	public override fun row(table: Table?) = super.row(table)
	public override fun <T : Any?> showSelect(b: Button?, values: Array<out T>?, current: T, getter: Cons<T>?) =
		super.showSelect(b, values, current, getter, 4) { it.size(144f, 40f) }

	override fun build(builder: LAssembler): LInstruction = op(builder.vars(args))

	override fun category(): LCategory = LCategory.operation

	override fun write(builder: StringBuilder) {
		builder.append(ID)
		builder.append(" ").append(op.name)
		for (s in args) {
			builder.append(" ").append(s)
		}
	}

	override fun name(): String {
		return Strings.insertSpaces(javaClass.simpleName.replace("Statement", ""))
	}

	companion object {
		const val ID = "bin_String"
		fun read(tokens: Array<String?>): LStatement {
			return PrintBufferStatement().apply {
				val length = tokens.size
				if (length > 1) op = StringOp.value(tokens[1])
				repeat(min(args.size, length - 2)) {
					args[it] = tokens[it + 2] ?: args[it]
				}
			}
		}

		@JvmStatic
		fun LAssembler.vars(symbols: Array<String>): IntArray {
			return IntArray(symbols.size) {
				`var`(symbols[it])
			}
		}

	}
}

