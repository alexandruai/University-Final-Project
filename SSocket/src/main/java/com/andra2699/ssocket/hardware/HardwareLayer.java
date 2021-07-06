package com.andra2699.ssocket.hardware;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface HardwareLayer {
    boolean get(int socket);
    CompletableFuture<Void> mutate(Consumer<StateMutator> actions);
    void shutdownAll();

    interface StateMutator {
        StateMutator set(int socket, boolean value);
    }
}
