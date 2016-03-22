package Generator;

import com.bronto.commons.io.StringSerializer;
import com.bronto.redis.client.*;
import com.bronto.redis.reliablequeue.Item;
import com.bronto.redis.reliablequeue.ReliableQueue;
import com.bronto.redis.reliablequeue.ReliableQueueParameters;
import com.bronto.redis.reliablequeue.ReliableQueues;
import com.google.common.util.concurrent.ListenableFuture;


public class QueueGenerator {
    public static final int NUM_QUEUES = 10;
    public static final int QUEUE_SIZE = 20;
    public static final int DEFAULT_PORT = 5004;
    public static final String DEFAULT_IP = "sd-vm19.csc.ncsu.edu";
    //public static final int DEFAULT_PORT = 30001;
    //public static final String DEFAULT_IP = "sd-vm12.csc.ncsu.edu";

    //public static final int DEFAULT_PORT = 7000;
    //public static final String DEFAULT_IP = "127.0.0.1";
    RedisConnection conn;

    public QueueGenerator() {
        conn = Redis.newConnection(DEFAULT_IP, DEFAULT_PORT);
    }

    public synchronized void run() {
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
            ReliableQueue<String> queue = ReliableQueues.newQueue(conn, "Queue_" + i, new StringSerializer(), new ReliableQueueParameters());
            System.out.println(queue.size());
            System.out.println("______________Queue " + i + "_________________");
            //loading items into the queue
            for(int j = 0; j < QUEUE_SIZE; j++) {
                ListenableFuture<Item<String>> item = queue.enqueue("Q_"+ i +"::Item_" + j);
                //print as we go to debug for now
                try {
                    System.out.println("Item: " + item.get().getValue());
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Created Queue_" + i);
            System.out.println();
        }
    }
}
