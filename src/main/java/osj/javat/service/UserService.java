package osj.javat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import osj.javat.Entity.User;
import osj.javat.dto.PasswordChangeRequest;
import osj.javat.dto.UserRequestDto;
import osj.javat.dto.UserResponseDto;
import osj.javat.exception.InvalidPasswordException;
import osj.javat.repository.UserRepository;
import osj.javat.security.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	
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

	@Transactional
	public void changePassword(String accessToken, PasswordChangeRequest request) throws InvalidPasswordException {
		Long id = jwtTokenProvider.getUserIdFromToken(accessToken); // JWT에서 사용자 추출
		User user = userRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		// 현재 비밀번호 검증
		if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
			throw new InvalidPasswordException("현재 비밀번호가 올바르지 않습니다.");
		}

		// 새 비밀번호 강도 확인
		if (!isPasswordStrong(request.getNewPassword())) {
			throw new InvalidPasswordException("새 비밀번호는 최소 8자 이상이어야 하며, 숫자와 특수문자를 포함해야 합니다.");
		}

		// 새 비밀번호 저장
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);
	}

	private boolean isPasswordStrong(String password) {
		boolean hasLength = password.length() >= 8;
		boolean hasSpecialChar = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
		boolean hasNumber = password.matches(".*\\d.*");
		return hasLength && hasSpecialChar && hasNumber;
	}
}
