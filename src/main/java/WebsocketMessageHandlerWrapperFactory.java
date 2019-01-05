import com.google.gson.*;

import java.lang.reflect.Type;

public class WebsocketMessageHandlerWrapperFactory implements JsonDeserializer<WebsocketMessageHandlerWrapper> {
    private Gson gson;

    WebsocketMessageHandlerWrapperFactory() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(WebsocketMessageHandlerWrapper.class, this);
        gson = gsonBuilder.create();
    }


    @Override
    public WebsocketMessageHandlerWrapper deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        WebSocketMessageType messageType = WebSocketMessageType.valueOf(jsonObject.get("command").getAsString());
        switch (messageType) {
            case GET_MESSAGES:
                return new WebsocketMessageHandlerWrapper<>(jsonObject,
                        new GetMessagesWebsocketMessageHandler(dataFromJson(jsonObject,
                                GetMessagesWebsocketMessageHandler.getDataClass())));
            case SEND_MESSAGE:
                return new WebsocketMessageHandlerWrapper<>(jsonObject,
                        new SendMessageWebsocketMessageHandler(dataFromJson(jsonObject,
                                SendMessageWebsocketMessageHandler.getDataClass())));
            case GET_FRIENDS:
                return new WebsocketMessageHandlerWrapper<>(jsonObject,
                        new GetFriendsWebsocketMessageHandler(dataFromJson(jsonObject,
                                GetFriendsWebsocketMessageHandler.getDataClass())));
            case SEARCH_FRIENDS:
                return new WebsocketMessageHandlerWrapper<>(jsonObject,
                        new SearchFriendsWebsocketMessageHandler(dataFromJson(jsonObject,
                                SearchFriendsWebsocketMessageHandler.getDataClass())));
            case LOGOUT:
                return new WebsocketMessageHandlerWrapper<>(jsonObject,
                        new LogoutWebsocketMessageHandler());
            case ADD_FRIEND:
                return new WebsocketMessageHandlerWrapper<>(jsonObject,
                        new AddFriendWebsocketMessageHandler(dataFromJson(jsonObject,
                                AddFriendWebsocketMessageHandler.getDataClass())));
            default:
                return null;
        }
    }

    public WebsocketMessageHandlerWrapper create(String message) {
        return gson.fromJson(message, WebsocketMessageHandlerWrapper.class);
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
