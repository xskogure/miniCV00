package lang.c.testhelpter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import lang.FatalErrorException;
import lang.IOContext;
import lang.InputStreamForTest;
import lang.PrintStreamForTest;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CToken;
import lang.c.CTokenRule;
import lang.c.CTokenizer;

import org.junit.After;
import org.junit.Before;

public class ParseTestHelper2<F extends CParseRule,T extends CParseRule> {

    InputStreamForTest inputStream;
    PrintStreamForTest outputStream;
    PrintStreamForTest errorOutputStream;
    CTokenizer tokenizer;
    IOContext context;
    CParseContext cpContext;
    Class<F> fc;
    Class<T> tc;
    Constructor<F> fcon;
    Constructor<T> tcon;
    Method fIsFirst;
    Method tIsFirst;


    public ParseTestHelper2(Class<F> fc, Class<T> tc)  {
        this.fc = fc;
        this.tc = tc;
        try {
            fcon = fc.getConstructor(CParseContext.class);
            fIsFirst = fc.getMethod("isFirst", CToken.class);
            tcon = tc.getConstructor(CParseContext.class,  CParseRule.class);
            tIsFirst = tc.getMethod("isFirst", CToken.class);
        } catch(Exception e) {
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

    // parse() 正答例
    public void parseAcceptTestList(String[] testDataArr) throws FatalErrorException {
        for (String testData: testDataArr) {
            parseAcceptTest(testData);
        }
    }

    public void parseAcceptTest(String testData) throws FatalErrorException  {
        resetEnvironment();
        inputStream.setInputString(testData);
        CToken tk = tokenizer.getNextToken(cpContext);
        try {

            if (!((boolean)fIsFirst.invoke(null, tk)))
                fail("This test cannot check "+ fc.getName() +"parse() because "+ fc.getName() +".isFirst() is false for this testDdata\"" + testData + "\"");
            F frule = fcon.newInstance(cpContext);
            frule.parse(cpContext);
            tk = tokenizer.getCurrentToken(cpContext);
            System.err.println("tk="+tk.getTokenString());
            if (!((boolean)tIsFirst.invoke(null, tk)))
                fail("This test cannot check "+ tc.getName() +"parse() because "+ tc.getName() +".isFirst() is false for this testDdata\"" + testData + "\"");
            T trule = tcon.newInstance(cpContext, frule);
            trule.parse(cpContext);
            assertThat(testData, errorOutputStream.getPrintBufferString(), is(""));
        } catch (FatalErrorException fee) {
            fail("This valid testData\"" + testData + "\" should have been accepted, but was rejected.");
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

    // parse() 不当例
    public void parseRejectTestList(TestDataAndErrMessage[] testDataArr) throws FatalErrorException {
        for (TestDataAndErrMessage testDataAndErrorMessage: testDataArr) {
            parseRejectTest(testDataAndErrorMessage);
        }
    }

    public void parseRejectTest(TestDataAndErrMessage testDataAndErrorMessage) throws FatalErrorException {
        resetEnvironment();
        String testData = testDataAndErrorMessage.getTestData();
        String errMessage = testDataAndErrorMessage.getErrMessage();
        inputStream.setInputString(testData);
        CToken tk = tokenizer.getNextToken(cpContext);
        try {
            if (!((boolean)fIsFirst.invoke(null, tk)))
                fail(fc.getName() +".isFirst() がfalseです");
            F frule = fcon.newInstance(cpContext);
            frule.parse(cpContext);
            tk = tokenizer.getCurrentToken(cpContext);
            if (!((boolean)tIsFirst.invoke(null, tk)))
                fail(tc.getName() +".isFirst() がfalseです");
            T trule = tcon.newInstance(cpContext, frule);
            trule.parse(cpContext);
            fail("This unjustified testData\"" + testData + "\" should have been rejected, but was accepted.\nIf the test data is unjustified data, you should fix the parse() of the class under test.\nIf the test data is valid data, you should test this test data using parseAcceptTestList() instead of parseRejectTestList().");
        } catch (FatalErrorException fee) {
            assertThat(testData, errorOutputStream.getPrintBufferString(), containsString(errMessage));
        } catch (IllegalAccessException iae) {
            fail("IllegalAccessException: isFirst() is not found. Please check declaration of \"public\" class ");
        } catch (Exception e){
            System.err.println("Error: I don't know the reason of error.");
            //fail("testData\"" + testData + "\": this testdata was rejected for a reason except FatalError.");
            e.printStackTrace();
        }
    }
}
