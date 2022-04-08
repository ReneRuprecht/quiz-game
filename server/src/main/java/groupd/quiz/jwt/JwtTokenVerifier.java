package groupd.quiz.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
public class JwtTokenVerifier extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;

    private final JwtSecretKey jwtSecretKey;


    /**
     * filters the request and authenticates it
     *
     * @param request     gets filled automatically by the request
     * @param response    gets filled automatically by the request
     * @param filterChain gets filled automatically by the request
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());

            // checks if the header is empty or does not start with the prefix
        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }
        // replaces the bearer part with an empty string
        String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");

        try {

            // builds the signed token
            Jws<Claims> claimsJws =
                    Jwts.parserBuilder()
                            .setSigningKey(
                                    jwtSecretKey.getSecretKey()
                            )
                            .build()
                            .parseClaimsJws(token);

            Claims body = claimsJws.getBody();
            String username = body.getSubject();

            var authorities = (List<Map<String, String>>) body.get("authorities");

            Set<SimpleGrantedAuthority> authority =
                    authorities.stream().map(m -> new SimpleGrantedAuthority(
                                    m.get("authority")))
                            .collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authority
            );
            // sets the user as authenticated
            SecurityContextHolder.getContext().setAuthentication(authentication);


        } catch (JwtException e) {
            response.getWriter().write("Token ist nicht valid");
            response.setStatus(403);
            response.getWriter().flush();
            //throw new IllegalStateException(String.format("Token %s ist nicht valid", token));
            return;

        }

        filterChain.doFilter(request, response);
    }
}
