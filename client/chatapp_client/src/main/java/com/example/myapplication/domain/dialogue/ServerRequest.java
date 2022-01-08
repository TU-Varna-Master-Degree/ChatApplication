package com.example.myapplication.domain.dialogue;


import com.example.myapplication.domain.enums.OperationType;

public class ServerRequest<T> {

    private OperationType operationType;

    private T data;

    public ServerRequest() {
    }

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
