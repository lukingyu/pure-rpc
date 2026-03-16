package com.github.lukingyu.server;

import com.github.lukingyu.common.api.UserService;
import com.github.lukingyu.server.service.UserServiceImpl;

public class Main {
    
    public static void main(String[] args) {
        RpcServer rpcServer = new RpcServer();

        rpcServer.register(UserService.class, new UserServiceImpl());

        rpcServer.start(8080);
    }
}
