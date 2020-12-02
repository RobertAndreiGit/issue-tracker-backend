package all.repository;

import all.domain.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue,Integer>
{
    List<Issue> findAllByBoard_Id(int id);
}
