package com.ncsu.csc492.group17.test;

import com.ncsu.csc492.group17.cluster.ServerStatus;

import java.util.Arrays;

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
    public void testGetList() throws Exception {
        String[] testArray = myServer.getList("Test1");
        assertEquals(testArray.length, 3);
        assertEquals(testArray[0], "Test1");
        assertEquals(testArray[1], "Test3");
        assertEquals(testArray[2], "Test2");

        String[] testArray2 = myServer.getList("randomStuff");
        assertEquals(testArray2.length, 0);
    }

    @org.junit.Test
    public void testGetQueueList() throws Exception {
        String[] testArray3 = myServer.getQueueList();
        System.out.println(Arrays.toString(testArray3));
        System.out.println(testArray3[0]);
        System.out.println(testArray3[1]);
        assertEquals(testArray3.length, 2);
        assertEquals(testArray3[0], "Test1");
        assertEquals(testArray3[1], "Test2");
    }

    @org.junit.Test
    public void testGetServerList() throws Exception {
        String[][] testServers = myServer.getServerList();

        //Check Single Complete Line
        assertEquals("4ff050068853b061ccb5d03ada28f5422e6a97a6", testServers[0][0]);
        assertEquals("Active", testServers[0][1]);
        assertEquals("Slave", testServers[0][2]);
        assertEquals("152.14.106.22", testServers[0][3]);
        assertEquals("30009", testServers[0][4]);

        //Check that all servers are there.
        assertEquals(9, testServers.length);
    }

}