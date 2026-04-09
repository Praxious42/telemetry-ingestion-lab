Aggregate update throughput is strongly affected by key cardinality.
High key reuse performs much better than low key reuse.
At 1000 messages/batch, throughput drops from ~83.5k ops/s with 1 distinct key
to ~44.6k ops/s with 1000 distinct keys.
This suggests map lookup/insertion and per-key overhead are major contributors.


Running histogram
LoadProfile[messageCount=5000000, distinctKeyCount=1, trafficPattern=SLOW]
count = 50
p50   = 84223 us
p95   = 91711 us
p99   = 151423 us
max   = 151423 us
mean  = 87932.8 us
LoadProfile[messageCount=5000000, distinctKeyCount=10, trafficPattern=SLOW]
count = 50
p50   = 61663 us
p95   = 69247 us
p99   = 175743 us
max   = 175743 us
mean  = 64611.2 us
LoadProfile[messageCount=5000000, distinctKeyCount=100, trafficPattern=SLOW]
count = 50
p50   = 60383 us
p95   = 70207 us
p99   = 186111 us
max   = 186111 us
mean  = 65833.92 us
LoadProfile[messageCount=5000000, distinctKeyCount=1000, trafficPattern=SLOW]
count = 50
p50   = 62271 us
p95   = 71743 us
p99   = 79359 us
max   = 79359 us
mean  = 64732.16 us
LoadProfile[messageCount=5000000, distinctKeyCount=1, trafficPattern=STEADY]
count = 50
p50   = 74815 us
p95   = 76671 us
p99   = 259455 us
max   = 259455 us
mean  = 80765.44 us
LoadProfile[messageCount=5000000, distinctKeyCount=10, trafficPattern=STEADY]
count = 50
p50   = 61695 us
p95   = 76671 us
p99   = 90879 us
max   = 90879 us
mean  = 63436.16 us
LoadProfile[messageCount=5000000, distinctKeyCount=100, trafficPattern=STEADY]
count = 50
p50   = 62143 us
p95   = 76287 us
p99   = 100991 us
max   = 100991 us
mean  = 64303.68 us
LoadProfile[messageCount=5000000, distinctKeyCount=1000, trafficPattern=STEADY]
count = 50
p50   = 62751 us
p95   = 73663 us
p99   = 141055 us
max   = 141055 us
mean  = 65968.0 us
LoadProfile[messageCount=5000000, distinctKeyCount=1, trafficPattern=BURSTY]
count = 50
p50   = 63071 us
p95   = 75903 us
p99   = 92351 us
max   = 92351 us
mean  = 66193.28 us
LoadProfile[messageCount=5000000, distinctKeyCount=10, trafficPattern=BURSTY]
count = 50
p50   = 63359 us
p95   = 74239 us
p99   = 266751 us
max   = 266751 us
mean  = 68961.6 us
LoadProfile[messageCount=5000000, distinctKeyCount=100, trafficPattern=BURSTY]
count = 50
p50   = 60575 us
p95   = 75007 us
p99   = 219135 us
max   = 219135 us
mean  = 65479.36 us
LoadProfile[messageCount=5000000, distinctKeyCount=1000, trafficPattern=BURSTY]
count = 50
p50   = 63103 us
p95   = 71999 us
p99   = 156927 us
max   = 156927 us
mean  = 66393.6 us

09/04/2026
Client-side blocking baseline:
100,000 fixed 32-byte frames sent over one TCP connection to a pre-started blocking server.
Client completed writes in 1,218 ms.
Approximate client-side throughput: 82,102 frames/sec (~2.63 MB/s raw payload).
This measures send completion, not guaranteed full server-side processing completion.