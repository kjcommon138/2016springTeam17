package hello;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GreetingController {

	private final String[] HOST_NAMES = {
			"sd-vm12.csc.ncsu.edu",
			"sd-vm19.csc.ncsu.edu",
			"sd-vm20.csc.ncsu.edu", 
	"sd-vm33.csc.ncsu.edu"};

	@RequestMapping(method=RequestMethod.POST, value="/removeServers")
	public String removeServers(@RequestBody ServerRequest2 request) {
		Server server = request.getServer();
		Server serverToRemove = request.getServerRemove();

		//connection to node we want to remove
		RedisClient redisClient2 = new RedisClient(serverToRemove.getHost(), serverToRemove.getPort());
		StatefulRedisConnection<String, String> connection2 = redisClient2.connect();
		RedisCommands<String, String> commands2 = connection2.sync();

		int beginningSlots = 0;
		int endSlots = 0;
		String nodeID = "";

		List<Server> allServers = getServers(serverToRemove);
		for(int i = 0 ; i < allServers.size(); i++){
			if(allServers.get(i).getPort() == serverToRemove.getPort()){
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
		commands2.clusterDelSlots(slotsToRemove);
		connection2.close();
		redisClient2.shutdown();

		//get every node in the cluster to delete/forget the slot
		for(int i = 0 ; i < allServers.size(); i++){

			if(allServers.get(i).getPort() != serverToRemove.getPort()){

				RedisClient redisClient3 = new RedisClient(allServers.get(i).getHost(), allServers.get(i).getPort());
				StatefulRedisConnection<String, String> connection3 = redisClient3.connect();
				RedisCommands<String, String> commands3 = connection3.sync();

				//add removed slots  to the other node
				commands3.clusterDelSlots(slotsToRemove);
				commands3.clusterForget(nodeID);

				connection3.close();
				redisClient3.shutdown();

			}
		}

		//connection to node we want to keep
		RedisClient redisClient = new RedisClient(server.getHost(), server.getPort());
		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> commands = connection.sync();

		commands.clusterAddSlots(slotsToRemove);


		connection.close();
		redisClient.shutdown();



		return null;
	}

	@RequestMapping(method=RequestMethod.POST, value="/addServers")
	public String addServers(@RequestBody Server server) {

		//Server server = request.getServer();
		//Server serverToAdd = request.getServerAdd();

		//connection to node we want to keep
		RedisClient redisClient = new RedisClient(server.getHost(), server.getPort());
		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> commands = connection.sync();


		int beginningSlots = 0;
		int endSlots = 0;
		String nodeID = "";

		List<Server> allServers = getServers(server);
		for(int i = 0 ; i < allServers.size(); i++){
			//make sure not to get info for server to add
			if(allServers.get(i).getPort() != server.getPort()){
				Server serverRemoveInfo = allServers.get(i);
				beginningSlots = serverRemoveInfo.getBeginningSlot();
				endSlots = serverRemoveInfo.getEndSlot();
				nodeID = serverRemoveInfo.getNodeID();
			}
		}


		String result = commands.clusterMeet(server.getHost(), server.getPort());

		connection.close();
		redisClient.shutdown();

		return result;

	}

	@RequestMapping(method=RequestMethod.POST, value="/getServers")
	public List<Server> getServers(@RequestBody Server server1) {

		RedisClient redisClient = new RedisClient(server1.getHost(), server1.getPort());

		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> commands = connection.sync();

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
			System.out.println(info[8]);
			index = info[8].indexOf("-");
			server.setBeginningSlot(Integer.parseInt(info[8].substring(0, index)));
			server.setEndSlot(Integer.parseInt(info[8].substring(index + 1)));

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

		RedisClient redisClient = new RedisClient(server.getHost(), server.getPort());
		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> commands = connection.sync();

		//String items = commands.get(server.getKey());
		List<String> itemsList = commands.lrange(server.getKey(), 0, -1);

		return itemsList;
	}

	@RequestMapping(method=RequestMethod.POST, value="/addSlots")
	public String addSlots(@RequestBody Server server1) {
		RedisClient redisClient = new RedisClient(server1.getHost(), server1.getPort());

		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> commands = connection.sync();

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