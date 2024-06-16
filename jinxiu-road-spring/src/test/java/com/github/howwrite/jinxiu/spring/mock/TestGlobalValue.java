package com.github.howwrite.jinxiu.spring.mock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class TestGlobalValue {
    private final Map<String, Object> logMap = new ConcurrentHashMap<>();

    public void addLog(String key, Object value) {
        logMap.put(key, value);
    }

    public Object getLog(String key) {
        return logMap.get(key);
    }

    public String printLog() {
        return logMap.entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue()).collect(Collectors.joining("|"));
    }
}
