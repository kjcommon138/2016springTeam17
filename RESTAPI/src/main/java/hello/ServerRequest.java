package hello;

public class ServerRequest {
	
	
	private int port;
	private String host;
	private String nodeID;
	
	//for servers that are already a part of the cluster and will remain in cluster
	private int portCluster;
	private String hostCluster;
	
	
	public int getPort(){
		return port;
	}
	
	public void setPort(int port){
		this.port = port;
	}
	
	public int getPortCluster(){
		return portCluster;
	}
	
	public void setPortCluster(int port){
		this.portCluster = port;
	}
	
	public String getHost(){
		return host;
	}
	
	public void setHost(String host){
		this.host = host;
	}
	
	public String getHostCluster(){
		return hostCluster;
	}
	
	public void setHostCluster(String host){
		this.hostCluster = host;
	}

	public String getNodeID(){
		return nodeID;
	}
	
	public void setNodeIDr(String nodeID){
		this.nodeID = nodeID;
	}
}
