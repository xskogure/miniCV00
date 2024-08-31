package lang;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

public class PrintStreamForTest extends PrintStream {

    LinkedList<String> printBuffer = new LinkedList<String>();

    public PrintStreamForTest(OutputStream out) {
        super(out);
    }

    public List<String> getPrintBuffer() {
        return printBuffer;
    }

    public String getPrintBufferString() {
        String str = "";
        for ( String e: printBuffer ) {
            str += e + "\n";
        }
        return str;
    }

    @Override
    public void println() {
        printBuffer.add("");
        super.println();
    }

    @Override
    public void println(String x) {
        printBuffer.add(x);
        super.println(x);
    }
}
