package osj.javat.Entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import osj.javat.dto.UserRequestDto;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="user")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="email", nullable=false, unique=true)
	private String email;
	
	@Column(name="loginId", nullable=false, unique=true)
	private String loginId;
	
	@Column(name="password", nullable=false)
	private String password;
	
	@Column(name="username", nullable=false, unique=true)
	private String username;
	
	@Enumerated(EnumType.STRING)
	@Column(name="role")
	private Role role;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
	private Auth auth;
	
	@Builder
	public User(String email, String loginId, String password, String username, Role role) {
		this.loginId = loginId;
		this.email = email;
		this.password = password;
		this.username = username;
		this.role = role;
	}
	
	public void update(UserRequestDto userRequest) {
		this.loginId = userRequest.getLoginId();
		this.email = userRequest.getEmail();
		this.password = userRequest.getPassword();
		this.username = userRequest.getUsername();
		this.role = userRequest.getRole();
	}
}
