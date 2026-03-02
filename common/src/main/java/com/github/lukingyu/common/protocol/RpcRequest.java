package com.github.lukingyu.common.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@Setter
@Getter
public class RpcRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -7277968650391027976L;

    private String className;
    private String methodName;
    private Class<?>[] paramTypes;
    private Object[] params;

}
