package im.with;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;

/**
 * RedisClient
 * @Description Redis配置类
 * @author lance
 */
public class RedisClient {

	public Jedis jedis;//
	private JedisPool jedisPool;//
	
	private String ip = "127.0.0.1";
	private int port = 7000;
	
	private static RedisClient instance = null;
	
	public static synchronized RedisClient getInstance() {
		if(instance == null)
			instance = new RedisClient();
		return instance;
	}
	
	public RedisClient() {
		initialPool();
		jedis = jedisPool.getResource();
	}
	
	/**
	 * 初始化非切片池(非分布式)
	 */
	private void initialPool() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(1024);//最大分配的对象数
		config.setMaxIdle(200);//最大能够保持Idle状态的对象数 
		config.setMaxWait(1000l);//当池内没有返回对象时，最大等待时间
		config.setTestOnBorrow(false);//当调用borrow Object方法时，是否进行有效性检查
		jedisPool = new JedisPool(config, ip, port);
	}

	public void destroy() {
		jedisPool.returnResource(jedis);
	}
	
	public static void main(String[] args) {
//		System.out.println("清空库中所有数据："+new RedisClient().jedis.flushDB()); 
		RedisClient client = new RedisClient();
		List<String> list = client.jedis.lrange("1229165", 0, -1);
		System.out.println(list.size());
		for (String key : list) {
			System.out.println(client.jedis.get(key));
		}
		
	}
	
}