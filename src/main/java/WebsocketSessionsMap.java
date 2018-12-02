import io.javalin.websocket.WsSession;

import java.util.HashMap;

public class WebsocketSessionsMap {
    private HashMap<Integer, WsSession> sessionMap;

    public void addSession(WsSession session, int userId) {
        sessionMap.put(userId, session);
    }

    public void removeSession() {

    }
}
