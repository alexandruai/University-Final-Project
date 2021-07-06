package com.andra2699.ssocket.hardware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class VirtualHardwareLayer implements HardwareLayer {
    private static final Logger logger = LoggerFactory.getLogger(VirtualHardwareLayer.class);

    private final boolean[] states;
    private final StateMutator mutator = new StateMutator();

    public VirtualHardwareLayer(int numSockets) {
        states = new boolean[numSockets + 1];
        Arrays.fill(states, false);
    }

    @Override
    public boolean get(int socket) {
        if (socket < 1 || socket >= states.length) {
            throw new RuntimeException("Invalid socket index " + socket);
        }
        return states[socket];
    }

    @Override
    public CompletableFuture<Void> mutate(Consumer<HardwareLayer.StateMutator> actions) {
        synchronized (states) {
            mutator.activate();
            actions.accept(mutator);
            mutator.deactivate();
            print();
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public void shutdownAll() {
        synchronized (states) {
            Arrays.fill(states, false);
            print();
        }
    }

    private void print() {
        synchronized (states) {
            String fmt = IntStream.range(1, states.length)
                    .mapToObj(i -> states[i] ? "ON " : "OFF")
                    .collect(Collectors.joining(" ", "[ ", " ]"));
            logger.info(fmt);
        }
    }

    public class StateMutator implements HardwareLayer.StateMutator {
        private boolean active;

        @Override
        public HardwareLayer.StateMutator set(int socket, boolean value) {
            if (active && socket > 0 && socket < states.length) {
                states[socket] = value;
            }
            return this;
        }

        private void activate() {
            active = true;
        }

        private void deactivate() {
            active = false;
        }
    }
}
