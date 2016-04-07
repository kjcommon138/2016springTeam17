package Generator;

public class Runner {
    public static final int NUM_QUEUES = 100;
    public static final int QUEUE_SIZE = 20;

    //public static final String DEFAULT_ADDR = "127.0.0.1:7000";
    public static final String DEFAULT_ADDR = "sd-vm12.csc.ncsu.edu:30001";
    //public static final String DEFAULT_ADDR = "sd-vm19.csc.ncsu.edu:5004";

    public static void main(String args[]) {
        QueueGenerator gen = new QueueGenerator(DEFAULT_ADDR);
        gen.run(NUM_QUEUES, QUEUE_SIZE);
    }
}
