package hello;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.cluster.RedisAdvancedClusterConnection;
import com.lambdaworks.redis.cluster.RedisClusterClient;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    
    @RequestMapping(method=RequestMethod.POST)
    public Greeting greeting2(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
    
    @RequestMapping("/servers")
    public Servers servers() {
    	List<String> s = new ArrayList<String>();
    	s.add("SNEHA");
    	
    	//RedisClient redisClient = new RedisClient("127.0.0.1", 6379);
    	RedisClient redisClient = new RedisClient("127.0.0.1", 30002);
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


    	String info = connection.clusterInfo();
    	System.out.println("INFO");
    	System.out.println(info);
    	String infoArray[] = info.split("\\r?\\n");


    	
    	redisClient.shutdown();
    	
        return new Servers(4,nodesArray);
    }
}