package Generator;

import com.bronto.commons.io.StringSerializer;
import com.bronto.redis.client.*;
import com.bronto.redis.reliablequeue.Item;
import com.bronto.redis.reliablequeue.ReliableQueue;
import com.bronto.redis.reliablequeue.ReliableQueueParameters;
import com.bronto.redis.reliablequeue.ReliableQueues;
import com.google.common.util.concurrent.ListenableFuture;


public class QueueGenerator {
    public static final int NUM_QUEUES = 100;
    public static final int QUEUE_SIZE = 20;

    //public static final String DEFAULT_ADDR = "127.0.0.1:7000";
    public static final String DEFAULT_ADDR = "sd-vm12.csc.ncsu.edu:30001";
    //public static final String DEFAULT_ADDR = "sd-vm19.csc.ncsu.edu:5004";

    RedisConnection conn;

    public QueueGenerator() {
        conn = Redis.newClusterConnection(DEFAULT_ADDR);
    }

    public void run() {
        //testing a PING to sanity check that a connection was established
        RedisRequest req = Redis.req(RedisCommands.PING);
        RedisResponse response = conn.execute(req);

        try {
            System.out.println(response.get());
        } catch(Exception e) {
            e.printStackTrace();
        }

        //generating the queues on the node
        for(int i = 0; i < NUM_QUEUES; i++) {
            ReliableQueue<String> queue = ReliableQueues.newQueue(conn, "{Queue_" + i +"}", new StringSerializer(), new ReliableQueueParameters());
            //System.out.println("______________Queue " + i + "_________________");
            //loading items into the queue
            for(int j = 0; j < QUEUE_SIZE; j++) {
                queue.enqueue("Q_"+ i +"::Item_" + j);
            }
            System.out.println("Created Queue_" + i);
        }

        conn.close();
    }
}
