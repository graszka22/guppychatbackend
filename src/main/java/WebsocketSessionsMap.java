import io.javalin.websocket.WsSession;

import java.util.HashMap;

public class WebsocketSessionsMap {
    private HashMap<Integer, WsSession> sessionMap = new HashMap<>();

    public void addSession(WsSession session, int userId) {
        sessionMap.put(userId, session);
    }

    public WsSession getUserSession(int userId) {
        return sessionMap.get(userId);
    }

    public void removeSession() {

    }
}
