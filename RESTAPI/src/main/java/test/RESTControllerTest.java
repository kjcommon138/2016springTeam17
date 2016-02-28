package test;

import hello.*;

import junit.framework.*;
import org.junit.*;
import org.junit.Test;

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
        server1.setPort(8000);

    }

    @Test
    public void testGetServers() {
        List<Server> list = controller.getServers(server1);
        assertEquals(4, list.size());
        Server testServer = list.get(0);

        //Check all servers
        assertEquals(testServer.getHost(), "152.14.106.29");
        assertEquals(testServer.getPort(), 8004);

        testServer = list.get(1);
        assertEquals(testServer.getHost(), "152.14.106.29");
        assertEquals(testServer.getPort(), 8001);

        testServer = list.get(2);
        assertEquals(testServer.getHost(), "152.14.106.29");
        assertEquals(testServer.getPort(), 8003);

        testServer = list.get(3);
        assertEquals(testServer.getHost(), "152.14.106.29");
        assertEquals(testServer.getPort(), 8000);
    }

    @Test
    public void testGetQueues() {
        List<String> list = controller.getQueues(server1);
        assertEquals(4, list.size());
        assertEquals("testList", list.get(0));
        assertEquals("bhalodia", list.get(1));
        assertEquals("sneha", list.get(2));
        assertEquals("foo", list.get(3));
    }


}
