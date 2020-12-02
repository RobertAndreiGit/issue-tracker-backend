package all.repository;

import all.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer>
{
    Optional<User> findUserByAccount_Username(String username);
}
