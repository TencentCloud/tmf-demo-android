package com.tencent.tmf.module.keyboard;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

@SuppressWarnings("unused")
public class TaskThread {
    private static final String TAG = "TMF.TaskThread";
    private static final String HANDLER_THREAD_NAME = "CommonHandlerThread";
    private Handler mTaskHandler;
    private Handler mMainLooperHandler;

    private TaskThread() {
        HandlerThread taskHandlerThread = new HandlerThread(HANDLER_THREAD_NAME);
        taskHandlerThread.start();
        Looper workLooper = taskHandlerThread.getLooper();
        if (workLooper != null) {
            mTaskHandler = new Handler(taskHandlerThread.getLooper());
        } else {
            mTaskHandler = new Handler(Looper.getMainLooper());
        }
        mMainLooperHandler = new Handler(Looper.getMainLooper());
    }

    private interface Holder {
        TaskThread INSTANCE = new TaskThread();
    }

    public static TaskThread getInstance() {
        return Holder.INSTANCE;
    }

    public void postToWorker(final Runnable task) {
        mTaskHandler.post(task);
    }

    public void postToWorkerDelayed(final Runnable task, long delayInMs) {
        mTaskHandler.postDelayed(task, delayInMs);
    }

    public void postToMainThread(final Runnable task) {
        mMainLooperHandler.post(task);
    }

    public void postToMainThreadDelayed(final Runnable task, long delayInMs) {
        mMainLooperHandler.postDelayed(task, delayInMs);
    }
}
