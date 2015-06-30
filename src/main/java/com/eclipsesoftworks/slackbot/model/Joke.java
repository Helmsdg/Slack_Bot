package com.eclipsesoftworks.slackbot.model;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "joke"
})
public class Joke {

    @JsonProperty("joke")
    private String joke;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The joke
     */
    @JsonProperty("joke")
    public String getJoke() {
        return joke;
    }

    /**
     *
     * @param joke
     * The joke
     */
    @JsonProperty("joke")
    public void setJoke(String joke) {
        this.joke = joke;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}