package store.csolved.csolved.global.utils;


import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordManager
{
    private static final int logRound = 12;

    public String hashPassword(String plainPassword)
    {
        String salt = BCrypt.gensalt(logRound);
        return BCrypt.hashpw(plainPassword, salt);
    }

    public boolean verifyPassword(String plainPassword, String hashedPassword)
    {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
