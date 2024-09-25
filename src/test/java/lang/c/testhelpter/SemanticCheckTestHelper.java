package lang.c.testhelpter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;

import lang.FatalErrorException;
import lang.IOContext;
import lang.InputStreamForTest;
import lang.PrintStreamForTest;
import lang.c.CParseContext;
import lang.c.CParseRule;
import lang.c.CTokenRule;
import lang.c.CTokenizer;

import org.junit.After;
import org.junit.Before;

public class SemanticCheckTestHelper<T extends CParseRule> {

    InputStreamForTest inputStream;
    PrintStreamForTest outputStream;
    PrintStreamForTest errorOutputStream;
    CTokenizer tokenizer;
    IOContext context;
    CParseContext cpContext;
    Class <T> c;
    Constructor <T> con;

    public SemanticCheckTestHelper(Class<T> c) {
        this.c = c;
        try {
            con = c.getConstructor(CParseContext.class);
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

    public void acceptListTest(String[] testDataArr) throws FatalErrorException {
        for (String testData: testDataArr) {
            acceptTest(testData);
        }
    }
    // semanticCheck() 正答例
    public void acceptTest(String testData)  {
        resetEnvironment();
        inputStream.setInputString(testData);
        tokenizer.getNextToken(cpContext);
        try {
            T c = con.newInstance(cpContext);
            c.parse(cpContext);
            c.semanticCheck(cpContext);
            assertThat(testData, errorOutputStream.getPrintBufferString(), is(""));
        } catch (FatalErrorException fee) {
            fail("testData\"" + testData + "\": this testdata was rejected.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // semanticCheck() typeTest
    public void typeListTest(TestDataAndCType[] testDataArr) throws FatalErrorException {
        for (TestDataAndCType testData: testDataArr) {
            typeTest(testData);
        }
    }

    // semanticCheck() typeTest
    public void typeTest(TestDataAndCType tc)  {
        typeTest(tc.getTestData(), tc.getType());
    }

    
    // semanticCheck() typeTest
    public void typeTest(String testData, int type)  {
        resetEnvironment();
        inputStream.setInputString(testData);
        tokenizer.getNextToken(cpContext);
        try {
            T c = con.newInstance(cpContext);
            c.parse(cpContext);
            c.semanticCheck(cpContext);
            assertThat("testData \"" + testData + "\"", c.getCType().getType(), is(type));
        } catch (FatalErrorException fee) {
            fail("testData\"" + testData + "\": this testdata was rejected.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // semanticCheck() typeAndConstantTest
    public void typeAndConstantListTest(TestDataAndCTypeAndConstant[] testDataArr) throws FatalErrorException {
        for (TestDataAndCTypeAndConstant testData: testDataArr) {
            typeAndConstantTest(testData);
        }
    }

    // semanticCheck() typeAndConstantTest
    public void typeAndConstantTest(TestDataAndCTypeAndConstant tc) throws FatalErrorException {
        typeAndConstantTest(tc.getTestData(), tc.getType(), tc.getIsConstant());
    }

    
    // semanticCheck() typeAndConstantTest
    public void typeAndConstantTest(String testData, int type, boolean isConstant) throws FatalErrorException  {
        resetEnvironment();
        inputStream.setInputString(testData);
        tokenizer.getNextToken(cpContext);
        try {
            T c = con.newInstance(cpContext);
            c.parse(cpContext);
            c.semanticCheck(cpContext);
            assertThat("testData \"" + testData + "\" type:", c.getCType().getType(), is(type));
            assertThat("testData \"" + testData + "\" isConstant", c.isConstant(), is(isConstant));
        } catch (FatalErrorException fee) {
            fail("testData\"" + testData + "\": this testdata was rejected.");
        } catch (Exception e) {
            //fail("testData\"" + testData + "\": this testdata was rejected for a reason except FatalError.");
            e.printStackTrace();
        }
    }

    public void rejectListTest(TestDataAndErrMessage[] testDataArr) throws FatalErrorException {
        for (TestDataAndErrMessage testData: testDataArr) {
            rejectTest(testData);
        }
    }

    // semanticCheck() 不当例
    public void rejectTest(TestDataAndErrMessage testDataAndErrorMessage) throws FatalErrorException {
        resetEnvironment();
        String testData = testDataAndErrorMessage.getTestData();
        String errMessage = testDataAndErrorMessage.getErrMessage();
        inputStream.setInputString(testData);
        tokenizer.getNextToken(cpContext);
        try {
            T c = con.newInstance(cpContext);
            c.parse(cpContext);
            c.semanticCheck(cpContext);
            fail("testData\"" + testData + "\": this testdata must be reject.");
        } catch (FatalErrorException fee) {
            assertThat(testData, errorOutputStream.getPrintBufferString(), containsString(errMessage));
        } catch (Exception e){
            //fail("testData\"" + testData + "\": this testdata was rejected for a reason except FatalError.");
            e.printStackTrace();
        }
    }
}
