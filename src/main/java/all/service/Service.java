package all.service;

import all.domain.*;
import all.repository.*;
import all.request_handler.request_entities.AddIssueRequestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private BCryptPasswordEncoder passwordEncoder;

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
        passwordEncoder=new BCryptPasswordEncoder();
    }

    private boolean isUserOwnerOfBoard(String ownerUsername,int boardId)
    {
        Optional<Board> optional=boardRepository.findById(boardId);

        if(!optional.isPresent())
        {
            return false;
        }

        Board board=optional.get();

        return board.getOwner().getAccount().getUsername().equals(ownerUsername);
    }

    private boolean isUserPartOfBoard(String username,int boardId)
    {
        Optional<Board> optionalBoard=boardRepository.findById(boardId);

        if(!optionalBoard.isPresent())
        {
            return false;
        }

        Board board=optionalBoard.get();

        Optional<User> optionalUser=board.getUsers().stream().filter((user)->user.getAccount().getUsername().equals(username)).findFirst();

        return optionalUser.isPresent();
    }

    public int createBoard(String name, String ownerUsername)
    {
        Optional<User> optionalUser=userRepository.findUserByAccount_Username(ownerUsername);

        if(!optionalUser.isPresent())
        {
            return -1;
        }

        Board board=new Board();
        board.setName(name);
        board.setOwner(optionalUser.get());
        Board savedBoard=boardRepository.save(board);

        return savedBoard.getId();
    }

    public int addUserToBoard(int boardId, String username, String ownerUsername)
    {
        if(!isUserOwnerOfBoard(ownerUsername,boardId))
        {
            return -1;
        }

        Optional<User> optionalUser=userRepository.findUserByAccount_Username(username);

        if(!optionalUser.isPresent())
        {
            return -1;
        }

        Board board=boardRepository.findById(boardId).get();

        User user=optionalUser.get();
        user.getBoards().add(board);
        userRepository.save(user);

        return user.getId();
    }

    public List<Stage> getStages()
    {
        return stageRepository.findAll();
    }

    public List<User> getBoardParticipants(int boardId, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return new ArrayList<>();
        }

        return boardRepository.findById(boardId).get().getUsers();
    }

    public List<Issue> getBoardIssues(int boardId, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return new ArrayList<>();
        }

        return issueRepository.findAllByBoard_Id(boardId);
    }

    public List<Board> getBoards(String accountUsername)
    {
        Optional<User> optionalUser=userRepository.findUserByAccount_Username(accountUsername);

        if(!optionalUser.isPresent())
        {
            return new ArrayList<>();
        }

        return optionalUser.get().getBoards();
    }

    private boolean isIssuePartOfBoard(int issueId,int boardId)
    {
        Optional<Issue> optionalIssue=issueRepository.findById(issueId);

        if(!optionalIssue.isPresent())
        {
            return false;
        }

        Issue issue=optionalIssue.get();

        return issue.getBoard().getId() == boardId;
    }

    public boolean takeIssue(int boardId, int issueId, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return false;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return false;
        }

        Issue issue =issueRepository.findById(issueId).get();

        Optional<User> optionalUser=userRepository.findUserByAccount_Username(accountUsername);

        if(!optionalUser.isPresent())
        {
            return false;
        }

        issue.setUser(optionalUser.get());
        issueRepository.save(issue);

        return true;
    }

    public List<Priority> getPriorities()
    {
        return priorityRepository.findAll();
    }

    public List<Priority> getCategories()
    {
        return priorityRepository.findAll();
    }

    public boolean createIssue(AddIssueRequestEntity entity, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,entity.getBoardId()))
        {
            return false;
        }

        Optional<Stage> optionalStage=stageRepository.findById(entity.getStageId());

        if(!optionalStage.isPresent())
        {
            return false;
        }

        Optional<Category> optionalCategory=categoryRepository.findById(entity.getCategoryId());

        if(!optionalCategory.isPresent())
        {
            return false;
        }

        Optional<Priority> optionalPriority=priorityRepository.findById(entity.getPriorityId());

        if(!optionalPriority.isPresent())
        {
            return false;
        }

        Issue issue=new Issue();
        issue.setTitle(entity.getTitle());
        issue.setText(entity.getText());
        issue.setStoryPoints(entity.getStoryPoints());
        issue.setStage(optionalStage.get());
        issue.setCategory(optionalCategory.get());
        issue.setPriority(optionalPriority.get());
        issue.setUser(null);
        issue.setBoard(boardRepository.findById(entity.getBoardId()).get());

        Optional<Label> optionalLabel=labelRepository.findByNameAndColour(entity.getLabel(),entity.getLabelColour());

        if(optionalLabel.isPresent())
        {
            issue.setLabel(optionalLabel.get());
        }
        else
        {
            Label label=new Label();
            label.setName(entity.getLabel());
            label.setColour(entity.getLabelColour());

            issue.setLabel(label);
        }

        issueRepository.save(issue);

        return true;
    }

    public boolean changeIssueStage(int boardId, int issueId, int stageId, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return false;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return false;
        }

        Issue issue =issueRepository.findById(issueId).get();

        Optional<Stage> optionalStage=stageRepository.findById(stageId);

        if(!optionalStage.isPresent())
        {
            return false;
        }

        issue.setStage(optionalStage.get());
        issueRepository.save(issue);

        return true;
    }

    public boolean changeStoryPoints(int boardId, int issueId, int value, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return false;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return false;
        }

        Issue issue =issueRepository.findById(issueId).get();

        issue.setStoryPoints(value);
        issueRepository.save(issue);

        return true;
    }

    public boolean changePriority(int boardId, int issueId, int priorityId, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return false;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return false;
        }

        Issue issue =issueRepository.findById(issueId).get();

        Optional<Priority> optionalPriority=priorityRepository.findById(priorityId);

        if(!optionalPriority.isPresent())
        {
            return false;
        }

        issue.setPriority(optionalPriority.get());
        issueRepository.save(issue);

        return true;
    }

    public boolean changeIssueLabel(int boardId, int issueId, String labelText, String colour, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return false;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return false;
        }

        if(colour.length()!=7 || colour.charAt(0)!='#')
        {
            colour="#FFFFFF";
        }

        Issue issue =issueRepository.findById(issueId).get();

        Optional<Label> optionalLabel=labelRepository.findByNameAndColour(labelText,colour);

        if(optionalLabel.isPresent())
        {
            issue.setLabel(optionalLabel.get());
        }
        else
        {
            Label label=new Label();
            label.setName(labelText);
            label.setColour(colour);

            issue.setLabel(label);
        }

        issueRepository.save(issue);

        return true;
    }

    public boolean changeIssueCategory(int boardId, int issueId, int categoryId, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return false;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return false;
        }

        Issue issue =issueRepository.findById(issueId).get();

        Optional<Category> optionalCategory=categoryRepository.findById(categoryId);

        if(!optionalCategory.isPresent())
        {
            return false;
        }

        issue.setCategory(optionalCategory.get());
        issueRepository.save(issue);

        return true;
    }

    public boolean changeIssueTitle(int boardId, int issueId, String title, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return false;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return false;
        }

        Issue issue =issueRepository.findById(issueId).get();

        issue.setTitle(title);
        issueRepository.save(issue);

        return true;
    }

    public boolean changeIssueText(int boardId, int issueId, String text, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return false;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return false;
        }

        Issue issue =issueRepository.findById(issueId).get();

        issue.setText(text);
        issueRepository.save(issue);

        return true;
    }

    public boolean deleteIssue(int boardId, int issueId, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return false;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return false;
        }

        Issue issue =issueRepository.findById(issueId).get();

        issueRepository.delete(issue);

        return true;
    }

    public boolean deleteBoard(int boardId, String ownerUsername)
    {
        if(!isUserOwnerOfBoard(ownerUsername,boardId))
        {
            return false;
        }

        Optional<Board> optionalBoard=boardRepository.findById(boardId);

        if(!optionalBoard.isPresent())
        {
            return false;
        }

        Board board=optionalBoard.get();
        List<Issue> issues=issueRepository.findAllByBoard_Id(boardId);

        issues.forEach((issue)->issueRepository.delete(issue));

        for(User user:board.getUsers())
        {
            user.getBoards().removeIf((userBoard)->board.getId()==userBoard.getId());
            userRepository.save(user);
        }

        board.getUsers().clear();
        boardRepository.save(board);

        return true;
    }

    public boolean deleteUserFromBoard(int boardId, String username, String ownerUsername)
    {
        if(!isUserOwnerOfBoard(ownerUsername,boardId))
        {
            return false;
        }

        if(!isUserPartOfBoard(username,boardId))
        {
            return false;
        }

        Optional<User> optionalUser=userRepository.findUserByAccount_Username(username);

        if(!optionalUser.isPresent())
        {
            return false;
        }

        removeUserFromBoardIssues(optionalUser.get(),boardId);
        removeUserFromMtmBoard(optionalUser.get(),boardId);

        return true;
    }

    private void removeUserFromBoardIssues(User user,int boardId)
    {
        List<Issue>issues=issueRepository.findAllByBoard_Id(boardId);
        List<Issue> foundIssues=issues.stream().filter((issue)->issue.getUser().getId()==user.getId()).collect(Collectors.toList());

        foundIssues.forEach((issue)->{issue.setUser(null); issueRepository.save(issue);});
    }

    private void removeUserFromMtmBoard(User user,int boardId)
    {
        Board board=boardRepository.findById(boardId).get();

        board.getUsers().removeIf((boardUser)->boardUser.getId()==user.getId());
        user.getBoards().removeIf((userBoard)->userBoard.getId()==boardId);
    }

    public boolean leaveBoard(int boardId, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return false;
        }

        Optional<User> optionalUser=userRepository.findUserByAccount_Username(accountUsername);

        if(!optionalUser.isPresent())
        {
            return false;
        }

        removeUserFromBoardIssues(optionalUser.get(),boardId);
        removeUserFromMtmBoard(optionalUser.get(),boardId);

        return true;
    }

    public boolean register(String username, String password, String name)
    {
        if(name.equals(""))
        {
            return false;
        }

        if(username.equals(""))
        {
            return false;
        }

        if(password.equals(""))
        {
            return false;
        }

        Optional<Account> optionalAccount=accountRepository.findByUsername(username);

        if(optionalAccount.isPresent())
        {
            return false;
        }

        Account account=new Account();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));

        User user=new User();
        user.setName(name);
        user.setAccount(account);

        userRepository.save(user);

        return true;
    }
}
