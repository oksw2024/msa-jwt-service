package osj.javat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import osj.javat.Entity.Auth;
import osj.javat.Entity.User;

public interface AuthRepository extends JpaRepository<Auth, Long>{
	boolean existsByUser(User user);
	Optional<Auth> findByRefreshToken(String refreshToken);
}
