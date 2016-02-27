package com.ncsu.csc492.group17.web.controller;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.lambdaworks.redis.cluster.RedisClusterClient;
import com.lambdaworks.redis.cluster.api.StatefulRedisClusterConnection;
import com.lambdaworks.redis.cluster.api.sync.RedisAdvancedClusterCommands;

import com.ncsu.csc492.group17.web.model.Server;
import com.ncsu.csc492.group17.web.model.ServerRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RESTController {

	private final String[] HOST_NAMES = {
			"sd-vm12.csc.ncsu.edu",
			"sd-vm19.csc.ncsu.edu",
			"sd-vm20.csc.ncsu.edu", 
	"sd-vm33.csc.ncsu.edu"};

	@RequestMapping(method=RequestMethod.POST, value="/removeServers")
	public String removeServers(@RequestBody Server server) {

		//connection to server we want to remove
		RedisURI uri1 = new RedisURI();
		uri1.setHost(server.getHost());
		uri1.setPort(server.getPort());
		RedisClusterClient redisClient1 = new RedisClusterClient(uri1);

		StatefulRedisClusterConnection<String, String> connection1 = redisClient1.connect();
		RedisAdvancedClusterCommands<String, String> commands1 = connection1.sync();


		int beginningSlots = 0;
		int endSlots = 0;
		int numSlots = endSlots - beginningSlots + 1;
		String nodeID = "";

		List<Server> allServers = getServers(server);
		for(int i = 0 ; i < allServers.size(); i++){
			if(allServers.get(i).getPort() == server.getPort() && allServers.get(i).getType() != "slave"){
				Server serverRemoveInfo = allServers.get(i);
				beginningSlots = serverRemoveInfo.getBeginningSlot();
				endSlots = serverRemoveInfo.getEndSlot();
				nodeID = serverRemoveInfo.getNodeID();
			}
		}

		System.out.println(beginningSlots);
		System.out.println(endSlots);
		//remove slots from the node we want to remove
		int[] slotsToRemove = getSlots(beginningSlots, endSlots);
		commands1.clusterDelSlots(slotsToRemove);
		connection1.close();
		redisClient1.shutdown();

		int numServers = allServers.size() - 1;
		// how many slots each of the remaining servers in cluster will get.
		int slotsPerServer = numSlots / numServers;
		// extra slots that we will add to the first server in the list
		int extraSlots = numSlots % numServers;

		int beginningSlotsToAdd = beginningSlots;
		int endSlotsToAdd = beginningSlots + slotsPerServer + extraSlots - 1;
		int[] slotsToAdd = getSlots(beginningSlotsToAdd, endSlotsToAdd);

		String host = "";
		int port = 0;
		//get every node in the cluster to delete/forget the slot
		for(int i = 0 ; i < allServers.size(); i++){

			if(allServers.get(i).getType() != "slave"){
				int p = allServers.get(i).getPort();
				int sp = server.getPort();

				if(p != sp){

					host = server.getHost();
					port = server.getPort();


					RedisURI uri3 = new RedisURI();
					uri3.setHost(allServers.get(i).getHost());
					uri3.setPort(allServers.get(i).getPort());
					RedisClusterClient redisClient3 = new RedisClusterClient(uri3);

					StatefulRedisClusterConnection<String, String> connection3 = redisClient3.connect();
					RedisAdvancedClusterCommands<String, String> commands3 = connection3.sync();

					System.out.println("TEST");
					System.out.println(p);
					System.out.println(sp);
					System.out.println(port);
					commands3.clusterDelSlots(slotsToRemove);
					commands3.clusterForget(nodeID);

					connection3.close();
					redisClient3.shutdown();
					break;

				}
			}
		}


		RedisURI uri2 = new RedisURI();
		uri2.setHost(host);
		uri2.setPort(port);
		RedisClusterClient redisClient2 = new RedisClusterClient(uri2);

		StatefulRedisClusterConnection<String, String> connection2 = redisClient2.connect();
		RedisAdvancedClusterCommands<String, String> commands2 = connection2.sync();

		//add removed slots  to the other node
		commands2.clusterAddSlots(slotsToRemove);


		connection2.close();
		redisClient2.shutdown();



		return null;
	}

	@RequestMapping(method=RequestMethod.POST, value="/addServers")
	public String addServers(@RequestBody ServerRequest request) {

		Server server = request.getServerAdd();
		Server existingServer = request.getServer();
		//RedisURI uri = new RedisURI();
		//uri.setHost(server.getHost());
		//uri.setPort(server.getPort());
		RedisClient redisClient = new RedisClient(server.getHost(), server.getPort());

		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> commands = connection.sync();


		int beginningSlots = 0;
		int endSlots = 0;
		
		RedisURI uri1 = new RedisURI();
		uri1.setHost(existingServer.getHost());
		uri1.setPort(existingServer.getPort());
		RedisClusterClient redisClient1 = new RedisClusterClient(uri1);

		StatefulRedisClusterConnection<String, String> connection1 = redisClient1.connect();
		RedisAdvancedClusterCommands<String, String> commands1 = connection1.sync();

		List<Server> allServers = getServers(existingServer);
		for(int i = 0 ; i < allServers.size(); i++){
			if(allServers.get(i).getPort() == existingServer.getPort() && allServers.get(i).getType() != "slave"){
				Server serverRemoveInfo = allServers.get(i);
				beginningSlots = serverRemoveInfo.getBeginningSlot();
				endSlots = serverRemoveInfo.getEndSlot();
				System.out.println(beginningSlots);
				System.out.println(endSlots);
				
				int [] slots = getSlots(beginningSlots, endSlots);
				int[] firstHalf = Arrays.copyOfRange(slots, 0, slots.length/2);
				
				//deleting slots from existing node
				System.out.println(commands1.clusterDelSlots(firstHalf));

				//adding slots to connection with new node
				System.out.println(commands.clusterAddSlots(firstHalf));

				System.out.println(commands1.clusterMeet(server.getHost(), server.getPort()));
						
				connection1.close();
				redisClient1.shutdown();
				
				break;
			}
		}


		connection.close();
		redisClient.shutdown();

		return null;

	}

	@RequestMapping(method=RequestMethod.POST, value="/getServers")
	public List<Server> getServers(@RequestBody Server server1) {

		/*RedisClient redisClient = new RedisClient(server1.getHost(), server1.getPort());

		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> commands = connection.sync();*/

		RedisURI uri = new RedisURI();
		uri.setHost(server1.getHost());
		uri.setPort(server1.getPort());
		RedisClusterClient redisClient = new RedisClusterClient(uri);

		StatefulRedisClusterConnection<String, String> connection = redisClient.connect();
		RedisAdvancedClusterCommands<String, String> commands = connection.sync();

		String nodes = commands.clusterNodes();
		//splits on new line
		String nodesArray[] = nodes.split("\\r?\\n");

		List<Server> servers = new ArrayList<Server>();

		for(int i = 0; i < nodesArray.length; i++){
			System.out.println(nodesArray[i]);

			String info [] = nodesArray[i].split("\\s+");

			Server server = new Server();

			server.setNodeID(info[0]);

			String hostAndPort = info[1];
			int index = info[1].indexOf(":");
			System.out.println(info[1]);

			server.setHost(info[1].substring(0, index));
			server.setPort(Integer.parseInt(info[1].substring(index + 1)));
			server.setType(info[2].indexOf("master") == -1 ? "Slave" : "Master");
			server.setStatus(nodesArray[i].indexOf("disconnected") == -1 ? "Active" : "Disabled");

			System.out.println(info[7]);

			if(info.length >= 9){
				System.out.println(info[8]);
				index = info[8].indexOf("-");
				server.setBeginningSlot(Integer.parseInt(info[8].substring(0, index)));
				server.setEndSlot(Integer.parseInt(info[8].substring(index + 1)));
			}

			servers.add(server);

		}


		String info = commands.clusterInfo();
		System.out.println("INFO");
		System.out.println(info);

		redisClient.shutdown();

		return servers;
	}

	@RequestMapping(method=RequestMethod.POST, value="/getQueues")
	public List<String> getQueues(@RequestBody Server server) {

		/*RedisClient redisClient = new RedisClient(server.getHost(), server.getPort());
		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> commands = connection.sync();*/

		RedisURI uri = new RedisURI();
		uri.setHost(server.getHost());
		uri.setPort(server.getPort());
		RedisClusterClient redisClient = new RedisClusterClient(uri);

		StatefulRedisClusterConnection<String, String> connection = redisClient.connect();
		RedisAdvancedClusterCommands<String, String> commands = connection.sync();

		//gets slots distribution for all servers in cluster
		/*List<Object> clusterSlots = commands.clusterSlots();

		String serversInfo[][] = new String[clusterSlots.size()][4];
		String clusterSlotsArray[] = null;
		for(int i = 0; i < clusterSlots.size(); i++){
			String clusterSlotsInfo = clusterSlots.get(i).toString();
			clusterSlotsInfo = clusterSlotsInfo.replace("[", "").replace("]", "");
			clusterSlotsArray = clusterSlotsInfo.split("\\s*,\\s*");
			for(int j = 0; j < clusterSlotsArray.length; j++){
				System.out.println(clusterSlotsArray[j]);
				serversInfo[i][j] = clusterSlotsArray[j];
			}

		}*/

		List<String> items = commands.clusterGetKeysInSlot(0, 15999);
		System.out.println("servers info: " + items);
		List<String> keys = commands.keys("*");
		int size = keys.size();

		for(int i = 0; i < size; i++)
			keys.add(Integer.toString(commands.lrange(keys.get(i), 0, -1).size()));

		redisClient.shutdown();

		return keys;

	}

	@RequestMapping(method=RequestMethod.POST, value="/getItems")
	public List<String> getItems(@RequestBody Server server) {

		/*RedisClient redisClient = new RedisClient(server.getHost(), server.getPort());
		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> commands = connection.sync();*/

		RedisURI uri = new RedisURI();
		uri.setHost(server.getHost());
		uri.setPort(server.getPort());
		RedisClusterClient redisClient = new RedisClusterClient(uri);

		StatefulRedisClusterConnection<String, String> connection = redisClient.connect();
		RedisAdvancedClusterCommands<String, String> commands = connection.sync();

		//String items = commands.get(server.getKey());
		List<String> itemsList = commands.lrange(server.getKey(), 0, -1);

		return itemsList;
	}

	@RequestMapping(method=RequestMethod.POST, value="/addSlots")
	public String addSlots(@RequestBody Server server1) {
		RedisClient redisClient = new RedisClient(server1.getHost(), server1.getPort());

		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> commands = connection.sync();

		/*RedisURI uri = new RedisURI();
		uri.setHost(server1.getHost());
		uri.setPort(server1.getPort());
		RedisClusterClient redisClient = new RedisClusterClient(uri);

		StatefulRedisClusterConnection<String, String> connection = redisClient.connect();
		RedisAdvancedClusterCommands<String, String> commands = connection.sync();*/

		int beginningSlot = server1.getBeginningSlot();
		int endSlot = server1.getEndSlot();
		int numSlots = endSlot - beginningSlot + 1;

		int[] slots = new int[numSlots];

		for(int i = beginningSlot; i < endSlot + 1; i ++){
			slots[i-beginningSlot] = i;
			//System.out.println(i-beginningSlot);
			//System.out.println(i);
		}

		String a = commands.clusterAddSlots(slots);
		System.out.println(a);

		connection.close();
		redisClient.shutdown();

		return("Slots " + beginningSlot + " to " + endSlot + " added to " + server1.getHost() + ":" + server1.getPort());

	}

	public int[] getSlots(int beginningSlot, int endSlot) {

		int numSlots = endSlot - beginningSlot + 1;

		System.out.println(numSlots);
		int[] slots = new int[numSlots];

		for(int i = beginningSlot; i < endSlot + 1; i ++){
			slots[i-beginningSlot] = i;
		}

		return slots;

	}

	@RequestMapping(method=RequestMethod.POST, value="/removeSlots")
	public String removeSlots(@RequestBody Server server1) {
		RedisClient redisClient = new RedisClient(server1.getHost(), server1.getPort());

		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> commands = connection.sync();

		/*RedisURI uri = new RedisURI();
		uri.setHost(server1.getHost());
		uri.setPort(server1.getPort());
		RedisClusterClient redisClient = new RedisClusterClient(uri);

		StatefulRedisClusterConnection<String, String> connection = redisClient.connect();
		RedisAdvancedClusterCommands<String, String> commands = connection.sync();*/

		int beginningSlot = server1.getBeginningSlot();
		System.out.println(beginningSlot);
		int endSlot = server1.getEndSlot();
		System.out.println(endSlot);
		int numSlots = endSlot - beginningSlot + 1;

		System.out.println(numSlots);
		int[] slots = new int[numSlots];

		for(int i = beginningSlot; i < endSlot + 1; i ++){
			slots[i-beginningSlot] = i;
		}

		String a = commands.clusterDelSlots(slots);
		System.out.println(a);

		connection.close();
		redisClient.shutdown();

		return("Slots " + beginningSlot + " to " + endSlot + " removed from " + server1.getHost() + ":" + server1.getPort());

	}
}