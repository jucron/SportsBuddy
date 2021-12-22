package com.joaorenault.sportbuddy.config;

import com.joaorenault.sportbuddy.services.LoginAccessServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
//    private final HashService hashService;
    private final LoginAccessServiceImpl loginAccessService;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, LoginAccessServiceImpl loginAccessService) {
        this.passwordEncoder = passwordEncoder;
        this.loginAccessService = loginAccessService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] staticResources  =  {
                "/", "index", "/css/**", "/images/**", "/fonts/**", "/scripts/**"
        };

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(staticResources)
                .permitAll()
//                .anyRequest()
//                .authenticated()
//                .antMatchers("/matches/**").hasAnyAuthority("user")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .defaultSuccessUrl("/matches/matches", true) //afterLogin goes to this page
                    .failureUrl("/index_badlogin")
                    .passwordParameter("password") //this is taken from form with name 'password'
                    .usernameParameter("username") //this is taken from form with name 'username'
//                .and()
//                .rememberMe()//Default of remember-me is 2 weeks
//                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))// customizing time to expiration
//                    .key("somethingverysecured")// Key used to hash the remember-cookie content
//                    .rememberMeParameter("remember-me")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    //This next line is only needed because CSRF is disabled. For change-state operations, we must use POST
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")) // https://docs.spring.io/spring-security/site/docs/4.2.12.RELEASE/apidocs/org/springframework/security/config/annotation/web/configurers/LogoutConfigurer.html
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .logoutUrl("/index")
                    .logoutSuccessUrl("/index");

    }

    @Override //No security webpoints
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/index_badlogin",
                "/register_page",
                "/index_GoodRegister",
                "/register",
                "/checkErrorslogin"
                );
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(loginAccessService);
        return provider;
    }
/*
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails annaSmithUser = User.builder()
                .username("annasmith")
                .password(passwordEncoder.encode("password"))
                .authorities(STUDENT.getGrantedAuthorities())
                .build();

        UserDetails lindaUser = User.builder()
                .username("linda")
                .password(passwordEncoder.encode("password123"))
                .authorities(ADMIN.getGrantedAuthorities())
                .build();

        UserDetails tomUser = User.builder()
                .username("tom")
                .password(passwordEncoder.encode("password123"))
                .authorities(ADMINTRAINEE.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(
                annaSmithUser,
                lindaUser,
                tomUser
        );

    }
*/
}
