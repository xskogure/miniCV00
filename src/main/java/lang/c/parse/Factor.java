package lang.c.parse;

import lang.*;
import lang.c.*;

public class Factor extends CParseRule {
	// 新しく非終端記号に対応するクラスを作成する際は，必ず拡張BNF をコメントでつけること
	// また，更新する際は，拡張BNFの「履歴」を残すこと（例えば，実験３まで：．．．． と 実験４から：．．． のように）
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
		// ここにやってくるときは、必ずisFirst()が満たされている
		number = new Number(pcx);
		number.parse(pcx);
	}

	public void semanticCheck(CParseContext pcx) throws FatalErrorException {
		if (number != null) {
			number.semanticCheck(pcx);
			setCType(number.getCType()); // number の型をそのままコピー
			setConstant(number.isConstant()); // number は常に定数
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