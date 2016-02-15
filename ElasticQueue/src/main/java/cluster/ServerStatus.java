package cluster;

import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.lambdaworks.redis.cluster.ClusterClientOptions;
import com.lambdaworks.redis.cluster.RedisClusterClient;
import com.lambdaworks.redis.cluster.api.StatefulRedisClusterConnection;
import com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by laureltimko on 1/26/16.
 */
public class ServerStatus {
    //public variables...
    private String serverStatus = "Active";
    private String[] elements;
    private String[] queueList;
    private String[] HOST_NAMES = {"152.14.106.22",
            "152.14.106.29",
            "152.14.106.30",
            "152.14.106.43"};

    //set redis connection
    private RedisClusterClient redisClient;
    private StatefulRedisClusterConnection<String, String> connection;
    private RedisClusterCommands<String, String> syncApi;

    public static void main(String[] args) {
    }

    public ServerStatus() {
        RedisURI initialUri = new RedisURI();
        initialUri.setHost(HOST_NAMES[0]);
        initialUri.setPort(30001);

        redisClient = RedisClusterClient.create(initialUri);
        redisClient.setOptions(new ClusterClientOptions.Builder()
                .refreshClusterView(true)
                .refreshPeriod(60, TimeUnit.SECONDS)
                .build());

        connection = redisClient.connect();
        syncApi = connection.sync();

		/*
        RedisClusterClient c = new RedisClusterClient(initialUri);
    	RedisAdvancedClusterConnection<String, String> connection2 = c.connectCluster();

    	System.out.println("CLUSTER INFO:" + connection2.clusterInfo());*/

        /*
        syncApi.clusterReset(false);

        for (int i = 0; i < HOST_NAMES.length; i++)
            syncApi.clusterMeet(HOST_NAMES[i], 30001);
*/
        String nodes = syncApi.clusterNodes();
        System.out.println(nodes);
        //String nodesArray[] = nodes.split("\\r?\\n");

        String info = syncApi.clusterInfo();
        System.out.println("INFO");
        System.out.println(info);

        queueList = new String[2];
        queueList[0] = "Test1";
        queueList[1] = "Test2";

        //set redis connection
        /*redisClient = new RedisClient(RedisURI.create("redis://localhost:7002/"));
        stateful = redisClient.connect();
        syncApi = stateful.sync();
        queueList = new String[2];
        queueList[0] = "Test1";
        queueList[1] = "Test2"; */
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
            RedisCommands<String, String> syncCommand = connection.getConnection(HOST_NAMES[0], 30001).sync();
            setServerStatus(!syncCommand.ping().isEmpty() ? "Active" : "Disabled");

            System.out.println("Connected to Redis");

            syncApi.flushdb();

            syncApi.sadd(queueList[0], "Test1", "Test2", "Test3");
            syncApi.sadd(queueList[1], "Hola", "Hello", "Bonjour", "Hallo", "Hej");

            Set<String> tempSet = syncApi.smembers(queueList[0]);
            elements = tempSet.toArray(new String[tempSet.size()]);
            System.out.println(elements[0]);

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
        for(int i = 30001; i <= 30009; i++) {
            RedisCommands<String, String> syncCommand = connection.getConnection(HOST_NAMES[0], i).sync();
            List<String> tempList = syncCommand.scan().getKeys();
            queueList = tempList.toArray(new String[tempList.size()]);

            if(queueList.length != 0) {
                System.out.println(queueList[0] + ", " + i);
                return queueList;
            }
        }
        return queueList;
    }

    public String[] getQueueList(String nodeId) {
        RedisCommands<String, String> syncCommand = connection.getConnection(nodeId).sync();
        List<String> tempList = syncCommand.scan().getKeys();
        queueList = tempList.toArray(new String[tempList.size()]);
        return queueList;
    }

    public String[][] getServerList() {
        String nodes = syncApi.clusterNodes();
        String nodesArray[] = nodes.split("\\r?\\n");
        String nodeIdArray[][] = new String[nodesArray.length][3];

        for (int i = 0; i < nodesArray.length; i++) {
            int index = nodesArray[i].indexOf(' ');

            nodeIdArray[i][0] = nodesArray[i].substring(0, index);
            nodeIdArray[i][1] = nodesArray[i].indexOf("disconnected") == -1 ? "Active" : "Disabled";
            nodeIdArray[i][2] = nodesArray[i].indexOf("master") == -1 ? "Slave" : "Master";
        }

        return nodeIdArray;
    }

    /*
    private void closeConnection() {
        syncApi.close();
        redisClient.shutdown();
    }*/

}