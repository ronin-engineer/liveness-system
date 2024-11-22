# [System Design] DSA
Design Liveness System

Solutions
1. Simple Hashed Wheel Timer
    - Path: `src/main/java/com/roninhub/SimpleLivenessApp.java`
2. Netty Hashed Wheel Timer
    - Path: `src/main/java/com/roninhub/NettyLivenessApp.java`
3. Speed up the process of `advanceClock()` by using multi-threading and disruptor
    - Path: `src/main/java/com/roninhub/DisruptorApp.java`
4. Hierarchical Timing Wheels
    - Do it by yourself