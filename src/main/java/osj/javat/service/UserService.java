package osj.javat.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import osj.javat.Entity.User;
import osj.javat.dto.UserRequestDto;
import osj.javat.dto.UserResponseDto;
import osj.javat.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	// 사용자 조회
	@Transactional
	public UserResponseDto findById(Long id) {
		User user = this.userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. user_id = " + id));
		return new UserResponseDto(user);
	}
	
	// 사용자 수정
	@Transactional
    public void update(Long id, UserRequestDto requestDto) {
        User user = this.userRepository.findById(id)
        		.orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. user_id = " + id));
        
        if (requestDto.getPassword() != null) {
        	requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        }
        user.update(requestDto);
    }
	
	// 사용자 삭제
	@Transactional
	public void delete(Long id) {
		User user = this.userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다. user_id = " + id));
		this.userRepository.delete(user);
	}
}
