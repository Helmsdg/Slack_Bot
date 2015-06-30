package com.eclipsesoftworks.slackbot;

import redis.clients.jedis.Jedis;

/**
 * Created by Helmsdg on 6/30/2015.
 */
public class JedisClient {

	private String connectionURL;
	private Integer port;

	private static Jedis jedisClient;// = new Jedis("eclipse-serv-03.eclipsesoftworks.com", 6379);

    private static JedisClient singleton;

    public static JedisClient getInstance(){
        if(singleton == null){
            singleton = new JedisClient("eclipse-serv-03.eclipsesoftworks.com", 6379);
        }
        return singleton;
    }

	public JedisClient(String url, Integer port){
        if(jedisClient == null || !connectionURL.equalsIgnoreCase(url) || port != this.port){
            if(jedisClient != null){
                jedisClient.close();
            }
            //Make a new instance
            this.connectionURL = url;
            this.port = port;
            connect();
        }
	}

	private void connect(){
		jedisClient = new Jedis(connectionURL, port);
	}

	public Boolean doesKeyExist(String key){
       return jedisClient.get(key) != null;
    }

    public void storeKey(String key, String value){
        jedisClient.set(key, value);
    }

    public String getValue(String key){
        return jedisClient.get(key);
    }
}