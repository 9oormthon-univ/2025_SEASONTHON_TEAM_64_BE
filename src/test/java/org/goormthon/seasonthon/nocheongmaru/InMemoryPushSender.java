package org.goormthon.seasonthon.nocheongmaru;

import org.goormthon.seasonthon.nocheongmaru.domain.notification.push.PushSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemoryPushSender implements PushSender {
    private final List<SingleRecord> singles = new ArrayList<>();
    private final List<MulticastRecord> multicasts = new ArrayList<>();

    @Override
    public void sendTo(String deviceToken, String title, String body, Map<String, String> data) {
        singles.add(new SingleRecord(deviceToken, title, body, data));
    }

    @Override
    public void sendMulticast(List<String> deviceTokens, String title, String body, Map<String, String> data) {
        multicasts.add(new MulticastRecord(deviceTokens, title, body, data));
    }

    public List<SingleRecord> getSingles() { return singles; }
    public List<MulticastRecord> getMulticasts() { return multicasts; }
    public void clear() { singles.clear(); multicasts.clear(); }
}

