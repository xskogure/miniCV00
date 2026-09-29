package lang.c;

import lang.SimpleToken;
import java.util.HashMap;

public class CToken extends SimpleToken {

    /* The following is defined in SimpleToken.java, so you can use it!
    public static final int TK_IDENT = 0; // Identifier (Label)
    public static final int TK_NUM = 1; // Number
    public static final int TK_EOF = -1; // (End-of-file marker)
    public static final int TK_ILL = -2; // Undefined token
     */
    public static final int TK_PLUS        = 2;    // +
    // add chapter1
    public static final int TK_MINUS    = 3;    // -

    // add chapter2
    public static final int TK_AMP        = 4;    // adress &

    // add chapter3
    public static final int TK_MULT     = 5;    // *
    public static final int TK_DIV      = 6;    // ÷
    public static final int TK_LPAR     = 7;    // (
    public static final int TK_RPAR     = 8;    // )

    // add chapter4
    public static final int TK_LBRA     = 9;    // [
    public static final int TK_RBRA     = 10;   // ]
    // public static final int TK_IDENT    = 11;   // ident is already implemented in SimpleToken.java

    // add chapter5
    public static final int TK_ASSIGN     = 12;    // =
    public static final int TK_SEMI        = 13;    // ;
    public static final int TK_INPUT    = 25;    // input (identified via ident)
    public static final int TK_OUTPUT   = 26;    // output (identified via ident)

    // add chapter6
    public static final int TK_TRUE        = 14;    // true (identify via ident)
    public static final int TK_FALSE       = 15;    // false (identify via ident)
    public static final int TK_LT         = 16;    // <
    public static final int TK_GT         = 17;    // >
    public static final int TK_LE         = 18;    // <=
    public static final int TK_GE        = 19;    // >=
    public static final int TK_EQ        = 20;    // ==
    public static final int TK_NE        = 21;    // !=

    // When operating true as 1, enable the following (in this experiment, please keep true fixed at 1)
    public static final String TRUE_NUM = "0x0001";

    // For false, please use either 0 or -1.
    // If you are using false as 0, please enable the following.
    public static final String FALSE_NUM = "0x0000";
    // If you use false as -1, please enable the following.
    // public static final String FALSE_NUM = "0xFFFF";

    // add chapter7
    public static final int TK_IF       = 22;    // if (identified via ident)
    public static final int TK_ELSE     = 23;    // else (identified via ident)
    public static final int TK_WHILE    = 24;    // while (identified via ident)
    public static final int TK_LCUR        = 27;    // {
    public static final int TK_RCUR        = 28;    // }

    // add chapter8
    // Please add your own content here.

    public CToken(int type, int lineNo, int colNo, String s) {
        super(type, lineNo, colNo, s);
    }

    private static final HashMap<Integer, String> CTOKENS = new HashMap<Integer, String>(){
        {
            // CSimpleToken from chapter0
            put(TK_IDENT,"TK_IDENT");
            put(TK_NUM,"TK_NUM");
            put(TK_EOF,"TK_EOF");
            put(TK_ILL,"TK_ILL");

            // With CToken, starting from chapter0
            put(TK_PLUS,"TK_PLUS");
            
            // add chapter1
            put(TK_MINUS,"TK_MINUS");
            
            // add chapter2
            put(TK_AMP,"TK_AMP");
            
            // add chapter3
            put(TK_MULT,"TK_MULT");
            put(TK_DIV,"TK_DIV");
            put(TK_LPAR,"TK_LPAR");
            put(TK_RPAR,"TK_RPAR");
            
            // add chapter4
            put(TK_LBRA,"TK_LBRA");
            put(TK_RBRA,"TK_RBRA");
            
            // add chapter5
            put(TK_ASSIGN,"TK_ASSIGN");
            put(TK_SEMI,"TK_SEMI");
            put(TK_INPUT,"TK_INPUT");
            put(TK_OUTPUT,"TK_OUTPUT");
            
            // add chapter6
            put(TK_TRUE,"TK_TRUE");
            put(TK_FALSE,"TK_FALSE");
            put(TK_LT,"TK_LT");
            put(TK_GT,"TK_GT");
            put(TK_LE,"TK_LE");
            put(TK_GE,"TK_GE");
            put(TK_EQ,"TK_EQ");
            put(TK_NE,"TK_NE");
            
            // add chapter7
            put(TK_IF,"TK_IF");
            put(TK_ELSE,"TK_ELSE");
            put(TK_WHILE,"TK_WHILE");
            
            put(TK_LCUR,"TK_LCUR");
            put(TK_RCUR,"TK_RCUR");
            
            // add chapter8
            // Please add your own content here.
}
    };

    static public String tokenString(int type) {
        return CToken.CTOKENS.get(type);
    }

    public String toDetailExplainString() {
        String str;
        if (this.getType() == TK_NUM) {
            str = super.toExplainString() + " type=" + getTokenString() + " [" + this.getType() + "] valule=" + this.getIntValue();
        } else {
            str = super.toExplainString() + " type=" + getTokenString() + " [" + this.getType() + "]";
        }        
        return str;
    }

    public String getTokenString() {
        return CToken.CTOKENS.get(this.getType());
    }

    public boolean is(int type) {
        return getType() == type;
    }

    public boolean is(CToken tk) {
        return is(tk.getType());
    }

    @Override
    public String toString() {
        return toDetailExplainString();
    }
}
