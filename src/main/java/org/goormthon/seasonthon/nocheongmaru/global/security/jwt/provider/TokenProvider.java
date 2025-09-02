package org.goormthon.seasonthon.nocheongmaru.global.security.jwt.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.goormthon.seasonthon.nocheongmaru.global.security.jwt.dto.TokenResponse;
import org.goormthon.seasonthon.nocheongmaru.domain.member.model.entity.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Slf4j
@Component
public class TokenProvider {
    
    private final Key key;
    
    private static final String ROLE_CLAIM = "role";
    private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60;
    private static final long REFRESH_TOKEN_EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 7;
    
    public TokenProvider(@Value("${jwt.secret}") String secret) {
        byte[] decode = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(decode);
    }
    
    public TokenResponse issueToken(Long memberId, Role role) {
        String accessToken = generateAccessToken(memberId, role);
        String refreshToken = generateRefreshToken();
        
        return TokenResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
    
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        String memberEmail = claims.getSubject();
        
        Role role = getRole(claims);
        Collection<SimpleGrantedAuthority> authorities = Collections.singleton(
            new SimpleGrantedAuthority(role.name())
        );
        
        return new UsernamePasswordAuthenticationToken(memberEmail, null, authorities);
    }
    
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (SignatureException | MalformedJwtException e) {
            log.warn("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT, 만료된 JWT 입니다.");
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT, 지원되지 않는 JWT 입니다.");
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims is empty, 잘못된 JWT 입니다.");
        }
        return false;
    }
    
    public Long getSubject(String token) {
        try {
            Claims claims = getClaims(token);
            return Long.valueOf(claims.getSubject());
        } catch (ExpiredJwtException e) {
            return Long.valueOf(e.getClaims().getSubject());
        }
    }
    
    public Role getRole(Claims claims) {
        String role = claims.get(ROLE_CLAIM, String.class);
        return Role.valueOf(role);
    }
    
    private String generateAccessToken(Long memberId, Role role) {
        Date expirationDate = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME);
        
        return Jwts.builder()
            .claim(ROLE_CLAIM, role.name())
            .setSubject(memberId.toString())
            .setExpiration(expirationDate)
            .signWith(key)
            .compact();
    }
    
    private String generateRefreshToken() {
        Date expirationDate = new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME);
        
        return Jwts.builder()
            .setExpiration(expirationDate)
            .signWith(key)
            .compact();
    }
    
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
    
}
