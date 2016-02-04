package hello;

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
		
		return null;
	}

	@RequestMapping(method=RequestMethod.POST, value="/addServers")
	public String addServers(@RequestBody ServerRequest request) {

		RedisURI initialUri = new RedisURI();
		initialUri.setHost("127.0.0.1");
		initialUri.setPort(7000);



		RedisClient redisClient = new RedisClient("127.0.0.1", 7000);
		RedisConnection<String, String> connect = redisClient.connect();
		
		String result = connect.clusterMeet(request.getHost(), request.getPort());


		//RedisClusterClient redisClusterClient = new RedisClusterClient(initialUri);
		//	RedisAdvancedClusterConnection<String,String> connection = redisClusterClient.connectCluster();



		//RedisURI uri = new RedisURI("127.0.0.1", 30009, 60, TimeUnit.SECONDS);
		//RedisURI.create("redis://127.0.0.1:30009");

		//RedisClientFactoryBean bean2 = new RedisClientFactoryBean();

		//RedisClientFactoryBeanExtended bean = new RedisClientFactoryBeanExtended();
		//bean.setRedisURI(initialUri);

		//RedisClient client = bean.createClusterInstance();

		//System.out.println(client.toString());
		//RedisConnection<String, String> connect = client.connect();
		//System.out.println(connect.clusterMeet("127.0.0.1", 30001));
		//	client

		//connection2.clusterM

		//String clusterMeet = connection.clusterMeet("127.0.0.1", 30009);
		//System.out.println(clusterMeet);



		return result;

	}

	@RequestMapping("/servers")
	public Servers servers() {

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

		for(int i = 0; i < nodesArray.length; i++){
			System.out.println(nodesArray[i]);
		}


		String info = connection.clusterInfo();
		System.out.println("INFO");
		System.out.println(info);
		String infoArray[] = info.split("\\r?\\n");


		redisClient.shutdown();

		return new Servers(nodesArray.length,nodesArray);
	}
}