package lang.c;

import lang.*;

public class TestCToken {
	private static class TestTokenizer extends CParseRule {
//		program  ::= { token } EOF
		public TestTokenizer(CParseContext pcx) {}
		public static boolean isFirst(CToken tk) { return true; }

		public void parse(CParseContext ctx) {
			CToken tk = ctx.getTokenizer().getCurrentToken(ctx);
			while (tk.getType() != CToken.TK_EOF) {
				ctx.getIOContext().getOutStream().println("Token=" + tk.toDetailExplainString());
				tk = ctx.getTokenizer().getNextToken(ctx);
			}
		}
		public void semanticCheck(CParseContext pcx) throws FatalErrorException {
			// do nothing
		}
		public void codeGen(CParseContext pcx) throws FatalErrorException {
			// do nothing
		}
	}

	public static void main(String[] args) {
		String inFile = args[0]; // 適切なファイルを絶対パスで与えること
		IOContext ioCtx = new IOContext(inFile, System.out, System.err);
		CTokenizer tknz = new CTokenizer(new CTokenRule());
		CParseContext pcx = new CParseContext(ioCtx, tknz);
		try {
			CTokenizer ct = pcx.getTokenizer();
			CToken tk = ct.getNextToken(pcx);
			if (TestTokenizer.isFirst(tk)) {
				CParseRule program = new TestTokenizer(pcx);
				program.parse(pcx);
				program.codeGen(pcx);
			}
		} catch (FatalErrorException e) {
			e.printStackTrace();
		}
	}
}

