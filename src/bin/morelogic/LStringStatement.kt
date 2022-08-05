package bin.morelogic

import arc.scene.ui.layout.Table
import mindustry.logic.*
import mindustry.logic.LExecutor.LInstruction
import mindustry.logic.LExecutor.Var

/**
 * @author bin
 */
object LStringStatement {
	@JvmStatic
	fun Var.str(): String {
		return if (isobj) {
			val objval = objval
			if (objval is String) objval else objval.toString()
		}
		else {
			numval.toString()
		}
	}

	@JvmStatic
	fun StringBuilder.appendBySpace(vararg strings: String) {
		for (s in strings) {
			append(" ").append(s)
		}
	}

	class AppendStringStatement : LStatement() {
		var dest = "result"
		var a = "a"
		var b = "b"
		override fun hidden(): Boolean {
			return false
		}

		override fun build(table: Table) {
			table.clearChildren()
			field(table, dest) { dest = it }
			table.add(" = ")
			row(table)
			field(table, a) { a = it }
			table.add(" append ")
			field(table, b) { b = it }
		}

		override fun build(builder: LAssembler): LInstruction {
			return AppendInstruction(builder.`var`(a), builder.`var`(b), builder.`var`(dest))
		}

		override fun category(): LCategory {
			return LCategory.operation
		}

		override fun write(builder: StringBuilder) {
			builder.append(ID)
			builder.appendBySpace(
				dest, a, b
			)
		}

		private class AppendInstruction(var a: Int, var b: Int, var dest: Int) : LInstruction {
			override fun run(exec: LExecutor) {
				val v = exec.`var`(a).str()
				val v2 = exec.`var`(b).str()
				exec.setobj(dest, v + v2)
			}
		}

		companion object {
			const val ID = "bin_AppendString"
			fun read(tokens: Array<String>): AppendStringStatement {
				return AppendStringStatement().apply {
					val length = tokens.size
					if (length > 1) dest = tokens[1]
					if (length > 2) a = tokens[2]
					if (length > 3) b = tokens[3]
				}
			}
		}
	}
}
