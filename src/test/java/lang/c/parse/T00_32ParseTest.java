package lang.c.parse;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import lang.FatalErrorException;
import lang.c.testhelpter.ParseTestHelper;
import lang.c.testhelpter.ParseTestHelper2;
import lang.c.testhelpter.TestDataAndErrMessage;

@RunWith(Enclosed.class)
public class T00_32ParseTest {
    public static class NumberTest {
        ParseTestHelper<Number> numberHelper = new ParseTestHelper<Number>(Number.class);

        // Number 正当例
        @Test
        public void accept() throws FatalErrorException {
            String[] testDataArr = {
                    "255", "377"
            };
            numberHelper.parseAcceptTestList(testDataArr);
        }

        // Number 不当例
        @Test
        public void reject() throws FatalErrorException {
            // parse() は最初のTokenのエラーチェックをしていない(isFirst()がtrueの時しか実行しない)ため
            // 本来は以下のデータはテストできない．
            // 下記は，ParseTestHelper 側で，parse() のテストをする前にまず isFirst() が true か確認しており
            // false だった場合に出している FatalError をキャッチしてテストしている．
            TestDataAndErrMessage[] testDataArr = {
                    new TestDataAndErrMessage("+", "isFirst() が false です"),
                    new TestDataAndErrMessage("=", "isFirst() が false です"),
                    new TestDataAndErrMessage("@", "isFirst() が false です"),
            };
            numberHelper.parseRejectTestList(testDataArr);
        }
    }

    public static class FactorTest {
        ParseTestHelper<Factor> factorHelper = new ParseTestHelper<Factor>(Factor.class);

        // Factor 正当例
        @Test
        public void accept() throws FatalErrorException {
            String[] testDataArr = {
                    "255", "377"
            };
            factorHelper.parseAcceptTestList(testDataArr);
        }

        // Factor 不当例
        @Test
        public void reject() throws FatalErrorException {
            // parse() は最初のTokenのエラーチェックをしていない(isFirst()がtrueの時しか実行しない)ため
            // 本来は以下のデータはテストできない．
            // 下記は，ParseTestHelper 側で，parse() のテストをする前にまず isFirst() が true か確認しており
            // false だった場合に出している FatalError をキャッチしてテストしている．
            TestDataAndErrMessage[] testDataArr = {
                    new TestDataAndErrMessage("+", "isFirst() が false です"),
                    new TestDataAndErrMessage("=", "isFirst() が false です"),
                    new TestDataAndErrMessage("@", "isFirst() が false です"),
            };
            factorHelper.parseRejectTestList(testDataArr);
        }
    }

    public static class TermTest {
        ParseTestHelper<Term> termHelper = new ParseTestHelper<Term>(Term.class);

        // Factor 正当例
        @Test
        public void accept() throws FatalErrorException {
            String[] testDataArr = {
                    "255", "377"
            };
            termHelper.parseAcceptTestList(testDataArr);
        }

        // Factor 不当例
        @Test
        public void reject() throws FatalErrorException {
            // parse() は最初のTokenのエラーチェックをしていない(isFirst()がtrueの時しか実行しない)ため
            // 本来は以下のデータはテストできない．
            // 下記は，ParseTestHelper 側で，parse() のテストをする前にまず isFirst() が true か確認しており
            // false だった場合に出している FatalError をキャッチしてテストしている．
            TestDataAndErrMessage[] testDataArr = {
                    new TestDataAndErrMessage("+", "isFirst() が false です"),
                    new TestDataAndErrMessage("=", "isFirst() が false です"),
                    new TestDataAndErrMessage("@", "isFirst() が false です"),
            };
            termHelper.parseRejectTestList(testDataArr);
        }
    }

    // ExpressionAdd は 単体では parse() 以降のテストはできない(コンストラクタに左辺のCParseRule を入れないといけないため．
    // 下記では，予定される左辺のクラスと，テストしたいクラスの2つを指定し，その2つの非終端記号が連続で来る(1個だけ見る)部分だけをテストする
    // 専用の ParseTestHelper2 を利用している
    public static class ExpressionAddTest {
        ParseTestHelper2<Term,ExpressionAdd> expressionAddHelper = new ParseTestHelper2<Term,ExpressionAdd>(Term.class,ExpressionAdd.class);
        @Test
        public void accept() throws FatalErrorException {
            String[] testDataArr = {
                    "2+4", // 全部解釈されて accept
                    "1+3+434+3", // 全部解釈されて accept
            };
            expressionAddHelper.parseAcceptTestList(testDataArr);
        }
        @Test
        public void reject() throws FatalErrorException {
            TestDataAndErrMessage[] arr = {
                // new TestDataAndErrMessage("1", "これは正当例なのでこのデータのテストは失敗します"),
                //new TestDataAndErrMessage("1+2-3", "TK_EOF"),
                new TestDataAndErrMessage("1+", "+の後ろはtermです"),
        };
        expressionAddHelper.parseRejectTestList(arr);
        }
    }

    public static class ExpressionTest {
        ParseTestHelper<Expression> expressionHelper = new ParseTestHelper<Expression>(Expression.class);

        // Expression 正当例
        // parse() は後ろに余計なTokenがあっても acceptされる（余計な Token の前までのToken が使われる) 点に注意
        @Test
        public void accept() throws FatalErrorException {
            String[] testDataArr = {
                    "2", // 全部解釈されて accept
                    "1+3+434+3", // 全部解釈されて accept
            };
            expressionHelper.parseAcceptTestList(testDataArr);
        }

        // Expression 不当例
        // parse() の Reject は，BNF 定義における「2つ目以降，必ず必要な項目」がない場合をチェックすること
        // CV00 では， program ::= expression EOF の EOFのチェックと，expressionAdd ::= PLUS terms
        // の term のチェックし
        // しかできない．
        // expression ::= term { expressionAdd } だから，expressionAdd
        // がないことのテストをしたくなるかもしれないが
        // { } は繰り返し回数0を許すため，exppressionAdd が0個でも acceptされるため意味がない
        @Test
        public void reject() throws FatalErrorException {
            TestDataAndErrMessage[] arr = {
                    // new TestDataAndErrMessage("1", "これは正当例なのでこのデータのテストは失敗します"),
                    //new TestDataAndErrMessage("1+2-3", "TK_EOF"),
                    new TestDataAndErrMessage("1+", "+の後ろはtermです"),
            };
            expressionHelper.parseRejectTestList(arr);
        }
    }

    public static class ProgramTest {
        ParseTestHelper<Program> programHelper = new ParseTestHelper<Program>(Program.class);

        // Program 正当例
        @Test
        public void accept() throws FatalErrorException {
            String[] testDataArr = {
                    "2", // 全部解釈されて accept
                    "1+3+434+3", // 全部解釈されて accept

            };
            programHelper.parseAcceptTestList(testDataArr);
        }

        // Program 不当例
        // parse() の Reject は，BNF 定義における「2つ目以降，必ず必要な項目」がない場合をチェックすること
        // CV00 では， program ::= expression EOF の EOFのチェックと，expressionAdd ::= PLUS terms
        // の term のチェックし
        // しかできない．
        // expression ::= term { expressionAdd | expressionSub } だから，expressionAdd や expressionSub
        // がないことのテストをしたくなるかもしれないが
        // { } は繰り返し回数0を許すため，exppressionAdd が0個でも acceptされるため意味がない
        @Test
        public void reject() throws FatalErrorException {
            TestDataAndErrMessage[] arr = {
                    // new TestDataAndErrMessage("1", "これは正当例なのでこのデータのテストは失敗します"),
                    new TestDataAndErrMessage("1+", "+の後ろはtermです"),
                    new TestDataAndErrMessage("1+2+3+4+5-", "プログラムの最後にゴミがあります"),
                    new TestDataAndErrMessage("2030###", "プログラムの最後にゴミがあります"),
            };
            programHelper.parseRejectTestList(arr);
        }
    }
}
