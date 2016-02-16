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
	public String removeServers(@RequestBody ServerRequest request) {

		RedisClient redisClient = new RedisClient(request.getHostCluster(), request.getPortCluster());
		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> commands = connection.sync();

		System.out.println("Node ID: " + request.getNodeID());

		String result = commands.clusterForget(request.getNodeID());

		redisClient.shutdown();


		return result;
	}

	@RequestMapping(method=RequestMethod.POST, value="/addServers")
	public String addServers(@RequestBody ServerRequest request) {

		RedisClient redisClient = new RedisClient(request.getHostCluster(), request.getPortCluster());
		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> commands = connection.sync();

		String result = commands.clusterMeet(request.getHost(), request.getPort());

		redisClient.shutdown();

		return result;

	}

	@RequestMapping(method=RequestMethod.POST, value="/servers")
	public List<Server> servers(@RequestBody Server server1) {

		RedisClient redisClient = new RedisClient(server1.getHost(), server1.getPort());

		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> commands = connection.sync();

		String nodes = commands.clusterNodes();
		String nodesArray[] = nodes.split("\\r?\\n");

		List<Server> servers = new ArrayList<Server>();

		for(int i = 0; i < nodesArray.length; i++){
			System.out.println(nodesArray[i]);
			int index = nodesArray[i].indexOf(' ');
			Server server = new Server();
			server.setNodeID(nodesArray[i].substring(0,index));
			server.setServerInfo(nodesArray[i].substring(index + 1));
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
	public String getItems(@RequestBody Server server) {
		
		RedisClient redisClient = new RedisClient(server.getHost(), server.getPort());
		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> commands = connection.sync();
		
		String items = commands.get(server.getKey());
		
		return items;
	}

	@RequestMapping(method=RequestMethod.POST, value="/addSlots")
	public void addSlots(@RequestBody Server server1) {
		RedisClient redisClient = new RedisClient(server1.getHost(), server1.getPort());

		StatefulRedisConnection<String, String> connection = redisClient.connect();
		RedisCommands<String, String> commands = connection.sync();

		int[] slots = new int[84];


		for(int i = 16300; i < 16384; i ++){
			slots[i-16300] = i;
			System.out.println(i-16300);
			System.out.println(i);
		}


		String a = commands.clusterAddSlots(slots);
		System.out.println(a);


		redisClient.shutdown();


	}
}