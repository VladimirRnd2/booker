package com.zuzex.booker.dto.serializer;

import com.google.gson.*;
import com.zuzex.booker.dto.AuthorResponse;
import com.zuzex.booker.dto.BookResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookResponseDeserializer implements JsonDeserializer<BookResponse> {
    @Override
    public BookResponse deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

        BookResponse bookResponse = new BookResponse();

        List<AuthorResponse> authorResponses = new ArrayList<>();

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement element = jsonObject.get("items");
        JsonArray items = element.getAsJsonArray();
        for (JsonElement el: items) {
            JsonObject obj = el.getAsJsonObject();
            JsonElement volumeInfo = null;
            if(obj.has("volumeInfo")) {
                volumeInfo = obj.get("volumeInfo");
            }
            JsonObject volumeInfoObj = null;
            if (volumeInfo != null) {
                volumeInfoObj = volumeInfo.getAsJsonObject();
            }

            if(volumeInfoObj.has("title")) {
                bookResponse.setTitle(volumeInfoObj.get("title").getAsString());
            }
            if(volumeInfoObj.has("publishedDate")) {
                bookResponse.setDate(volumeInfoObj.get("publishedDate").getAsString());
            }
            if(volumeInfoObj.has("authors")) {
                JsonArray authors = volumeInfoObj.getAsJsonArray("authors");
                for (JsonElement a: authors) {
                    if(a.isJsonPrimitive()) {
                        authorResponses.add(new AuthorResponse(a.getAsString()));
                    }
                }
            }
            bookResponse.setAuthors(authorResponses);
            break;
        }
        return bookResponse;
    }
}
