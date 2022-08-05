package bin.morelogic;

import arc.util.Log;
import mindustry.logic.LAssembler;
import mindustry.mod.Plugin;

import static bin.morelogic.LStringStatement.AppendStringStatement;

/**
 * @author bin
 */
public class MoreStringLogicJavaPlugin extends Plugin {

    @Override
    public void loadContent() {
        Log.info("Loading MoreStringLogicJavaPlugin.");
        LAssembler.customParsers.put(AppendStringStatement.ID, AppendStringStatement::read);
        Log.info("End Loaded MoreStringLogicJavaPlugin.");
    }

}
