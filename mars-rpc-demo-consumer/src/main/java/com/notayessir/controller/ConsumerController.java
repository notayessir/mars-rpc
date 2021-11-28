package com.notayessir.controller;


import com.github.luckyMax.bean.ResultBean;
import com.github.luckyMax.bean.SignInResult;
import com.notayessir.rpc.api.annotation.RPCReference;
import com.github.luckyMax.service.SignInService;
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
