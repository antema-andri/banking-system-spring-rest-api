package com.backend.bankingsystem.service;

import com.nimbusds.jwt.SignedJWT;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TokenService {
    private final JwtEncoder encoder;

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> !authority.startsWith("ROLE"))
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("http://localhost:8080")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();
        var encoderParameters =  getEncoderParameters(claims);
        return this.encoder.encode(encoderParameters).getTokenValue();
    }

    public JwtEncoderParameters getEncoderParameters(JwtClaimsSet claims) {
        JwtEncoderParameters encoderParameters=JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS512).build(), claims);
        return encoderParameters;
    }

    public boolean isValidToken(String token) throws ParseException {
        return validateSignature(token);
    }

    public boolean validateSignature(String token) throws ParseException {
        boolean isValidSignature;
        SignedJWT signedJWT=SignedJWT.parse(token);
        String algoToken=signedJWT.getHeader().getAlgorithm().getName();
        isValidSignature=algoToken.equals("HS512");
        return isValidSignature;
    }
}
