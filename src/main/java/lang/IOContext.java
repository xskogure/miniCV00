package lang;

import java.io.*;

public class IOContext {
	private String inputFileName;
	private InputStream in;
	private PrintStream out;
	private PrintStream err;

	public InputStream getInStream() {
		return in;
	}

	public PrintStream getOutStream() {
		return out;
	}

	public PrintStream getErrStream() {
		return err;
	}

	public String getInputFileName() {
		return inputFileName;
	}

	// public otherCodeExample() {
	// 	IOContext pcx = IOContext( in, out, err );
	// }

	public IOContext(String inputFileName, PrintStream out, PrintStream err) {
		this.out = out;
		this.err = err;
		openInput(inputFileName);
		this.inputFileName = inputFileName;
	}

	// constructor for testing.
	public IOContext(InputStream inputStream, PrintStream out, PrintStream err) {
		this.out = out;
		this.err = err;
		this.in = inputStream;
		this.inputFileName = "FROM_TESTCASE";
	}

	private void openInput(String inputFileName) {
		// inputFileNameをオープンしてinにつなぐ
		try {
			in = new FileInputStream(inputFileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace(err);
		}
	}

	public void allClose() {
		try {
			if (in != null) {
				in.close();
				in = null;
			}
			if (out != null) {
				out.close();
				out = null;
			}
			if (err != null) {
				err.close();
				err = null;
			}
		} catch (IOException e) {
			e.printStackTrace(err);
		}
	}
}
