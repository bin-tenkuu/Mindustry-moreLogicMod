package bin.morelogic;

import arc.func.Cons;
import arc.scene.ui.Button;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.util.Strings;
import bin.morelogic.statement.StringOp;
import mindustry.logic.LAssembler;
import mindustry.logic.LCategory;
import mindustry.logic.LExecutor;
import mindustry.logic.LStatement;

/**
 * @author bin
 * @version 1.0.0
 * @since 2024/2/14
 */
public class PrintBufferStatement extends LStatement {
    public static final String ID = "bin_String";

    public StringOp op = StringOp.GetLength;
    public String[] args = {"dest", "a", "b", "c"};

    @Override
    public Cell<TextField> field(Table table, String value, Cons<String> setter) {
        return super.field(table, value, setter);
    }

    @Override
    public void build(Table table) {
        op.invoke(this, table);
    }

    @Override
    public <T> void showSelect(Button b, T[] values, T current, Cons<T> getter) {
        super.showSelect(b, values, current, getter, 4, it -> it.size(144f, 40f));
    }

    @Override
    public LExecutor.LInstruction build(LAssembler builder) {
        return op.invoke(vars(builder, args));
    }

    @Override
    public LCategory category() {
        return LCategory.operation;
    }

    @Override
    public void write(StringBuilder builder) {
        builder.append(ID);
        builder.append(" ").append(op.name());
        for (var s : args) {
            builder.append(" ").append(s);
        }
    }

    @Override
    public String name() {
        return Strings.insertSpaces("PrintBuffer");
    }

    public static LStatement read(String[] tokens) {
        var statement = new PrintBufferStatement();
        var length = tokens.length;
        if (length > 1) {
            statement.op = StringOp.value(tokens[1]);
        }
        var args = statement.args;
        for (int it = 0, size = Math.min(args.length, length - 2); it < size; it++) {
            String token = tokens[it + 2];
            if (token != null) {
                args[it] = token;
            }
        }
        return statement;
    }

    public static int[] vars(LAssembler builder, String[] symbols) {
        var arr = new int[symbols.length];
        for (int it = 0, size = symbols.length; it < size; it++) {
            arr[it] = builder.var(symbols[it]);
        }
        return arr;
    }
}
