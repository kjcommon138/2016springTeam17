package com.ncsu.csc492.group17.test;

import com.ncsu.csc492.group17.web.model.*;
import com.ncsu.csc492.group17.web.controller.*;
import com.ncsu.csc492.group17.cluster.*;

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
        server1.setHost("152.14.106.29");
        server1.setPort(6000);

    }

    @Test
    public void testGetServers() {
        List<Server> list = controller.getServers(server1);
        assertEquals(4, list.size());

        Server test0 = new Server();
        test0.setHost("152.14.106.29");
        test0.setPort(6000);

        Server test1 = new Server();
        test1.setHost("152.14.106.29");
        test1.setPort(6001);

        Server test2 = new Server();
        test2.setHost("152.14.106.29");
        test2.setPort(6002);

        Server test3 = new Server();
        test3.setHost("152.14.106.29");
        test3.setPort(6003);

        //Check all servers are present. Cannot check for a specific order.
        assertEquals(list.contains(test0), true);
        assertEquals(list.contains(test1), true);
        assertEquals(list.contains(test2), true);
        assertEquals(list.contains(test3), true);
    }

    @Test
    public void testRemoveThenAddServers() {
        List<Server> list = controller.getServers(server1);
        assertEquals(4, list.size());

        //Create the new server we are adding.
        Server testServer = new Server();
        testServer.setHost("152.14.106.29");
        testServer.setPort(6003);
        assertEquals(list.contains(testServer), true);

        //Create the server we are adding the new  server to.
        //Make sure it exists.
        Server existingServer = new Server();
        existingServer.setHost("152.14.106.29");
        existingServer.setPort(6000);
        assertEquals(list.contains(existingServer), true);

        //remove the server so it may be added.
        String result = controller.removeServers(testServer);
        assertEquals("Server 152.14.106.29 6003 removed.", result);
        list = controller.getServers(server1);
        assertEquals(3, list.size());

        //Set the servers for the request. We are add the new server
        //to the existing server.
        ServerRequest request = new ServerRequest();
        request.setServer(existingServer);
        request.setServerAdd(testServer);

        result = controller.addServers(request);
        assertEquals("Server 152.14.106.296003 added.", result);
        list = controller.getServers(server1);
        assertEquals(4, list.size());

    }

    /**
    @Test
    public void testGetQueues() {
        List<String> list = controller.getQueues(server1);
        assertEquals(1, list.size());
        assertEquals("myList2", list.get(0));
    }

    @Test
    public void testAddSlots() {
        Server.Slots s = new Server.Slots();
        s.setBeginningSlot(12000);
        s.setEndSlot(13000);

        Server.Slots slot[] = new Server.Slots[1];
        slot[0] = s;

        server1.setSlots(slot);
        String message = controller.addSlots(server1);
        assertEquals("Slots 12000 to 13000 added to 152.14.106.29:6000", message);
    }

    */

}
