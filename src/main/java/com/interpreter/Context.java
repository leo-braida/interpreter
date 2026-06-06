package com.interpreter;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private final Map<String, Object> metadata = new HashMap<>();
    private int headingCount = 0;

    public int incrementHeadingCount() {
        return ++headingCount;
    }

    public void setMetadata(String key, Object value) {
        metadata.put(key, value);
    }

    public Object getMetadata(String key) {
        return metadata.get(key);
    }
}
