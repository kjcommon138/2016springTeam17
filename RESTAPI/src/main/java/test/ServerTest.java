package test;

import hello.Server;
import hello.Server.Slots;

import org.junit.*;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by laureltimko on 2/25/16.
 */
public class ServerTest {

    Server myServer = new Server();
    Slots slot[] = new Slots[1];

    @Test
    public void testSetBeginningSlot() throws Exception {
    	myServer.getSlots()[0].setBeginningSlot(1001);
        assertEquals(myServer.getSlots()[0].getBeginningSlot(), 1001);
    }

    @Test
    public void testGetBeginningSlot() throws Exception {
        myServer.getSlots()[0].setBeginningSlot(2001);
        assertEquals(myServer.getSlots()[0].getBeginningSlot(), 2001);
    }

    @Test
    public void testSetEndSlot() throws Exception {
    	myServer.getSlots()[0].setEndSlot(1001);
        assertEquals(myServer.getSlots()[0].getEndSlot(), 1001);
    }

    @Test
    public void testGetEndSlot() throws Exception {
    	myServer.getSlots()[0].setEndSlot(2001);
        assertEquals(myServer.getSlots()[0].getEndSlot(), 2001);
    }

    @Test
    public void testSetPort() throws Exception {
        myServer.setPort(30001);
        assertEquals(myServer.getPort(), 30001);
    }

    @Test
    public void testGetPort() throws Exception {
        myServer.setPort(30002);
        assertEquals(myServer.getPort(), 30002);
    }

    @Test
    public void testSetKey() throws Exception {
        myServer.setKey("test");
        assertEquals(myServer.getKey(), "test");
    }

    @Test
    public void testGetKey() throws Exception {
        myServer.setKey("test2");
        assertEquals(myServer.getKey(), "test2");
    }

    @Test
    public void testSetType() throws Exception {
        myServer.setType("test");
        assertEquals(myServer.getType(), "test");
    }

    @Test
    public void testGetType() throws Exception {
        myServer.setType("test2");
        assertEquals(myServer.getType(), "test2");
    }

    @Test
    public void testSetHost() throws Exception {
        myServer.setHost("111.11.11.111");
        assertEquals(myServer.getHost(), "111.11.11.111");
    }

    @Test
    public void testGetHost() throws Exception {
        myServer.setHost("111.22.33.444");
        assertEquals(myServer.getHost(), "111.22.33.444");
    }

    @Test
    public void testSetNodeID() throws Exception {
        myServer.setNodeID("node1");
        assertEquals(myServer.getNodeID(), "node1");
    }

    @Test
    public void testGetNodeID() throws Exception {
        myServer.setNodeID("node2");
        assertEquals(myServer.getNodeID(), "node2");
    }

    @Test
    public void testSetServerInfo() throws Exception {
        myServer.setServerInfo("item1, item2, item3");
        assertEquals(myServer.getServerInfo(), "item1, item2, item3");
    }

    @Test
    public void testGetServerInfo() throws Exception {
        myServer.setServerInfo("item1, item2");
        assertEquals(myServer.getServerInfo(), "item1, item2");
    }


}
