import Generator.QueueGenerator;
import com.bronto.redis.client.exceptions.RedisException;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class QueueGeneratorTest {

    @Test
    public void runTest() {
        QueueGenerator q = new QueueGenerator("sd-vm19.csc.ncsu.edu:10001");

        assertTrue(q.run(10, 10));
    }

    @Test(expected = RedisException.class)
    public void invalidAddrTest() {
        new QueueGenerator("localhost:9999");
    }


}
