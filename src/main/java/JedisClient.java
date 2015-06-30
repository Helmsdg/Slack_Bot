/**
 * Created by Helmsdg on 6/30/2015.
 */
public class JedisClient {

	private String connectionURL;
	private Integer port;

	private static Jedis jedisClient;// = new Jedis("eclipse-serv-03.eclipsesoftworks.com", 6379);

	public JedisClient(String url, Integer port){
		this.connectionURL = url;
		this.port = port;
		connect();
	}

	private connect(){
		jedisClient = new Jedis(connectionURL, port);
	}

	public static Boolean doesKeyExist(String key){
       return jedisClient.get(key) != null;
    }

    public static void storeKey(String key, String value){
        jedisClient.set(key, value);
    }


}