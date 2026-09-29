package lang.c.parse;

import lang.*;
import lang.c.*;

public class Factor extends CParseRule {
	// When creating a new class corresponding to a non-terminal symbol, you must always include an extended BNF in the comments.
	// Also, when updating, retain the “history” of the extended BNF (e.g., up to Experiment 3: ······· and from Experiment 4: ·····).	// expressionAdd ::= '+' term
	// factor ::= number
	CParseRule number;

	public Factor(CParseContext pcx) {
		super("Factor");
		setBNF("Factor ::= Number");
	}

	public static boolean isFirst(CToken tk) {
		return Number.isFirst(tk);
	}

	public void parse(CParseContext pcx) throws FatalErrorException {
		// If processing occurs here, isFirst() must be true.
		number = new Number(pcx);
		number.parse(pcx);
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		if (number != null) {
			number.semanticCheck(pcx);
			setCType(number.getCType()); // Copy the type of number as-is
			setConstant(number.isConstant()); // number is always a constant
		}
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		CodeGenCommon cgc = pcx.getCodeGenCommon();
		cgc.printStartComment(getBNF(getId()));
		if (number != null) {
			number.codeGen(pcx);
		}
		cgc.printCompleteComment(getBNF(getId()));
	}
}