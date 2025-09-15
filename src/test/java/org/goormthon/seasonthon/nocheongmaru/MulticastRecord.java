package org.goormthon.seasonthon.nocheongmaru;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record MulticastRecord(
    List<String> tokens,
    String title,
    String body,
    Map<String, String> data
) {
    public MulticastRecord {
        tokens = tokens == null ? List.of() : List.copyOf(tokens);
        data = data == null ? Map.of() : new HashMap<>(data);
    }
}

