package com.github.lukingyu.server.service;

import com.github.lukingyu.common.api.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public Object sayHello(String name) {
        return "Server says: Hello " + name;
    }
}
