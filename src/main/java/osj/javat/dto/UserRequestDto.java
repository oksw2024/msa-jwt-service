package osj.javat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import osj.javat.Entity.Role;
import osj.javat.Entity.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
	private String loginId;
	private String email;
	private String password;
	private String username;
	private Role role;
	
	public User toEntity() {
		return User.builder()
				.role(this.role)
				.email(this.email)
				.password(this.password)
				.username(this.username)
				.loginId(this.loginId)
				.build();
	}
}
