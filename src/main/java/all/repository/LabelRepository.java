package all.repository;

import all.domain.Label;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LabelRepository extends JpaRepository<Label,Integer>
{
    Optional<Label> findByNameAndColour(String name,String colour);
}
