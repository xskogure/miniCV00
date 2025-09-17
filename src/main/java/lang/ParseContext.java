package lang;

public abstract class ParseContext {
	private String warningColor = "\u001b[00;31m"; //red
	private String errorColor   = "\u001b[00;32m"; //grean
	private String messageColor   = "\u001b[00;41m"; //grean
	private String resetColor   = "\u001b[00m";
	// 入出力に関わるメソッド群
	@SuppressWarnings("rawtypes")
	public ParseContext(IOContext ioCtx, Tokenizer tknz) {
		setIOContext(ioCtx);
		setTokenizer(tknz);
	}

	private IOContext ioCtx; // 入出力コンテキスト
	@SuppressWarnings("rawtypes")
	private Tokenizer tknz; // 字句切り出しクラス

	public void setIOContext(IOContext ioCtx) {
		this.ioCtx = ioCtx;
	}

	public IOContext getIOContext() {
		return ioCtx;
	}

	@SuppressWarnings("rawtypes")
	public void setTokenizer(Tokenizer tknz) {
		this.tknz = tknz;
	}

	@SuppressWarnings("rawtypes")
	public Tokenizer getTokenizer() {
		return tknz;
	}

	// エラーの扱いに関するもの
	private int warningNo = 0; // 解析警告数
	private int errorNo = 0; // 解析エラー数

	public void errorReport() {
		String errstr, warnstr;
		if (errorNo > 0) {
			errstr = messageColor + "%%% 問題箇所が全部で" + errorNo + "件ありました。" + resetColor;
		} else {
			errstr = messageColor + "%%% 問題箇所はありません。" + resetColor;
		}
		warnstr = (warningNo > 0) ? ("その他に警告は" + warningNo + "件ありました。") : "";
		ioCtx.getErrStream().println(errstr + warnstr);
	}

	private void message(final String s) {
		ioCtx.getErrStream().println(s);
	}

	// エラー（処理系が処理しきれない誤り）
	public boolean hasNoError() {
		return errorNo == 0;
	}

	public void error(final String s) {
		message(errorColor + "Error: " + s + resetColor);
		++errorNo;
	}

	// 本当に致命的な場合は例外を投げる
	public void fatalError(final String s) throws FatalErrorException {
		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		String className = ste.getClassName();   // FatalError を出したクラス名
		String methodName = ste.getMethodName(); // FatalError を出したメソッド名
		error(errorColor+"FatalError[" + errorNo + "]: " + className + ": " + methodName + "(): " + s + resetColor);
		throw new FatalErrorException(s);
	}

	// 警告（回復できる些細な誤り）
	public void warning(final String s) {
		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		String className = ste.getClassName();   // FatalError を出したクラス名
		String methodName = ste.getMethodName(); // FatalError を出したメソッド名
		message(warningColor+"Warning[" + warningNo + "]: " + className + ": " + methodName + "(): "+ s + resetColor);
		++warningNo;
	}
}