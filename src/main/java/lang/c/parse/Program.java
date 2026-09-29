package lang.c.parse;

import lang.*;
import lang.c.*;

public class Program extends CParseRule {
	// When creating a new class corresponding to a non-terminal symbol, you must always include an extended BNF in the comments.
	// Also, when updating, retain the “history” of the extended BNF (e.g., up to Experiment 3: ······· and from Experiment 4: ·····).	// expressionAdd ::= '+' term
	// program ::= expression EOF
	CParseRule program;

	public Program(CParseContext pcx) {
		super("Program");
		setBNF("Program ::= Expression EOF");
	}

	public static boolean isFirst(CToken tk) {
		return Expression.isFirst(tk);
	}

	public void parse(CParseContext pcx) throws FatalErrorException {
		// If processing occurs here, isFirst() must be true.
		program = new Expression(pcx);
		program.parse(pcx);
		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getCurrentToken(pcx);
		if (!tk.is(CToken.TK_EOF)) {
			pcx.fatalError(tk, "Garbage at the end of the program.");
		}
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		if (program != null) {
			program.semanticCheck(pcx);
		}
	}

	public void codeGen(CParseContext pcx) throws FatalErrorException {
		CodeGenCommon cgc = pcx.getCodeGenCommon();
		if (program != null) {
			cgc.printStartComment(getBNF(getId()));
			// program header code
			cgc.printInstCodeGen("", ".= 0x0100", getClassName() + ": start address");
			cgc.printInstCodeGen("", "JMP __START", getClassName() + ": jump to __START");

			// This section will eventually require code generation for variable declarations.
			// cgc.printLabel("i_a: .word 100", "Allocation and Initialization of Regular Variables (1 word)");
			// cgc.printLabel("ia_a: .blkw 10", "Assignment of an array variable (10 elements)");

			cgc.printLabel("__START:", getClassName() + ": start label");
			cgc.printInstCodeGen("", "MOV #0x1000, SP", getClassName() + ": initializing SP");

			// program code body
			program.codeGen(pcx);

			// program footer code
			cgc.printPopCodeGen("", "R0", getClassName() + ": Move the calculation result to R0 (for checking the result).");
			cgc.printInstCodeGen("", "HLT\t", getClassName() + ": HALT");
			cgc.printInstCodeGen("", ".end\t", getClassName() + ": ");
			cgc.printCompleteComment(getBNF(getId()));
		}
	}
}
