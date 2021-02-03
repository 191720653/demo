package org.example.fork.join;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * fork join demo
 */
public class ForkJoinDemo {

    public static void main(String[] args) {
        SumTask task = new SumTask(1, 100);
        System.out.println(ForkJoinPool.commonPool().invoke(task));
    }

    static class SumTask extends RecursiveTask<Long> {

        private static final long MAX = 10;

        private long start;
        private long end;

        public SumTask(long start, long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (end - start <= MAX) {
                long sum = 0;
                for (long i = start; i <= end; i++) {
                    sum += i;
                }
                System.out.println(start + " ~ " + end + " -> " + sum);
                return sum;
            } else {
                SumTask sumTaskStart = new SumTask(start, (start + end) / 2);
                SumTask sumTaskEnd = new SumTask((start + end) / 2 + 1, end);
                /*sumTaskStart.fork();
                sumTaskEnd.fork();*/
                invokeAll(sumTaskStart, sumTaskEnd);
                Long result = sumTaskStart.join() + sumTaskEnd.join();
                return result;
            }
        }
    }

}
