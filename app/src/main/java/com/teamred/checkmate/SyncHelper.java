package com.teamred.checkmate;

import java.util.concurrent.Semaphore;

public class SyncHelper {
    private static Semaphore semaphore = new Semaphore(0);
    public static void acquire(){
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void release(){
        semaphore.release();
    }
}
