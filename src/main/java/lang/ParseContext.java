package lang;

import lang.c.CToken;

public abstract class ParseContext {
	private String warningColor = "\u001b[00;33m"; //yellow
	private String errorColor   = "\u001b[00;31m"; //red
	private String messageColor   = "\u001b[00;41m"; //red backgrond / while foreground
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
			errstr = messageColor + "%%% A total of " + errorNo + "errors were found" + resetColor;
		} else {
			errstr = messageColor + "%%% Compilation completed successfully." + resetColor;
		}
		warnstr = (warningNo > 0) ? ("%%% " + warningNo + " warnings were also issued.") : "";
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
		String classFullName = ste.getClassName(); // FatalError を出したクラス名
		String[] classPaths = classFullName.split("\\.");
		String className = classPaths.length > 0 ? classPaths[classPaths.length-1] : classFullName; 
		String methodName = ste.getMethodName(); // FatalError を出したメソッド名
		error(errorColor+"FatalError[" + errorNo + "]: " + className + ": " + methodName + "(): " + s + resetColor);
		throw new FatalErrorException(s);
	}

	// 本当に致命的な場合は例外を投げる
	public void fatalError(final CToken tk, final String s) throws FatalErrorException {
		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		String classFullName = ste.getClassName(); // FatalError を出したクラス名
		String[] classPaths = classFullName.split("\\.");
		String className = classPaths.length > 0 ? classPaths[classPaths.length-1] : classFullName; 
		String methodName = ste.getMethodName(); // FatalError を出したメソッド名
		error(errorColor+"FatalError[" + errorNo + "]: " + className + ": " + methodName + "(): " + tk + ": " + s + resetColor);
		throw new FatalErrorException(s);
	}

	// // 回復可能なエラー（実験９で利用）
	// public void recoverableError(final String s) throws RecoverableErrorException {
	// 	StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
	// 	String classFullName = ste.getClassName(); // FatalError を出したクラス名
	// 	String[] classPaths = classFullName.split("\\.");
	// 	String className = classPaths.length > 0 ? classPaths[classPaths.length-1] : classFullName; 
	// 	String methodName = ste.getMethodName(); // Warning を出したメソッド名
	// 	error(errorColor+"RecoverableError[" + errorNo + "]: " + className + ": " + methodName + "(): " + s + resetColor);
	// 	throw new RecoverableErrorException(s);
	// }

	// 警告（回復できる些細な誤り）
	public void warning(final String s) {
		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		String classFullName = ste.getClassName(); // FatalError を出したクラス名
		String[] classPaths = classFullName.split("\\.");
		String className = classPaths.length > 0 ? classPaths[classPaths.length-1] : classFullName; 
		String methodName = ste.getMethodName(); // Warning を出したメソッド名
		message(warningColor+"Warning[" + warningNo + "]: " + className + ": " + methodName + "(): "+ s + resetColor);
		++warningNo;
	}
}