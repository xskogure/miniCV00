package lang.c.testhelpter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;

import lang.FatalErrorException;
import lang.IOContext;
import lang.InputStreamForTest;
import lang.PrintStreamForTest;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenRule;
import lang.c.CTokenizer;

public class CodeGenTestHelper<T extends CParseRule> {
    InputStreamForTest inputStream;
    PrintStreamForTest outputStream;
    PrintStreamForTest errorOutputStream;
    CTokenizer tokenizer;
    IOContext context;
    CParseContext cpContext;
    Class<T> c;
    Constructor<T> con;
    Method isFirst;

    public CodeGenTestHelper(Class <T> c) {
        this.c = c;
       try { 
            con = c.getConstructor(CParseContext.class);
            isFirst = c.getMethod("isFirst", CToken.class);
       } catch (Exception e) {
            e.printStackTrace();
       }
    }
    @Before
    public void setUp() {
        inputStream = new InputStreamForTest();
        outputStream = new PrintStreamForTest(System.out);
        errorOutputStream = new PrintStreamForTest(System.err);
        context = new IOContext(inputStream, outputStream, errorOutputStream);
        tokenizer = new CTokenizer(new CTokenRule());
        cpContext = new CParseContext(context, tokenizer);
    }

    @After
    public void tearDown() {
        inputStream = null;
        outputStream = null;
        errorOutputStream = null;
        tokenizer = null;
        context = null;
        cpContext = null;
    }

    public void resetEnvironment() {
        tearDown();
        setUp();
    }
    private final String opRule = 
    "(MOV|CLR|ADD|SUB|CMP|OR|XOR|AND|BIT|ASL|ASR|LSL|LSR|ROL|ROR|JMP|RJP|BRN|BRZ|BRV|BRC|RBN|RBZ|JSR|RJS|SVC)";
    //private final String pseudoOpRule = "(\\.\\s*END|\\.\\s*WORD|\\.\\s*\\=|\\.\\s*BLKW)";
    /**
     * ロバストな比較用にコメント行なし・コメント部分なし・空白/タブなしのデータに変換する
     * 
     * @param data 処理対象の文字列のリスト
     * @return コメント行・コメント部分・空白/タブ削除済の文字列のリスト
     */
    LinkedList<String> convertToValidateData(LinkedList<String> data) {
        LinkedList<String> dataForValidate = new LinkedList<String>();
        for (String line : data) {
            String str = line.replaceAll("[ \t]*;.*$", "");
            str = str.replaceAll(opRule+"[ \t]", "$1_AABBCCDDEEFF_"); // オペコード後ろの空白を特別Token化
            str = str.replaceAll("^[ \t]+", "_____FIRSTBLANK_____"); // 先頭空白 tab の特別Token化
            str = str.replaceAll("[ \t]+", ""); // 空白 tab の削除
            str = str.replaceAll("_AABBCCDDEEFF_", " ");
            // 先頭にあった1個以上の空白タブは後ろにコード等があれば半角2つ空白にする
            str = str.replaceAll("_____FIRSTBLANK_____(.+)$", "  $1"); 
            str = str.replaceAll("_____FIRSTBLANK_____$", ""); // 先頭にあった1個以上の空白タブが後ろに何もなければ消す
            if (str.length() == 0) {
                continue;
            }
            dataForValidate.add(str);
        }
        return dataForValidate;
    }

    String[] convertToValidateData(String[] data) {
        LinkedList<String> list = convertToValidateData(new LinkedList<String>(Arrays.asList(data)));
        String[] result = (String[])list.toArray();
        return result;
    }

    // Check only code portion, not validate comments
    public void checkCodePortion(List<String> actual, String[] expected) {
        LinkedList<String> expectedForValidate = convertToValidateData(new LinkedList<String>(Arrays.asList(expected)));
        LinkedList<String> actualForValidate = convertToValidateData(new LinkedList<String>(actual));

        assertThat("Actual " + actualForValidate.toString() + "  Line Size: ", actualForValidate.size(),
                is(expectedForValidate.size()));

        int actualNoCommentPos = 0;
        for (int i = 0; i < expectedForValidate.size(); i++) {
            String message = "Line: " + String.valueOf(i);

            // Remove comment area, and blank characters at the head and/or tail.
            String expectedCode = expectedForValidate.get(i);
            String actualCode = actualForValidate.get(actualNoCommentPos);
            actualNoCommentPos++;

            assertThat(message, actualCode, is(expectedCode));
        }
    }

    // Check only code portion, not validate comments
    public void checkCodePortion(List<String> actual, String expected) {
        String[] expectedList = expected.split("[\n\r]");
        checkCodePortion(actual, expectedList);
    }

    public void checkCodeGen(String testData, String[] expected)
            throws FatalErrorException {
        resetEnvironment();
        inputStream.setInputString(testData);
        tokenizer.getNextToken(cpContext);
        try {
            T rule = con.newInstance(cpContext);
            CToken tk = tokenizer.getCurrentToken(cpContext);
            if (!(boolean)isFirst.invoke(null, tk))
                fail("isFirst() is false.");
            rule.parse(cpContext);
            rule.semanticCheck(cpContext);

            // call test target
            rule.codeGen(cpContext);

            // Check finished without errors
            String errorOutput = errorOutputStream.getPrintBufferString();
            assertThat(errorOutput, is(""));

            // Check only code portion, not validate comments
            List<String> outputBuffer = outputStream.getPrintBuffer();
            checkCodePortion(outputBuffer, expected);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkCodeGen(String testData, String expected)
            throws FatalErrorException {
        String[] expectedList = expected.split("[\n\r]");
        checkCodeGen(testData, expectedList);
    }


}
