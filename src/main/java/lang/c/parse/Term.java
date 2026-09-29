package lang.c.parse;

import lang.*;
import lang.c.*;

public class Term extends CParseRule {
	// When creating a new class corresponding to a non-terminal symbol, you must always include an extended BNF in the comments.
	// Also, when updating, retain the “history” of the extended BNF (e.g., up to Experiment 3: ······· and from Experiment 4: ·····).	// expressionAdd ::= '+' term
	// term ::= factor
	CParseRule factor;

	public Term(CParseContext pcx) {
		super("Term");
		setBNF("Term ::= Factor");
	}

	public static boolean isFirst(CToken tk) {
		return Factor.isFirst(tk);
	}

	public void parse(CParseContext pcx) throws FatalErrorException {
		// If processing occurs here, isFirst() must be true.
		factor = new Factor(pcx);
		factor.parse(pcx);
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		if (factor != null) {
			factor.semanticCheck(pcx);
			this.setCType(factor.getCType()); // Copy the type of factor as-is
			this.setConstant(factor.isConstant());
		}
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		CodeGenCommon cgc = pcx.getCodeGenCommon();
		cgc.printStartComment(getBNF(getId()));
		if (factor != null) {
			factor.codeGen(pcx);
		}
		cgc.printCompleteComment(getBNF(getId()));
	}
}
