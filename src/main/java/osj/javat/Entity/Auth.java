package osj.javat.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Auth {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	// Bearer 타입
	@Column(name="tokenType", nullable = false)
	private String tokenType;
	
	// access 요청을 위한 수명이 짧은 token
	@Column(name="accessToken", nullable = false)
	private String accessToken;
	
	// accessToken을 발급받기 위한 수명이 긴 token
	@Column(name="refreshToken", nullable = false)
	private String refreshToken;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	@Builder
	public Auth(User user, String tokenType, String accessToken, String refreshToken) {
		this.user = user;
		this.tokenType = tokenType;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
	
	public void updateAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
