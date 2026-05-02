package br.com.eboscatto.todolist.security;

import br.com.eboscatto.todolist.model.UserModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Gera chave em memória a cada execuçao
    // private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String SECRET = "minha-chave-super-secreta-12345678901234567890";
    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // Gera token com username do usuário autenticado
    public static String generateToken(UserModel user) {
        return Jwts.builder()
                .setSubject(user.getUserName())
                .claim("userId", user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();
    }

    // Valida o token
    public static Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // retorna o userName
    public static String getUsername(String token) {

        return getClaims(token).getSubject();
    }

    // retorna o userId
    public static String getUserId(String token) {

        return getClaims(token).get("userId", String.class);
    }

}
