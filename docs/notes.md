Aggregate update throughput is strongly affected by key cardinality.
High key reuse performs much better than low key reuse.
At 1000 messages/batch, throughput drops from ~83.5k ops/s with 1 distinct key
to ~44.6k ops/s with 1000 distinct keys.
This suggests map lookup/insertion and per-key overhead are major contributors.


Running histogram
5 million messages