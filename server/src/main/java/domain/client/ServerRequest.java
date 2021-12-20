package domain.client;

import domain.enums.OperationType;

import java.io.Serializable;

public class ServerRequest<T> implements Serializable {

    private OperationType operationType;

    private T data;

    public ServerRequest(OperationType operationType) {
        this.operationType = operationType;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
