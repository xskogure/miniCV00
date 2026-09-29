package lang.c.parse;

import lang.*;
import lang.c.*;

public class Number extends CParseRule {
	// When creating a new class corresponding to a non-terminal symbol, you must always include an extended BNF in the comments.
	// Also, when updating, retain the “history” of the extended BNF (e.g., up to Experiment 3: ······· and from Experiment 4: ·····).	// expressionAdd ::= '+' term
	// number ::= NUM
	CToken num;

	public Number(CParseContext pcx) {
		super("Number");
		setBNF("Number ::= TK_NUM");
	}

	public static boolean isFirst(CToken tk) {
		return tk.getType() == CToken.TK_NUM;
	}

	public void parse(CParseContext pcx) throws FatalErrorException {
		// If processing occurs here, isFirst() must be true.
		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getCurrentToken(pcx);
		num = tk;
		tk = ct.getNextToken(pcx);
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		this.setCType(CType.getCType(CType.T_int));
		this.setConstant(true);
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		CodeGenCommon cgc = pcx.getCodeGenCommon();
		cgc.printStartComment(getBNF(getId()));
		if (num != null) {
			cgc.printPushCodeGen("", "#"+num.getText(), getClassName() + ": push Number");
		}
		cgc.printCompleteComment(getBNF(getId()));
	}
}
