@file:JvmName("StringOp")

package bin.morelogic.statement

import arc.scene.ui.Button
import arc.scene.ui.layout.Cell
import arc.scene.ui.layout.Table
import bin.morelogic.PrintBufferStatement
import mindustry.content.Blocks
import mindustry.ctype.Content
import mindustry.ctype.MappableContent
import mindustry.game.Team
import mindustry.gen.Building
import mindustry.logic.LExecutor
import mindustry.logic.LExecutor.LInstruction
import mindustry.ui.Styles
import mindustry.world.blocks.logic.MessageBlock

enum class StringOp(
	private val symbol: String,
) : (IntArray) -> Op {
	ReadFrom("ReadFrom") {
		override fun invoke(args: IntArray): Op = OpI(args)
		override fun buttonTooltip(cell: Cell<Button>) {
			cell.tooltip("从信息板读取字符到打印缓存，否则与 Print 作用相同")
		}

		override fun PrintBufferStatement.invoke(table: Table) {
			table.clearChildren()
			opButton(table, table)
			field(table, args[0]) {
				args[0] = it
			}
		}

		private inner class OpI(args: IntArray) : Op(args) {
			override fun run(exec: LExecutor) {
				val d = exec.building(args[0])
				if (d is MessageBlock.MessageBuild && (d.team == exec.team || exec.privileged)) {
					exec.textBuffer.setLength(0)
					exec.textBuffer.append(d.message)
					return
				}
				val str = exec.`var`(args[0]).str()
				exec.textBuffer.append(str, 0, LExecutor.maxTextBuffer.coerceAtMost(str.length))
			}
		}
	},
	GetLength("GetLength") {
		override fun invoke(args: IntArray): Op = OpI(args)
		override fun buttonTooltip(cell: Cell<Button>) {
			cell.tooltip("获取打印缓存长度")
		}

		override fun PrintBufferStatement.invoke(table: Table) {
			table.clearChildren()
			field(table, args[0]) {
				args[0] = it
			}
			table.add(" = ")
			opButton(table, table)
		}

		private inner class OpI(args: IntArray) : Op(args) {
			override fun run(exec: LExecutor) {
				exec.setnum(args[0], exec.textBuffer.length.toDouble())
			}
		}
	},
	Clear("Clear") {
		override fun invoke(args: IntArray): Op = OpI(args)
		override fun buttonTooltip(cell: Cell<Button>) {
			cell.tooltip("清除打印缓存")
		}

		override fun PrintBufferStatement.invoke(table: Table) {
			table.clearChildren()
			opButton(table, table)
		}

		private inner class OpI(args: IntArray) : Op(args) {
			override fun run(exec: LExecutor) {
				exec.textBuffer.clear()
			}
		}
	},
	SetLength("SetLength") {
		override fun invoke(args: IntArray): Op = OpI(args)
		override fun buttonTooltip(cell: Cell<Button>) {
			cell.tooltip("将打印缓存截取到固定长度")
		}

		override fun PrintBufferStatement.invoke(table: Table) {
			table.clearChildren()
			opButton(table, table)
			field(table, args[0]) {
				args[0] = it
			}
		}

		private inner class OpI(args: IntArray) : Op(args) {
			override fun run(exec: LExecutor) {
				val length = exec.numi(args[0]).coerceIn(0, exec.textBuffer.length)
				exec.textBuffer.setLength(length)
			}
		}
	},
	IndexOf("IndexOf") {
		override fun invoke(args: IntArray): Op = OpI(args)
		override fun buttonTooltip(cell: Cell<Button>) {
			cell.tooltip("寻找打印缓存中某字符串的位置")
		}

		override fun PrintBufferStatement.invoke(table: Table) {
			table.clearChildren()
			field(table, args[0]) {
				args[0] = it
			}
			table.add(" = ")
			opButton(table, table)
			field(table, args[1]) {
				args[1] = it
			}
		}

		private inner class OpI(args: IntArray) : Op(args) {
			override fun run(exec: LExecutor) {
				val str = exec.`var`(args[1]).str().toString()
				exec.setnum(args[0], exec.textBuffer.indexOf(str).toDouble())
			}
		}
	},
	SubString("SubString") {
		override fun invoke(args: IntArray): Op = OpI(args)
		override fun buttonTooltip(cell: Cell<Button>) {
			cell.tooltip("截取打印缓存中的字符串")
		}

		override fun PrintBufferStatement.invoke(table: Table) {
			table.clearChildren()
			field(table, args[0]) {
				args[0] = it
			}
			table.add(" = ")
			opButton(table, table)
			field(table, args[1]) {
				args[1] = it
			}
			field(table, args[2]) {
				args[2] = it
			}
		}

		private inner class OpI(args: IntArray) : Op(args) {
			override fun run(exec: LExecutor) {
				val buffer = exec.textBuffer
				val start = exec.numi(args[1]).coerceIn(0, buffer.length)
				val end = exec.numi(args[2])
				exec.setobj(
					args[0],
					if (end < 0) buffer.substring(start)
					else buffer.substring(start, end.coerceIn(start, buffer.length))
				)
			}
		}
	},

	;

	abstract override fun invoke(args: IntArray): Op
	override fun toString(): String = symbol

	open fun PrintBufferStatement.invoke(table: Table) {
		table.clearChildren()
		field(table, args[0]) {
			args[0] = it
		}
		table.add(" = ")
		opButton(table, table)
	}

	open fun buttonTooltip(cell: Cell<Button>) {}

	protected fun PrintBufferStatement.opButton(table: Table, parent: Table) {
		buttonTooltip(table.button({ b: Button ->
			b.label { op.symbol }
			b.clicked {
				showSelect(b, all, op) { o: StringOp ->
					op = o
					build(parent)
				}
			}
		}, Styles.logict) {}.size(128f, 40f).pad(4f).color(table.color))
	}

	companion object {
		@JvmStatic
		private fun LExecutor.Var.str(): CharSequence = if (!isobj) when {
			((numval - numval.toLong()) < 0.00001) -> numval.toLong().toString()
			else -> numval.toString()
		}
		else when (val obj: Any? = objval) {
			null -> "null"
			is CharSequence -> obj
			(obj == Blocks.stoneWall) -> "solid"  //special alias
			is MappableContent -> obj.name
			is Content -> "[content]"
			is Building -> obj.block.name
			is mindustry.gen.Unit -> obj.type.name
			is Enum<*> -> obj.name
			is Team -> obj.name
			else -> "[object]"
		}

		val all = StringOp.values()
		fun value(s: String?): StringOp {
			if (s === null) return ReadFrom
			for (stringOp in all) {
				if (stringOp.name.equals(s, true)) {
					return stringOp
				}
			}
			return ReadFrom
		}
	}
}

abstract class Op(val args: IntArray) : LInstruction
