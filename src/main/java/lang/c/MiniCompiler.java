package lang.c;

import lang.*;

public class MiniCompiler {
	public static void main(String[] args) {
		boolean isComment = true;
		if (args[0].equals("-noComment")) {
			isComment = false;
			args[0] = args[1];
		}
		String inFile = args[0]; // 適切なファイルを絶対パスで与えること
		IOContext ioCtx = new IOContext(inFile, System.out, System.err);
		MiniCompilerImpl compiler = new MiniCompilerImpl();
		compiler.compile(ioCtx,isComment);
	}
}
