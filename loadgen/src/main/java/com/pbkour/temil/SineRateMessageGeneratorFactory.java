package com.pbkour.temil;

public class SineRateMessageGeneratorFactory {
    public static SineRateMessageGenerator getLowBurst() {
        return new SineRateMessageGenerator(0.5, 1.5, 500);
    }

    public static SineRateMessageGenerator getNormalBurst() {
        return new SineRateMessageGenerator(1.0, 4.0, 300);
    }

    public static SineRateMessageGenerator getHighBurst() {
        return new SineRateMessageGenerator(2.0, 18.0, 200);
    }
}
