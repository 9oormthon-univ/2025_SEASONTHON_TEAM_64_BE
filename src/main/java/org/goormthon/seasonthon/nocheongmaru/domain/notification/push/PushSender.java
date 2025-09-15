package org.goormthon.seasonthon.nocheongmaru.domain.notification.push;

import java.util.List;
import java.util.Map;

public interface PushSender {
    
    void sendTo(String deviceToken, String title, String body, Map<String, String> data);
    
    void sendMulticast(List<String> deviceTokens, String title, String body, Map<String, String> data);
    
}

