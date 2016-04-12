package hello;

import hello.Server.Slots;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lambdaworks.redis.RedisURI;
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

	@RequestMapping(method = RequestMethod.POST, value = "/softRemoveServer")
	public String softRemoveServer(@RequestBody Server server) {
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

		List<Server> allServers = getServers(server);

		//assigns the serverToRemove and serverToKeep
		for (int i = 0; i < allServers.size(); i++) {
			Server currentServer = allServers.get(i);
			if (currentServer.getPort() == serverRemovePort && currentServer.getHost().equals(serverRemoveHost) && currentServer.getType().equalsIgnoreCase("slave")) {
				try {
					commands1.clusterFailover(true);
				} catch (Exception e) {
					return "Error with Cluster Failover of " + serverRemoveHost + ":" + serverRemovePort;
				}
			}
		}

		return "Successful Failover of " + serverRemoveHost + ":" + serverRemovePort;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/softRemoveServer2")
	public String softRemoveServer2(@RequestBody Server server) {
		int serverRemovePort = server.getPort();
		String serverRemoveHost = server.getHost();
		//the node to remove
		Server serverRemove = null;
		//a node in the cluster that will take over
		Server serverKeep = null;

		List<Server> allServers = getServers(server);


		//get all information for server to remove
		for (int i = 0; i < allServers.size(); i++) {
			Server currentServer = allServers.get(i);
			if (currentServer.getPort() == serverRemovePort && currentServer.getHost().equals(serverRemoveHost) && !currentServer.getType().equalsIgnoreCase("slave")) {
				serverRemove = allServers.get(i);
				System.out.println("Server Remove: " + serverRemove.getPort());
			}
		}

		//assigns the slave serverToKeep
		for (int i = 0; i < allServers.size(); i++) {
			Server currentServer = allServers.get(i);
			if (currentServer.getType().equalsIgnoreCase("slave") && !currentServer.getType().equalsIgnoreCase("handshake") && serverKeep == null && currentServer.getSlaveOf() == serverRemove.getNodeID()) {
				serverKeep = allServers.get(i);
				System.out.println("Server Keep: " + serverKeep.getPort());
			}
		}

		//connection to slave we want to take over the master
		RedisURI uri2 = new RedisURI();
		uri2.setHost(serverKeep.getHost());
		uri2.setPort(serverKeep.getPort());
		RedisClusterClient redisClient2 = RedisClusterClient.create(uri2);

		StatefulRedisClusterConnection<String, String> connection2 = redisClient2.connect();

		RedisClusterCommands<String, String> commands2 = connection2.getConnection(serverRemoveHost, serverRemovePort).sync();

		commands2.clusterFailover(true);
		System.out.print("Failover: " + commands2.clusterFailover(true));
		
		connection2.close();
		redisClient2.shutdown();


		//forget the node to remove from every node except for the node itself
		for (int i = 0; i < allServers.size(); i++) {
			Server currentServer = allServers.get(i);
			if (currentServer.getPort() != serverRemovePort || !currentServer.getHost().equals(serverRemoveHost)) {

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


		return "Successful Failover of " + serverRemoveHost + ":" + serverRemovePort;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/removeServers")
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
		for (int i = 0; i < allServers.size(); i++) {
			Server currentServer = allServers.get(i);
			System.out.println("Current Server: " + currentServer.getPort());
			if (currentServer.getPort() == serverRemovePort && currentServer.getHost().equals(serverRemoveHost) && !currentServer.getType().equalsIgnoreCase("slave")) {
				serverRemove = allServers.get(i);
				nodeID = serverRemove.getNodeID();
				slots = serverRemove.getSlots();
				System.out.println("Server Remove: " + serverRemove.getPort());
				System.out.println(nodeID);
			} else if (!currentServer.getType().equalsIgnoreCase("slave") && !currentServer.getType().equalsIgnoreCase("handshake") && serverKeep == null) {
				serverKeep = allServers.get(i);
				System.out.println("Server Keep: " + serverKeep.getPort());
			}
		}

		//gets all the slaves for the server to remove
		//Server [] slaves = new Server[0];
		List<Server> slaves = new ArrayList<Server>();
		for (int i = 0; i < allServers.size(); i++) {
			Server currentServer = allServers.get(i);
			if (currentServer.getType().equalsIgnoreCase("slave") && currentServer.getSlaveOf().equals(serverRemove.getNodeID())) {
				slaves.add(currentServer);
				System.out.println("Slave: " + currentServer.getPort());
			}
		}


		int[] allSlotsToRemove = new int[0];
		//gets all the slots to remove in one array
		for (int i = 0; i < slots.length; i++) {
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

		for (int k = 0; k < allSlotsToRemove.length; k++) {
			//Source node gets keys
			int numKeys = (int) (long) commands1.clusterCountKeysInSlot(allSlotsToRemove[k]);
			//only migrate keys if there are keys
			if (numKeys > 0) {
				//Destination node has to issue the importing command
				System.out.println("Set Importing");
				System.out.println(commands3.clusterSetSlotImporting(allSlotsToRemove[k], serverRemove.getNodeID()));
				//Source node issues set migrate command
				System.out.println("Set Migrating");
				System.out.println(commands1.clusterSetSlotMigrating(allSlotsToRemove[k], serverKeep.getNodeID()));
				//Get all the keys in the current slot
				System.out.println("Getting Keys");
				List<String> keys = commands1.clusterGetKeysInSlot(allSlotsToRemove[k], numKeys);
				//Source node sends migrate command
				System.out.println("Migrating");
				for (int i = 0; i < keys.size(); i++) {
					System.out.println(commands1.migrate(serverKeep.getHost(), serverKeep.getPort(), keys.get(i), 0, 1000));
				}
			}
			//Both nodes set the slot node to the node to keep in the cluster
			System.out.println("Setting");
			System.out.println(commands1.clusterSetSlotNode(allSlotsToRemove[k], serverKeep.getNodeID()));
			System.out.println(commands3.clusterSetSlotNode(allSlotsToRemove[k], serverKeep.getNodeID()));
		}

		connection1.close();
		redisClient1.shutdown();

		connection3.close();
		redisClient3.shutdown();


		//forget all the slaves of node to remove from every node
		if (slaves.size() > 0) {
			for (int i = 0; i < allServers.size(); i++) {
				Server currentServer = allServers.get(i);

				RedisURI uri = new RedisURI();
				uri.setHost(currentServer.getHost());
				uri.setPort(currentServer.getPort());
				RedisClusterClient redisClient = RedisClusterClient.create(uri);

				StatefulRedisClusterConnection<String, String> connection = redisClient.connect();
				RedisClusterCommands<String, String> commands = connection.getConnection(currentServer.getHost(), currentServer.getPort()).sync();

				//System.out.println(slaves.size());

				for (int k = 0; k < slaves.size(); k++) {
					//System.out.println(currentServer.getPort());
					if (currentServer.getPort() != slaves.get(k).getPort()) {
						commands.clusterForget(slaves.get(k).getNodeID());
					}
				}

				connection.close();
				redisClient.shutdown();
			}
		}

		//forget the node to remove from every node except for the node itself
		for (int i = 0; i < allServers.size(); i++) {
			Server currentServer = allServers.get(i);
			String slaveOf = "";
			slaveOf = currentServer.getSlaveOf();
			if (currentServer.getSlaveOf() == null) {
				slaveOf = "";
			} else {
				slaveOf = currentServer.getSlaveOf();
			}

			//if(currentServer.getPort() != serverRemovePort && !currentServer.getType().equalsIgnoreCase("slave")){
			//if(currentServer.getPort() != serverRemovePort && !a.equals(serverRemove.getNodeID())){
			if ((currentServer.getPort() != serverRemovePort || !currentServer.getHost().equals(serverRemoveHost)) && !slaveOf.equals(serverRemove.getNodeID())) {

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


		System.out.println("Server " + server.getHost() + " " + server.getPort() + " removed.");
		return "Server " + server.getHost() + " " + server.getPort() + " removed.";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/addServers")
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
		System.out.println(commands.clusterMeet(serverAdd.getHost(), serverAdd.getPort()));

		/*try {
            Thread.sleep(7500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/


		while (serverAdd.getNodeID() == null) {
			System.out.println("Searching for node");
			List<Server> allServers = getServers(existingServer);
			for (int i = 0; i < allServers.size(); i++) {
				Server currentServer = allServers.get(i);
				System.out.println("Current Server: " + currentServer.getPort());
				if (currentServer.getPort() == serverAddPort && currentServer.getHost().equals(serverAddHost)) {
					serverAdd = currentServer;
					System.out.println("Server Add: " + currentServer.getPort());
				} else if (currentServer.getPort() == existingServerPort && currentServer.getHost().equals(existingServerHost)) {
					existingServer = currentServer;
					System.out.println("Existing Server: " + currentServer.getPort());
				}
			}
			try {
				Thread.sleep(1000);
				System.out.println("Sleep");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}


		int[] allSlots = new int[0];
		Slots[] existingSlots = existingServer.getSlots();
		//gets all the slots to remove in one array
		for (int i = existingSlots.length - 1; i >= 0; i--) {
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
		System.out.println(allSlots[allSlots.length - 1]);

		//System.out.println(commandsExisting.clusterMeet(serverAdd.getHost(), serverAdd.getPort()));

		//slots we are migrating from existing to new node
		int[] firstHalf = Arrays.copyOfRange(allSlots, 0, allSlots.length / 2);

		//resharding/migration of slots
		for (int k = 0; k < firstHalf.length; k++) {
			//Source node gets keys
			int numKeys = (int) (long) commandsExisting.clusterCountKeysInSlot(firstHalf[k]);
			//only migrate keys if there are keys
			if (numKeys > 0) {
				//Destination node has to issue the importing command
				System.out.println("Set Importing");
				System.out.println(commands.clusterSetSlotImporting(firstHalf[k], existingServer.getNodeID()));
				//Source node issues migrate command
				System.out.println("Set Migrating");
				System.out.println(commandsExisting.clusterSetSlotMigrating(firstHalf[k], serverAdd.getNodeID()));
				System.out.println("Getting Keys");
				List<String> keys = commandsExisting.clusterGetKeysInSlot(firstHalf[k], numKeys);
				//Source node sends migrate command
				System.out.println("Migrating");
				for (int i = 0; i < keys.size(); i++) {
					System.out.println(commandsExisting.migrate(serverAdd.getHost(), serverAdd.getPort(), keys.get(i), 0, 1000));
				}
			}
			//Both nodes set the slot node to the node to keep in the cluster
			System.out.println("Setting");
			System.out.println(commands.clusterSetSlotNode(firstHalf[k], serverAdd.getNodeID()));
			System.out.println(commandsExisting.clusterSetSlotNode(firstHalf[k], serverAdd.getNodeID()));
		}

		connection.close();
		redisClient.shutdown();

		connectionExisting.close();
		redisClientExisting.shutdown();


		System.out.println("Server " + request.getServerAdd().getHost() + " " + request.getServerAdd().getPort() + " added.");
		return "Server " + request.getServerAdd().getHost() + request.getServerAdd().getPort() + " added.";

	}

	@RequestMapping(method = RequestMethod.POST, value = "/getServers")
	public List<Server> getServers(@RequestBody Server server1) {

		/*RedisClient redisClient = new RedisClient(server1.getHost(), server1.getPort());

		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> commands = connection.sync();*/

		RedisURI uri = new RedisURI();
		uri.setHost(server1.getHost());
		uri.setPort(server1.getPort());
		//RedisClusterClient redisClient = new RedisClusterClient(uri);
		RedisClusterClient redisClient = RedisClusterClient.create(uri);

		StatefulRedisClusterConnection<String, String> connection = redisClient.connect();
		RedisAdvancedClusterCommands<String, String> commands = connection.sync();


		String nodes = commands.clusterNodes();
		//splits on new line
		String nodesArray[] = nodes.split("\\r?\\n");

		List<Server> servers = new ArrayList<>();
		List<Server> masterNodes = new ArrayList<>();

		for (int i = 0; i < nodesArray.length; i++) {
			System.out.println(nodesArray[i]);

			String info[] = nodesArray[i].split("\\s+");

			Server server = new Server();

			server.setNodeID(info[0]);

			String hostAndPort = info[1];
			int index = hostAndPort.indexOf(":");
			System.out.println(hostAndPort);

			server.setHost(hostAndPort.substring(0, index));
			server.setPort(Integer.parseInt(hostAndPort.substring(index + 1)));
			server.setType(info[2].indexOf("master") == -1 ? "Slave" : "Master");
			server.setStatus(nodesArray[i].indexOf("disconnected") == -1 ? "Active" : "Disabled");

			if (server.getType().equals("Slave")) {
				server.setSlaveOf(info[3]);
			} else if(server.getType().equals("Master")) {
				masterNodes.add(server);
			}


			Slots slots[] = new Slots[info.length - 8];

			for (int k = 8; k < info.length; k++) {
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

		for(int k = 0; k < servers.size(); k++) {
			Server temp = servers.get(k);

			if(temp.getType().equals("Slave")) {

				Server master = new Server();
				master.setNodeID(temp.getSlaveOf());
				int idx = masterNodes.indexOf(master);

				System.out.println(temp.getHost() + ":" + temp.getPort() + " -> " + idx);
				if (idx != -1) {
					System.out.println("Slave assigned");
					servers.get(k).setMasterHost(masterNodes.get(idx).getHost());
					servers.get(k).setMasterPort(Integer.toString(masterNodes.get(idx).getPort()));
				}
			}
		}

		String info = commands.clusterInfo();
		System.out.println("INFO");
		System.out.println(info);

		connection.close();
		redisClient.shutdown();

		return servers;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getQueues")
	public List<String> getQueues(@RequestBody Server server) {


		/*RedisClient redisClient = new RedisClient(server.getHost(), server.getPort());
		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> commands = connection.sync();*/

		RedisURI uri = new RedisURI();
		uri.setHost(server.getHost());
		uri.setPort(server.getPort());
		//RedisClusterClient redisClient = new RedisClusterClient(uri);
		RedisClusterClient redisClient = RedisClusterClient.create(uri);

		StatefulRedisClusterConnection<String, String> connection = redisClient.connect();
		//RedisAdvancedClusterCommands<String, String> commands = connection.sync();


		RedisClusterCommands<String, String> commands = connection.getConnection(server.getHost(), server.getPort()).sync();

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

		//List<String> items = commands.clusterGetKeysInSlot(0, 15999);
		//System.out.println("servers info: " + items);
		//List<String> keys = commands.keys("*");
		List<String> keys = commands.keys("*");
		int size = keys.size();
		String movedHostPort = server.getHost() + ":" + server.getPort();

		for (int i = 0; i < size; i++) {
			try {
				String type = commands.type(keys.get(i));
				if (type.equalsIgnoreCase("hash"))
					keys.add(Long.toString(commands.hlen(keys.get(i))));
				else if (type.equalsIgnoreCase("list"))
					keys.add(Long.toString(commands.llen(keys.get(i))));
				else if (type.equalsIgnoreCase("set"))
					keys.add(Long.toString(commands.scard(keys.get(i))));
				else if (type.equalsIgnoreCase("none"))
					keys.add("0");
				else
					keys.add("1");
			} catch (Exception e) {
				String[] splitMessage = e.getMessage().split("\\r?\\n");
				String[] splitColon = splitMessage[0].split(":");
				String newPort = splitColon[1];
				String newHost = splitColon[0].split(" ")[2];

				if (splitColon[0].contains("MOVED")) {
					commands = connection.getConnection(newHost, Integer.parseInt(newPort)).sync();

					System.out.println("We just moved it. :P");
					String type2 = commands.type(keys.get(i));
					if (type2.equalsIgnoreCase("hash"))
						keys.add(Long.toString(commands.hlen(keys.get(i))));
					else if (type2.equalsIgnoreCase("list"))
						keys.add(Long.toString(commands.llen(keys.get(i))));
					else if (type2.equalsIgnoreCase("set"))
						keys.add(Long.toString(commands.scard(keys.get(i))));
					else if (type2.equalsIgnoreCase("none"))
						keys.add("0");
					else
						keys.add("1");

					movedHostPort = server.getHost() + ":" + server.getPort() + " - Moved to " + newHost + ":" + newPort;
				} else {
					connection.close();
					redisClient.shutdown();
					return new ArrayList<String>();
				}
			}
		}

		connection.close();
		redisClient.shutdown();

		keys.add(movedHostPort);
		return keys;

	}

	@RequestMapping(method = RequestMethod.POST, value = "/getMemory")
	public Server getMemory(@RequestBody Server server1) {
		RedisURI uri1 = new RedisURI();
		uri1.setHost(server1.getHost());
		uri1.setPort(server1.getPort());

		RedisClusterClient clusterClient = RedisClusterClient.create(uri1);
		StatefulRedisClusterConnection<String, String> connection = clusterClient.connect();

		RedisAdvancedClusterCommands<String, String> commands = connection.sync();

		String info = commands.info();
		System.out.println(info);

		int beginningMem = info.indexOf("# Memory");
		int endMem = info.indexOf("# Persistence");

		String memory = info.substring(beginningMem, endMem);

		String memoryArray[] = memory.split("\\r?\\n");
		String memoryUsage[] = memoryArray[1].split(":");
		String memoryRSS[] = memoryArray[3].split(":");
		double memoryPercent = Double.parseDouble(memoryUsage[1]) / Double.parseDouble(memoryRSS[1]);

		System.out.println("Memory Used: " + memoryArray[1]);
		System.out.println("Memory RSS: " + memoryArray[3]);
		System.out.println("Memory Percent: " + memoryPercent);

		int beginningCPU = info.indexOf("# CPU");
		int endCPU = info.indexOf("# Cluster");

		String cpu = info.substring(beginningCPU, endCPU);

		String cpuArray[] = cpu.split("\\r?\\n");
		String cpuUsage[] = cpuArray[1].split(":");

		System.out.println("CPU Used: " + cpuArray[1]);

		Server server = new Server();
		server = server1;
		server.setCpu(cpuUsage[1]);
		server.setMemory(memoryPercent);

		return server;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/addSlots")
	public String addSlots(@RequestBody Server server1) {
		RedisURI uri1 = new RedisURI();
		uri1.setHost(server1.getHost());
		uri1.setPort(server1.getPort());
		RedisClusterClient redisClient = RedisClusterClient.create(uri1);


		RedisClusterClient clusterClient = RedisClusterClient.create(uri1);
		StatefulRedisClusterConnection<String, String> connection = clusterClient.connect();

		RedisClusterCommands<String, String> commands = connection.getConnection(server1.getHost(), server1.getPort()).sync();


		//int beginningSlot = server1.getBeginningSlot();
		int beginningSlot = server1.getSlots()[0].getBeginningSlot();
		//int endSlot = server1.getEndSlot();
		int endSlot = server1.getSlots()[0].getEndSlot();
		int numSlots = endSlot - beginningSlot + 1;

		int[] slots = new int[numSlots];

		for (int i = beginningSlot; i < endSlot + 1; i++) {
			slots[i - beginningSlot] = i;
		}

		String a = commands.clusterAddSlots(slots);
		System.out.println(a);

		connection.close();
		redisClient.shutdown();

		return ("Slots " + beginningSlot + " to " + endSlot + " added to " + server1.getHost() + ":" + server1.getPort());

	}

	public int[] getSlots(int beginningSlot, int endSlot) {

		int numSlots = endSlot - beginningSlot + 1;

		System.out.println(numSlots);
		int[] slots = new int[numSlots];

		for (int i = beginningSlot; i < endSlot + 1; i++) {
			slots[i - beginningSlot] = i;
		}

		return slots;

	}

}