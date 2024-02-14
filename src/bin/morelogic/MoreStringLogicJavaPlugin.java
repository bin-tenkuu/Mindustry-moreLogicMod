package bin.morelogic;

import arc.util.Log;
import mindustry.gen.LogicIO;
import mindustry.logic.LAssembler;
import mindustry.mod.Plugin;

/**
 * @author bin
 * @version 1.0.0
 * @since 2024/2/14
 */
@SuppressWarnings("unused")
public class MoreStringLogicJavaPlugin extends Plugin {
    @Override
    public void init() {
		Log.info("Loading MoreStringLogicJavaPlugin.");
		LAssembler.customParsers.put(PrintBufferStatement.ID, PrintBufferStatement::read);
		LogicIO.allStatements.add(PrintBufferStatement::new);
		Log.info("End Loaded MoreStringLogicJavaPlugin.");
    }
}
