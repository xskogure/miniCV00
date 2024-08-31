package lang;


public interface Compiler<Pctx> {
	public void semanticCheck(Pctx pcx) throws FatalErrorException;
	public void codeGen(Pctx pcx) throws FatalErrorException;
}

