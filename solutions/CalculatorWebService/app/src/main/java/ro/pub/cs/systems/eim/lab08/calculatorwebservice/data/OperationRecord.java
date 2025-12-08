package ro.pub.cs.systems.eim.lab08.calculatorwebservice.data;

public class OperationRecord {
    private long id;
    private String operator1;
    private String operator2;
    private String operation;
    private String method;
    private String result;
    private long timestamp;

    public OperationRecord(long id, String operator1, String operator2, String operation, String method, String result, long timestamp) {
        this.id = id;
        this.operator1 = operator1;
        this.operator2 = operator2;
        this.operation = operation;
        this.method = method;
        this.result = result;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public String getOperator1() {
        return operator1;
    }

    public String getOperator2() {
        return operator2;
    }

    public String getOperation() {
        return operation;
    }

    public String getMethod() {
        return method;
    }

    public String getResult() {
        return result;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

