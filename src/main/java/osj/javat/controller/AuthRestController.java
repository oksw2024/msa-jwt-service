package osj.javat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import osj.javat.dto.ApiResponse;
import osj.javat.dto.AuthRequestDto;
import osj.javat.dto.AuthResponseDto;
import osj.javat.dto.UserRequestDto;
import osj.javat.service.AuthService;
import osj.javat.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthRestController {
	private final AuthService authService;
	private final UserService userService;

	// 로그인
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequestDto requestDto) {
		AuthResponseDto responseDto = this.authService.login(requestDto);
		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}
	
	// 회원가입
	@PostMapping("/signup")
    public ResponseEntity<?> singUp(@RequestBody UserRequestDto requestDto) {
		if (authService.isLoginIdExists(requestDto.getLoginId())) {
			// 중복된 아이디에 대한 BAD_REQUEST 응답
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ApiResponse(false, "이미 사용 중인 아이디입니다."));
		}

		this.authService.signup(requestDto);
	    return ResponseEntity.status(HttpStatus.OK).body(null);
    }
	
	// 토큰 갱신
	@GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("REFRESH_TOKEN") String refreshToken) {
        String newAccessToken = this.authService.refreshToken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(newAccessToken);
    }
}
