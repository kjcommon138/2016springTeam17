package com.ncsu.csc492.group17.test;

import com.ncsu.csc492.group17.web.model.*;

import org.junit.*;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by laureltimko on 3/3/16.
 */
public class ServerRequestTest {

    Server server1;
    ServerRequest request;

    @Before
    public void setUp() throws Exception {
        request = new ServerRequest();
        server1 = new Server();
        server1.setHost("111.22.33.444");
        server1.setPort(1000);

    }


    @Test
    public void testGetServer() throws Exception {
        request.setServer(server1);
        assertEquals(request.getServer(), server1);
    }

    @Test
    public void testSetServer() throws Exception {
        request.setServer(server1);
        assertEquals(request.getServer(), server1);
    }

    @Test
    public void testGetServerRemove() throws Exception {
        request.setServerRemove(server1);
        assertEquals(request.getServerRemove(), server1);
    }

    @Test
    public void testSetServerRemove() throws Exception {
        request.setServerRemove(server1);
        assertEquals(request.getServerRemove(), server1);
    }

    @Test
    public void testGetServerAdd() throws Exception {
        request.setServerAdd(server1);
        assertEquals(request.getServerAdd(), server1);
    }

    @Test
    public void testSetServerAdd() throws Exception {
        request.setServerAdd(server1);
        assertEquals(request.getServerAdd(), server1);
    }
}
