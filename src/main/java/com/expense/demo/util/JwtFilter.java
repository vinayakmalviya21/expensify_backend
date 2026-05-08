package com.expense.demo.util;

import java.io.IOException;
import java.util.ArrayList;

import com.expense.demo.entity.User;
import com.expense.demo.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository repo;

    // 🔥 SKIP LOGIN + REGISTER
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/auth");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws IOException, ServletException {

        String header = req.getHeader("Authorization");

        // 🔥 CRITICAL SAFETY CHECK
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        try {
            String token = header.substring(7);

            String email = jwtUtil.extractUsername(token);

            User user = repo.findByEmail(email).orElse(null);

            if (user != null) {
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                user, null, new ArrayList<>());

                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception e) {
            // ignore invalid token
        }

        chain.doFilter(req, res);
    }

}