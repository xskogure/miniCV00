package lang.c;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import lang.*;

public class CTokenizer extends Tokenizer<CToken, CParseContext> {
	@SuppressWarnings("unused")
	private CTokenRule rule;
	private int lineNo, colNo;
	private char backCh;
	private boolean backChExist = false;

	private final int ST_NORMAL = 0;
	private final int ST_EOF = 1;
	private final int ST_ILL = 2;
	private final int ST_NUM = 3;
	private final int ST_PLUS = 4;

	private final char __EOF__ = (char)-1;

	public CTokenizer(CTokenRule rule) {
		this.rule = rule;
		lineNo = 1;
		colNo = 1;
	}

	private InputStream in;
	private PrintStream err;

	private char readChar() {
		char ch;
		if (backChExist) {
			ch = backCh;
			backChExist = false;
		} else {
			try {
				ch = (char) in.read();
			} catch (IOException e) {
				e.printStackTrace(err);
				ch = (char) -1;
			}
		}
		++colNo;
		if (ch == '\n') {
			colNo = 1;
			++lineNo;
		}
		// System.out.print("'"+ch+"'("+(int)ch+")");
		return ch;
	}

	private void backChar(char c) {
		backCh = c;
		backChExist = true;
		--colNo;
		if (c == '\n') {
			--lineNo;
		}
	}

	// 現在読み込まれているトークンを返す
	private CToken currentTk = null;

	public CToken getCurrentToken(CParseContext pctx) {
		return currentTk;
	}

	// 次のトークンを読んで返す
	public CToken getNextToken(CParseContext pctx) {
		in = pctx.getIOContext().getInStream();
		err = pctx.getIOContext().getErrStream();
		currentTk = readToken();
		return currentTk;
	}

	private CToken readToken() {
		CToken tk = null;
		char ch;
		int startCol = colNo;
		StringBuffer text = new StringBuffer();

		int state = 0;
		boolean accept = false;
		while (!accept) {
			switch (state) {
				case ST_NORMAL: // 初期状態
					ch = readChar();
					if (ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r') {
					} else if (ch == __EOF__) { // EOF
						startCol = colNo - 1;
						state = ST_EOF;
					} else if (ch >= '0' && ch <= '9') {
						startCol = colNo - 1;
						text.append(ch);
						state = ST_NUM;
					} else if (ch == '+') {
						startCol = colNo - 1;
						text.append(ch);
						state = ST_PLUS;
					} else { // ヘンな文字を読んだ
						startCol = colNo - 1;
						text.append(ch);
						state = ST_ILL;
					}
					break;
				case ST_EOF: // EOFを読んだ
					tk = new CToken(CToken.TK_EOF, lineNo, startCol, "end_of_file");
					accept = true;
					break;
				case ST_ILL: // ヘンな文字を読んだ
					tk = new CToken(CToken.TK_ILL, lineNo, startCol, text.toString());
					accept = true;
					break;
				case ST_NUM: // 数（10進数）の開始
					ch = readChar();
					if (Character.isDigit(ch)) {
						text.append(ch);
					} else {
						// 数の終わり
						backChar(ch); // 数を表さない文字は戻す（読まなかったことにする）
						tk = new CToken(CToken.TK_NUM, lineNo, startCol, text.toString());
						accept = true;
					}
					break;
				case ST_PLUS: // +を読んだ
					tk = new CToken(CToken.TK_PLUS, lineNo, startCol, "+");
					accept = true;
					break;
			}
		}
		return tk;
	}
}
