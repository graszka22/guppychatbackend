import com.google.gson.*;

import java.lang.reflect.Type;

public class WebsocketMessageHandlerWrapperFactory implements JsonDeserializer<WebsocketMessageHandlerWrapper> {
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
            default:
                return null;
        }
    }

    public WebsocketMessageHandlerWrapper create(String message) {
        return new Gson().fromJson(message, WebsocketMessageHandlerWrapper.class);
    }

    private <T> T dataFromJson(JsonObject jsonObject, Class<T> cl) {
        return new Gson().fromJson(jsonObject.get("data"), cl);
    }

    private enum WebSocketMessageType {
        GET_MESSAGES,
        SEND_MESSAGE
    }
}