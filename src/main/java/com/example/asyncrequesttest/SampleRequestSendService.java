package com.example.asyncrequesttest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SampleRequestSendService {
    public String sendRequest(String target) {
        try {
            Thread.sleep((long) (Math.random() * 10000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return String.format("%s - %s", target, Thread.currentThread().getName());
    }
}
