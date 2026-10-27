package com.APIZero.Zero.config;


import com.APIZero.Zero.services.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private  final JwtService jwtService;

    public JwtFilter(JwtService jwtService){
        this.jwtService = jwtService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer")){
            String token = authHeader.substring(7);

            try {
                //Se o token estiver expirado ou adulterado, cairá no bloco catch automaticamente
                String email = jwtService.extractSubject(token);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                    //Registra o usuário como "autenticado na memoria desta requisição especifica
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            email, null, Collections.emptyList()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }catch (Exception e){
                //Token invalido, a requisição seguirá sem credenciais e o spring bloqueara o acesso
            }
        }
        filterChain.doFilter(request, response);
    }
}
