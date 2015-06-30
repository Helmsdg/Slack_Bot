package com.eclipsesoftworks.slackbot.processors;

import com.eclipsesoftworks.slackbot.JedisClient;
import com.eclipsesoftworks.slackbot.JokeClient;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

/**
 * Created by Helmsdg on 6/30/2015.
 */
public class JokeProcessor implements SlackCommandProcessor {

    @Override
    public void processCommand(SlackMessagePosted event, SlackSession session, JedisClient jedisClient) {
        if(event.getMessageContent().toLowerCase().contains("joke")){
            try {
                session.sendMessageOverWebSocket(session.findChannelByName("general"), getRandomJoke(jedisClient), null);
            }
            catch (Exception err){
                err.printStackTrace();
            }
        }
    }

    public static String getRandomJoke(JedisClient jedisClient){
        String test = JokeClient.getRandomJoke().getJoke();
        while(jedisClient.doesKeyExist(test)){
            test = JokeClient.getRandomJoke().getJoke();
        }
        jedisClient.storeKey(test, "used");
        return test;
    }
}
