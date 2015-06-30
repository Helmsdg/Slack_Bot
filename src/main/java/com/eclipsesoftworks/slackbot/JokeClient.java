package com.eclipsesoftworks.slackbot;

import com.eclipsesoftworks.slackbot.model.Joke;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Created by Helmsdg on 6/24/2015.
 */
public class JokeClient {

    private static String REQUEST_URI = "http://tambal.azurewebsites.net/";
    private static String RANDOM_JOKE_METHOD = "joke/random";

    public static Joke getRandomJoke(){
        WebTarget webTarget = ClientBuilder.newClient().target(REQUEST_URI).path(RANDOM_JOKE_METHOD);

        Response response = webTarget.request().get();
        Response.StatusType statusInfo = response.getStatusInfo();
        return response.readEntity(Joke.class);

    }

}
