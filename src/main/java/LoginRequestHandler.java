import io.javalin.Context;
import io.javalin.Handler;
import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class LoginRequestHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) throws Exception {
        String username = context.formParam("username");
        String password = context.formParam("password");
        AccountFinder accountFinder = new AccountFinder();
        AccountGateway account = accountFinder.findByUsername(username);
        if(BCrypt.checkpw(password, account.getPassword())) {
            String token = generateToken();
            account.setSessionToken(token);
            account.update();
            context.result(token);
            context.status(200);
            return;
        }
        context.status(403);
    }

    private String generateToken() {
        byte[] bytes = new byte[16];
        try {
            SecureRandom.getInstanceStrong().nextBytes(bytes);
            StringBuilder builder = new StringBuilder();
            for(byte b : bytes) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch(NoSuchAlgorithmException e) {
            throw new RuntimeException("Can't generate session token", e);
        }
    }
}
