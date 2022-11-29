package com.example.asyncrequesttest;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;

public class AsyncCollectionFunctionExecutor<V> {
    private AtomicBoolean exitFlag = new AtomicBoolean(false);

    private CompletableFuture<Collection<V>> task;

    public static <T, K> AsyncCollectionFunctionExecutor<T> startList(Collection<K> params, Function<K, T> func) {
        AsyncCollectionFunctionExecutor<T> executor = new AsyncCollectionFunctionExecutor<>();
        executor.task = executor.collectAsyncFunctionResultsToList(params, func);
        return executor;
    }

    public <T> Collection<V> exitThenGetResults() {
        exitFlag.set(false);
        Collection<V> results = task.join();
        task = null;
        return results;
    }

    protected <T, K> CompletableFuture<Collection<T>> collectAsyncFunctionResultsToList(Collection<K> params, Function<K, T> func) {
        return collectAsyncFunctionResults(params, func, ArrayList::new);
    }

    protected <T, K> CompletableFuture<Collection<T>> collectAsyncFunctionResultsToSet(Collection<K> params, Function<K, T> func) {
        return collectAsyncFunctionResults(params, func, HashSet::new);
    }

    protected <T, K, C extends Collection<T>> CompletableFuture<C> collectAsyncFunctionResults(
            Collection<K> params, Function<K, T> func, Supplier<C> supplier) {
        C results = params.stream().map(param -> {
                    if (exitFlag.get()) {
                        return null;
                    } else {
                        return func.apply(param);
                    }
                }).filter(Objects::nonNull)
                .collect(supplier, C::add, C::addAll);
        return CompletableFuture.supplyAsync(() -> results);
    }
}