package br.com.eboscatto.todolist.autentication;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.eboscatto.todolist.repository.IUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component

public class FilterTasksAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository iUserRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();

        if (servletPath.equals("/tasks/")) {
            // Pegar a autenticação (usuário e senha)
            var authorization = request.getHeader("Authorization");

            // Receber as credenciais em BASiC64
            var authEncoded = authorization.substring("Basic".length()).trim();

            // Gerar um Array de caracteres somente do BASIC64
            byte[] authDecode = Base64.getDecoder().decode(authEncoded);

            // Receber o Array com as credenciais
            var authString = new String(authDecode);

            // Separando o Array em duas partes:
            // usuário / senha ["eboscatto", "1234']
            String[] credentials = authString.split(":");
            String userName = credentials[0];
            String password = credentials[1];

            // Verificar a saída se usuário de fato existe e se a senha está correta

        /*
        System.out.println("Authorization");
        System.out.println(userName);
        System.out.println(password);
        */


            // Pega somente a parte da senha do Basic e remove todos os espaços
            // authorization.substring("Basic".length()).trim();


            // Validar usuário se usuário existe
            var user = this.iUserRepository.findByUserName(userName);
            if (user == null) {
                response.sendError(401);
            } else {
                // Validar senha
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());

                if (passwordVerify.verified) {
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }
            }

        } else {

            filterChain.doFilter(request, response);
        }
    }
}


