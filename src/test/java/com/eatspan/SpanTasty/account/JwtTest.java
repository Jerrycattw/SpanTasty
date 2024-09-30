package com.eatspan.SpanTasty.account;

import static org.springframework.test.web.client.response.MockRestResponseCreators.withException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtTest {
	
	@Test
	public void testGen() {
		Map<String, Object> claims = new HashMap<>();
		claims.put("memberId", 1);
		claims.put("memberName", "測試");
		
		
		String token = JWT.create()
				.withClaim("member",claims)
				.withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*12))
				.sign(Algorithm.HMAC256("test"));
	
		System.out.println(token);
	}
	
	@Test
	public void testParse() {
	 	String token ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJtZW1iZXIiOnsibWVtYmVyTmFtZSI6Iua4rOippiIsIm1lbWJlcklkIjoxfSwiZXhwIjoxNzI3NTYwODY1fQ.TaD70qCtq4z2X-2xo3Q86I83NQXuGFzAi74dg3M-Wdc";
		JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("test")).build();
		
		DecodedJWT decodedJwt = jwtVerifier.verify(token);
		
		Map<String, Claim> claims = decodedJwt.getClaims();
		System.out.println(claims.get("member"));
		
		
	}
	
	

}
