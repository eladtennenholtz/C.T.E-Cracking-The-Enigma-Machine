package atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class TheAtomicClass {

    public static class Counter {
        private AtomicInteger c = new AtomicInteger(0);

        public void increment() {
            c.getAndIncrement();
        }

        public int value() {
            return c.get();
        }

        public void makeEmpty(){
            c.set(0);
        }
    }
}
