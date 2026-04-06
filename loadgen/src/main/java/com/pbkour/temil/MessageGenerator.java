package com.pbkour.temil;

import com.pbkour.temil.telemetry.TelemetryMessage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageGenerator {

    public static List<TelemetryMessage> generateMessages(LoadProfile loadProfile) {
        List<TelemetryMessage> messages = new ArrayList<>();
        int messageCount = loadProfile.messageCount();

        SineRateMessageGenerator sineRateMessageGenerator = switch (loadProfile.trafficPattern()) {
            case SLOW -> SineRateMessageGeneratorFactory.getLowBurst();
            case BURSTY -> SineRateMessageGeneratorFactory.getHighBurst();
            case STEADY -> SineRateMessageGeneratorFactory.getNormalBurst();
        };

        //tying duration to period so we can at least get the number of messages we want, not final design but will do
        List<Long> timestamps = sineRateMessageGenerator.generateMessageTimestamps(1, messageCount).subList(0, messageCount);

        for (int i = 0; i < messageCount; i++) {
            long deviceId = (i % loadProfile.distinctKeyCount()) + 1L;
            TelemetryMessage telemetryMessage = new TelemetryMessage(deviceId, 1, timestamps.get(i), 1);
            messages.add(telemetryMessage);
        }

        return messages;
    }
}
