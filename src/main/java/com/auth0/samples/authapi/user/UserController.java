package com.auth0.samples.authapi.user;

import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

import static com.auth0.samples.authapi.security.SecurityConstants.SECRET;
import static com.auth0.samples.authapi.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/users")
public class UserController {
	private ApplicationUserRepository applicationUserRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;


	public UserController(ApplicationUserRepository applicationUserRepository,
						  BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.applicationUserRepository = applicationUserRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@PostMapping("/sign-up")
	public ResponseEntity<String> signUp(@RequestBody ApplicationUser user) {

		ApplicationUser u = applicationUserRepository.findByUsername(user.getUsername());
		if (u != null){
			return new ResponseEntity<String>("{\"message\": \"User Already Exist\"}", HttpStatus.NOT_ACCEPTABLE);
		}
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		applicationUserRepository.save(user);
		u = applicationUserRepository.findByUsername(user.getUsername());
		return new ResponseEntity<String>(
				"{\"message\": \"Success\"," +
						"\"createdId\": " + u.getId() +
						"}", HttpStatus.CREATED);
	}



	@PostMapping("/verify-token")
	public ResponseEntity<String> getToken(@RequestHeader(value = "Authorization") String token){
		//user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		// = req.toSingleValueMap().get("Authorization");

		String res = "{'statusCode': 200, 'statusDescription': ''}";
		Map<String, Object> m = new HashMap<String, Object>();

		//ResponseEntity<Map<String, Object>> response = new ResponseEntity<Map<String, Object>>();

		System.out.println(token);
		if (token != null) {
			// parse the token.
			String user = Jwts.parser()
					.setSigningKey(SECRET)
					.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
					.getBody()
					.getSubject();

			if (user != null) {
				//return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
				m.put("statusCode", 200);
				m.put("statusDescription", "Token is Valid");


				return new ResponseEntity<String>("Success", HttpStatus.OK);

			}else {
				m.put("statusCode",  HttpStatus.UNAUTHORIZED);
				m.put("statusDescription", "Invalid Token");
				return new ResponseEntity<String>("Invalid Token", HttpStatus.UNAUTHORIZED);
			}
		}
		m.put("statusCode",  HttpStatus.UNAUTHORIZED);
		m.put("statusDescription", "Unauthorized Access");
		return new ResponseEntity<String>("Unauthorized Access", HttpStatus.UNAUTHORIZED);

	}

}
