package bin.morelogic

import arc.scene.ui.Button
import arc.scene.ui.layout.Table
import bin.morelogic.statement.StringOp
import mindustry.logic.*
import mindustry.logic.LExecutor.LInstruction
import mindustry.ui.Styles

/**
 * TODO: 字符串的切片，插入，拆除，判断
 * @author bin
 */
object LStringStatement {

	@JvmStatic
	fun StringBuilder.appendBySpace(vararg strings: String) {
		for (s in strings) {
			append(" ").append(s)
		}
	}

	fun LAssembler.vars(vararg symbols: String): IntArray {
		return IntArray(symbols.size) {
			`var`(symbols[it])
		}
	}

	/**
	 * 字符串拼接
	 */
	class AppendStringStatement : LStatement() {
		private var op = StringOp.Length
		private var dest = "ab"
		private var a = "a"
		private var b = "b"
		private var c = "c"
		override fun build(table: Table) = rebuild(table)

		private fun rebuild(table: Table) {
			table.clearChildren()
			field(table, dest) { dest = it }
			table.add(" = ")
			if (op.args == 1) {
				opButton(table, table)
				field(table, a) { a = it }
				return
			}
			row(table)
			if (LCanvas.useRows()) {
				table.left()
				table.row()
				table.table { c: Table ->
					c.color.set(category().color)
					c.left()
					funcs(c, table)
				}.colspan(2).left()
				return
			}
			funcs(table, table)
		}

		private fun funcs(table: Table, parent: Table) {
			opButton(table, parent)
			field(table, a) { a = it }
			field(table, b) { b = it }
		}

		private fun opButton(table: Table, parent: Table) {
			table.button({ b: Button ->
				b.label { op.symbol }
				b.clicked {
					showSelect(b, StringOp.all, op) { o: StringOp ->
						op = o
						rebuild(parent)
					}
				}
			}, Styles.logict) {}.size(64f, 40f).pad(4f).color(table.color)
		}

		override fun build(builder: LAssembler): LInstruction {
			return op(builder.vars(dest, a, b))
		}

		override fun category(): LCategory = LCategory.operation

		override fun write(builder: StringBuilder) {
			builder.append(ID)
			builder.appendBySpace(op.name, dest, a, b)
		}

		companion object {
			const val ID = "bin_String"
			fun read(tokens: Array<String>): LStatement {
				return AppendStringStatement().apply {
					val length = tokens.size
					var i = 0
					if (length > 0) op = StringOp.value(tokens[i++])
					if (length > i) dest = tokens[i++]
					if (length > i) a = tokens[i++]
					if (length > i) b = tokens[i]
				}
			}
		}
	}
}
