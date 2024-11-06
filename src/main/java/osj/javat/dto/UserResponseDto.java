package osj.javat.dto;

import lombok.Getter;
import lombok.Setter;
import osj.javat.Entity.User;

@Getter
@Setter
public class UserResponseDto {
	private Long id;
	private String role;
	private String email;
	private String loginId;
	private String username;
	
	public UserResponseDto(User entity) {
		this.id = entity.getId();
		this.email = entity.getEmail();
		this.loginId = entity.getLoginId();
		this.username = entity.getUsername();
		// Enum Type -> String Type
		this.role = entity.getRole().name(); 
	}
}
