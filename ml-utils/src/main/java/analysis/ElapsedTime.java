package analysis;

public class ElapsedTime {
    private static long staticStartTime = -1;


    public static void tic() {
        if (staticStartTime != -1) {
            throw new RuntimeException("already started!");
        }

        staticStartTime = System.nanoTime();
    }

    public static double toc() {
        long elapsedTime = System.nanoTime() - staticStartTime;
        staticStartTime = -1;
        return elapsedTime / 1000000000.0;
    }

    private long startTime;

    public ElapsedTime() {
        startTime = -1;
    }

    public void start() {
        if (startTime != -1) {
            throw new RuntimeException("already started!");
        }

        startTime = System.nanoTime();
    }

    public long stop() {
        long elapsedTime = System.nanoTime() - startTime;
        startTime = -1;
        return elapsedTime;
    }
}
