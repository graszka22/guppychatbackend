import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        app.post("/register", new RegisterRequestHandler());
        app.post("/login", new LoginRequestHandler());
        Javalin.create().ws("/chat", new WebSocketHandler()).start(7070);
    }
}