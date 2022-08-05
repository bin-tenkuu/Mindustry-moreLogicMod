package bin.morelogic

import arc.util.Log
import mindustry.logic.LAssembler
import mindustry.mod.Plugin

/**
 * @author bin
 */
class MoreStringLogicJavaPlugin : Plugin() {
	override fun loadContent() {
		Log.info("Loading MoreStringLogicJavaPlugin.")
		LAssembler.customParsers.put(
			LStringStatement.AppendStringStatement.ID, LStringStatement.AppendStringStatement::read
		)
		Log.info("End Loaded MoreStringLogicJavaPlugin.")
	}
}
