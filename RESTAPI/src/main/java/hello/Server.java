package hello;

import java.util.List;

public class Server {

    private String nodeID;
    private String serverInfo;

    public String getNodeID() {
        return nodeID;
    }
    
    public void setNodeID(String nodeID){
    	this.nodeID = nodeID;
    }

    //public List<String> getServersList() {
    public String getServerInfo() {
        return serverInfo;
    }
    
    public void setServerInfo(String serverInfo){
    	this.serverInfo = serverInfo;
    }
}
