package lang.c.parse;

import lang.*;
import lang.c.*;

public class Expression extends CParseRule {
	// 新しく非終端記号に対応するクラスを作成する際は，必ず拡張BNF をコメントでつけること
	// また，更新する際は，拡張BNFの「履歴」を残すこと（例えば，実験３まで：．．．． と 実験４から：．．． のように）
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
		// ここにやってくるときは、必ずisFirst()が満たされている
		CParseRule term = null, list = null;
		term = new Term(pcx);
		term.parse(pcx);
		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getCurrentToken(pcx);
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
			this.setCType(expression.getCType()); // expression の型をそのままコピー
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
