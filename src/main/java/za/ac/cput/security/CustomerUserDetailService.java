package za.ac.cput.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.User;
import za.ac.cput.repository.UserRepo.UserRepository;

@Service
public class CustomerUserDetailService implements UserDetailsService {

    private final UserRepository repository;

    public CustomerUserDetailService(UserRepository repository){
        this.repository=repository;
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = repository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        return new AppUserPrincipal(user);
    }
}
