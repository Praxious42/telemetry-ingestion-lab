package com.pbkour.temil.stats;

import lombok.Builder;

@Builder
public record StatsDto(long messagesReceived, long messagesProcessed, int queueDepth, long drops,
                       long uptimeSeconds) {
}
