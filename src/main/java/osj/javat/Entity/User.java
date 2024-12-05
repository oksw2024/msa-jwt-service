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
import lombok.*;
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
	
	@Setter
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
	
	public void update(UserRequestDto requestDto) {
		if (requestDto.getUsername() != null) {
			this.username = requestDto.getUsername();
		}
		if (requestDto.getEmail() != null) {
			this.email = requestDto.getEmail();
		}
		if (requestDto.getPassword() != null) {
			this.password = requestDto.getPassword();
		}
	}

}
