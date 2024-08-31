package lang.c;

import lang.*;

public class CParseContext extends ParseContext {
	CodeGenCommon cgc;
	public CParseContext(IOContext ioCtx,  CTokenizer tknz) {
		super(ioCtx, tknz);
		this.cgc = new CodeGenCommon(ioCtx.getOutStream());
	}

	@Override
	public CTokenizer getTokenizer()		{ return (CTokenizer) super.getTokenizer(); }

	private int seqNo = 0;
	public int getSeqId() { return ++seqNo; }
	public CodeGenCommon getCodeGenCommon() {
		return cgc;
	}
}
