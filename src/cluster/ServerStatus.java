package cluster;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;

/**
 * Created by laureltimko on 1/26/16.
 */
public class ServerStatus {

    public String serverStatus = "Active";

    public static void main(String[] args) {
    }

    public String getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(String serverStatus) {
        this.serverStatus = serverStatus;
    }

    public String getRedisConnection() {
        //set redis connection
        RedisClient redisClient = new RedisClient(RedisURI.create("redis://localhost/"));
        StatefulRedisConnection<String, String> stateful = redisClient.connect();
        RedisCommands<String, String> syncApi = stateful.sync();
        setServerStatus(!syncApi.ping().isEmpty() ? "Active" : "Disabled");
        //if connection is good
        //set serverstatus to "Active"
        //else
        //set serverstatus as "Disabled"

        System.out.println("Connected to Redis");

        syncApi.close();
        redisClient.shutdown();

        return getServerStatus();
    }

}
