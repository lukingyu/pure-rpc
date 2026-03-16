package com.github.lukingyu.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

import com.github.lukingyu.common.protocol.RpcRequest;
import com.github.lukingyu.common.protocol.RpcResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RpcClientProxy<T> implements InvocationHandler {

    private String host;
    private int port;
    private Class<T> serviceInterface;

    public T getProxy() {
        Object proxy = Proxy.newProxyInstance(
            serviceInterface.getClassLoader(), 
            new Class[]{serviceInterface}, 
            this
        );
        return (T) proxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try (Socket socket = new Socket(host, port);
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        ) {
            RpcRequest request = new RpcRequest(serviceInterface.getName(), method.getName(), method.getParameterTypes(), args);

            output.writeObject(request);
            output.flush();

            RpcResponse response = (RpcResponse) input.readObject();

            if (response.getException() != null) {
                throw response.getException();
            }

            return response.getResult();
            
        }
    }
    
}
