package osj.javat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import osj.javat.dto.UserRequestDto;
import osj.javat.dto.UserResponseDto;
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
}
