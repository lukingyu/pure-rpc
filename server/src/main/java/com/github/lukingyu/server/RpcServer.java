package com.github.lukingyu.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.lukingyu.common.protocol.RpcRequest;
import com.github.lukingyu.common.protocol.RpcResponse;
import lombok.AllArgsConstructor;

public class RpcServer {

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    private final Map<String, Object> serviceRegistry = new HashMap<>();

    public void register(Class<?> serviceInterface, Object impl) {
        serviceRegistry.put(serviceInterface.getName(), impl);
    }

    public void start(int port) {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("RPC Server started on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                executor.execute(new RequestHandler(socket, serviceRegistry));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @AllArgsConstructor
    private static class RequestHandler implements Runnable {

        private Socket socket;
        private Map<String, Object> serviceRegistry;

        @Override
        public void run() {
            try (ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            ) {
                RpcRequest request = (RpcRequest) input.readObject();
                Object serviceImpl = serviceRegistry.get(request.getClassName());

                RpcResponse response = new RpcResponse();
                if (serviceImpl == null) {
                    response.setException(new RuntimeException("service impl not found: " + request.getClassName()));
                } else {
                    Method method = serviceImpl.getClass().getMethod(request.getMethodName(), request.getParamTypes());
                    Object result = method.invoke(serviceImpl, request.getParams());
                    response.setResult(result);
                }

                output.writeObject(response);
                output.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
