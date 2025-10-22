package com.youcode.services;

import com.youcode.repositories.HelloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    @Autowired
    private HelloRepository helloRepository;

    public String getGreeting() {
        return helloRepository.getMessage();
    }
}
