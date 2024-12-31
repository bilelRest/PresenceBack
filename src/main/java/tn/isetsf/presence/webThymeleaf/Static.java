package tn.isetsf.presence.webThymeleaf;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collection;

@Component
@Transactional
public class Static {
    //public static final Collection<? extends GrantedAuthority> CURRENT_ROLE = ;
    static String CURRENT_USER;
}
