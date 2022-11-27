package com.example.asyncrequesttest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SampleRequestTargetService {
    public List<List<String>> getTargetsList() {
        return Arrays.asList(
                Arrays.asList("a1", "b1", "c1", "d1"),
                Arrays.asList("a2", "b2", "c2", "d2"),
                Arrays.asList("a3", "b3", "c3", "d3"),
                Arrays.asList("a4", "b4", "c4", "d4")
        );
    }
}
