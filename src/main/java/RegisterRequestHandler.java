import io.javalin.Context;
import io.javalin.Handler;
import org.jetbrains.annotations.NotNull;
import org.mindrot.jbcrypt.BCrypt;

public class RegisterRequestHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) throws Exception {
        String username = context.formParam("username");
        String password = context.formParam("password");
        String email = context.formParam("email");

        password = BCrypt.hashpw(password, BCrypt.gensalt(12));
        AccountGateway accountGateway = new AccountGateway(username, password, email);
        accountGateway.insert();
        Registry.getAccountIdentityMap().addAccount(accountGateway);

        context.status(201);
    }
}
