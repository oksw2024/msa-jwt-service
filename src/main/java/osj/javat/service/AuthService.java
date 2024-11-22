package osj.javat.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import osj.javat.Entity.Auth;
import osj.javat.Entity.Role;
import osj.javat.Entity.User;
import osj.javat.detail.CustomUserDetails;
import osj.javat.dto.AuthRequestDto;
import osj.javat.dto.AuthResponseDto;
import osj.javat.dto.UserRequestDto;
import osj.javat.repository.AuthRepository;
import osj.javat.repository.UserRepository;
import osj.javat.security.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final AuthRepository authRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	
	// 로그인
	@Transactional
	public AuthResponseDto login(AuthRequestDto requestDto) {
		User user = this.userRepository.findByLoginId(requestDto.getLoginId())
				.orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다. id = " + requestDto.getLoginId()));
		if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다. id = " + requestDto.getLoginId());
		}
		
		String accessToken = this.jwtTokenProvider.generateAccessToken(
				new UsernamePasswordAuthenticationToken(new CustomUserDetails(user), user.getPassword()));
		String refreshToken = this.jwtTokenProvider.generateRefreshToken(
				new UsernamePasswordAuthenticationToken(new CustomUserDetails(user), user.getPassword()));
		
		if (this.authRepository.existsByUser(user)) {
			user.getAuth().updateAccessToken(accessToken);
			user.getAuth().updateRefreshToken(refreshToken);
			return new AuthResponseDto(user.getAuth());
		}
		
		Auth auth = this.authRepository.save(Auth.builder()
				.user(user)
				.tokenType("Bearer")
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build());
		return new AuthResponseDto(auth);
	}
	
	// 회원가입
	@Transactional
	public void signup(UserRequestDto requestDto) {
		// create 기능
		requestDto.setRole(Role.ROLE_USER);
		requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
		this.userRepository.save(requestDto.toEntity());
	}
	
	// 토큰 갱신, validateToken 수정 필요
	@Transactional
	public String refreshToken(String refreshToken) {
		Auth auth = this.authRepository.findByRefreshToken(refreshToken)
				.orElseThrow(() -> new IllegalArgumentException("해당 refresh_token을 찾을 수 없습니다\nrefresh_token = " + refreshToken));
		String newAccessToken = this.jwtTokenProvider.generateAccessToken(
				new UsernamePasswordAuthenticationToken(
						new CustomUserDetails(auth.getUser()), auth.getUser().getPassword()));
		auth.updateAccessToken(newAccessToken);
		return newAccessToken;
	}
}
