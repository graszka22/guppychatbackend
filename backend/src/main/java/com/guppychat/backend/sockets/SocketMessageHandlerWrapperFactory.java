package com.guppychat.backend.sockets;

import com.google.gson.*;
import com.guppychat.backend.sockets.handlers.*;

import java.lang.reflect.Type;

public class SocketMessageHandlerWrapperFactory implements JsonDeserializer<SocketMessageHandlerWrapper> {
    private Gson gson;

    SocketMessageHandlerWrapperFactory() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SocketMessageHandlerWrapper.class, this);
        gson = gsonBuilder.create();
    }


    @Override
    public SocketMessageHandlerWrapper deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        WebSocketMessageType messageType = WebSocketMessageType.valueOf(jsonObject.get("command").getAsString());
        switch (messageType) {
            case GET_MESSAGES:
                return new SocketMessageHandlerWrapper<>(jsonObject,
                        new GetMessagesSocketMessageHandler(dataFromJson(jsonObject,
                                GetMessagesSocketMessageHandler.getDataClass())));
            case SEND_MESSAGE:
                return new SocketMessageHandlerWrapper<>(jsonObject,
                        new SendMessageSocketMessageHandler(dataFromJson(jsonObject,
                                SendMessageSocketMessageHandler.getDataClass())));
            case GET_FRIENDS:
                return new SocketMessageHandlerWrapper<>(jsonObject,
                        new GetFriendsSocketMessageHandler(dataFromJson(jsonObject,
                                GetFriendsSocketMessageHandler.getDataClass())));
            case SEARCH_FRIENDS:
                return new SocketMessageHandlerWrapper<>(jsonObject,
                        new SearchFriendsSocketMessageHandler(dataFromJson(jsonObject,
                                SearchFriendsSocketMessageHandler.getDataClass())));
            case LOGOUT:
                return new SocketMessageHandlerWrapper<>(jsonObject,
                        new LogoutSocketMessageHandler());
            case ADD_FRIEND:
                return new SocketMessageHandlerWrapper<>(jsonObject,
                        new AddFriendSocketMessageHandler(dataFromJson(jsonObject,
                                AddFriendSocketMessageHandler.getDataClass())));
            default:
                return null;
        }
    }

    public SocketMessageHandlerWrapper create(String message) {
        return gson.fromJson(message, SocketMessageHandlerWrapper.class);
    }

    private <T> T dataFromJson(JsonObject jsonObject, Class<T> cl) {
        return gson.fromJson(jsonObject.get("data"), cl);
    }

    private enum WebSocketMessageType {
        GET_MESSAGES,
        SEND_MESSAGE,
        GET_FRIENDS,
        SEARCH_FRIENDS,
        LOGOUT,
        ADD_FRIEND,
    }
}
