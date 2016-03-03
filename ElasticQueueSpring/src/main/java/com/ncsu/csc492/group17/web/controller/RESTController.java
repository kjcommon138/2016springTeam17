package com.ncsu.csc492.group17.web.controller;

import hello.Server.Slots;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.lambdaworks.redis.cluster.RedisClusterClient;
import com.lambdaworks.redis.cluster.api.StatefulRedisClusterConnection;
import com.lambdaworks.redis.cluster.api.sync.RedisAdvancedClusterCommands;
import com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands;

@RestController
public class RESTController {

	private final String[] HOST_NAMES = {
			"sd-vm12.csc.ncsu.edu",
			"sd-vm19.csc.ncsu.edu",
			"sd-vm20.csc.ncsu.edu", 
	"sd-vm33.csc.ncsu.edu"};

	@RequestMapping(method=RequestMethod.POST, value="/removeServers")
	public String removeServers(@RequestBody Server server) {


		int serverRemovePort = server.getPort();
		String serverRemoveHost = server.getHost();
		//the node to remove
		Server serverRemove = null;
		//a node in the cluster that will get the slots from the removed node
		Server serverKeep = null;


		//connection to server we want to remove
		RedisURI uri1 = new RedisURI();
		uri1.setHost(serverRemoveHost);
		uri1.setPort(serverRemovePort);
		RedisClusterClient redisClient1 = RedisClusterClient.create(uri1);

		StatefulRedisClusterConnection<String, String> connection1 = redisClient1.connect();

		//RedisAdvancedClusterCommands<String, String> commands1 = connection1.sync();
		RedisClusterCommands<String, String> commands1 = connection1.getConnection(serverRemoveHost, serverRemovePort).sync();

		String nodeID = "";
		Slots slots[] = null;

		List<Server> allServers = getServers(server);

		//assigns the serverToRemove and serverToKeep
		for(int i = 0 ; i < allServers.size(); i++){
			Server currentServer = allServers.get(i);
			System.out.println("Current Server: " + currentServer.getPort());
			if(currentServer.getPort() == serverRemovePort && currentServer.getType() != "slave"){
				serverRemove = allServers.get(i);
				nodeID = serverRemove.getNodeID();
				slots = serverRemove.getSlots();
				System.out.println("Server Remove: " + serverRemove.getPort());
				System.out.println(nodeID);
			}else if(!currentServer.getType().equals("slave") && !currentServer.getType().equals("handshake") && serverKeep == null){
				serverKeep = allServers.get(i);
				System.out.println("Server Keep: " + serverKeep.getPort());

			}
		}


		int[] allSlotsToRemove = new int[0];
		//gets all the slots to remove in one array
		for(int i = 0; i < slots.length; i ++){
			int beginningSlots = slots[i].getBeginningSlot();
			int endSlots = slots[i].getEndSlot();
			int[] slotsToRemove = getSlots(beginningSlots, endSlots);

			int[] existingSlotsList = allSlotsToRemove;
			allSlotsToRemove = new int[allSlotsToRemove.length + slotsToRemove.length];

			System.arraycopy(slotsToRemove, 0, allSlotsToRemove, 0, slotsToRemove.length);
			System.arraycopy(existingSlotsList, 0, allSlotsToRemove, slotsToRemove.length, existingSlotsList.length);

		}

		//NEW


		RedisURI uri3 = new RedisURI();
		System.out.println("Server Keep Port: " + serverKeep.getPort());
		uri3.setHost(serverKeep.getHost());
		uri3.setPort(serverKeep.getPort());
		RedisClusterClient redisClient3 = RedisClusterClient.create(uri3);

		StatefulRedisClusterConnection<String, String> connection3 = redisClient3.connect();
		RedisAdvancedClusterCommands<String, String> commands4 = connection3.sync();
		RedisClusterCommands<String, String> commands3 = connection3.getConnection(serverKeep.getHost(), serverKeep.getPort()).sync();

		for(int k = 0; k < allSlotsToRemove.length; k++){
			//Destination node has to issue the importing command
			System.out.println("Importing");
			System.out.println(commands3.clusterSetSlotImporting(allSlotsToRemove[k], serverRemove.getNodeID()));
			//Source node issues migrate command
			System.out.println("Migrating");
			System.out.println(commands1.clusterSetSlotMigrating(allSlotsToRemove[k], serverKeep.getNodeID()));
			//Both nodes set the slot node to the node to keep in the cluster
			System.out.println("Setting");
			System.out.println(commands1.clusterSetSlotNode(allSlotsToRemove[k], serverKeep.getNodeID()));
			System.out.println(commands3.clusterSetSlotNode(allSlotsToRemove[k], serverKeep.getNodeID()));
		}

		connection1.close();
		redisClient1.shutdown();

		connection3.close();
		redisClient3.shutdown();


		//forget the node to remove from every node except for the node itself
		for(int i = 0 ; i < allServers.size(); i++){
			Server currentServer = allServers.get(i);
			if(currentServer.getPort() != serverRemovePort){

				RedisURI uri = new RedisURI();
				uri.setHost(currentServer.getHost());
				uri.setPort(currentServer.getPort());
				RedisClusterClient redisClient = RedisClusterClient.create(uri);

				StatefulRedisClusterConnection<String, String> connection = redisClient.connect();
				RedisClusterCommands<String, String> commands = connection.getConnection(currentServer.getHost(), currentServer.getPort()).sync();

				commands.clusterForget(serverRemove.getNodeID());

				connection.close();
				redisClient.shutdown();
			}

		}


		int numServers = allServers.size() - 1;
		// how many slots each of the remaining servers in cluster will get.
		//int slotsPerServer = numSlots / numServers;
		// extra slots that we will add to the first server in the list
		//int extraSlots = numSlots % numServers;

		//int beginningSlotsToAdd = beginningSlots;
		//int endSlotsToAdd = beginningSlots + slotsPerServer + extraSlots - 1;
		//int[] slotsToAdd = getSlots(beginningSlotsToAdd, endSlotsToAdd);



		return null;
	}

	@RequestMapping(method=RequestMethod.POST, value="/addServers")
	public String addServers(@RequestBody ServerRequest request) {

		//node we are adding
		Server serverAdd = request.getServerAdd();
		String serverAddHost = serverAdd.getHost();
		int serverAddPort = serverAdd.getPort();

		//node that already exists in the cluster
		Server existingServer = request.getServer();
		String existingServerHost = existingServer.getHost();
		int existingServerPort = existingServer.getPort();
		
		//connection to node to add
		RedisURI uri = new RedisURI();
		uri.setHost(serverAddHost);
		uri.setPort(serverAddPort);
		RedisClusterClient redisClient = RedisClusterClient.create(uri);

		StatefulRedisClusterConnection<String, String> connection = redisClient.connect();
		RedisClusterCommands<String, String> commands = connection.getConnection(serverAddHost, serverAddPort).sync();

		//connection to existing cluster node
		RedisURI uriExisting = new RedisURI();
		uriExisting.setHost(existingServerHost);
		uriExisting.setPort(existingServerPort);
		RedisClusterClient redisClientExisting = RedisClusterClient.create(uriExisting);

		StatefulRedisClusterConnection<String, String> connectionExisting = redisClientExisting.connect();
		RedisClusterCommands<String, String> commandsExisting1 = connectionExisting.sync();
		RedisClusterCommands<String, String> commandsExisting = connectionExisting.getConnection(existingServerHost, existingServerPort).sync();
		
		System.out.println(commandsExisting1.clusterMeet(serverAdd.getHost(), serverAdd.getPort()));
		System.out.println(commandsExisting.clusterMeet(serverAdd.getHost(), serverAdd.getPort()));
		
		/*try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		List<Server> allServers = getServers(existingServer);
		for(int i = 0 ; i < allServers.size(); i++){
			Server currentServer = allServers.get(i);
			System.out.println("Current Server: " + currentServer.getPort());
			if(currentServer.getPort() == serverAddPort){
				serverAdd = currentServer;
				System.out.println("Server Add: " + currentServer.getPort());
			}else if(currentServer.getPort() == existingServerPort){
				existingServer = currentServer;
				System.out.println("Existing Server: " + currentServer.getPort());
			}
		}


		int[] allSlots = new int[0];
		Slots[] existingSlots = existingServer.getSlots();
		//gets all the slots to remove in one array
		for(int i = existingSlots.length - 1; i >= 0; i --){
			int beginningSlots = existingSlots[i].getBeginningSlot();
			int endSlots = existingSlots[i].getEndSlot();

			//one range of slots
			int[] slotsRange = getSlots(beginningSlots, endSlots);

			int[] existingSlotsList = allSlots;
			allSlots = new int[allSlots.length + slotsRange.length];

			System.arraycopy(slotsRange, 0, allSlots, 0, slotsRange.length);
			System.arraycopy(existingSlotsList, 0, allSlots, slotsRange.length, existingSlotsList.length);

		}
		System.out.println("SLOTS!!!!!!");
		System.out.println(allSlots[0]);
		System.out.println(allSlots[allSlots.length-1]);
		
		//System.out.println(commandsExisting.clusterMeet(serverAdd.getHost(), serverAdd.getPort()));

		//slots we are migrating from existing to new node
		int[] firstHalf = Arrays.copyOfRange(allSlots, 0, allSlots.length/2);
		
		//resharding/migration of slots
		for(int k = 0; k < firstHalf.length; k++){
			//Destination node has to issue the importing command
			System.out.println("Importing");
			System.out.println(commands.clusterSetSlotImporting(firstHalf[k], existingServer.getNodeID()));
			//Source node issues migrate command
			System.out.println("Migrating");
			System.out.println(serverAdd.getNodeID());
			System.out.println(commandsExisting.clusterSetSlotMigrating(firstHalf[k], serverAdd.getNodeID()));
			//Both nodes set the slot node to the node to keep in the cluster
			System.out.println("Setting");
			System.out.println(commands.clusterSetSlotNode(firstHalf[k], serverAdd.getNodeID()));
			System.out.println(commandsExisting.clusterSetSlotNode(firstHalf[k], serverAdd.getNodeID()));
		}

		connection.close();
		redisClient.shutdown();

		connectionExisting.close();
		redisClientExisting.shutdown();

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
			//server.setType(info[2]);
			server.setType(info[2].indexOf("master") == -1 ? "Slave" : "Master");
			server.setStatus(nodesArray[i].indexOf("disconnected") == -1 ? "Active" : "Disabled");
			


			Slots slots[] = new Slots[info.length - 8];

			for(int k = 8; k < info.length; k ++){
				Slots slot = new Slots();
				System.out.println(info[k]);
				index = info[k].indexOf("-");
				slot.setBeginningSlot(Integer.parseInt(info[k].substring(0, index)));
				slot.setEndSlot(Integer.parseInt(info[k].substring(index + 1)));
				slots[k - 8] = slot;

			}

			server.setSlots(slots);



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

		RedisClient redisClient = new RedisClient(server.getHost(), server.getPort());
		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> commands = connection.sync();

		//Use the cluster client to get all of the queues in the cluster
		/*RedisURI uri = new RedisURI();
		uri.setHost(server.getHost());
		uri.setPort(server.getPort());
		RedisClusterClient redisClient = new RedisClusterClient(uri);

		StatefulRedisClusterConnection<String, String> connection = redisClient.connect();
		RedisAdvancedClusterCommands<String, String> commands = connection.sync();*/

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
		RedisURI uri1 = new RedisURI();
		uri1.setHost(server1.getHost());
		uri1.setPort(server1.getPort());
		RedisClusterClient redisClient = RedisClusterClient.create(uri1);

		/*StatefulRedisClusterConnection<String, String> connection = redisClient.connect();
		RedisAdvancedClusterCommands<String, String> commands = connection.sync();*/

		RedisClusterClient clusterClient = RedisClusterClient.create(uri1);
		StatefulRedisClusterConnection<String, String> connection = clusterClient.connect();

		RedisClusterCommands<String, String> commands = connection.getConnection(server1.getHost(), server1.getPort()).sync();


		//int beginningSlot = server1.getBeginningSlot();
		int beginningSlot = server1.getSlots()[0].getBeginningSlot();
		//int endSlot = server1.getEndSlot();
		int endSlot = server1.getSlots()[0].getEndSlot();
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
		/*RedisURI uri1 = new RedisURI();
		uri1.setHost(server1.getHost());
		uri1.setPort(server1.getPort());
		RedisClusterClient redisClient = RedisClusterClient.create(uri1);


		StatefulRedisClusterConnection<String, String> connection = redisClient.connect();
		RedisAdvancedClusterCommands<String, String> commands = connection.sync();*/
		int beginningSlot = server1.getSlots()[0].getBeginningSlot();
		int endSlot = server1.getSlots()[0].getEndSlot();
		int[] slots = getSlots(beginningSlot, endSlot);


		List<Server> allServers = getServers(server1);


		for(int i = 0 ; i < allServers.size(); i++){
			Server currentServer = allServers.get(i);

			RedisURI uri1 = new RedisURI();
			uri1.setHost(currentServer.getHost());
			uri1.setPort(currentServer.getPort());
			RedisClusterClient redisClient = RedisClusterClient.create(uri1);
			//RedisClusterClient redisClient = new RedisClusterClient(uri1);

			//RedisClient redisClient = new RedisClient(currentServer.getHost(), currentServer.getPort());

			StatefulRedisClusterConnection<String, String> connection = redisClient.connect();
			//RedisAdvancedClusterCommands<String, String> commands = connection.sync();
			RedisClusterCommands<String, String> commands = connection.getConnection(currentServer.getHost(), currentServer.getPort()).sync();


			commands.clusterDelSlots(slots);

			connection.close();
			redisClient.shutdown();

		}


		/*int beginningSlot = server1.getSlots()[0].getBeginningSlot();
		System.out.println(beginningSlot);
		int endSlot = server1.getSlots()[0].getEndSlot();
		System.out.println(endSlot);

		int[] slots = getSlots(beginningSlot, endSlot);*/

		//String a = commands.clusterDelSlots(slots);
		//System.out.println(a);

		//connection.close();
		//redisClient.shutdown();
		//return null;

		return("Slots " + beginningSlot + " to " + endSlot + " removed from " + server1.getHost() + ":" + server1.getPort());

	}
}