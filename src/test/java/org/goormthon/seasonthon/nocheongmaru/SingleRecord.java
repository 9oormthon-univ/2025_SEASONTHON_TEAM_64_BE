package org.goormthon.seasonthon.nocheongmaru;

import java.util.HashMap;
import java.util.Map;

public record SingleRecord(
    String token,
    String title,
    String body,
    Map<String, String> data
) {
    public SingleRecord {
        data = data == null ? Map.of() : new HashMap<>(data);
    }
}

