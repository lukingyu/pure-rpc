package com.github.lukingyu.client;

import com.github.lukingyu.common.api.UserService;

public class Main {
    

    public static void main(String[] args) {
        
        UserService userService = new RpcClientProxy<>("localhost", 8080, UserService.class).getProxy();

        System.out.println("Client: Start calling RPC...");
        Object result = userService.sayHello("小明");
        System.out.println("Client: Receive result -> " + result);
    }
}
