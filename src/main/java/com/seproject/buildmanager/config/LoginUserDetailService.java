package com.seproject.buildmanager.config;

import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.seproject.buildmanager.entity.MstUser;
import com.seproject.buildmanager.repository.MstUserRepository;

@Service
public class LoginUserDetailService implements UserDetailsService {
  private final MstUserRepository userRepository;

  public LoginUserDetailService(MstUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String loginCd) throws UsernameNotFoundException {
    Optional<MstUser> _user = userRepository.findByLoginCd(loginCd);
    return _user.map(user -> new LoginUserDetails(user))
        .orElseThrow(() -> new UsernameNotFoundException("not found loginCd=" + loginCd));
  }
}
