package com.pbkour.temil;

public record LoadProfile(int messageCount, int distinctKeyCount, boolean ordered, TrafficPattern trafficPattern) {
}
