package tn.isetsf.presence.sec.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tn.isetsf.presence.sec.entity.AppUser;
import tn.isetsf.presence.sec.service.AppUserInterfaceImpl;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private AppUserInterfaceImpl appUserInterface;

    public SecurityConfig(AppUserInterfaceImpl appUserInterface) {
        this.appUserInterface = appUserInterface;

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
auth.userDetailsService(new UserDetailsService() {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser=appUserInterface.LoadUserByUserName(username);
        Collection<GrantedAuthority> authorities=new ArrayList<>();
        appUser.getRoleCollection().forEach(appRole -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+appRole.getRoleName()));
        });
        return new User(appUser.getUsername(),appUser.getPassword(), authorities);
    }
});
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().loginPage("/login").defaultSuccessUrl("/default", true);
        http
                .authorizeRequests()
                .antMatchers("/login","/emploi/creneau", "https://www.apirest.tech/downloads/**","/CheckPass","/ForgotPassword","/forgot","/reset-password","/default", "/webjars/**", "/smiley.png","/logo.png","/deconnect", "/error").permitAll() // Accès libre
                .antMatchers("/EspaceEnseignant","/AddUserPhoto","/CheckCred","/CheckPoint","/AddUserImg","/AbsenceEtudiant","/saveUser","/EditUser").hasRole("ENSEIGNANT") // ENSEIGNANT pour une page spécifique
                .antMatchers("/**").hasRole("ADMIN") // ADMIN peut accéder à tout

                .anyRequest().authenticated() // Autres requêtes nécessitant authentification
                .and()
                .sessionManagement().maximumSessions(1);
    }


}
