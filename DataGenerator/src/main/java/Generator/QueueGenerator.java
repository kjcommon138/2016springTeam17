package Generator;

import com.bronto.commons.io.StringSerializer;
import com.bronto.redis.client.*;
import com.bronto.redis.reliablequeue.Item;
import com.bronto.redis.reliablequeue.ReliableQueue;
import com.bronto.redis.reliablequeue.ReliableQueueParameters;
import com.bronto.redis.reliablequeue.ReliableQueues;
import com.google.common.util.concurrent.ListenableFuture;


public class QueueGenerator {
    public static final String DEFAULT_ADDR = "sd-vm12.csc.ncsu.edu:30001";

    RedisConnection conn;

    public QueueGenerator() {
        this(DEFAULT_ADDR);
    }

    public QueueGenerator(String addr) {
        conn = Redis.newClusterConnection(addr);
    }

    public void run(int numQueues, int queueSize) {
        //testing a PING to sanity check that a connection was established
        RedisRequest req = Redis.req(RedisCommands.PING);
        RedisResponse response = conn.execute(req);

        try {
            System.out.println(response.get());
        } catch(Exception e) {
            e.printStackTrace();
        }

        //generating the queues on the node
        for(int i = 0; i < numQueues; i++) {
            ReliableQueue<String> queue = ReliableQueues.newQueue(conn, "{Queue_" + i +"}", new StringSerializer(), new ReliableQueueParameters());
            //System.out.println("______________Queue " + i + "_________________");
            //loading items into the queue
            for(int j = 0; j < queueSize; j++) {
                queue.enqueue("Q_"+ i +"::Item_" + j);
            }
            System.out.println("Created Queue_" + i);
        }

        conn.close();
    }
}
