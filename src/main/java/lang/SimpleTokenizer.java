package lang;

import java.io.*;

public class SimpleTokenizer extends Tokenizer<SimpleToken, SimpleParseContext> {
	private int lineNo, colNo;
	private char backCh;
	private boolean backChExist = false;
	private boolean caseSensitive = false;

	public SimpleTokenizer() {
		lineNo = 1;
		colNo = 0;
		setupCharSet();
	}

	private final int CHAR_SPACE = 0;
	private final int CHAR_ALPHA = 1;
	private final int CHAR_NUM = 2;
	private final int CHAR_PUNCT = 3;
	private final int CHAR_COMMENT = 4;
	private int[] charSet = new int[255];
	private boolean useHexNumber = false;
	private boolean useOctalNumber = false;
	// private boolean useDirective = false;

	private void setChar(String s, int type) {
		for (int i = 0; i < s.length(); ++i) {
			charSet[s.charAt(i)] = type;
		}
	}

	private void setupCharSet() {
		for (int i = 0; i < charSet.length; ++i) {
			charSet[i] = CHAR_PUNCT;
		}
		for (char c = 'A'; c <= 'Z'; ++c) {
			charSet[c] = CHAR_ALPHA;
		}
		for (char c = 'a'; c <= 'z'; ++c) {
			charSet[c] = CHAR_ALPHA;
		}
		for (char c = '0'; c <= '9'; ++c) {
			charSet[c] = CHAR_NUM;
		}
	}

	public void setSpaceChars(String s) {
		setChar(s, CHAR_SPACE);
	}

	public void setCommentChar(char c) {
		charSet[c] = CHAR_COMMENT;
	}

	public void setAlphaChar(char c) {
		charSet[c] = CHAR_ALPHA;
	}

	public void setAlphaChars(String s) {
		setChar(s, CHAR_ALPHA);
	}

	public void useHexNumber(boolean b) {
		useHexNumber = b;
	}

	public void useOctalNumber(boolean b) {
		useOctalNumber = b;
	}

	public void caseSensitive(boolean b) {
		caseSensitive = b;
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
		ch = caseSensitive ? ch : Character.toLowerCase(ch);
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

	public void skipToNL(SimpleParseContext pctx) {
		in = pctx.getIOContext().getInStream();
		err = pctx.getIOContext().getErrStream();
		// 構文エラー時に、行末まで読み飛ばして復帰
		char ch;
		do {
			ch = readChar();
		} while (ch != '\n');
		++lineNo;
		colNo = 0;
	}

	private SimpleToken currentTk = null;

	// 現在読み込まれているトークンを返す
	@Override
	public SimpleToken getCurrentToken(SimpleParseContext pcx) {
		return currentTk;
	}

	// 次のトークンを読んで返す
	@Override
	public SimpleToken getNextToken(SimpleParseContext pcx) {
		in = pcx.getIOContext().getInStream();
		err = pcx.getIOContext().getErrStream();
		currentTk = readToken();
		// System.out.println("#readToken()='" + currentTk.toExplainString() +
		// currentTk.getType());
		return currentTk;
	}

	private SimpleToken readToken() {
		SimpleToken tk = null;
		char ch;
		int startCol;
		StringBuffer text = new StringBuffer();

		// 空白文字の読み飛ばし
		do {
			ch = readChar();
			if (ch == (char) -1) {
				break;
			} // EOF
			if (charSet[ch] == CHAR_COMMENT) { // コメントは行末まで読み飛ばし
				ch = readChar();
				while (ch != '\n') {
					ch = readChar();
				}
			}
		} while (charSet[ch] == CHAR_SPACE);
		startCol = colNo; // この桁からトークンが始まる

		if (ch == (char) -1) { // EOF
			tk = new SimpleToken(SimpleToken.TK_EOF, lineNo, startCol, "end_of_file");
		} else {
			String s;
			switch (charSet[ch]) {
				case CHAR_ALPHA:
					do {
						text.append(ch);
						ch = readChar();
					} while (charSet[ch] == CHAR_ALPHA || charSet[ch] == CHAR_NUM);
					backChar(ch);
					s = text.toString();
					tk = new SimpleToken(SimpleToken.TK_IDENT, lineNo, startCol, s);
					break;
				case CHAR_NUM:
					if (ch == '0') {
						text.append('0');
						ch = readChar();
						if (useHexNumber && (ch == 'x' || ch == 'X')) { // 16進数
							text.append(ch);
							ch = readChar();
							if (charSet[ch] == CHAR_NUM || (ch >= 'A' && ch <= 'F') || (ch >= 'a' && ch <= 'f')) {
								do {
									text.append(ch);
									ch = readChar();
								} while (charSet[ch] == CHAR_NUM || (ch >= 'A' && ch <= 'F')
										|| (ch >= 'a' && ch <= 'f'));
								backChar(ch);
							} else { // 中途半端な16進数
								backChar(ch);
								tk = new SimpleToken(SimpleToken.TK_ILL, lineNo, startCol, text.toString());
								break;
							}
						} else if (useOctalNumber) { // 8進数
							while (ch >= '0' && ch <= '7') {
								text.append(ch);
								ch = readChar();
							}
							backChar(ch);
						} else { // 10進数
							while (charSet[ch] == CHAR_NUM) {
								text.append(ch);
								ch = readChar();
							}
							backChar(ch);
						}
					} else { // 10進数
						do {
							text.append(ch);
							ch = readChar();
						} while (charSet[ch] == CHAR_NUM);
						backChar(ch);
					}
					tk = new SimpleToken(SimpleToken.TK_NUM, lineNo, startCol, text.toString());
					break;
				case CHAR_PUNCT:
					text.append(ch);
					s = text.toString();
					tk = new SimpleToken(SimpleToken.TK_ILL, lineNo, startCol, s);
					break;
			}
		}
		if (ch == '\n') {
			++lineNo;
			colNo = 0;
		}
		return tk;
	}
}
