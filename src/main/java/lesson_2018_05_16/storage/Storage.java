package lesson_2018_05_16.storage;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

public class Storage {

    private volatile String string = "DEFAULT";
    private boolean isWriting;
    private volatile int readingThreads;


    @SneakyThrows
    public void setString(String newValue) {
        String threadName = Thread.currentThread().getName();

        synchronized (this) {
            while (readingThreads != 0) {
                wait();
            }
            isWriting = true;
            doWriting(newValue, threadName);
            isWriting = false;
            notifyAll();
        }
    }

    @SneakyThrows
    public String getString() {
        String threadName = Thread.currentThread().getName();

        String result;
        synchronized (this) {
            while (isWriting) {
                wait();
            }
            ++readingThreads;
        }
        result = doReading(threadName);
        synchronized (this) {
            --readingThreads;
            notifyAll();
        }
        return result;
    }

    private String doReading(String threadName) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(1000);
        System.out.println("Start reading thread " + threadName);
        String result;
        result = string;
        return result;
    }

    private void doWriting(String newValue, String threadName) throws InterruptedException {
        System.out.println("\nStart writing thread " + threadName + " <------------------");
        TimeUnit.MILLISECONDS.sleep(1000);
        string = newValue;
        System.out.println("Finish writing thread " + threadName + " ------------------>" + "\n");
    }
}
