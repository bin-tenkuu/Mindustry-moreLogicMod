package bin.morelogic;

import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import mindustry.graphics.Pal;
import mindustry.logic.*;
import mindustry.logic.LExecutor.LInstruction;
import mindustry.logic.LExecutor.Var;

/**
 * @author bin
 */
@SuppressWarnings({"AlibabaUndefineMagicConstant", "AlibabaClassNamingShouldBeCamel"})
public class LStringStatement {

    public static class AppendStringStatement extends LStatement {
        public static final String ID = "bin_AppendString";
        public String dest = "result", a = "a", b = "b";

        public static AppendStringStatement read(String[] tokens) {
            final var result = new AppendStringStatement();
            final var length = tokens.length;
            if (length > 1) {
                result.dest = tokens[1];
            }
            if (length > 2) {
                result.a = tokens[2];
            }
            if (length > 3) {
                result.b = tokens[3];
            }
            return result;
        }

        @Override
        public void build(Table table) {
            table.clearChildren();
            field(table, dest, str -> dest = str);
            table.add(" = ");
            row(table);
            field(table, a, str -> a = str);
            table.add(" append ");
            field(table, b, str -> b = str);
        }

        @Override
        public LInstruction build(LAssembler builder) {
            return new AppendInstruction(builder.var(a), builder.var(b), builder.var(dest));
        }

        @Override
        public Color color() {
            return Pal.logicOperations;
        }

        @Override
        public void write(final StringBuilder builder) {
            builder.append(ID);
            append(builder,
                    dest,
                    a,
                    b
            );
        }

        private static class AppendInstruction implements LInstruction {
            public int a, b, dest;

            public AppendInstruction(int a, int b, int dest) {
                this.a = a;
                this.b = b;
                this.dest = dest;
            }

            @Override
            public void run(LExecutor exec) {
                String v = str(exec.var(a)), v2 = str(exec.var(b));
                exec.setobj(dest, v + v2);
            }
        }

    }

    public static String str(Var v) {
        if (v.isobj) {
            final var objval = v.objval;
            return objval instanceof String ? (String) objval : String.valueOf(objval);
        } else {
            return Double.toString(v.numval);
        }
    }

    public static void append(StringBuilder builder, String... strings) {
        for (final String s : strings) {
            builder.append(" ").append(s);
        }
    }
}
