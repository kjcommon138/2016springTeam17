package test;

import junit.framework.*;
import junit.framework.Assert;

import org.junit.*;
import org.junit.Test;

import cluster.*;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by laureltimko on 2/25/16.
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
        Server testServer = list.get(0);

        //Check all servers
        assertEquals(testServer.getHost(), "152.14.106.29");
        assertEquals(testServer.getPort(), 6003);

        testServer = list.get(1);
        assertEquals(testServer.getHost(), "152.14.106.29");
        assertEquals(testServer.getPort(), 6000);

        testServer = list.get(2);
        assertEquals(testServer.getHost(), "152.14.106.29");
        assertEquals(testServer.getPort(), 6002);

        testServer = list.get(3);
        assertEquals(testServer.getHost(), "152.14.106.29");
        assertEquals(testServer.getPort(), 6001);
    }

    @Test
    public void testGetQueues() {
        List<String> list = controller.getQueues(server1);
        assertEquals(1, list.size());
        assertEquals("myList2", list.get(0));
    }

   /* @Test
    public void testGetItems() {
        server1.setKey("myList2");
        List<String> list = controller.getItems(server1);
        assertEquals(9, list.size());
        assertEquals("item2", list.get(0));
        assertEquals("item1", list.get(1));
        assertEquals("test", list.get(2));
        assertEquals("f", list.get(3));
        assertEquals("e", list.get(4));
        assertEquals("d", list.get(5));
        assertEquals("c", list.get(6));
        assertEquals("b", list.get(7));
        assertEquals("a", list.get(8));
    }*/

   /* @Test
    public void testRemoveSlots() {
        Server.Slots s = new Server.Slots();
        s.setBeginningSlot(12000);
        s.setEndSlot(13000);

        Server.Slots slot[] = new Server.Slots[1];
        slot[0] = s;

        server1.setSlots(slot);
        String message = controller.removeSlots(server1);
        assertEquals("Slots 12000 to 13000 removed from 152.14.106.29:6000", message);
    }*/

 /*   @Test
    public void testAddSlots() {
        Server.Slots s = new Server.Slots();
        s.setBeginningSlot(12000);
        s.setEndSlot(13000);

        Server.Slots slot[] = new Server.Slots[1];
        slot[0] = s;

        server1.setSlots(slot);
        String message = controller.addSlots(server1);
        assertEquals("Slots 12000 to 13000 added to 152.14.106.29:6000", message);
    }*/


}