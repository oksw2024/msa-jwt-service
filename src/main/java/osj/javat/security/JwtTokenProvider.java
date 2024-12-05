package osj.javat.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import osj.javat.detail.CustomUserDetails;

@Component
public class JwtTokenProvider {
	//access token용 secret key
	@Value("${custom.jwt.secret}")
	private String jwtSecretKey;
	//refresh token용 secret key
	@Value("${custom.jwt.secret2}")
	private String jwtSecretKey2;
	@Value("${custom.jwt.accessTokenExpirationTime}")
	private Long jwtAccessTokenExpirationTime;
	@Value("${custom.jwt.refreshTokenExpirationTime}")
	private Long jwtRefreshTokenExpirationTime;
	
	@PostConstruct
    protected void init() {
        jwtSecretKey = Base64.getEncoder().encodeToString(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
        jwtSecretKey2 = Base64.getEncoder().encodeToString(jwtSecretKey2.getBytes(StandardCharsets.UTF_8));
    }
	
	private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
	
	private SecretKey getSigningKey2() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey2);
        return Keys.hmacShaKeyFor(keyBytes);
    }
	
	public String generateAccessToken(Authentication authentication) {
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Date now = new Date();
        String accessToken = Jwts.builder()
        		.subject(customUserDetails.getUsername())
        		.claim("user-id", customUserDetails.getId())
        		.claim("user-email", customUserDetails.getEmail())
        		.issuedAt(now)
                .expiration(new Date(now.getTime() + jwtAccessTokenExpirationTime)).signWith(getSigningKey()).compact();
        return accessToken;
	}
	
	public String generateRefreshToken(Authentication authentication) {
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Date now = new Date();
        String refreshToken = Jwts.builder()
        		.subject(customUserDetails.getUsername())
        		.claim("user-id", customUserDetails.getId())
        		.claim("user-email", customUserDetails.getEmail())
        		.issuedAt(now)
                .expiration(new Date(now.getTime() + jwtRefreshTokenExpirationTime)).signWith(getSigningKey2()).compact();
        return refreshToken;
	}
	
	public Long getUserIdFromToken(String accessToken) {
		Long info = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(accessToken).getPayload()
				.get("user-id", Long.class);
		return info;
	}
	
	public String getUsernameFromToken(String accessToken) {
        String info = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(accessToken).getPayload()
                .getSubject();
        return info;
    }
	
	// 토큰 만료 여부 확인
	public boolean validateToken(String accessToken) {
        try {
            Jws<Claims> claims = Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(accessToken);
            return !claims.getPayload().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
        	return false;
        } catch (SignatureException e) {
        	return false;
        } catch (Exception e) {
            return false;
        }
    }


}
