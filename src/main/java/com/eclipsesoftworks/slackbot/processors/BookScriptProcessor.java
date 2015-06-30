package com.eclipsesoftworks.slackbot.processors;

import com.eclipsesoftworks.slackbot.JedisClient;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackChatConfiguration;

import java.util.Scanner;

/**
 * Created by Helmsdg on 6/30/2015.
 */
public class BookScriptProcessor implements SlackCommandProcessor {

    private final Integer POSTS_PER_PAGE = 5;

    private String book;
    private Scanner bookReader;

    public BookScriptProcessor(String book){
        this.book = book;
    }

    @Override
    public void processCommand(SlackMessagePosted event, SlackSession session, JedisClient jedisClient) {
        if(event.getMessageContent().toLowerCase().contains("read a book")){
            //Reset scanner and turn the page
            bookReader = new Scanner(book);
            SlackChatConfiguration config = SlackChatConfiguration.getConfiguration().asUser();
            config.withName("Wise Old Man");
            session.sendMessage(session.findChannelByName("general"), "_Began reading_ *" + bookReader.nextLine() + "*", null, config);
            readNextPage(session);
        }
        else if(event.getMessageContent().toLowerCase().contains("turn the page")){
            readNextPage(session);
        }
    }

    private void readNextPage(SlackSession session){
        int count = 0;
        while( count < POSTS_PER_PAGE && bookReader.hasNext()){
            boolean read = postMessage(bookReader.nextLine(), session);
            if(read){
                count++;
            }
        }
    }

    private boolean postMessage(String line, SlackSession session){
        if(!line.contains(":")){
            return false;
        }

        String lineLower = line.toLowerCase();

        //Figure out if it is an action line or a statement
        if(lineLower.contains("Scene:")){
            SlackChatConfiguration config = SlackChatConfiguration.getConfiguration().asUser();
            config.withName("Scene Manager");
            session.sendMessage(session.findChannelByName("general"), "_" + line + "_", null, config);
            return true;
        }

        SlackChatConfiguration config = SlackChatConfiguration.getConfiguration().asUser();
        config.withName(line.substring(0, line.indexOf(':')));
        session.sendMessage(session.findChannelByName("general"), line.substring(line.indexOf(':') + 1).trim() , null, config);
        return true;
    }
}
