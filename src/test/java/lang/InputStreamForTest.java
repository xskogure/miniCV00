package lang;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamForTest extends InputStream {

    int pos = 0;
    String inputString = "";

    public void setInputString(String s) {
        pos = 0;
        inputString = new String(s);
    }

    public String getRestString() {
        return inputString.substring(pos, inputString.length());
    }

    public int getPos() {
        return pos;
    }

    @Override
    public int read() throws IOException {
        try {
            int c = inputString.charAt(pos);
            pos++;
            return c;
        } catch ( IndexOutOfBoundsException e ) {
            throw new IOException();
        }
    }
}
