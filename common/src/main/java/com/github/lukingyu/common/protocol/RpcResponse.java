package com.github.lukingyu.common.protocol;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
public class RpcResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -8969634762940347207L;

    private Object result;
    private Exception exception;

}
