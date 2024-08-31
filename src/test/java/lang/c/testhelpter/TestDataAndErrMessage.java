package lang.c.testhelpter;

public class TestDataAndErrMessage {
    private String testData;
    private String errMessage;

    public TestDataAndErrMessage(String testData, String errMessage) {
        this.testData = testData;
        this.errMessage = errMessage;
    }

    public String getTestData() {
        return testData;
    }

    public String getErrMessage() {
        return errMessage;
    }
}
