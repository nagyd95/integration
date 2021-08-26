package integration.rest;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@RestController
public class Login {

    @Autowired
    private AuthUserTokenRepository tokenRepository;

    @PostMapping("/authentication")
    public String login(@RequestParam("token") String loginToken) {
        String token;
        if(succesLogin(loginToken)) {
             token = getJWTToken(loginToken);
        }else{
            token = "Invalid Login Token!";
        }

        return token;

    }

    private boolean succesLogin(String loginToken) {
        String token=loginToken.trim();
        List<AuthUserToken> authUserToken=tokenRepository.checkToken(token);

        return authUserToken.size()==0 ? false : true;
    }

    private String getJWTToken(String loginToken) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(loginToken)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Bearer " + token;
    }


}
