package com.notayessir.service;


import com.notayessir.bean.SignInResult;
import com.notayessir.rpc.api.annotation.RPCService;
import org.springframework.stereotype.Service;

import java.util.UUID;


@RPCService(rpcInterface = SignInService.class)
@Service
public class SignInServiceImpl implements SignInService {

    @Override
    public SignInResult signIn(String name, String pass) {
        if (!name.equalsIgnoreCase("demo") || !pass.equalsIgnoreCase("demo")){
            return new SignInResult(false, null);
        }
        return new SignInResult(true, UUID.randomUUID().toString());
    }
}
