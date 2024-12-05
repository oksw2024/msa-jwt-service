package osj.javat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import osj.javat.dto.ApiResponse;
import osj.javat.dto.PasswordChangeRequest;
import osj.javat.dto.UserRequestDto;
import osj.javat.dto.UserResponseDto;
import osj.javat.exception.InvalidPasswordException;
import osj.javat.security.JwtTokenProvider;
import osj.javat.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserRestController {
	private final UserService userService;
	private final JwtTokenProvider jwtTokenProvider;
	
	// 회원정보 조회
	@GetMapping("/api/v1/user")
	public ResponseEntity<?> findUser(@RequestHeader("Authorization") String accessToken) {
		Long id = this.jwtTokenProvider.getUserIdFromToken(accessToken.substring(7));
		UserResponseDto userResponseDto = this.userService.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
	}
	
	// 회원정보 수정
	@PutMapping("/api/v1/user")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String accessToken, 
                                        @RequestBody UserRequestDto requestDto) {
        Long id = this.jwtTokenProvider.getUserIdFromToken(accessToken.substring(7));      
        this.userService.update(id, requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
	
	// 회원정보 삭제
	@DeleteMapping("/api/v1/user")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String accessToken) {
        Long id = this.jwtTokenProvider.getUserIdFromToken(accessToken.substring(7));
        this.userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

	// 비밀번호 검증
	@PostMapping("/api/v1/user/change-password")
	public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String token,
											@RequestBody PasswordChangeRequest request) {
		try {
			String accessToken = token.replace("Bearer ", ""); // Access token 추출
			userService.changePassword(accessToken, request);
			return ResponseEntity.ok(new ApiResponse(true, "비밀번호가 성공적으로 변경되었습니다."));
		} catch (InvalidPasswordException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(false, "비밀번호 변경 오류"));
		}
	}
}
