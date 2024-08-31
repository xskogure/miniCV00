package lang;

public interface Assembler<Pctx> {
	public abstract void pass1(Pctx pcx) throws FatalErrorException;
	public abstract void pass2(Pctx pcx) throws FatalErrorException;
}

