package lang.c;

/**
 * codeGen() single-line data management class
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

    // Enter the entire “LABEL:” for the label. Do not add any extra characters here.
    // Manual Instruction (Enter all OpeCodes and Operands manually)
    public CodeGenEntry(int type, String label, String opeCodeAndOperand, String comment){
        this.type = type;
        this.label = label;
        this.opeCodeAndOperand = opeCodeAndOperand;
        this.comment = comment;
    }

    // Enter the entire “LABEL:” or “LABEL = (LABEL|NUM)” label. Do not add any extra characters here.
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

    // for INST (include PSEUDOINST)
    public String codeGenInst() {
        return label + "\t" + opeCodeAndOperand + "\t; " + comment;
    }

    // Enter the entire label as “LABEL:” or “LABEL = (LABEL|NUM)”. Do not add any extra characters here.
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
