package br.com.eboscatto.todolist.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    // Gera chave em memória a cada execuçao
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Gera token com username do usuário autenticado
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Aqui pega o username do banco
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // Expira em 1 dia
                .signWith(key)
                .compact();
    }

    // Valida o token e retorna o username
    public static String validateToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject(); // retorna o userName
    }
}
