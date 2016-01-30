package cluster;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;

import java.util.List;
import java.util.Set;

/**
 * Created by laureltimko on 1/26/16.
 */
public class ServerStatus {
    //public variables...
    private String serverStatus = "Active";
    private String[] elements;
    private String[] queueList;

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

    private void setServerStatus(String serverStatus) {
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

            syncApi.flushall();

            syncApi.sadd(queueList[0], "Test1", "Test2", "Test3");
            syncApi.sadd(queueList[1], "Hola", "Hello", "Bonjour", "Hallo", "Hej");

            Set<String> tempSet = syncApi.smembers(queueList[0]);
            elements = tempSet.toArray(new String[tempSet.size()]);

            return getServerStatus();
        }

        setServerStatus("Disabled");
        return getServerStatus();
    }

    public String[] getList(String key) {
        elements = syncApi.smembers(key).toArray(new String[syncApi.smembers(key).size()]);
        return elements;
    }

    public String[] getList() {
        return elements;
    }

    public String[] getQueueList() {
        List<String> tempList = syncApi.scan().getKeys();
        queueList = tempList.toArray(new String[tempList.size()]);
        return queueList;
    }

    /*
    private void closeConnection() {
        syncApi.close();
        redisClient.shutdown();
    }*/

}