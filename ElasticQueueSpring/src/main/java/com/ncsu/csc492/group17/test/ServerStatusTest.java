package com.ncsu.csc492.group17.test;

import com.ncsu.csc492.group17.cluster.ServerStatus;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Kevin-Lenovo on 1/29/2016.
 */
public class ServerStatusTest {

    ServerStatus myServer = new ServerStatus();

    @org.junit.Test
    public void testGetRedisConnection() throws Exception {
        assertEquals(myServer.getRedisConnection(), "Active");
    }

    @org.junit.Test
    public void testGetServerStatus() throws Exception {
        assertEquals(myServer.getServerStatus(), "Active");
    }

    @org.junit.Test
    public void testGetServerList() throws Exception {
        String[][] testServers = myServer.getServerList();

        //Check Single Complete Line
        //By specific Master values
        for(int i = 0; i < testServers.length; i++) {
            if(testServers[0][3] == "152.14.106.22" && testServers[0][4] == "30001") {
                assertEquals("Active", testServers[0][1]);
                assertEquals("Master", testServers[0][2]);
                assertEquals("152.14.106.22", testServers[0][3]);
                assertEquals("30001", testServers[0][4]);
            }
        }
    }

}