import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import SymbolTable.*;
import java.io.*;
import java.util.*;

import mar.*;
import util.*;

public class marCompiler {
    public static class Evaluator extends marBaseListener {
        private final ParseTreeProperty<Scope> scopes;
        private ParseTreeProperty<Boolean> hasReturn;
        private Scope currentScope;
        private final Scope globalScope;
        private final ByteArrayOutputStream byteArray;
        private final DataOutputStream byteArrayOutputStream;
        private final ArrayList<Const> constPool;
        private final ArrayList<String> opCodes;
        private final ArrayList<Const> vars;
        private final Stack<Integer> ifElsePos;
        private final Stack<Integer> whilePos;
        private boolean changedValue;
        private int typeErrors;
        private int funcPos;
        private int nLocals;
        private FunctionSymbol currentFunc;

        public Evaluator(ParseTreeProperty<Scope> scopes, ParseTreeProperty<Boolean> hasReturn, Scope global) {
            this.typeErrors = 0;
            this.funcPos = -1;
            this.nLocals = 0;
            this.byteArray = new ByteArrayOutputStream();
            this.byteArrayOutputStream = new DataOutputStream(byteArray);
            this.vars = new ArrayList<>();
            this.opCodes = new ArrayList<>();
            this.constPool = new ArrayList<>(50);
            this.ifElsePos = new Stack<>();
            this.whilePos = new Stack<>();
            this.hasReturn = hasReturn;
            this.changedValue = false;
            this.scopes = scopes;
            this.currentScope = null;
            this.currentFunc = null;
            this.globalScope = global;
            if (global.getNVars() > 0)
                this.opCodes.add("GLOBAL " + global.getNVars());
        }

        private void pushVar(Const x) {
            this.vars.add(x);
        }

        private Const popVar() {
            return this.vars.remove(this.vars.size() - 1);
        }

        public void writeData() {
            String[] inst;
            OpCode op;
            try {
                for (String string : this.opCodes) {
                    inst = string.split(" ");
                    op = OpCode.valueOf(inst[0]);
                    switch (op) {
                        case ADD -> this.byteArrayOutputStream.writeInt(OpCode.ADD.getValue());
                        case SUB -> this.byteArrayOutputStream.writeInt(OpCode.SUB.getValue());
                        case MULT -> this.byteArrayOutputStream.writeInt(OpCode.MULT.getValue());
                        case DIV -> this.byteArrayOutputStream.writeInt(OpCode.DIV.getValue());
                        case UMINUS -> this.byteArrayOutputStream.writeInt(OpCode.UMINUS.getValue());
                        case HALT -> this.byteArrayOutputStream.writeInt(OpCode.HALT.getValue());
                        case AND -> this.byteArrayOutputStream.writeInt(OpCode.AND.getValue());
                        case CONCAT -> this.byteArrayOutputStream.writeInt(OpCode.CONCAT.getValue());
                        case EQ_B -> this.byteArrayOutputStream.writeInt(OpCode.EQ_B.getValue());
                        case EQ_N -> this.byteArrayOutputStream.writeInt(OpCode.EQ_N.getValue());
                        case EQ_NIL -> this.byteArrayOutputStream.writeInt(OpCode.EQ_NIL.getValue());
                        case EQ_S -> this.byteArrayOutputStream.writeInt(OpCode.EQ_S.getValue());
                        case FALSE -> this.byteArrayOutputStream.writeInt(OpCode.FALSE.getValue());
                        case GEQ -> this.byteArrayOutputStream.writeInt(OpCode.GEQ.getValue());
                        case GT -> this.byteArrayOutputStream.writeInt(OpCode.GT.getValue());
                        case LEQ -> this.byteArrayOutputStream.writeInt(OpCode.LEQ.getValue());
                        case LT -> this.byteArrayOutputStream.writeInt(OpCode.LT.getValue());
                        case NEQ_B -> this.byteArrayOutputStream.writeInt(OpCode.NEQ_B.getValue());
                        case NEQ_N -> this.byteArrayOutputStream.writeInt(OpCode.NEQ_N.getValue());
                        case NEQ_NIL -> this.byteArrayOutputStream.writeInt(OpCode.NEQ_NIL.getValue());
                        case NEQ_S -> this.byteArrayOutputStream.writeInt(OpCode.NEQ_S.getValue());
                        case NIL -> this.byteArrayOutputStream.writeInt(OpCode.NIL.getValue());
                        case NOT -> this.byteArrayOutputStream.writeInt(OpCode.NOT.getValue());
                        case OR -> this.byteArrayOutputStream.writeInt(OpCode.OR.getValue());
                        case PRINT_B -> this.byteArrayOutputStream.writeInt(OpCode.PRINT_B.getValue());
                        case PRINT_N -> this.byteArrayOutputStream.writeInt(OpCode.PRINT_N.getValue());
                        case PRINT_NIL -> this.byteArrayOutputStream.writeInt(OpCode.PRINT_NIL.getValue());
                        case PRINT_S -> this.byteArrayOutputStream.writeInt(OpCode.PRINT_S.getValue());
                        case TRUE -> this.byteArrayOutputStream.writeInt(OpCode.TRUE.getValue());
                        case JUMP -> {
                            this.byteArrayOutputStream.writeInt(OpCode.JUMP.getValue());
                            this.byteArrayOutputStream.writeInt(Integer.parseInt(inst[1]));
                        }
                        case JUMPF -> {
                            this.byteArrayOutputStream.writeInt(OpCode.JUMPF.getValue());
                            this.byteArrayOutputStream.writeInt(Integer.parseInt(inst[1]));
                        }
                        case CONST -> {
                            this.byteArrayOutputStream.writeInt(OpCode.CONST.getValue());
                            this.byteArrayOutputStream.writeInt(Integer.parseInt(inst[1]));
                        }
                        case GLOBAL -> {
                            this.byteArrayOutputStream.writeInt(OpCode.GLOBAL.getValue());
                            this.byteArrayOutputStream.writeInt(Integer.parseInt(inst[1]));
                        }
                        case STOREG -> {
                            this.byteArrayOutputStream.writeInt(OpCode.STOREG.getValue());
                            this.byteArrayOutputStream.writeInt(Integer.parseInt(inst[1]));
                        }
                        case LOADG -> {
                            this.byteArrayOutputStream.writeInt(OpCode.LOADG.getValue());
                            this.byteArrayOutputStream.writeInt(Integer.parseInt(inst[1]));
                        }
                        case STOREL -> {
                            this.byteArrayOutputStream.writeInt(OpCode.STOREL.getValue());
                            this.byteArrayOutputStream.writeInt(Integer.parseInt(inst[1]));
                        }
                        case LOADL -> {
                            this.byteArrayOutputStream.writeInt(OpCode.LOADL.getValue());
                            this.byteArrayOutputStream.writeInt(Integer.parseInt(inst[1]));
                        }
                        case CALL -> {
                            this.byteArrayOutputStream.writeInt(OpCode.CALL.getValue());
                            this.byteArrayOutputStream.writeInt(Integer.parseInt(inst[1]));
                        }
                        case RETURN -> {
                            this.byteArrayOutputStream.writeInt(OpCode.RETURN.getValue());
                            this.byteArrayOutputStream.writeInt(Integer.parseInt(inst[1]));
                        }
                        case LOCAL -> {
                            this.byteArrayOutputStream.writeInt(OpCode.LOCAL.getValue());
                            this.byteArrayOutputStream.writeInt(Integer.parseInt(inst[1]));
                        }
                        case POP -> {
                            this.byteArrayOutputStream.writeInt(OpCode.POP.getValue());
                            this.byteArrayOutputStream.writeInt(Integer.parseInt(inst[1]));
                        }
                        default -> throw new IllegalArgumentException("Unexpected value: " + op);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public byte[] constToByteArray() {
            ByteArrayOutputStream array = new ByteArrayOutputStream();
            DataOutputStream arrayOutputStream = new DataOutputStream(array);
            try {
                arrayOutputStream.writeInt(this.constPool.size());
                for (Const const1 : constPool) {
                    if (const1.isNumber()) {
                        arrayOutputStream.writeInt(OpCode.NUMBER.getValue());
                        arrayOutputStream.writeDouble((Double) const1.getValue());
                    } else { // must be string
                        arrayOutputStream.writeInt(OpCode.STRING.getValue());
                        arrayOutputStream.writeUTF((String) const1.getValue());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return array.toByteArray();
        }

        private void printError(ParserRuleContext ctx, String message) {
            String line = ctx.getStart().getLine() + ":" + ctx.getStop().getCharPositionInLine();
            System.out.println("line: " + line + " error: " + message);
        }

        public void enterProg(marParser.ProgContext ctx) {
            this.currentScope = this.globalScope;
        }

        public void exitProg(marParser.ProgContext ctx) {
            this.opCodes.add("HALT");
        }

        public void exitPrint(marParser.PrintContext ctx) {
            String OP = "";
            Const temp = this.popVar();
            if (temp.isNumber()) {
                OP = "PRINT_N";
            } else if (temp.isString()) {
                OP = "PRINT_S";
            } else if (temp.isBool()) {
                OP = "PRINT_B";
            } else if (temp.isNil()) {
                OP = "PRINT_NIL";
            }
            this.opCodes.add(OP);
        }

        public void exitNil(marParser.NilContext ctx) {
            this.pushVar(new Const(Type.tNIL, null));
            this.opCodes.add("NIL");
        }

        public void exitBinMulDiv(marParser.BinMulDivContext ctx) {
            String OP;
            Const right = this.popVar(), left = this.popVar();
            if (right.isNumber() && left.isNumber()) {
                if (ctx.op.getType() == marParser.MULT) {
                    OP = "MULT";
                } else {
                    OP = "DIV";
                }
                opCodes.add(OP);
            } else {
                this.typeErrors++;
                this.printError(ctx, "operator " + ctx.getChild(1).getText() + " is invalid between "
                        + left.getType() + " and " + right.getType());
            }
            this.pushVar(right);
        }

        public void exitLogicOr(marParser.LogicOrContext ctx) {
            Const right = this.popVar(), left = this.popVar();
            if (right.isBool() && left.isBool()) {
                this.pushVar(new Const(Type.tBOOL, false));
                this.opCodes.add("OR");
            } else {
                this.typeErrors++;
                this.printError(ctx, "operator " + ctx.getChild(1).getText() + " is invalid between "
                        + left.getType() + " and " + right.getType());
            }
            this.pushVar(new Const(Type.tBOOL, false));
        }

        public void exitNumber(marParser.NumberContext ctx) {
            double result = Double.parseDouble(ctx.NUMBER().getText());
            Const temp = new Const(Type.tNUMBER, result);
            this.pushVar(temp);
            this.constPool.add(temp);
            this.opCodes.add("CONST " + (this.constPool.size() - 1));
        }

        public void exitInEquality(marParser.InEqualityContext ctx) {
            String OP;
            boolean NEQ = false;
            Const right = this.popVar(), left = this.popVar();
            if (ctx.op.getType() == marParser.NEQ)
                NEQ = true;
            if (right.isNumber() && left.isNumber()) {
                OP = "EQ_N";
            } else if (right.isString() && left.isString()) {
                OP = "EQ_S";
            } else if (right.isBool() && left.isBool()) {
                OP = "EQ_B";
            } else if (right.isNil() && left.isNil()) {
                OP = "EQ_NIL";
            } else {
                this.typeErrors++;
                this.printError(ctx, "operator " + ctx.getChild(1).getText() + " is invalid between "
                        + left.getType() + " and " + right.getType());
                return;
            }
            if (NEQ)
                OP = "N" + OP;
            this.pushVar(new Const(Type.tBOOL, false));
            this.opCodes.add(OP);
        }

        public void exitRelational(marParser.RelationalContext ctx) {
            String OP = null;
            Const right = this.popVar(), left = this.popVar();
            if (right.isNumber() && left.isNumber()) {
                if (ctx.op.getType() == marParser.LT) {
                    OP = "LT";
                } else if (ctx.op.getType() == marParser.GT) {
                    OP = "GT";
                } else if (ctx.op.getType() == marParser.LEQ) {
                    OP = "LEQ";
                } else if (ctx.op.getType() == marParser.GEQ) {
                    OP = "GEQ";
                }
            } else {
                this.typeErrors++;
                this.printError(ctx, "operator " + ctx.getChild(1).getText() + " is invalid between "
                        + left.getType() + " and " + right.getType());
                return;
            }
            this.pushVar(new Const(Type.tBOOL, false));
            this.opCodes.add(OP);
        }

        public void exitString(marParser.StringContext ctx) {
            String result = ctx.STRING().getText();
            Const temp = new Const(Type.tSTRING, result);
            this.pushVar(temp);
            this.constPool.add(temp);
            this.opCodes.add("CONST " + (this.constPool.size() - 1));
        }

        public void exitBoolean(marParser.BooleanContext ctx) {
            Boolean result = Boolean.parseBoolean(ctx.op.getText());
            this.pushVar(new Const(Type.tBOOL, result));
            this.opCodes.add(result.toString().toUpperCase());
        }

        public void exitLogicAnd(marParser.LogicAndContext ctx) {
            Const right = this.popVar(), left = this.popVar();
            if (right.isBool() && left.isBool()) {
                opCodes.add("AND");
            } else {
                this.typeErrors++;
                this.printError(ctx, "operator " + ctx.getChild(1).getText() + " is invalid between "
                        + left.getType() + " and " + right.getType());
            }
            this.pushVar(new Const(Type.tBOOL, false));
        }

        public void exitUnary(marParser.UnaryContext ctx) {
            boolean error = false;
            Const temp = this.popVar();
            if (ctx.op.getType() == marParser.SUB) {
                if (temp.isNumber()) {
                    this.opCodes.add("UMINUS");
                } else {
                    error = true;
                }
            } else { // must be "not" operator
                if (temp.isBool()) {
                    this.opCodes.add("NOT");
                } else {
                    error = true;
                }
            }
            this.pushVar(temp);
            if (error) {
                this.typeErrors++;
                this.printError(ctx, "unary operator " + ctx.op.getText() + " is invalid for " + temp.getType());
            }
        }

        public void exitBinAddSub(marParser.BinAddSubContext ctx) {
            String OP = "";
            Const right = this.popVar(), left = this.popVar();
            if (ctx.op.getType() == marParser.ADD) {
                if (right.isNumber() && left.isNumber()) {
                    OP = "ADD";
                } else if (right.isString() && left.isString()) {
                    OP = "CONCAT";
                } else {
                    this.typeErrors++;
                    this.printError(ctx, "operator " + ctx.getChild(1).getText() + " is invalid between "
                            + left.getType() + " and " + right.getType());
                }
            } else { // must be subtraction
                if (right.isNumber() && left.isNumber()) {
                    OP = "SUB";
                } else {
                    this.typeErrors++;
                    this.printError(ctx, "operator " + ctx.getChild(1).getText() + " is invalid between "
                            + left.getType() + " and " + right.getType());
                }
            }
            this.pushVar(right);
            opCodes.add(OP);
        }

        public void exitAssign(marParser.AssignContext ctx) {
            Const tempConst = this.popVar();
            Symbol test = this.currentScope.resolve(ctx.ID().getText());
            if (test instanceof VariableSymbol) {
                VariableSymbol temp = (VariableSymbol) test;
                temp.assign();
                if (temp.getType() != tempConst.getType()) {
                    printError(ctx, "cannot assign an expression of type " + tempConst.getType().getText()
                         + " to a variable of type " + temp.getType().getText());
                    this.typeErrors++;
                }
                if (temp.getScope().isGlobal())
                    this.opCodes.add("STOREG " + temp.getIndex());
                else // local
                    this.opCodes.add("STOREL " + temp.getIndex());
            }
        }

        public void exitVarDecl(marParser.VarDeclContext ctx) {
            if (ctx.AFFECT() != null) {
                VariableSymbol temp = (VariableSymbol) this.currentScope.resolve(ctx.ID().getText());
                temp.assign();
                Const tempConst = this.popVar();
                if (temp.getType() != tempConst.getType()) {
                    printError(ctx, "cannot assign " + tempConst.getType().getText() + " to type " + temp.getType().getText());
                    this.typeErrors++;
                }
                if (temp.getScope().isGlobal())
                    this.opCodes.add("STOREG " + temp.getIndex());
                else // local
                    this.opCodes.add("STOREL " + temp.getIndex());
            }
        }

        public void exitId(marParser.IdContext ctx) {
            Symbol test = this.currentScope.resolve(ctx.ID().getText());
            if (test instanceof VariableSymbol) {
                VariableSymbol temp = (VariableSymbol) test;
                Type tempType = temp.getType();
                switch (tempType) {
                    case tNUMBER -> this.pushVar(new Const(Type.tNUMBER, 1.0));
                    case tBOOL -> this.pushVar(new Const(Type.tBOOL, false));
                    case tNIL -> this.pushVar(new Const(Type.tNIL, null));
                    case tSTRING -> this.pushVar(new Const(Type.tSTRING, ""));
                    default -> throw new IllegalArgumentException("Unexpected value: " + tempType);
                }
                if (temp.getScope().isGlobal())
                    this.opCodes.add("LOADG " + temp.getIndex());
                else // local
                    this.opCodes.add("LOADL " + temp.getIndex());
            } else {
                this.pushVar(new Const(test.getType(), null));
            }

        }

        public void exitIfCond(marParser.IfCondContext ctx) {
            Const temp = this.popVar();
            if (!temp.isBool()) {
                this.printError(ctx, "if expression must be of type bool");
                this.typeErrors++;
            }
            this.opCodes.add("JUMPF ");
            this.ifElsePos.push(this.opCodes.size() - 1);            
        }

        public void enterElseInst(marParser.ElseInstContext ctx) {
            this.opCodes.add("JUMP ");
            int index = this.ifElsePos.pop();
            this.opCodes.set(index, this.opCodes.get(index) + this.opCodes.size());
            this.ifElsePos.push(this.opCodes.size() - 1);
        }

        public void exitWhileCond(marParser.WhileCondContext ctx) {
            Const temp = this.popVar();
            if (!temp.isBool()) {
                this.printError(ctx, "while condition must be of type bool");
                this.typeErrors++;
            }
            this.opCodes.add("JUMPF ");
            this.whilePos.push(this.opCodes.size() - 1);
        }    
        
        public void enterWhile(marParser.WhileContext ctx) {
            this.whilePos.push(this.opCodes.size() - 1);
        }

        public void exitWhile(marParser.WhileContext ctx) {
            int temp = this.whilePos.pop();
            ArrayList<Integer> loadIndexes = this.conditionChecker(this.whilePos.peek(), temp, "LOAD");
            this.whilePos.push(temp);
            if (loadIndexes.size() == 0) {
                this.printError(ctx.getParent(), "loop is likely to be endless");
                this.typeErrors++;
            } else {
                String[] loadString, storeString;
                OpCode load, store;
                ArrayList<Integer> storeIndexes = this.conditionChecker(this.whilePos.peek(), this.opCodes.size(), "STORE");
                for (int integer : loadIndexes) {
                    loadString = this.opCodes.get(integer).split(" ");
                    load = OpCode.valueOf(loadString[0]);
                    for (int integer2 : storeIndexes) {
                        storeString = this.opCodes.get(integer2).split(" ");
                        store = OpCode.valueOf(storeString[0]);
                        if (load == store.next()) {
                            if (storeString[1].equals(loadString[1])) { // compare var indexes
                                changedValue = true;
                                break;
                            }    
                        }    
                    }    
                    if (changedValue) break; // get out earlier
                }    
                if (!changedValue) {
                    this.printError(ctx.getParent(), "loop is likely to be endless");
                    this.typeErrors++;
                }    
            }    
            this.changedValue = false;
            int index = this.whilePos.pop();
            this.opCodes.set(index, this.opCodes.get(index) + (this.opCodes.size() + 1));
            index = this.whilePos.pop();
            this.opCodes.add("JUMP " + (index + 1));
        }    
        
        private ArrayList<Integer> conditionChecker(int start, int end, String keyword) {
            ArrayList<Integer> indexes = new ArrayList<>();
            String str;
            int index;
            for (int i = start; i < end; i++) {
                str = this.opCodes.get(i);
                index = str.indexOf(keyword);
                if (index != -1)
                    indexes.add(i);
            }
            return indexes;
        }
        
        public void exitIf(marParser.IfContext ctx) {
            int index = this.ifElsePos.pop();
            this.opCodes.set(index, this.opCodes.get(index) + this.opCodes.size());
        }

        public void enterFunctionDecl(marParser.FunctionDeclContext ctx) {
            if (this.funcPos < 0) {
                this.funcPos = this.opCodes.size();
                this.opCodes.add("JUMP ");
            }
            this.currentFunc = (FunctionSymbol) this.currentScope.resolve(ctx.ID().getText());
            this.currentFunc.setPos(this.opCodes.size());
            this.currentFunc.mapArguments();
        }

        public void exitFunctionDecl(marParser.FunctionDeclContext ctx) {
            if (this.currentFunc.getType() != Type.tNIL) {// falta meter se n tiver return
                if (!this.hasReturn.get(ctx)) {
                    this.printError(ctx, "missing return in function " + this.currentFunc.getId());
                    this.typeErrors++;
                }
            }
            this.currentFunc = null;
        }
        
        public void exitFunctions(marParser.FunctionsContext ctx) {
            if (this.funcPos >= 0) {
                this.opCodes.set(this.funcPos, this.opCodes.get(this.funcPos) + this.opCodes.size());
                this.funcPos = -1;
            }
        }

        public void enterBlock(marParser.BlockContext ctx) {
            this.currentScope = this.scopes.get(ctx);
            if (this.currentFunc == null)
                this.currentScope.mapVars(this.nLocals);
            else
                this.currentScope.mapVars(this.nLocals + 2);
            if (this.currentScope.getNVars() > 0)
            this.opCodes.add("LOCAL " + this.currentScope.getNVars());
            this.nLocals += this.currentScope.getNVars();
        }
        
        public void exitBlock(marParser.BlockContext ctx) {
            if (this.currentScope.getNVars() > 0)
                this.opCodes.add("POP " + this.currentScope.getNVars());
            if (this.currentFunc != null) { // se na func e n tiver return
                if (this.currentFunc.getType() == Type.tNIL && !this.hasReturn.get(ctx)) {
                    this.opCodes.add("NIL");
                    this.opCodes.add("RETURN " + this.currentFunc.getArguments().size());
                }
            }
            this.nLocals -= this.currentScope.getNVars();
            this.currentScope = this.currentScope.getEnclosingScope();
        }

        public void exitCall(marParser.CallContext ctx) {
            Symbol test = this.currentScope.resolve(ctx.ID().getText());
            if (test instanceof FunctionSymbol) {
                FunctionSymbol temp = (FunctionSymbol) this.currentScope.resolve(ctx.ID().getText());
                this.opCodes.add("CALL " + temp.getPos());
                if (ctx.exprList() != null) {
                    if (ctx.exprList().expr().size() != temp.getArguments().size()) {
                        this.printError(ctx, temp.getId() +  " has " + temp.getArguments().size() + " arguments, not " + ctx.exprList().expr().size());
                        this.typeErrors++;
                    } else {
                        for (int i = 0; i < temp.getArguments().size(); i++) {
                            if (this.vars.get(i).getType() != temp.getArgument(i).getType()) {
                                this.printError(ctx.exprList().expr(i), "actual parameter '" + ctx.exprList().expr(i).getText() + "' if of type " + this.vars.get(i).getType().getText()
                                + ", but type " + temp.getArgument(i).getType().getText() + " expected");
                                this.typeErrors++;
                            }
                        }
                    }
                }
                for (int i = 0; i < temp.getArguments().size(); i++)
                    this.popVar();
                this.pushVar(new Const(temp.getType(), null));
                if (temp.getType() == Type.tNIL && !(ctx.getParent() instanceof marParser.UselessContext)) {
                    this.opCodes.add("POP 1");
                }
            }
        }

        public void exitReturn(marParser.ReturnContext ctx) {
            Const tempConst;
            if (this.currentFunc != null) { // se for void
                if (this.currentFunc.getType() == Type.tNIL) {
                    this.opCodes.add("NIL");
                    this.pushVar(new Const(Type.tNIL, null));
                    if (ctx.expr() != null) { // e tiver return expr
                        this.popVar();
                        this.popVar();
                        this.printError(ctx, this.currentFunc.getId() + " should not return a value");
                        this.typeErrors++;
                    }
                } else if (ctx.expr() != null) { // se n for void e tiver return expr
                    tempConst = this.popVar();
                    if (tempConst.getType() != this.currentFunc.getType()) {
                        this.printError(ctx, this.currentFunc.getId() + " should return a value of type " + this.currentFunc.getType().getText() +
                        ", not " + tempConst.getType().getText());
                        this.typeErrors++;
                    }
                } else {
                    this.printError(ctx, this.currentFunc.getId() + " should return a value");
                    this.typeErrors++;                    
                }
                this.opCodes.add("RETURN " + this.currentFunc.getArguments().size());
            } else {
                this.printError(ctx, "return statement outside function");
                this.typeErrors++;
            }
        }
    
        public void exitUseless(marParser.UselessContext ctx) {
            this.opCodes.add("POP 1");
        }
    }

    public static void main(String[] args) {
        String inputFileName = null, outputFileName = "outputs/out.marbc";
        File outputFile = new File(outputFileName), temp;
        boolean debug = false;
        if (args.length > 0)
            inputFileName = args[0];
        if (args.length > 1 && args[1].compareTo("-debug") == 0)
            debug = true;
        InputStream is = System.in;
        try {
            if (inputFileName != null) {
                is = new FileInputStream(inputFileName);
                temp = new File(inputFileName);
                outputFile = new File("outputs/" + temp.getName() + "bc");
                assert (temp.delete());
            }
            CharStream input = CharStreams.fromStream(is);
            marLexer lexer = new marLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            marParser parser = new marParser(tokens);
            ParseTree tree = parser.prog();

            if (parser.getNumberOfSyntaxErrors() > 0) System.exit(1);

            if (debug) System.out.println("... parsing done");

            ParseTreeWalker walker = new ParseTreeWalker();
            DefPhase def = new DefPhase();
            walker.walk(def, tree);

            def.mapGlobals(); // define the indexes of the global variables

            RefPhase ref = new RefPhase(def.scopes, def.global);
            walker.walk(ref, tree);

            Evaluator eval = new Evaluator(def.scopes, def.hasReturn, def.global);
            walker.walk(eval, tree);

            if (debug) System.out.println("... identification and type checking done");
            
            if (eval.typeErrors <= 0 && def.getNumErrors() <= 0 && ref.getNumErrors() <= 0) {
                DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(outputFile));
                dataOutputStream.write(eval.constToByteArray());
                eval.writeData();
                dataOutputStream.write(eval.byteArray.toByteArray());
                dataOutputStream.close();
                if (debug) {
                    System.out.println("... code generation");
                    System.out.println("Constant pool:");
                    for (int i = 0; i < eval.constPool.size(); i++)
                        System.out.println(i + ": " + eval.constPool.get(i));
                    System.out.println("Generated assembly code:");
                    int i = 0;
                    for (String string : eval.opCodes)
                        System.out.println(i++ + ": " + string);
                    System.out.println("Saving the constant pool and the bytecodes to " + outputFile);
                }
            } else System.out.println(inputFileName + " has " + (eval.typeErrors + def.getNumErrors() + ref.getNumErrors()) + " semantic errors");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}