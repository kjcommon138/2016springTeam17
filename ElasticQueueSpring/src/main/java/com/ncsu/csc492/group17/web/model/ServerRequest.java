package com.ncsu.csc492.group17.web.model;

public class ServerRequest {

	private Server server;
	private Server serverRemove;
	private Server serverAdd;
	
	public Server getServer(){
		return server;
	}
	
	public void setServer(Server server){
		this.server = server;
	}
	
	public Server getServerRemove(){
		return serverRemove;
	}
	
	public void setServerRemove(Server serverRemove){
		this.serverRemove = serverRemove;
	}
	
	public Server getServerAdd(){
		return serverAdd;
	}
	
	public void setServerAdd(Server serverAdd){
		this.serverAdd = serverAdd;
	}
	
	
}
