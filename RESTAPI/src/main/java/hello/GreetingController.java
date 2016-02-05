package hello;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.RedisURI;

@RestController
public class GreetingController {

	
	@RequestMapping(method=RequestMethod.POST, value="/removeServers")
	public String removeServers(@RequestBody ServerRequest request) {
		
		RedisURI initialUri = new RedisURI();
		initialUri.setHost("127.0.0.1");
		initialUri.setPort(7000);



		RedisClient redisClient = new RedisClient("127.0.0.1", 7000);
		RedisConnection<String, String> connection = redisClient.connect();
		
		System.out.println("Node ID: " + request.getNodeID());
		
		String result = connection.clusterForget(request.getNodeID());
		
		redisClient.shutdown();
		
		
		return result;
	}

	@RequestMapping(method=RequestMethod.POST, value="/addServers")
	public String addServers(@RequestBody ServerRequest request) {

		RedisURI initialUri = new RedisURI();
		initialUri.setHost("127.0.0.1");
		initialUri.setPort(7000);



		RedisClient redisClient = new RedisClient("127.0.0.1", 7000);
		RedisConnection<String, String> connection = redisClient.connect();
		
		String result = connection.clusterMeet(request.getHost(), request.getPort());
		
		redisClient.shutdown();

		return result;

	}

	@RequestMapping("/servers")
	public List<Server> servers() {

		//RedisClient redisClient = new RedisClient("127.0.0.1", 6379);
		RedisClient redisClient = new RedisClient("127.0.0.1", 7000);
		//RedisClient redisClient = new RedisClient();

		/*RedisURI initialUri = new RedisURI();
    	initialUri.setHost("127.0.0.1");
    	initialUri.setPort(30002);
    	RedisClusterClient c = new RedisClusterClient(initialUri);
    	RedisAdvancedClusterConnection<String, String> connection2 = c.connectCluster();

    	System.out.println("CLUSTER INFO:" + connection2.clusterInfo());*/

		RedisConnection<String, String> connection = redisClient.connect();

		String nodes = connection.clusterNodes();
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


		String info = connection.clusterInfo();
		System.out.println("INFO");
		System.out.println(info);

		redisClient.shutdown();

		return servers;
	}
}