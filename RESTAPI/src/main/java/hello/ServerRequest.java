package hello;

public class ServerRequest {
	
	private int port;
	private String host;
	private String nodeID;
	
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

	public String getNodeID(){
		return nodeID;
	}
	
	public void setNodeID(String nodeID){
		this.nodeID = nodeID;
	}
}
