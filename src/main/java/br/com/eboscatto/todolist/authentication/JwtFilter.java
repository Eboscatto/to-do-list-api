package br.com.eboscatto.todolist.authentication;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // Protege rotas /tasks
        if (path.startsWith("/tasks")) {

            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(401, "Cabeçalho de autorização ausente ou inválido");
                return;
            }

            try {
                String token = authHeader.substring(7);

                // valida token e pega username
                String username = JwtUtil.getUsername(token);
                String userId = JwtUtil.getUserId(token);

                // cria autenticação no contexto do Spring
                var authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.emptyList()
                );

                SecurityContextHolder.clearContext();
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // opcional: colocar userId na request
                request.setAttribute("userId", userId);

                filterChain.doFilter(request, response);

            } catch (Exception e) {
                response.sendError(401, "Token inválido");
            }

        } else {
            filterChain.doFilter(request, response);
        }
    }
}
