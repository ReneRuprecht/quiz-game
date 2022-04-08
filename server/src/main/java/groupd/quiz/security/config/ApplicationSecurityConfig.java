package groupd.quiz.security.config;

import groupd.quiz.jwt.JWTUsernameAndPasswordAuthenticationFilter;
import groupd.quiz.jwt.JwtConfig;
import groupd.quiz.jwt.JwtSecretKey;
import groupd.quiz.jwt.JwtTokenVerifier;
import groupd.quiz.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtConfig jwtConfig;
    private final JwtSecretKey jwtSecretKey;

    /**
     * configures all the routes and their security options
     *
     * @param http gets inserted automatically by spring
     * @throws Exception   an exception
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/v*/registration/**")
                .permitAll().and()
                .authorizeRequests()
                .antMatchers("/api/v*/question/**")
                .permitAll().and()
                .authorizeRequests()
                .antMatchers("/quiz-game-websocket/**")
                .permitAll().and()
                .authorizeRequests()
                .antMatchers("/user-photos/**")
                .permitAll().and()
                .addFilter(jwtUsernameAndPasswordAuthenticationFilter())
                .addFilterAfter(new JwtTokenVerifier(jwtConfig, jwtSecretKey), JWTUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest()
                .authenticated();


    }


    /**
     * sets up the login endpoint for jwt authentication filter
     *
     * @return a jwt filter
     * @throws Exception throws an exception
     */
    public JWTUsernameAndPasswordAuthenticationFilter jwtUsernameAndPasswordAuthenticationFilter() throws Exception {
        JWTUsernameAndPasswordAuthenticationFilter jwtUsernameAndPasswordAuthenticationFilter =
                new JWTUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, jwtSecretKey);
        jwtUsernameAndPasswordAuthenticationFilter.setFilterProcessesUrl("/api/v1/login");
        return jwtUsernameAndPasswordAuthenticationFilter;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    // is used for authentication with the database
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }
}
