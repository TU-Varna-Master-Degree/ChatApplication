package domain.client.dialogue;

import domain.client.enums.OperationType;
import domain.client.enums.StatusCode;

import java.io.Serializable;

public class ServerResponse<T> implements Serializable {

    private OperationType operationType;
    private StatusCode code;
    private String message;
    private T data;

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
