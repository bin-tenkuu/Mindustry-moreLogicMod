package bin.morelogic.statement;

import arc.scene.ui.Button;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import bin.morelogic.PrintBufferStatement;
import mindustry.content.Blocks;
import mindustry.ctype.Content;
import mindustry.ctype.MappableContent;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.logic.LExecutor;
import mindustry.ui.Styles;
import mindustry.world.blocks.logic.MessageBlock;

/**
 * @author bin
 * @version 1.0.0
 * @since 2024/2/14
 */
public enum StringOp {
    ReadFrom {
        @Override
        public LExecutor.LInstruction invoke(int[] args) {
            return new OpI(args);
        }

        @Override
        public void buttonTooltip(Cell<Button> cell) {
            cell.tooltip("从信息板读取字符到打印缓存，否则与 Print 作用相同");
        }

        @Override
        public void invoke(PrintBufferStatement statement, Table table) {
            table.clearChildren();
            opButton(statement, table, table);
            statement.field(table, statement.args[0], it -> statement.args[0] = it);
        }

        private final class OpI implements LExecutor.LInstruction {
            private final int[] args;

            private OpI(int[] args) {
                this.args = args;
            }

            @Override
            public void run(LExecutor exec) {
                final var d = exec.building(args[0]);
                if (d instanceof MessageBlock.MessageBuild && (d.team == exec.team || exec.privileged)) {
                    exec.textBuffer.setLength(0);
                    exec.textBuffer.append(((MessageBlock.MessageBuild) d).message);
                    return;
                }
                final var str = str(exec.var(args[0]));
                var size = Math.min(LExecutor.maxTextBuffer, str.length());
                exec.textBuffer.append(str, 0, size);
            }
        }
    },
    GetLength {
        @Override
        public LExecutor.LInstruction invoke(int[] args) {
            return new OpI(args);
        }

        @Override
        public void buttonTooltip(Cell<Button> cell) {
            cell.tooltip("获取打印缓存长度");
        }

        @Override
        public void invoke(PrintBufferStatement statement, Table table) {
            table.clearChildren();
            statement.field(table, statement.args[0], it -> statement.args[0] = it);
            table.add(" = ");
            opButton(statement, table, table);
        }

        private final class OpI implements LExecutor.LInstruction {
            private final int[] args;

            private OpI(int[] args) {
                this.args = args;
            }

            @Override
            public void run(LExecutor exec) {
                exec.setnum(args[0], exec.textBuffer.length());
            }

        }
    },
    SetLength {
        @Override
        public LExecutor.LInstruction invoke(int[] args) {
            return new OpI(args);
        }

        @Override
        public void buttonTooltip(Cell<Button> cell) {
            cell.tooltip("将打印缓存截取到固定长度");
        }

        @Override
        public void invoke(PrintBufferStatement statement, Table table) {
            table.clearChildren();
            opButton(statement, table, table);
            statement.field(table, statement.args[0], it -> statement.args[0] = it);
        }

        private final class OpI implements LExecutor.LInstruction {
            private final int[] args;

            private OpI(int[] args) {
                this.args = args;
            }

            @Override
            public void run(LExecutor exec) {
                var length = exec.numi(args[0]);
                StringBuilder builder = exec.textBuffer;
                if (length < 0) {
                    builder.setLength(0);
                } else if (length < builder.length()) {
                    builder.setLength(length);
                }
            }

        }
    },
    Clear {
        @Override
        public LExecutor.LInstruction invoke(int[] args) {
            return new OpI();
        }

        @Override
        public void buttonTooltip(Cell<Button> cell) {
            cell.tooltip("清除打印缓存");
        }

        @Override
        public void invoke(PrintBufferStatement statement, Table table) {
            table.clearChildren();
            opButton(statement, table, table);
        }

        private final class OpI implements LExecutor.LInstruction {

            @Override
            public void run(LExecutor exec) {
                exec.textBuffer.setLength(0);
            }

        }
    },
    IndexOf {
        @Override
        public LExecutor.LInstruction invoke(int[] args) {
            return new OpI(args);
        }

        @Override
        public void buttonTooltip(Cell<Button> cell) {
            cell.tooltip("寻找打印缓存中某字符串的位置");
        }

        @Override
        public void invoke(PrintBufferStatement statement, Table table) {
            table.clearChildren();
            statement.field(table, statement.args[0], it -> statement.args[0] = it);
            table.add(" = ");
            opButton(statement, table, table);
            statement.field(table, statement.args[1], it -> statement.args[1] = it);
        }

        private final class OpI implements LExecutor.LInstruction {
            private final int[] args;

            private OpI(int[] args) {
                this.args = args;
            }

            @Override
            public void run(LExecutor exec) {
                var str = str(exec.var(args[1])).toString();
                exec.setnum(args[0], exec.textBuffer.indexOf(str));
            }

        }
    },
    SubString {
        @Override
        public LExecutor.LInstruction invoke(int[] args) {
            return new OpI(args);
        }

        @Override
        public void buttonTooltip(Cell<Button> cell) {
            cell.tooltip("截取打印缓存中的字符串，第二个参数可为负数");
        }

        @Override
        public void invoke(PrintBufferStatement statement, Table table) {
            table.clearChildren();
            opButton(statement, table, table);
            statement.field(table, statement.args[0], it -> statement.args[0] = it);
            statement.field(table, statement.args[1], it -> statement.args[1] = it);
        }

        private final class OpI implements LExecutor.LInstruction {
            private final int[] args;

            private OpI(int[] args) {
                this.args = args;
            }

            @Override
            public void run(LExecutor exec) {
                final var buffer = exec.textBuffer;
                var start = exec.numi(args[0]);
                var length = buffer.length();
                var end = exec.numi(args[1]);
                if (start < end && end < length) {
                    buffer.setLength(end);
                }
                if (start > 0 && start < length) {
                    buffer.delete(0, start);
                }
            }

        }
    },
    CodePointAt {
        @Override
        public LExecutor.LInstruction invoke(int[] args) {
            return new OpI(args);
        }

        @Override
        public void buttonTooltip(Cell<Button> cell) {
            cell.tooltip("获取打印缓存中某一位字符的码点");
        }

        @Override
        public void invoke(PrintBufferStatement statement, Table table) {
            table.clearChildren();
            statement.field(table, statement.args[0], it -> statement.args[0] = it);
            table.add(" = ");
            opButton(statement, table, table);
            statement.field(table, statement.args[1], it -> statement.args[1] = it);
        }

        private final class OpI implements LExecutor.LInstruction {
            private final int[] args;

            private OpI(int[] args) {
                this.args = args;
            }

            @Override
            public void run(LExecutor exec) {
                final var buffer = exec.textBuffer;
                if (buffer.length() == 0) {
                    exec.setnum(args[0], 0.0);
                    return;
                }
                var start = exec.numi(args[1]);
                start = Math.min(Math.max(0, start), buffer.length() - 1);
                exec.setnum(args[0], buffer.codePointAt(start));
            }

        }
    },
    AddCodePoint {
        @Override
        public LExecutor.LInstruction invoke(int[] args) {
            return new OpI(args);
        }

        @Override
        public void buttonTooltip(Cell<Button> cell) {
            cell.tooltip("将码点转换为字符添加到打印缓存");
        }

        @Override
        public void invoke(PrintBufferStatement statement, Table table) {
            table.clearChildren();
            opButton(statement, table, table);
            statement.field(table, statement.args[0], it -> statement.args[0] = it);
        }

        private final class OpI implements LExecutor.LInstruction {
            private final int[] args;

            private OpI(int[] args) {
                this.args = args;
            }

            @Override
            public void run(LExecutor exec) {
                final var buffer = exec.textBuffer;
                var start = exec.numi(args[0]);
                buffer.appendCodePoint(start);
            }

        }
    },
    ;

    public abstract LExecutor.LInstruction invoke(int[] args);

    public abstract void invoke(PrintBufferStatement statement, Table table);

    public abstract void buttonTooltip(Cell<Button> cell);

    protected final void opButton(PrintBufferStatement statement, Table table, Table parent) {
        var button = table.button((Button b) -> {
            b.add(statement.op.name());
            b.clicked(() -> {
                statement.showSelect(b, all, statement.op, o -> {
                    statement.op = o;
                    statement.build(parent);
                });
            });
        }, Styles.logict, () -> {
        });
        button.size(128f, 40f);
        button.pad(4f);
        button.color(table.color);
        buttonTooltip(button);
    }

    public static final StringOp[] all = StringOp.values();

    public static StringOp value(String s) {
        if (s == null) {
            return ReadFrom;
        }
        for (var stringOp : all) {
            if (stringOp.name().equalsIgnoreCase(s)) {
                return stringOp;
            }
        }
        return ReadFrom;
    }

    private static CharSequence str(LExecutor.Var varObj) {
        if (!varObj.isobj) {
            if (((varObj.numval - (long) varObj.numval) < 0.00001)) {
                return String.valueOf((long) varObj.numval);
            } else {
                return String.valueOf(varObj.numval);
            }
        } else {
            Object obj = varObj.objval;
            if (obj == null) {
                return "null";
            } else if (obj instanceof CharSequence) {
                return (CharSequence) obj;
            } else if (obj == Blocks.stoneWall) {
                return "solid"; //special alias
            } else if (obj instanceof MappableContent) {
                return ((MappableContent) obj).name;
            } else if (obj instanceof Content) {
                return "[content]";
            } else if (obj instanceof Building) {
                return ((Building) obj).block.name;
            } else if (obj instanceof Unit) {
                return ((Unit) obj).type.name;
            } else if (obj instanceof Enum<?>) {
                return ((Enum<?>) obj).name();
            } else if (obj instanceof Team) {
                return ((Team) obj).name;
            } else {
                return "[object]";
            }
        }
    }

}
