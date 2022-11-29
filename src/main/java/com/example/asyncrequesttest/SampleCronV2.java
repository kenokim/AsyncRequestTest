package com.example.asyncrequesttest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class SampleCronV2 {
    private final SampleRequestSendService sendService;
    private final SampleRequestTargetService targetService;

    private final long TIMEOUT_MILLISECONDS = 5000;

    @Scheduled(cron = "0 0/1 * * * *")
    public void process() throws InterruptedException {
        List<List<String>> targets = targetService.getTargetsList();
        AsyncCollectionFunctionExecutor<List<String>> executor = AsyncCollectionFunctionExecutor.startList(targets,
                (targetList) -> targetList.stream().map(sendService::sendRequest).collect(Collectors.toList()));
        Thread.sleep(TIMEOUT_MILLISECONDS);
        log.info(executor.exitThenGetResults().toString());
    }
}

