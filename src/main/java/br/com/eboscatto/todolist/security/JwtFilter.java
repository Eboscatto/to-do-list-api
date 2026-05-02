package br.com.eboscatto.todolist.security;

import br.com.eboscatto.todolist.repository.IUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import static br.com.eboscatto.todolist.security.JwtUtil.getUserId;
import static br.com.eboscatto.todolist.security.JwtUtil.getUsername;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // Só protege rotas privadas
        if (path.startsWith("/tasks") || path.startsWith("/auth/me")) {

            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Cabeçalho de autorização ausente ou inválido!");
                return;
            }

            try {
                String token = authHeader.substring(7);

                // Extrai username do token
                String username = getUsername(token);

                String userId = getUserId(token);

                // Busca usuário no banco
                var user = userRepository.findByUserName(username);

                if (user == null) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuário não existe");
                    return;
                }

                // Salva o ID no request
                request.setAttribute("userId", user.getId().toString());
                request.setAttribute("userId", userId);

                // Seta autenticação no Spring
                UserDetailsImpl userDetails = new UserDetailsImpl(user);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido!");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
