import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;
import redis.clients.jedis.Jedis;

import java.io.IOException;

/**
 * Created by Helmsdg on 6/19/2015.
 */
public class SlackBot {

    private String apiKey;

    public SlackBot(String apiKey){
        this.apiKey = apiKey;
    }

    public static void main(String[] args) throws IOException {

        SlackBot slackBot = new SlackBot("xoxb-6635722290-kkS2XjX1cTZ72NI3KJV3Uz3p");

        SlackSession session = SlackSessionFactory.createWebSocketSlackSession("xoxb-6635722290-kkS2XjX1cTZ72NI3KJV3Uz3p");
        session.connect();

        session.addMessagePostedListener(new SlackMessagePostedListener()
        {
            @Override
            public void onEvent(SlackMessagePosted event, SlackSession session)
            {
                if(event.getMessageContent().contains("@") && !event.getSender().getUserName().equalsIgnoreCase("karmabot")){
                    //Find users
                    String convertedMessage = event.getMessageContent().replace(" ", "@").replace("<", "").replace(">", "");
                    String[] names = convertedMessage.split("@");

                    //Find operation
                    boolean add = convertedMessage.contains("++");
                    boolean subtract = convertedMessage.contains("--");
                    boolean level = convertedMessage.contains("level");
                    if(!add && !subtract && !level){
                        return;
                    }
                    for(String temp: names){
                        temp = temp.replace(":", "");
                        temp = temp.replace("--", "");
                        temp = temp.replace("++", "");
                        temp = temp.replace("level", "");
                        if(!temp.isEmpty()){
                            if(add){
                                session.sendMessageOverWebSocket(session.findChannelByName("general"), "<@" + temp + "> is on the rise! (Karma: " + incrementKarma(temp.toUpperCase()) + ")", null);
                            }
                            else if(subtract){
                                session.sendMessageOverWebSocket(session.findChannelByName("general"), "<@" + temp + "> is slipping! (Karma: " + decrementKarma(temp.toUpperCase()) + ")", null);
                            }
                            else if(level){
                                session.sendMessageOverWebSocket(session.findChannelByName("general"), "<@" + temp + "> has " + getKarma(temp.toUpperCase()) + " Karma!", null);
                            }
                        }
                    }


                }
                else if(event.getMessageContent().toLowerCase().contains("joke")){
                    try {
                        session.sendMessageOverWebSocket(session.findChannelByName("general"), getRandomJoke(), null);
                    }
                    catch (Exception err){
                        err.printStackTrace();
                    }
                }
            }


        });

    }

    private static Jedis jedisClient = new Jedis("eclipse-serv-03.eclipsesoftworks.com", 6379);

    public static Boolean doesKeyExist(String key){
       return jedisClient.get(key) != null;
    }

    public static void storeKey(String key, String value){
        jedisClient.set(key, value);
    }

    public static Integer incrementKarma(String user){
        Integer value = 0;
        if(doesKeyExist(user)){
            value = Integer.parseInt(jedisClient.get(user));
        }
        value++;
        storeKey(user, value.toString());
        return value;
    }

    public static Integer decrementKarma(String user){
        Integer value = 0;
        if(doesKeyExist(user)){
            value = Integer.parseInt(jedisClient.get(user));
        }
        value--;
        storeKey(user, value.toString());
        return value;
    }

    public static Integer getKarma(String user){
        Integer value = 0;
        if(doesKeyExist(user)){
            value = Integer.parseInt(jedisClient.get(user));
        }
        return value;
    }

    public static String getRandomJoke(){
        String test = JokeClient.getRandomJoke().getJoke();
        while(doesKeyExist(test)){
            test = JokeClient.getRandomJoke().getJoke();
        }
        storeKey(test, "used");
        return test;
    }

}
