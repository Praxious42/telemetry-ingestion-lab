package com.pbkour.temil.services;

import com.pbkour.temil.aggregate.AggregateStore;
import com.pbkour.temil.telemetry.TelemetryMessage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BatchIngestServiceTest {

	@Test
	void should_call_upsert_for_each_message() {
		var recorded = new ArrayList<TelemetryMessage>();

		AggregateStore store = new AggregateStore() {
			@Override
			public void upsert(TelemetryMessage message) {
				recorded.add(message);
			}
		};

		var svc = new BatchIngestService(store);
		var msgs = List.of(
				new TelemetryMessage(1L, 1, 1L, 1.0),
				new TelemetryMessage(2L, 2, 2L, 2.0)
		);

		svc.updateStoreWithMessages(msgs);

		assertEquals(2, recorded.size());
		assertEquals(msgs, recorded);
	}

	@Test
	void should_continue_when_store_throws() {
		var recorded = new ArrayList<TelemetryMessage>();

		AggregateStore store = new AggregateStore() {
			@Override
			public void upsert(TelemetryMessage message) {
				recorded.add(message);
				if (message.metricId() == 2) {
					throw new AggregateStore.AggregateStoreException("boom");
				}
			}
		};

		var svc = new BatchIngestService(store);
		var msgs = List.of(
				new TelemetryMessage(1L, 1, 1L, 1.0),
				new TelemetryMessage(2L, 2, 2L, 2.0),
				new TelemetryMessage(3L, 3, 3L, 3.0)
		);

		assertDoesNotThrow(() -> svc.updateStoreWithMessages(msgs));

		assertEquals(3, recorded.size());
		assertEquals(msgs, recorded);
	}

	@Test
	void should_do_nothing_for_empty_list() {
		var recorded = new ArrayList<TelemetryMessage>();

		AggregateStore store = new AggregateStore() {
			@Override
			public void upsert(TelemetryMessage message) {
				recorded.add(message);
			}
		};

		var svc = new BatchIngestService(store);

		svc.updateStoreWithMessages(List.of());

		assertTrue(recorded.isEmpty());
	}

	@Test
	void should_throw_for_null_input() {
		AggregateStore store = new AggregateStore() {
			@Override
			public void upsert(TelemetryMessage message) {
			}
		};

		var svc = new BatchIngestService(store);

		assertThrows(BatchIngestService.BatchIngestServiceException.class,
				() -> svc.updateStoreWithMessages(null));
	}
}