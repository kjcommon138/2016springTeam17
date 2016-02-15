package hello;

import java.util.List;

public class Server {

	private int port;
	private String host;
    private String nodeID;
    private String serverInfo;
    
	public int getPort(){
		return port;
	}
	
	public void setPort(int port){
		this.port = port;
	}
	
	public String getHost(){
		return host;
	}
	
	public void setHost(String host){
		this.host = host;
	}

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
