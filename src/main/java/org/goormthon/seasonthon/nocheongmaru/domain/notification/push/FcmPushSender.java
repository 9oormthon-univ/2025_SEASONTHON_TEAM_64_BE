package org.goormthon.seasonthon.nocheongmaru.domain.notification.push;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class FcmPushSender implements PushSender {

    private final FirebaseMessaging firebaseMessaging;

    @Override
    public void sendTo(String deviceToken, String title, String body, Map<String, String> data) {
        if (deviceToken == null || deviceToken.isBlank()) return;
        try {
            Message.Builder builder = Message.builder()
                .setToken(deviceToken)
                .setNotification(Notification.builder().setTitle(title).setBody(body).build());
            if (data != null && !data.isEmpty()) builder.putAllData(data);
            firebaseMessaging.send(builder.build());
        } catch (Exception e) {
            log.warn("FCM 단건 발송 실패 token={}, reason={}", mask(deviceToken), e.getMessage());
        }
    }

    @Override
    public void sendMulticast(List<String> deviceTokens, String title, String body, Map<String, String> data) {
        List<String> tokens = deviceTokens == null ? Collections.emptyList() : deviceTokens.stream().filter(t -> t != null && !t.isBlank()).toList();
        if (tokens.isEmpty()) return;
        try {
            MulticastMessage.Builder builder = MulticastMessage.builder()
                .addAllTokens(tokens)
                .setNotification(Notification.builder().setTitle(title).setBody(body).build());
            if (data != null && !data.isEmpty()) builder.putAllData(data);
            firebaseMessaging.sendEachForMulticast(builder.build());
        } catch (Exception e) {
            log.warn("FCM 멀티캐스트 발송 실패 size={}, reason={}", tokens.size(), e.getMessage());
        }
    }

    private String mask(String token) {
        if (token.length() <= 8) return "********";
        return token.substring(0, 4) + "****" + token.substring(token.length() - 4);
    }
    
}

