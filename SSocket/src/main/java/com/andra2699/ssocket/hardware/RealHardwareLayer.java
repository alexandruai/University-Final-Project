package com.andra2699.ssocket.hardware;

import com.pi4j.io.gpio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RealHardwareLayer implements HardwareLayer {
    private static final Logger logger = LoggerFactory.getLogger(RealHardwareLayer.class);


    private final int numSockets;
    private final Map<GpioPinDigitalOutput, Integer> gpioMap;
    private final Map<Integer, Boolean> states = new HashMap<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor(runnable -> new Thread(runnable, "GPIO"));
    private final StateMutator mutator = new StateMutator();

    public RealHardwareLayer(int numSockets) {
        try {
            gpioMap = GPIOMapBuilder.build()
                    .bind(RaspiPin.GPIO_28, RaspiPin.GPIO_29)  // 1 -> 38, 40
                    .bind(RaspiPin.GPIO_26, RaspiPin.GPIO_27)  // 2 -> 32, 36
                    .bind(RaspiPin.GPIO_24, RaspiPin.GPIO_25)  // 3 -> 35, 67
                    .bind(RaspiPin.GPIO_22, RaspiPin.GPIO_23)  // 4 -> 31, 32
                    .create(executor);
            int avaliableSockets = (int)gpioMap.values().stream().distinct().count();
            if (avaliableSockets < numSockets) {
                logger.warn("Can't use more sockets than the {} declared", avaliableSockets);
                numSockets = avaliableSockets;
            }
        } catch (Exception e) {
            executor.shutdownNow();
            throw e;
        }
        this.numSockets = numSockets;
        for (int i = 1; i <= numSockets; i++) {
            states.put(i, false);
        }
        shutdownAll();
    }

    /**
     * Gets the current state
     * @param socket targer
     * @return the state
     */
    @Override
    public boolean get(int socket) {
        Boolean st;
        synchronized (states) {
            st = states.get(socket);
        }
        if (st == null) {
            throw new RuntimeException("Invalid socket index " + socket);
        }
        return st;
    }

    /**
     * Change the state of pins
     * @param actions lambda to be invokes to change states
     * @return future that completes once pins are updated
     */
    @Override
    public CompletableFuture<Void> mutate(Consumer<HardwareLayer.StateMutator> actions) {
        synchronized (states) {
            mutator.activate();
            actions.accept(mutator);
            mutator.deactivate();
        }
        return updateSockets();
    }

    /**
     * Shuts down all sockets
     */
    @Override
    public void shutdownAll() {
        synchronized (states) {
            for (int i = 1; i <= numSockets; i++) {
                states.put(i, false);
            }
        }
        updateSockets();
    }

    private CompletableFuture<Void> updateSockets() {
        return CompletableFuture.runAsync(() -> {
            synchronized (states) {
                String fmt = states.values().stream()
                        .map(b -> b ? "ON " : "OFF")
                        .collect(Collectors.joining(" ", "[ ", " ]"));
                logger.info(fmt);
                gpioMap.forEach((pin, socket) -> pin.setState(states.get(socket)));
            }
        }, executor);
    }


    public class StateMutator implements HardwareLayer.StateMutator {
        private boolean active;

        @Override
        public StateMutator set(int socket, boolean value) {
            if (active) {
                if (RealHardwareLayer.this.states.get(socket) != null) {
                    states.put(socket, value);
                } else {
                    logger.warn("Setting non-existent socket {} to value {}. This operation will have no effect", socket, value);
                }
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

class GPIOMapBuilder {
    private static final Logger logger = LoggerFactory.getLogger(GPIOMapBuilder.class);

    private final Map<Pin, Integer> gmap = new HashMap<>();
    private int index = 1;

    private GPIOMapBuilder() {
    }

    public static GPIOMapBuilder build() {
        return new GPIOMapBuilder();
    }

    public GPIOMapBuilder bind(Pin ... gpios) {
        for (Pin gpio : gpios) {
            Integer last = gmap.put(gpio, index);
            if (last != null) {
                logger.warn("Rebinding GPIO {} from socket {} to socket {}", last, gpio, index);
            }
        }
        index++;
        return this;
    }

    public Map<GpioPinDigitalOutput, Integer> create(Executor executor) {
        Map<GpioPinDigitalOutput, Integer> outmap = new HashMap<>();
        try {
            CompletableFuture.runAsync(() -> {
                GpioController controller = GpioFactory.getInstance();
                gmap.forEach((pin, socket) -> {
                    GpioPinDigitalOutput out = controller.provisionDigitalOutputPin(pin, PinState.LOW);
                    out.setShutdownOptions(true, PinState.LOW);
                    logger.info("Initialized pin {} for socket {}", pin, socket);
                    outmap.put(out, socket);
                });
            }, executor).get();
        } catch (InterruptedException | ExecutionException ex) {
            logger.error("GPIO initialization failed", ex);
            throw new RuntimeException("GPIO initialization failed", ex);
        }
        return outmap;
    }
}