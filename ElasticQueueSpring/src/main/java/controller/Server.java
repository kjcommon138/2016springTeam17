package controller;

public class Server {

	private int port;
	private String host;
    private String nodeID;
    private String type;
    private String serverInfo;
    private String key;
    private int beginningSlot;
    private int endSlot;
    
	public int getBeginningSlot(){
		return beginningSlot;
	}
	
	public void setBeginningSlot(int beginningSlot){
		this.beginningSlot = beginningSlot;
	}
	
	public int getEndSlot(){
		return endSlot;
	}
	
	public void setEndSlot(int endSlot){
		this.endSlot = endSlot;
	}
    
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
	
	public String getType(){
		return type;
	}
	
	public void setType(String type){
		this.type = type;
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
