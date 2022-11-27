package com.example.asyncrequesttest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class SampleCronV1 {
    private final SampleRequestSendService sendService;
    private final SampleRequestTargetService targetService;

    private final long TIMEOUT_MILLISECONDS = 5000;
    private AtomicBoolean timeoutFlag = new AtomicBoolean(false);


    @Scheduled(cron = "0 0/1 * * * *")
    public void process() throws InterruptedException {
        timeoutFlag.set(false);
        exampleJob();
    }

    private List<List<String>> exampleJob() throws InterruptedException {
        List<CompletableFuture<List<String>>> futureList = targetService.getTargetsList()
                .stream().map(this::sendRequestAsync).toList();
        CompletableFuture<List<List<String>>> futures = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()]))
                .thenApply(none -> futureList.stream().map(CompletableFuture::join).toList());
        Thread.sleep(TIMEOUT_MILLISECONDS);
        timeoutFlag.set(true);
        return futures.getNow(null);
    }

    private CompletableFuture<List<String>> sendRequestAsync(List<String> targets) {
        return CompletableFuture.supplyAsync(() -> {
            List<String> result = new ArrayList<>();
            for (String target : targets) {
                if (timeoutFlag.get()) {
                    log.info(String.format("%s 까지 프로세싱 했어여", target));
                    return null;
                }
                result.add(sendService.sendRequest(target));
            }
            return result;
        });
    }

}
