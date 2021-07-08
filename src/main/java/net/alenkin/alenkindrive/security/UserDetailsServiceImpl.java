package net.alenkin.alenkindrive.security;

import net.alenkin.alenkindrive.model.User;
import net.alenkin.alenkindrive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    private static Long authUserId = 0L;
    @Autowired
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = repository.findByName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("Unknown user: "+userName);
        }
        authUserId = user.getId();
        boolean blocked = user.getStatus().name().equals("BANNED");
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getName())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .accountExpired(blocked)
                .disabled(blocked)
                .credentialsExpired(blocked)
                .accountLocked(blocked)
                .build();
    }

    public static Long getAuthUserId() {
        return authUserId;
    }
}
