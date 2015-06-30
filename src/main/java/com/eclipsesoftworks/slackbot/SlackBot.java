package com.eclipsesoftworks.slackbot;

import com.eclipsesoftworks.slackbot.processors.JokeProcessor;
import com.eclipsesoftworks.slackbot.processors.KarmaProcessor;
import com.eclipsesoftworks.slackbot.processors.SlackCommandProcessor;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Helmsdg on 6/19/2015.
 */
public class SlackBot {

    private String apiKey;
    private Boolean readBook;
    private Boolean manageKarma;
    private Boolean tellJokes;

    private SlackSession session;
    private JedisClient jedisClient;

    private Map<String, SlackCommandProcessor> commandProcessors;

    public SlackBot(String apiKey) throws IOException{
        this.apiKey = apiKey;
        setReadBook(true);
        setManageKarma(true);
        setTellJokes(true);
        commandProcessors = new HashMap<>();

        init();
    }

    private void init() throws IOException{
        session = SlackSessionFactory.createWebSocketSlackSession(apiKey);
        session.connect();

        jedisClient = JedisClient.getInstance();

        session.addMessagePostedListener(new SlackMessagePostedListener(){
            @Override
            public void onEvent(SlackMessagePosted event, SlackSession session){
                processEvent(event, session);
            }
        });
    }

    public void setReadBook(Boolean readBook){
        this.readBook = readBook;
    }

    public void setManageKarma(Boolean manageKarma){
        this.manageKarma = manageKarma;
    }

    public void setTellJokes(Boolean tellJokes){
        this.tellJokes = tellJokes;
    }

    private void processEvent(SlackMessagePosted event, SlackSession session){
        for(SlackCommandProcessor temp : commandProcessors.values()){
            temp.processCommand(event, session, jedisClient);
        }
    }

    public void addProcessor(SlackCommandProcessor processor){
        commandProcessors.put(processor.getClass().getName(), processor);
    }

    public void removeProcessor(SlackCommandProcessor instance){
        commandProcessors.remove(instance.getClass().getName());
    }

    public static void main(String[] args) throws IOException {
        SlackBot slackBot = new SlackBot("xoxb-6635722290-kkS2XjX1cTZ72NI3KJV3Uz3p");
        slackBot.addProcessor(new JokeProcessor());
        slackBot.addProcessor(new KarmaProcessor());
    }

}
