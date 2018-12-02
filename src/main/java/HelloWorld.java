import io.javalin.Javalin;

public class HelloWorld {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        app.post("/register", new RegisterRequestHandler());
        app.post("/login", new LoginRequestHandler());
        Javalin.create().ws("/chat", ws -> {
            ws.onConnect(session -> {
                session.send("Hello user :3");
            });

            ws.onMessage((session, message) -> {
                session.send("ECHO: "+message);
            });
        }).start(7070);
    }
}