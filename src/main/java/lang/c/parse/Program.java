package lang.c.parse;

import lang.*;
import lang.c.*;

public class Program extends CParseRule {
	// 新しく非終端記号に対応するクラスを作成する際は，必ず拡張BNF をコメントでつけること
	// また，更新する際は，拡張BNFの「履歴」を残すこと（例えば，実験３まで：．．．． と 実験４から：．．． のように）
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
		// ここにやってくるときは、必ずisFirst()が満たされている
		program = new Expression(pcx);
		program.parse(pcx);
		CTokenizer ct = pcx.getTokenizer();
		CToken tk = ct.getCurrentToken(pcx);
		if (tk.getType() != CToken.TK_EOF) {
			pcx.fatalError(tk.toExplainString() + "プログラムの最後にゴミがあります");
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
			cgc.printInstCodeGen("", ".= 0x0100", "Program: 開始番地");
			cgc.printInstCodeGen("", "JMP __START", "Program: __STARTに飛ぶ");
			// ここには将来、変数宣言に対するコード生成が必要
			// cgc.printLabel("i_a:	.word 100", "通常変数(1word)割当と初期化");
			// cgc.printLabel("ia_a:	.blkw 10", "配列変数(10要素)の割当");

			cgc.printLabel("__START:", "Program: ここから開始");
			cgc.printInstCodeGen("", "MOV #0x1000, R6", "Program: SP初期化");

			// program コード本体
			program.codeGen(pcx);

			// program 最後コード
			cgc.printPopCodeGen("", "R0", "Program: 計算結果をR0に取り出す(計算結果確認用)");
			cgc.printInstCodeGen("", "HLT\t", "Program:");
			cgc.printInstCodeGen("", ".end\t", "Program:");
			cgc.printCompleteComment(getBNF(getId()));
		}
	}
}
