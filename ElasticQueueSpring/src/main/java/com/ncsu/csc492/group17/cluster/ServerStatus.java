package com.ncsu.csc492.group17.cluster;

import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.cluster.ClusterClientOptions;
import com.lambdaworks.redis.cluster.RedisClusterClient;
import com.lambdaworks.redis.cluster.api.StatefulRedisClusterConnection;
import com.lambdaworks.redis.cluster.api.sync.RedisAdvancedClusterCommands;
import com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands;

import java.util.concurrent.TimeUnit;

/**
 * Created by laureltimko on 1/26/16.
 *
 * This class serves as the Lettuce Interface used to
 * access Redis Databases for use in JSP webpage.
 */
public class ServerStatus {
    //Private variables for ServerStatus class containing host names
    private String serverStatus = "Active";
    private String[] queueList;
    private String[] HOST_NAMES = {"152.14.106.22",
            "152.14.106.29",
            "152.14.106.30",
            "152.14.106.43"};

    //Private variables containing Redis connection components
    private RedisClusterClient redisClient;
    private StatefulRedisClusterConnection<String, String> connection;
    private RedisClusterCommands<String, String> syncApi;

    /**
     * Initializer class
     */
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

        getRedisConnection();
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
            RedisAdvancedClusterCommands<String, String> syncCommand = connection.sync();
            setServerStatus(syncCommand.clusterInfo().indexOf("deactivated") == -1 ? "Active" : "Disabled");

            System.out.println("Connected to Redis");

            //syncCommand.flushdb();

            //syncApi.lpush(queueList[0], "Test1", "Test2", "Test3");
            //syncApi.lpush(queueList[1], "Hola", "Hello", "Bonjour", "Hallo", "Hej");

            return getServerStatus();
        }

        setServerStatus("Disabled");
        return getServerStatus();
    }


    public String[][] getServerList() {
        String nodes = syncApi.clusterNodes();
        String nodesArray[] = nodes.split("\\r?\\n");
        String nodeIdArray[][] = new String[nodesArray.length][5];

        for (int i = 0; i < nodesArray.length; i++) {
            int index = nodesArray[i].indexOf(' ');
            int portIndex = nodesArray[i].indexOf(':') + 1;
            nodeIdArray[i][0] = nodesArray[i].substring(0, index);
            nodeIdArray[i][1] = nodesArray[i].indexOf("disconnected") == -1 ? "Active" : "Disabled";
            nodeIdArray[i][2] = nodesArray[i].indexOf("master") == -1 ? "Slave" : "Master";
            nodeIdArray[i][3] = nodesArray[i].substring(index + 1, portIndex - 1);
            nodeIdArray[i][4] = nodesArray[i].substring(portIndex, nodesArray[i].indexOf(" ", portIndex));
        }

        return nodeIdArray;
    }

}