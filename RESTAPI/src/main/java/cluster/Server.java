package cluster;


public class Server {
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Server server = (Server) o;

		return nodeID != null ? nodeID.equals(server.nodeID) : server.nodeID == null;

	}

	@Override
	public int hashCode() {
		return nodeID != null ? nodeID.hashCode() : 0;
	}

	private int port;
	private String host;
	private String nodeID;
	private String type;
	private String serverInfo;
	private String key;
	private String status;
	private String slaveOf;
	private String masterHost;
	private String masterPort;
	private double memory;
	private String cpu;

	private Slots slots[];

	public String getMasterHost() {
		return masterHost;
	}

	public void setMasterHost(String masterHost) {
		this.masterHost = masterHost;
	}

	public String getMasterPort() {
		return masterPort;
	}

	public void setMasterPort(String masterPort) {
		this.masterPort = masterPort;
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
	
	public double getMemory() {
		return memory;
	}

	public void setMemory(double memory){
		this.memory = memory;
	}
	
	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu){
		this.cpu = cpu;
	}
}
