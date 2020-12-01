package all.service;

import all.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class Service
{
    private AccountRepository accountRepository;
    private BoardRepository boardRepository;
    private CategoryRepository categoryRepository;
    private IssueRepository issueRepository;
    private LabelRepository labelRepository;
    private PriorityRepository priorityRepository;
    private StageRepository stageRepository;
    private UserRepository userRepository;

    @Autowired
    public Service(AccountRepository accountRepository, BoardRepository boardRepository, CategoryRepository categoryRepository, IssueRepository issueRepository, LabelRepository labelRepository, PriorityRepository priorityRepository, StageRepository stageRepository, UserRepository userRepository)
    {
        this.accountRepository = accountRepository;
        this.boardRepository = boardRepository;
        this.categoryRepository = categoryRepository;
        this.issueRepository = issueRepository;
        this.labelRepository = labelRepository;
        this.priorityRepository = priorityRepository;
        this.stageRepository = stageRepository;
        this.userRepository = userRepository;
    }
}
