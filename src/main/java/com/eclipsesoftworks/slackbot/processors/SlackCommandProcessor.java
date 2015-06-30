package com.eclipsesoftworks.slackbot.processors;

import com.eclipsesoftworks.slackbot.JedisClient;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

/**
 * Created by Helmsdg on 6/19/2015.
 */
public interface SlackCommandProcessor {

	public void processCommand(SlackMessagePosted event, SlackSession session, JedisClient jedisClient);

}