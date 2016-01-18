package io;

import java.util.LinkedList;

/**
 *
 * @author lsdpirate
 */
public class OutBufferTracker {

    private final static LinkedList<Transfer> transfers = new LinkedList<>();

    public static void addTransfer(Transfer t) {
        synchronized (transfers) {
            transfers.add(t);
        }
    }

    public static void removeTransfer(Transfer t) {
        synchronized (transfers) {
            transfers.remove(t);
        }
    }

}
