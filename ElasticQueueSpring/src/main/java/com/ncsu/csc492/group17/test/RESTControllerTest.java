package com.ncsu.csc492.group17.test;

import com.ncsu.csc492.group17.web.model.*;
import com.ncsu.csc492.group17.web.controller.*;
import java.util.ArrayList;

import org.junit.*;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by laureltimko on 3/3/16.
 */
public class RESTControllerTest {
    RESTController controller;
    Server server1;
    ServerRequest request;

    @Before
    public void setUp() throws Exception {
        controller = new RESTController();
        request = new ServerRequest();
        server1 = new Server();
        server1.setHost("152.14.106.22");
        server1.setPort(10001);
    }

    @Test
    public void testGetServers() {
        List<Server> list = controller.getServers(server1);
        assertEquals(9, list.size());

        ArrayList portList = new ArrayList();
        portList.add(10001);
        portList.add(10002);
        portList.add(10003);
        portList.add(10004);
        portList.add(10005);
        portList.add(10006);
        portList.add(10007);
        portList.add(10008);
        portList.add(10009);

        //Check all servers are present. Cannot check for a specific order.
        for(int i=0; i < list.size(); i++) {
            assertEquals(true, portList.contains(list.get(i).getPort()));
        }
    }


    @Test
    public void testSoftRemoveThenAddServers() {
        List<Server> list = controller.getServers(server1);
        assertEquals(9, list.size());

        //Create the new server we are adding.
        Server testServer = new Server();
        testServer.setHost("152.14.106.22");
        testServer.setPort(10003);
        //assertEquals(true, list.contains(testServer));

        //Create the server we are adding the new  server to.
        //Make sure it exists.
        Server existingServer = new Server();
        existingServer.setHost("152.14.106.22");
        existingServer.setPort(10001);
        //assertEquals(true, list.contains(existingServer));

        //remove the server so it may be added.
        //Removes the master node and promotes a slave.
        String result = controller.softRemoveServer(testServer);
        assertEquals(true, result.contains("Successful Failover of 152.14.106.22:10003 Successful Takeover by slave"));
        list = controller.getServers(existingServer);
        assertEquals(8, list.size());

        //Set the servers for the request. We are add the new server
        //to the existing server.
        ServerRequest request = new ServerRequest();
        request.setServer(existingServer);
        request.setServerAdd(testServer);


        //Add only adds the single node; not all of it's slaves.
        result = controller.addServers(request);
        assertEquals("Server 152.14.106.22:10003 added.", result);
        list = controller.getServers(server1);
        assertEquals(9, list.size());
    }

    @Test
    public void testHardRemoveThenAddServers() {
        List<Server> list = controller.getServers(server1);
        assertEquals(9, list.size());

        //Create the new server we are adding.
        Server testServer = new Server();
        testServer.setHost("152.14.106.22");
        testServer.setPort(10003);
        //assertEquals(true, list.contains(testServer));

        //Create the server we are adding the new  server to.
        //Make sure it exists.
        Server existingServer = new Server();
        existingServer.setHost("152.14.106.22");
        existingServer.setPort(10001);
        //assertEquals(true, list.contains(existingServer));

        //remove the server so it may be added.
        //Removes the master node and all of it's slaves
        String result = controller.removeServers(testServer);
        assertEquals("Server 152.14.106.22 10003 removed.", result);
        list = controller.getServers(existingServer);
        assertEquals(6, list.size());

        //Set the servers for the request. We are add the new server
        //to the existing server.
        ServerRequest request = new ServerRequest();
        request.setServer(existingServer);
        request.setServerAdd(testServer);


        //Add only adds the single node; not all of it's slaves.
        result = controller.addServers(request);
        assertEquals("Server 152.14.106.22:10003 added.", result);
        list = controller.getServers(server1);
        assertEquals(7, list.size());

        //Will add both of the slaves back.
        testServer.setPort(10008);
        request.setServerAdd(testServer);
        result = controller.addServers(request);
        assertEquals("Server 152.14.106.22:10008 added.", result);
        list = controller.getServers(server1);

        testServer.setPort(10009);
        request.setServerAdd(testServer);
        result = controller.addServers(request);
        assertEquals("Server 152.14.106.22:10009 added.", result);
        list = controller.getServers(server1);
        assertEquals(9, list.size());


    }



    @Test
    public void testGetQueues() {
        List<String> list = controller.getQueues(server1);
        assertEquals(true, list.size() >= 1);
    }

    /**
    @Test
    public void testAddSlots() {
        Server.Slots s = new Server.Slots();
        s.setBeginningSlot(12001);
        s.setEndSlot(13000);

        Server.Slots slot[] = new Server.Slots[1];
        slot[0] = s;

        server1.setSlots(slot);
        String message = controller.addSlots(server1);
        assertEquals("Slots 12001 to 13000 added to 152.14.106.29:6000", message);
    }
    */

}
