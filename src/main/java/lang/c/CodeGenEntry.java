package lang.c;

/**
 * codeGen()一行分のデータ管理クラス
 */
public class CodeGenEntry {
    private String label="";
    private String opeCodeAndOperand="";
    private String comment="";
    private int type=0;

    public int getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public String getOpeCodeAndOperand() {
        return opeCodeAndOperand;
    }

    public String getComment() {
        return comment;
    }

    public final static int LABEL      = 1;  // LABEL:
    public final static int COMMENT    = 2;  // ;;; comment;
    public final static int INST       = 3;  // hlt, nop, ret, mov, jmp, clr, .=NUM , .end,  .word NUMLIST, .blkw NUMLIST

    // label は "LABEL:" を全部入力すること．こちらでは余計な文字は一切追加しない．
    // 通常命令 (OpeCode と Operand をすべて手動で入れる)
    public CodeGenEntry(int type, String label, String opeCodeAndOperand, String comment){
        this.type = type;
        this.label = label;
        this.opeCodeAndOperand = opeCodeAndOperand;
        this.comment = comment;
    }

    // label は "LABEL:" ないしは "LABEL = (LABEL|NUM)" を全部入力すること．こちらでは余計な文字は一切追加しない．
    public CodeGenEntry(int type, String str){
        this.type = type;
        if (type == CodeGenEntry.COMMENT) {
            this.comment = str;
        } else if (type == CodeGenEntry.LABEL) {
            this.label = str;
        }
    }

    public CodeGenEntry(int type, String label, String comment){
        this.type = type;
        if (type == CodeGenEntry.LABEL) {
            this.label = label;
            this.comment = comment;
        }
    }

    public boolean isLabel() {
        return type == CodeGenEntry.LABEL;
    }

    public boolean isComment() {
        return type == CodeGenEntry.COMMENT;
    }

    public boolean isInst() {
        return type == CodeGenEntry.INST;
    }

    public String codeGen() {
        switch (type) {
            case CodeGenEntry.LABEL:        return codeGenLabel();
            case CodeGenEntry.COMMENT:      return codeGenComment();
            case CodeGenEntry.INST:         return codeGenInst();
            default: return "";
        }
    }

    // for INST (PSEUDOINST も含む)
    public String codeGenInst() {
        return label + "\t" + opeCodeAndOperand + "\t; " + comment;
    }

    // label は "LABEL:" ないしは "LABEL = (LABEL|NUM)" を全部入力すること．こちらでは余計な文字は一切追加しない．
    public String codeGenLabel() {
        return label + "\t\t\t; " + comment;
    }

    public String codeGenComment() {
        return ";;; " + comment;
    }

    @Override
    public String toString() {
        return codeGen();
    }
}
