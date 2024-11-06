package osj.javat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import osj.javat.Entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findById(Long id);
	Optional<User> findByLoginId(String loginId);
}
