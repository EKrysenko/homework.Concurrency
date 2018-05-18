package lesson_2018_05_16.storage;

public class Launcher {

    public static void main(String[] args) {
        Storage storage = new Storage();

        Writer writer = new Writer(storage);
        new Thread(writer, "writer").start();

        Thread[] readers = new Thread[4];
        for (int i = 0; i < readers.length; ++i) {
            readers[i] = new Thread(new Reader(storage), "reader" + i);
        }

        for (Thread reader : readers) {
            reader.start();
        }
    }
}
