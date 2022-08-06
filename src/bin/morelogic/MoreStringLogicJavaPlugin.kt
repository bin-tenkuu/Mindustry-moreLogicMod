package bin.morelogic

import arc.util.Log
import mindustry.gen.LogicIO
import mindustry.logic.LAssembler
import mindustry.mod.Plugin

/**
 * @author bin
 */
class MoreStringLogicJavaPlugin : Plugin() {
	override fun init() {
		Log.info("Loading MoreStringLogicJavaPlugin.")
		LAssembler.customParsers.put(StringStatement.ID, StringStatement::read)
		LogicIO.allStatements.add(::StringStatement)
		Log.info("End Loaded MoreStringLogicJavaPlugin.")
	}
}
