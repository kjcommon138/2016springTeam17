package hello;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.lambdaworks.redis.cluster.RedisClusterClient;
import com.lambdaworks.redis.cluster.api.StatefulRedisClusterConnection;
import com.lambdaworks.redis.cluster.api.sync.RedisAdvancedClusterCommands;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hello.Server.Slots;

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

		int serverRemovePort = server.getPort();
		String serverRemoveHost = server.getHost();
		Server serverRemove = null;
		Server serverKeep = null;


		//connection to server we want to remove
		RedisURI uri1 = new RedisURI();
		uri1.setHost(serverRemoveHost);
		uri1.setPort(serverRemovePort);
		RedisClusterClient redisClient1 = new RedisClusterClient(uri1);

		StatefulRedisClusterConnection<String, String> connection1 = redisClient1.connect();
		RedisAdvancedClusterCommands<String, String> commands1 = connection1.sync();

		String nodeID = "";
		Slots slots[] = null;

		List<Server> allServers = getServers(server);
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

		//System.out.println(beginningSlots);
		//System.out.println(endSlots);
		//remove slots from the node we want to remove
		int[] allSlotsToRemove = new int[0];

		for(int i = 0; i < slots.length; i ++){
			int beginningSlots = slots[i].getBeginningSlot();
			int endSlots = slots[i].getEndSlot();
			int[] slotsToRemove = getSlots(beginningSlots, endSlots);

			int[] existingSlotsList = allSlotsToRemove;
			allSlotsToRemove = new int[allSlotsToRemove.length + slotsToRemove.length];

			System.arraycopy(slotsToRemove, 0, allSlotsToRemove, 0, slotsToRemove.length);
			System.arraycopy(existingSlotsList, 0, allSlotsToRemove, slotsToRemove.length, existingSlotsList.length);

		}

		System.out.println(allSlotsToRemove[0]);
		System.out.println(allSlotsToRemove[allSlotsToRemove.length-1]);
		//commands1.clusterDelSlots(allSlotsToRemove);

		//NEW
		for(int k = 0; k < allSlotsToRemove.length; k++){
			System.out.println(commands1.clusterSetSlotImporting(allSlotsToRemove[k], serverRemove.getNodeID()));
			System.out.println(commands1.clusterSetSlotNode(allSlotsToRemove[k], serverKeep.getNodeID()));
			System.out.println(commands1.clusterSetSlotMigrating(allSlotsToRemove[k], serverKeep.getNodeID()));
			
		}

		connection1.close();
		redisClient1.shutdown();

		RedisURI uri3 = new RedisURI();
		uri3.setHost(serverKeep.getHost());
		uri3.setPort(serverKeep.getPort());
		RedisClusterClient redisClient3 = new RedisClusterClient(uri3);

		StatefulRedisClusterConnection<String, String> connection3 = redisClient3.connect();
		RedisAdvancedClusterCommands<String, String> commands3 = connection3.sync();

		commands3.clusterForget(serverRemove.getNodeID());

		connection3.close();
		redisClient3.shutdown();

		//NEW


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
				Server currentServer = allServers.get(i);
				//beginningSlots = currentServer.getBeginningSlot();
				//endSlots = currentServer.getEndSlot();
				beginningSlots = currentServer.getSlots()[0].getBeginningSlot();
				endSlots = currentServer.getSlots()[0].getEndSlot();
				System.out.println("Slots: ");
				System.out.println(beginningSlots);
				System.out.println(endSlots);

				int [] slots = getSlots(beginningSlots, endSlots);
				int[] firstHalf = Arrays.copyOfRange(slots, 0, slots.length/2);

				//deleting slots from existing node
				System.out.println(commands1.clusterDelSlots(firstHalf));

				//adding slots to connection with new node
				//System.out.println(commands.clusterAddSlots(firstHalf));


				System.out.println(commands1.clusterMeet(server.getHost(), server.getPort()));

				commands1.close();
				connection1.close();
				redisClient1.shutdown();

				System.out.println(commands.clusterAddSlots(firstHalf));
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
			server.setType(info[2]);

			System.out.println(info[7]);


			Slots slots[] = new Slots[info.length - 8];

			for(int k = 8; k < info.length; k ++){
				Slots slot = new Slots();
				System.out.println(info[k]);
				index = info[k].indexOf("-");
				slot.setBeginningSlot(Integer.parseInt(info[k].substring(0, index)));
				slot.setEndSlot(Integer.parseInt(info[k].substring(index + 1)));
				slots[k - 8] = slot;

				//server.setBeginningSlot(Integer.parseInt(info[8].substring(0, index)));
				//server.setEndSlot(Integer.parseInt(info[8].substring(index + 1)));
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
		RedisClient redisClient = new RedisClient(server1.getHost(), server1.getPort());

		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> commands = connection.sync();

		/*RedisURI uri = new RedisURI();
		uri.setHost(server1.getHost());
		uri.setPort(server1.getPort());
		RedisClusterClient redisClient = new RedisClusterClient(uri);

		StatefulRedisClusterConnection<String, String> connection = redisClient.connect();
		RedisAdvancedClusterCommands<String, String> commands = connection.sync();*/

		//int beginningSlot = server1.getBeginningSlot();
		int beginningSlot = server1.getSlots()[0].getBeginningSlot();
		//int endSlot = server1.getEndSlot();
		int endSlot = server1.getSlots()[0].getBeginningSlot();
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

		//int beginningSlot = server1.getBeginningSlot();
		int beginningSlot = server1.getSlots()[0].getBeginningSlot();
		System.out.println(beginningSlot);
		//int endSlot = server1.getEndSlot();
		int endSlot = server1.getSlots()[0].getEndSlot();
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