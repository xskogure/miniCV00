package lang.c.parse;

import lang.FatalErrorException;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenizer;
import lang.c.CType;
import lang.c.CodeGenCommon;

public class ExpressionAdd extends CParseRule {
	// When creating a new class corresponding to a non-terminal symbol, you must always include an extended BNF in the comments.
	// Also, when updating, retain the “history” of the extended BNF (e.g., up to Experiment 3: ······· and from Experiment 4: ·····).	// expressionAdd ::= '+' term
	CToken op;
	CParseRule left, right;

	public ExpressionAdd(CParseContext pcx, CParseRule left) {
		super("ExpressionAdd");
		this.left = left;
		setBNF("ExpressionAdd ::= TK_PLUS Term");
	}

	public static boolean isFirst(CToken tk) {
		return tk.getType() == CToken.TK_PLUS;
	}

	public void parse(CParseContext pcx) throws FatalErrorException {
		// If processing occurs here, isFirst() must be true.
		CTokenizer ct = pcx.getTokenizer();
		op = ct.getCurrentToken(pcx);
		// Read the next token after the plus token.
		CToken tk = ct.getNextToken(pcx);

		if (!Term.isFirst(tk))
			pcx.fatalError(tk, "expected non-terminal 'Term' after '+' token.");
		right = new Term(pcx);
		right.parse(pcx);
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		// Rules for the Variable Type of the Result of Addition Operations
		final int s[][] = {
				// T_err       T_int   : Row is left, and Column is right.
				{ CType.T_err, CType.T_err }, // T_err
				{ CType.T_err, CType.T_int }, // T_int
		};
		if (left != null && right != null) {
			left.semanticCheck(pcx);
			right.semanticCheck(pcx);
			int lt = left.getCType().getType(); // The type of the left-hand side of the plus sign
			int rt = right.getCType().getType(); // The type of the right-hand side of the plus sign
			int nt = s[lt][rt]; // Type Calculation by Rules
			String lts = left.getCType().toString();
			String rts = right.getCType().toString();
			if (nt == CType.T_err) {
				pcx.fatalError(op, "Cannot add left type[" + lts + "] and right type[" + rts + "].");
			}
			this.setCType(CType.getCType(nt));
			this.setConstant(left.isConstant() && right.isConstant()); // Only when both sides of the plus sign are constants is it a constant.
		}
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		CodeGenCommon cgc = pcx.getCodeGenCommon();
		if (left != null && right != null) {
			cgc.printStartComment(getBNF(getId()));
			left.codeGen(pcx); // Outsource the code generation for the left subtree to term.codeGen()
			right.codeGen(pcx); // Outsource the code generation for the right subtree to term.codeGen()
			String lt = left.getCType().toString();
			String rt = right.getCType().toString();
			String t = getCType().toString();
			cgc.printPopCodeGen("", "R0", getClassName() + ": pop right value to R0["+rt+"]");
			cgc.printPopCodeGen("", "R1", getClassName() + ": pop left value to R1["+lt+"]");
			cgc.printInstCodeGen("", "ADD R1, R0", getClassName() + ": add R1["+lt+"] to R0["+rt+"]");
			cgc.printPushCodeGen("", "R0", getClassName() + ": push result R0["+t+"]");
			cgc.printCompleteComment(getBNF(getId()));
		}
	}
}
