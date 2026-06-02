package re.session17_bai01.Controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MovieTicketJwtExample {
    public static void main(String[] args) {
        // (1) Tạo Secret Key (nên lưu trong config, ở đây demo hardcode)
        String secret = "mySuperSecretKeyForJwtMovieTicketSystem123456"; // >=32 bytes
        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        // (2) Claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 123L);
        claims.put("roles", "USER");

        // (3) Thời gian
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 3600 * 1000); // 1 giờ

        // (4) Tạo JWT
        String jwtToken = Jwts.builder()
                .claims(claims)
                .subject("user@movieticket.com")
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key) // không cần truyền SignatureAlgorithm nữa
                .compact();

        System.out.println("Generated JWT: " + jwtToken);

        try {
            // (5) Parse và verify JWT
            var parsedClaims = Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getPayload();

            System.out.println("JWT is valid and verified!");

            // (6) Lấy dữ liệu từ payload
            String subject = parsedClaims.getSubject();
            Long userId = parsedClaims.get("userId", Long.class);
            String roles = parsedClaims.get("roles", String.class);

            System.out.println("Subject: " + subject);
            System.out.println("User ID: " + userId);
            System.out.println("Roles: " + roles);
        } catch (Exception e) {
            System.err.println("Invalid JWT: " + e.getMessage());
        }
    }
}
