package org.example;

import com.google.gson.*;

import java.lang.reflect.Type;

public class RedditPostDeserializer implements JsonDeserializer<RedditPost> {
    @Override
    public RedditPost deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonObject postObject = jsonObject.getAsJsonObject("data");
        return new RedditPost(
                postObject.get("name").getAsString(),
                postObject.get("title").getAsString(),
                postObject.get("thumbnail").getAsString(),
                postObject.get("url").getAsString(),
                postObject.get("permalink").getAsString()
        );
    }
}
