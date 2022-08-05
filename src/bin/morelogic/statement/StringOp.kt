@file:JvmName("StringOp")

package bin.morelogic.statement

import mindustry.logic.LExecutor
import mindustry.logic.LExecutor.LInstruction
import org.intellij.lang.annotations.MagicConstant

enum class StringOp(
	val symbol: String,
	@MagicConstant(intValues = [1, 2])
	val args: Int,
) : (IntArray) -> Op {
	/**
	 * 长度
	 */
	Length("Length", 2) {
		override fun invoke(args: IntArray): Op = LengthI(args)
		private inner class LengthI(args: IntArray) : Op(args) {
			override fun run(exec: LExecutor) {
				val v = exec.`var`(args[1]).str()
				exec.setnum(args[0], v.length.toDouble())
			}
		}
	},

	/**
	 * 拼接
	 */
	Append("Append", 3) {
		override fun invoke(args: IntArray): Op = AppendI(args)
		private inner class AppendI(args: IntArray) : Op(args) {
			override fun run(exec: LExecutor) {
				val v = exec.`var`(args[1]).str()
				val v2 = exec.`var`(args[2]).str()
				exec.setobj(args[0], v + v2)
			}
		}
	},

	;


	abstract override fun invoke(args: IntArray): Op
	override fun toString(): String {
		return symbol
	}

	companion object {
		@JvmStatic
		private fun LExecutor.Var.str(): String {
			return if (isobj) {
				val objval = objval
				if (objval is String) objval else objval.toString()
			}
			else {
				numval.toString()
			}
		}

		val all = StringOp.values()
		fun value(s: String): StringOp {
			for (stringOp in all) {
				if (stringOp.name.equals(s, true)) {
					return stringOp
				}
			}
			return Append
		}
	}
}

abstract class Op(val args: IntArray) : LInstruction
