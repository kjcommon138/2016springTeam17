package hello;

import java.util.List;

public class Server {

	private int port;
	private String host;
    private String nodeID;
    private String serverInfo;
    private String key;
    
	public int getPort(){
		return port;
	}
	
	public void setPort(int port){
		this.port = port;
	}
	
	public String getKey(){
		return key;
	}
	
	public void setKey(String key){
		this.key = key;
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
