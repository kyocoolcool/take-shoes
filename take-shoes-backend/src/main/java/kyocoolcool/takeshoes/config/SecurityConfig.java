package kyocoolcool.takeshoes.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * @author Chris Chen
 * @version 1.0
 * @className SecurityConfig
 * @description
 * @date 2020/5/8 4:55 PM
 **/

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ReaderRepository readerRepository;

    @Autowired
    public SecurityConfig(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/").access("hasRole('READER')")
//                .antMatchers("/**").permitAll()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .failureUrl("/login?error=true");
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests()
                .anyRequest() //對象為所有網址
                .permitAll() //存取必須通過驗證
                .and()
                .formLogin() //若未不符合authorize條件，則產生預設login表單
                .and()
                .httpBasic() //產生基本表單
                .and()
                .csrf()
                .disable();

    }

    @Bean
    public UserDetailsService userDetailsService() {
        User.UserBuilder users = User.withDefaultPasswordEncoder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(users.username("user").password("password").roles("USER").build());
        manager.createUser(users.username("admin").password("password").roles("USER", "ADMIN").build());
        return manager;

    }

//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(new UserDetailsService() {
//            @Override
//            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//                return readerRepository.findByUsername("chris").get(0);
//            }
//        });
//    }

}
