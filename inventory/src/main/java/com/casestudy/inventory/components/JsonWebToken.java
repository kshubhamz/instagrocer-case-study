package com.casestudy.inventory.components;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.casestudy.instagrocer.commons.exception.AuthorizationException;
import com.casestudy.instagrocer.commons.exception.BadRequestException;

@Component
public class JsonWebToken {
	@Value("${jwt_secret}")
	private String secret;
	
	private static final String USERNAME_KEY = "username";
	private static final String NAME_KEY = "name";

	public String generateToken(String name, String username) {
		// putting all claims inside an object
		Map<String, Object> claims = new HashMap<>();
		claims.put(USERNAME_KEY, username);
		claims.put(NAME_KEY, name);

		return generateToken(claims);
	}

	public String getNameFromToken(Map<String, Claim> claim) {
		return claim.get(NAME_KEY).asString();
	}

	public String getUsernameFromToken(Map<String, Claim> claim) {
		return claim.get(USERNAME_KEY).asString();
	}

	public String getNameFromToken(String token) {
		return getClaim(NAME_KEY, token).asString();
	}

	public String getUsernameFromToken(String token) {
		return getClaim(USERNAME_KEY, token).asString();
	}

	public Claim getClaim(String claim, String token) {
		Map<String, Claim> claims = getAllClaimsFromToken(token);
		return claims.get(claim);
	}

	protected Map<String, Claim> getAllClaimsFromToken(String token) {
		DecodedJWT decodedJWT = verifyToken(token);
		return decodedJWT.getClaims();
	}

	private DecodedJWT verifyToken(String token) {
		DecodedJWT jwt = null;

		JWTVerifier verifier = com.auth0.jwt.JWT.require(Algorithm.HMAC256(secret)).build();
		
		try {
			jwt = verifier.verify(token);
		} catch (JWTVerificationException e) {
			// invalid token
			throw new AuthorizationException("Couldn't verify token. Token modified/changed.");
		}

		return jwt;
	}

	private String generateToken(Map<String, Object> payloadClaims) {
		String token = null;
		
		try {
			token = com.auth0.jwt.JWT.create().withPayload(payloadClaims).sign(Algorithm.HMAC256(secret));
		} catch (JWTCreationException e) {
			// Invalid Signing configuration / Couldn't convert Claims
			throw new BadRequestException("Invalid configuration / Couldn't convert Claims");
		}

		return token;
	}
}
