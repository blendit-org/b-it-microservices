package com.blenditorg.auth_api.services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.blenditorg.auth_api.dtos.RegisterUserDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	// This auto-wires/injects the application.properties:
	// security.jwt.secret-key property to this
	// variable [*I guess] <--------------------------------- re-check
	@Value("${security.jwt.secret-key}")
	private String secretKey;

	@Value("${security.jwt.expiration-time}")
	private long jwtExpiration;

	/**
	 * Extract <code>T</code> type of data i.e. subject, expiration date, role etc.
	 * from JWT. This uses a functional interface for applying different types of
	 * getters i.e. Claims::getSubject(), Claims::getExpirationDate() etc.
	 * <code>claimsResolver.apply(claims)</code> results in claims.getSubject(),
	 * claims.getExpirationDate() etc...
	 * 
	 * @param <T>
	 * @param token
	 * @param claimsResolver
	 * @return T
	 */
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		System.out.println("[DEBUG]: extractClaim() called");
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * 1. .parserBuilder() creates an empty parser configuration object This has a
	 * signature like this: </br>
	 * <code>
	 * 		{
	 * 			<ul>signingKey: 		NULL,</ul>
	 * 			<ul>clock:				DefaultClock,</ul>
	 * 			<ul>requireExpiration:	false,</ul>
	 * 			<ul>requireIssuedAt:	false, //... etc</ul>
	 * 		}
	 * 		</code> </br>
	 * 
	 * 2. .setSigningKey() sets the signing key value.</br>
	 * 3. .build() makes the JwtParser immutable. </br>
	 * 4. .parseClaimsJws() checks if the token is valid by hashing [header+"
	 * "+payload] by the signing key from step-2 and matching it with [signature] of
	 * the <b> token </b> and returns a Jws<Claims> object </br>
	 * 5. .getBody() returns a Claims objects which is basically Map<String, Object>
	 * type.
	 * 
	 * @param token
	 * @return Claims
	 */
	private Claims extractAllClaims(String token) {
		System.out.println("[DEBUG]: extractAllClaims() called");
		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
	}

	/**
	 * This means "security.jwt.secret-key" property should be a BASE64 encoded
	 * string or else this will not work.
	 * 
	 * @pritomash guesses, this is to force the service moderators to use strong
	 *            secret-key
	 * 
	 */
	private Key getSignInKey() {
		
		System.out.println("[DEBUG]: getSignInKey() called");

		byte[] key = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(key);
	}

	public String extractUsername(String token) {
		System.out.println("[DEBUG]: extractUsername() called");
		return extractClaim(token, Claims::getSubject);
	}
	
	public String generateToken(UserDetails userDetails) {
		System.out.println("[DEBUG]: generateToken(UserDetails) called");
		return generateToken(new HashMap<>(), userDetails);
	}
	
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		System.out.println("[DEBUG]: generateToken(Map<String, Object, UserDetails) called");
		return buildToken(extraClaims, userDetails, jwtExpiration);
	}
	
	public String buildToken(
			Map<String, Object> extraClaims,
			UserDetails userDetails,
			long expiration
	) {
		System.out.println("[DEBUG]: buildToken() called");
		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	public String buildEmailToken(RegisterUserDto registerUserDto) {
		Map<String, Object> waitingUser = Map.of(
				"userId", registerUserDto.getUserId(),
				"password", registerUserDto.getPassword(),
				"fullName", registerUserDto.getFullName());
		return Jwts
				.builder()
				.setClaims(waitingUser)
				.setSubject(registerUserDto.getEmail())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 15*60*1000))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	public boolean isTokenValid(String token, UserDetails userDetails) {
		System.out.println("[DEBUG]: isTokenValid() called");
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		System.out.println("[DEBUG]: isTokenExpired() called");
		final Date expirationTime =  extractClaim(token, Claims::getExpiration);
		return expirationTime.before(new Date());
	}
	
	public long getExpirationTime() {
		return jwtExpiration;
	}

}
