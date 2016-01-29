package cluster;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;

/**
 * Created by laureltimko on 1/26/16.
 */
public class ServerStatus {
    //public variables...
    public String serverStatus = "Active";
    private String cacheKey = "listOfLanguages";
    private String[] elements;
    public String[] queueList;

    //set redis connection
    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> stateful;
    private RedisCommands<String, String> syncApi;


    public static void main(String[] args) {
    }

    public ServerStatus() {
        //set redis connection
        redisClient = new RedisClient(RedisURI.create("redis://localhost/"));
        stateful = redisClient.connect();
        syncApi = stateful.sync();
        queueList = new String[2];
        queueList[0] = "Test1";
        queueList[1] = "Test2";
    }

    public String getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(String serverStatus) {
        this.serverStatus = serverStatus;
    }

    public String getRedisConnection() {
        //if connection is good
        //set serverstatus to "Active"
        //else
        //set serverstatus as "Disabled"
        if (syncApi != null) {
            setServerStatus(!syncApi.ping().isEmpty() ? "Active" : "Disabled");

            System.out.println("Connected to Redis");
            syncApi.del(cacheKey);

            syncApi.sadd(cacheKey, "Test1", "Test2", "Test3");
            elements = syncApi.smembers(cacheKey).toArray(new String[syncApi.smembers(cacheKey).size()]);
            syncApi.close();
            redisClient.shutdown();

            return getServerStatus();
        }

        setServerStatus("Disabled");
        return getServerStatus();
    }

    public String[] getList() {
        return elements;
    }

    public int getListSize() {return elements.length; }

    public String[] getQueueList() {
        return queueList;
    }

}