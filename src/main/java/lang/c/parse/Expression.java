package lang.c.parse;

import lang.*;
import lang.c.*;

public class Expression extends CParseRule {
	// When creating a new class corresponding to a non-terminal symbol, you must always include an extended BNF in the comments.
	// Also, when updating, retain the “history” of the extended BNF (e.g., up to Experiment 3: ······· and from Experiment 4: ·····).
	// expression ::= term { expressionAdd | expressionSub }
	CParseRule expression;

	public Expression(CParseContext pcx) {
		super("Expression");
		setBNF("Expression ::= Term { ExpressionAdd }");
	}

	public static boolean isFirst(CToken tk) {
		return Term.isFirst(tk);
	}

	public void parse(CParseContext pcx) throws FatalErrorException {
		// If processing occurs here, isFirst() must be true.
		CParseRule term = null, list = null;
		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getCurrentToken(pcx);
		term = new Term(pcx);
		term.parse(pcx);
		tk = ct.getCurrentToken(pcx);
		while (ExpressionAdd.isFirst(tk)) {
			list = new ExpressionAdd(pcx, term);
			list.parse(pcx);
			term = list;
			tk = ct.getCurrentToken(pcx);
		}
		expression = term;
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		if (expression != null) {
			expression.semanticCheck(pcx);
			this.setCType(expression.getCType()); // Copy the type of the expression as-is
			this.setConstant(expression.isConstant());
		}
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		CodeGenCommon cgc = pcx.getCodeGenCommon();
		cgc.printStartComment(getBNF(getId()));
		if (expression != null) {
			expression.codeGen(pcx);
		}
		cgc.printCompleteComment(getBNF(getId()));
	}
}
