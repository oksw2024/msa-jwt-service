package osj.javat.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import osj.javat.Entity.User;
import osj.javat.detail.CustomUserDetails;
import osj.javat.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
	private final UserRepository userRepository;
	
	@Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(loginId)
        		.orElseThrow(() -> new UsernameNotFoundException("해당 유저가 존재하지 않습니다. id = " + loginId));
        return new CustomUserDetails(user);
	}
	
	public UserDetails loadUserByUserId(Long userId) throws IllegalArgumentException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. user_id = " + userId));
		return new CustomUserDetails(user);
	}
}
