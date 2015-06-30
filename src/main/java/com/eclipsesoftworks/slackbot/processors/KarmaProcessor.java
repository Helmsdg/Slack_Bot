package com.eclipsesoftworks.slackbot.processors;

import com.eclipsesoftworks.slackbot.JedisClient;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

/**
 * Created by Helmsdg on 6/30/2015.
 */
public class KarmaProcessor implements SlackCommandProcessor {

    @Override
    public void processCommand(SlackMessagePosted event, SlackSession session, JedisClient jedisClient) {
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
                        //SlackChatConfiguration test = SlackChatConfiguration.getConfiguration().asUser();
                        //test.withName("GROG THE IMMORTAL");
                        //session.sendMessage(session.findChannelByName("general"), "<@" + temp + "> is on the rise! (Karma: " + incrementKarma(temp.toUpperCase()) + ")", null, test);
                        session.sendMessageOverWebSocket(session.findChannelByName("general"), "<@" + temp + "> is on the rise! (Karma: " + incrementKarma(temp.toUpperCase(), jedisClient) + ")", null);
                    }
                    else if(subtract){
                        session.sendMessageOverWebSocket(session.findChannelByName("general"), "<@" + temp + "> is slipping! (Karma: " + decrementKarma(temp.toUpperCase(), jedisClient) + ")", null);
                    }
                    else if(level){
                        session.sendMessageOverWebSocket(session.findChannelByName("general"), "<@" + temp + "> has " + getKarma(temp.toUpperCase(), jedisClient) + " Karma!", null);
                    }
                }
            }


        }
    }

    public static Integer incrementKarma(String user, JedisClient jedisClient){
        Integer value = 0;
        if(jedisClient.doesKeyExist(user)){
            value = Integer.parseInt(jedisClient.getValue(user));
        }
        value++;
        jedisClient.storeKey(user, value.toString());
        return value;
    }

    public static Integer decrementKarma(String user, JedisClient jedisClient){
        Integer value = 0;
        if(jedisClient.doesKeyExist(user)){
            value = Integer.parseInt(jedisClient.getValue(user));
        }
        value--;
        jedisClient.storeKey(user, value.toString());
        return value;
    }

    public static Integer getKarma(String user, JedisClient jedisClient){
        Integer value = 0;
        if(jedisClient.doesKeyExist(user)){
            value = Integer.parseInt(jedisClient.getValue(user));
        }
        return value;
    }
}
