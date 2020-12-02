package all.service;

import all.domain.*;
import all.repository.*;
import all.request_handler.request_entities.AddIssueRequestEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

    public int createBoard(String name, String ownerUsername)
    {
        return -1;
    }

    public boolean addUserToBoard(int idBoard, String username, String ownerUsername)
    {
        return false;
    }

    public List<Stage> getStages()
    {
        return null;
    }

    public List<User> getBoardParticipants(int boardId, String accountUsername)
    {
        return null;
    }

    public List<Issue> getBoardIssues(int boardId, String accountUsername)
    {
        return null;
    }

    public List<Board> getBoards(String accountUsername)
    {
        return null;
    }

    public boolean takeIssue(int boardId, int issueId, String accountUsername)
    {
        return false;
    }

    public boolean changeIssueStage(int boardId, int issueId, int stageId, String accountUsername)
    {
        return false;
    }

    public List<Priority> getPriorities()
    {
        return null;
    }

    public List<Priority> getCategories()
    {
        return null;
    }

    public boolean createIssue(AddIssueRequestEntity entity, String accountUsername)
    {
        return false;
    }

    public boolean changeStoryPoints(int boardId, int issueId, int value, String accountUsername)
    {
        return false;
    }

    public boolean changePriority(int boardId, int issueId, int priorityId, String accountUsername)
    {
        return false;
    }

    public boolean deleteIssue(int boardId, int issueId, String accountUsername)
    {
        return false;
    }

    public boolean deleteBoard(int boardId, String accountUsername)
    {
        return false;
    }

    public boolean deleteUserFromBoard(int boardId, String username, String ownerUsername)
    {
        return false;
    }

    public boolean leaveBoard(int boardId, String username, String accountUsername)
    {
        return false;
    }

    public boolean changeIssueLabel(int boardId, int issueId, String label, String accountUsername)
    {
        return false;
    }

    public boolean changeIssueCategory(int boardId, int issueId, int categoryId, String accountUsername)
    {
        return false;
    }

    public boolean changeIssueTitle(int boardId, int issueId, String title, String accountUsername)
    {
        return false;
    }

    public boolean changeIssueText(int boardId, int issueId, String text, String accountUsername)
    {
        return false;
    }
}
