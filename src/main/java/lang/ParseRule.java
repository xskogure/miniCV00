package lang;

public abstract class ParseRule<Pctx> {
	public abstract void parse(Pctx pcx) throws FatalErrorException;

	public abstract void semanticCheck(Pctx pcx) throws FatalErrorException;

	public abstract void codeGen(Pctx pcx) throws FatalErrorException;
}
