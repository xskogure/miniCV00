package lang.c;

import lang.FatalErrorException;
import lang.IOContext;
import lang.c.parse.Program;

public class MiniCompilerImpl {
    void compile(IOContext ioContext, boolean isComment) {
        CTokenizer tknz = new CTokenizer(new CTokenRule());
		CParseContext pcx = new CParseContext(ioContext, tknz);
		pcx.setComment(isComment);
		try {
			CTokenizer ct = pcx.getTokenizer();
			CToken tk = ct.getNextToken(pcx);
			if (Program.isFirst(tk)) {
				CParseRule parseTree = new Program(pcx);
				parseTree.parse(pcx);									// parsing
				if (pcx.hasNoError()) parseTree.semanticCheck(pcx);		// semantic analysis
				if (pcx.hasNoError()) parseTree.codeGen(pcx);			// generating code
				pcx.errorReport();
			} else {
				pcx.fatalError(tk + ": Garbage at the start of the program. ");
			}
		} catch (FatalErrorException e) {
			e.printStackTrace();
		}
    }
}
