package com.okason.prontoshop.util;

import java.util.concurrent.Executor;

/**
 * Created by Valentine on 10/29/2016.
 */

public class ThreadPerTaskExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        new Thread(command).start();
    }
}
