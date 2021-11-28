package com.notayessir.controller;


import com.notayessir.bean.ResultBean;
import com.notayessir.bean.SignInResult;
import com.notayessir.rpc.api.annotation.RPCReference;
import com.notayessir.service.SignInService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class ConsumerController {

    @RPCReference
    SignInService signInService;


    @RequestMapping("hello")
    public String hello(){
        return "hello, this is consumer.";
    }


    @GetMapping("signIn")
    public ResultBean<?> signIn(String name, String pass){
        SignInResult signInResult = signInService.signIn(name, pass);
        if (!signInResult.isSuccess()){
            return new ResultBean<>("1001", "fail", null);
        }
        return new ResultBean<>("1000", "success", signInResult);
    }
}
