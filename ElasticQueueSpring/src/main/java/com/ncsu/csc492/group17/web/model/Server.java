package com.ncsu.csc492.group17.web.model;


public class Server {

	private int port;
	private String host;
	private String nodeID;
	private String type;
	private String serverInfo;
	private String key;
	private String status;
	private String slaveOf;

	private Slots slots[];

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Server server = (Server) o;

		if (port != server.port) return false;
		return host.equals(server.host);

	}

	@Override
	public int hashCode() {
		int result = port;
		result = 31 * result + host.hashCode();
		return result;
	}


	public static class Slots{
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

	};

	public Slots[] getSlots(){
		return slots;
	}

	public void setSlots(Slots [] slots){
		this.slots = slots;
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

	public String getStatus(){
		return status;
	}

	public void setStatus(String status){
		this.status = status;
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

	public String getServerInfo() {
		return serverInfo;
	}

	public void setServerInfo(String serverInfo){
		this.serverInfo = serverInfo;
	}

	public String getSlaveOf() {
		return slaveOf;
	}

	public void setSlaveOf(String slaveOf){
		this.slaveOf = slaveOf;
	}
}
