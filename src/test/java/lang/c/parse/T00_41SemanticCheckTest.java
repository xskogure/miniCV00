package lang.c.parse;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import lang.FatalErrorException;
import lang.c.CType;
import lang.c.testhelpter.SemanticCheckTestHelper;
import lang.c.testhelpter.TestDataAndCType;
import lang.c.testhelpter.TestDataAndErrMessage;

@RunWith(Enclosed.class)
public class T00_41SemanticCheckTest {
    
    
    
    public static class numberTest {
        SemanticCheckTestHelper<Number> numberHelper = new SemanticCheckTestHelper<Number>(Number.class);
        
        // 一個ずつのacceptテスト
        @Test
        public void accept() throws FatalErrorException {
            numberHelper.acceptTest("1");
            numberHelper.acceptTest("2");
        }

        // リストを渡すacceptテスト
        @Test
        public void acceptList() throws FatalErrorException {
            String[] testDataArr = {"3", "4"};
            numberHelper.acceptListTest(testDataArr);
        }

        // 一個ずつの typeテスト
        @Test
        public void type() throws FatalErrorException {
            numberHelper.typeTest("30000", CType.T_int);
            numberHelper.typeTest("0177", CType.T_int);
        }

        // リストを渡す typeテスト
        @Test
        public void typeList() throws FatalErrorException {
            TestDataAndCType[] ttList = {
                new TestDataAndCType("4", CType.T_int),
                new TestDataAndCType("0x0100", CType.T_int),
            };
            numberHelper.typeListTest(ttList);
        }

    }

    public static class ExpressionTest {
        SemanticCheckTestHelper<Expression> expressionHelper = new SemanticCheckTestHelper<Expression>(Expression.class);

        // 個別テスト (エラーが出ないことのテスト)
        @Test
        public void accept() throws FatalErrorException{
            String testData = "33+44+55+66";
            expressionHelper.acceptTest(testData);
        }
        
        // リストテスト (エラーが出ないことのテスト)
        @Test
        public void acceptList() throws FatalErrorException{
            String[] testDataArr = {
                "2+3",
                "1+3+434+3", // 全部解釈されて accept
                "1#",        // 1 まで解釈されて accept
                "1020@@@@"   // 10120 まで解釈されて accept
            };
            expressionHelper.acceptListTest(testDataArr);
        }

        // 個別型テスト
        @Test
        public void type() throws FatalErrorException{
            expressionHelper.typeTest("1+3", CType.T_int);
            expressionHelper.typeTest("1+3+5", CType.T_int);
        }
    
        // リスト型テスト
        @Test
        public void typeList() throws FatalErrorException{
            TestDataAndCType[] ttList = {
                new TestDataAndCType("2+3", CType.T_int),
                new TestDataAndCType("1+2+3+4+5", CType.T_int)
            };
            expressionHelper.typeListTest(ttList);
        }
    
    }

    public static class ProgramTest {
        SemanticCheckTestHelper<Program> programHelper = new SemanticCheckTestHelper<Program>(Program.class);

        // 個別acceptテスト
        @Test
        public void accept() throws FatalErrorException{
            programHelper.acceptTest("1+3");
            programHelper.acceptTest("321+123");
        }
    
        // リストacceptテスト
        @Test
        public void acceptList() throws FatalErrorException{
            String[] testDataArr = {
                "1+3",
                "332+123"
            };
            programHelper.acceptListTest(testDataArr);
        }
    
        /*
        Reject() については， semanticCheck で実行される fatalError() を確認する 必要がある
        cv00 にはその例が存在しない
        以下は parse() で発生させた fatalError を キャッチしている（semanticCheck() には意味のないテスト)
         */
        @Test
        public void reject() throws FatalErrorException{
            // このテストは結局 parse() のエラーをチェックしているに過ぎない．
            TestDataAndErrMessage te1 = new TestDataAndErrMessage("1+3@@@@@", "プログラムの最後にゴミがあります");
            TestDataAndErrMessage te2 = new TestDataAndErrMessage("1234+", "+の後ろはtermです");

            programHelper.rejectTest(te1);
            programHelper.rejectTest(te2);
        }

        /*
        Reject() については， semanticCheck で実行される fatalError() を確認する 必要がある
        cv00 にはその例が存在しない
        以下は parse() で発生させた fatalError を キャッチしている（semanticCheck() には意味のないテスト)
         */
        @Test
        public void rejectList() throws FatalErrorException{
            // このテストは結局 parse() のエラーをチェックしているに過ぎない．
            TestDataAndErrMessage[] teList = {
                new TestDataAndErrMessage("1+3@@@@@", "プログラムの最後にゴミがあります"),
                new TestDataAndErrMessage("1+", "+の後ろはtermです")
            }; 
            programHelper.rejectListTest(teList);
        }
    }
}
