package lang.c;

import java.io.PrintStream;

/**
 * CodeGen 出力用
 */
public class CodeGenCommon {
    private PrintStream output;

    CodeGenCommon(PrintStream ps) {
        this.output = ps;
    }

    public void codeGenAll(String cl, CodeGenEntry[] cgeList) {
        printStartComment(cl);
        for (CodeGenEntry cge: cgeList) {
            output.println(cge.codeGen());
        }
        printCompleteComment(cl);
    }

    public void printStartComment(String cl) {
        comment(cl + " starts.");
    }
    public void printCompleteComment(String cl) {
        comment(cl + " complete.");
    }
    public void comment(String cl) {
        CodeGenEntry co = new CodeGenEntry(CodeGenEntry.COMMENT, cl);
        output.println(co.codeGen());
    }

    public void printPushCodeGen(String label, String op, String comment) {
        CodeGenEntry cge = new CodeGenEntry(CodeGenEntry.INST, label, "MOV "+op+", (R6)+", comment);
        printComment("\t\t\t; push " + op + "; R6 は Stack Pointer");
        output.println(cge.codeGen());
    }

    public void printPopCodeGen(String label, String op, String comment) {
        CodeGenEntry cge = new CodeGenEntry(CodeGenEntry.INST, label, "MOV -(R6), "+op+" ", comment);
        printComment("\t\t\t; pop " + op + "; R6 は Stack Pointer");
        output.println(cge.codeGen());
    }

    public void printInstCodeGen(String label, String op, String comment) {
        CodeGenEntry cge = new CodeGenEntry(CodeGenEntry.INST, label, op, comment);
        output.println(cge.codeGen());
    }

    public void printLabel(String label, String comment) {
        CodeGenEntry cge = new CodeGenEntry(CodeGenEntry.LABEL, label, comment);
        output.println(cge.codeGen());
    }
    
    public void printComment(String comment) {
        CodeGenEntry cge = new CodeGenEntry(CodeGenEntry.COMMENT, comment);
        output.println(cge.codeGen());
    }

}


