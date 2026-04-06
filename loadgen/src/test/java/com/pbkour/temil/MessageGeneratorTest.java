package com.pbkour.temil;

import com.pbkour.temil.telemetry.TelemetryMessage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageGeneratorTest {

    @Test
    void test() {
        LoadProfile loadProfile = new LoadProfile(2000, 2, true, TrafficPattern.SLOW);
        List<TelemetryMessage> telemetryMessages = MessageGenerator.generateMessages(loadProfile);

        telemetryMessages.forEach(message -> System.out.println(message.timestamp()));
    }

}