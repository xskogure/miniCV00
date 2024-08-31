package lang.c;

import lang.*;

public class MiniCompiler {
	public static void main(String[] args) {
		String inFile = args[0]; // 適切なファイルを絶対パスで与えること
		IOContext ioCtx = new IOContext(inFile, System.out, System.err);
		MiniCompilerImpl compiler = new MiniCompilerImpl();
		compiler.compile(ioCtx);
	}
}
