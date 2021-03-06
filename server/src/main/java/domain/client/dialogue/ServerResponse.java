package domain.client.dialogue;

import domain.enums.OperationType;
import domain.enums.StatusCode;

public class ServerResponse<T> {

    private OperationType operationType;
    private StatusCode code;
    private String message;
    private T data;

    public ServerResponse() {
    }

    public ServerResponse(StatusCode code) {
        this.code = code;
    }

    public ServerResponse(StatusCode code, String message) {
        this.code = code;
        this.message = message;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StatusCode getCode() {
        return code;
    }

    public void setCode(StatusCode code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
