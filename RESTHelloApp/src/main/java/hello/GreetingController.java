package hello;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

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
    	
    	RedisConnection<String, String> connection = redisClient.connect();

    	String info = connection.clusterInfo();
    	System.out.println("INFO");
    	System.out.println(info);
    	
    	redisClient.shutdown();
    	
        return new Servers(counter.incrementAndGet(),s);
    }
}