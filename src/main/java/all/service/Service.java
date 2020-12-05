package all.service;

import all.domain.*;
import all.repository.*;
import all.request_handler.request_entities.AddIssueRequestEntity;
import all.request_handler.response_entities.utils.Messages;
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

    public String addUserToBoard(int boardId, String username, String ownerUsername)
    {
        if(!isUserOwnerOfBoard(ownerUsername,boardId))
        {
            return Messages.USER_NOT_THE_BOARD_OWNER_;
        }

        Optional<User> optionalUser=userRepository.findUserByAccount_Username(username);

        if(!optionalUser.isPresent())
        {
            return Messages.USER_ADD_DOES_NOT_EXIST;
        }

        Board board=boardRepository.findById(boardId).get();

        User user=optionalUser.get();
        user.getBoards().add(board);
        userRepository.save(user);

        return Integer.toString(user.getId());
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

    public String takeIssue(int boardId, int issueId, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return Messages.USER_NOT_BOARD_MEMBER;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return Messages.ISSUE_NOT_PART_OF_BOARD;
        }

        Issue issue =issueRepository.findById(issueId).get();

        Optional<User> optionalUser=userRepository.findUserByAccount_Username(accountUsername);

        if(!optionalUser.isPresent())
        {
            return Messages.USER_DOES_NOT_EXIST;
        }

        issue.setUser(optionalUser.get());
        issueRepository.save(issue);

        return Messages.SUCCESS;
    }

    public List<Priority> getPriorities()
    {
        return priorityRepository.findAll();
    }

    public List<Priority> getCategories()
    {
        return priorityRepository.findAll();
    }

    public String createIssue(AddIssueRequestEntity entity, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,entity.getBoardId()))
        {
            return Messages.USER_NOT_BOARD_MEMBER;
        }

        Optional<Stage> optionalStage=stageRepository.findById(entity.getStageId());

        if(!optionalStage.isPresent())
        {
            return Messages.STAGE_DOES_NOT_EXIST;
        }

        Optional<Category> optionalCategory=categoryRepository.findById(entity.getCategoryId());

        if(!optionalCategory.isPresent())
        {
            return Messages.CATEGORY_DOES_NOT_EXIST;
        }

        Optional<Priority> optionalPriority=priorityRepository.findById(entity.getPriorityId());

        if(!optionalPriority.isPresent())
        {
            return Messages.PRIORITY_DOES_NOT_EXIST;
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

        return Messages.SUCCESS;
    }

    public String changeIssueStage(int boardId, int issueId, int stageId, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return Messages.USER_NOT_BOARD_MEMBER;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return Messages.ISSUE_NOT_PART_OF_BOARD;
        }

        Issue issue =issueRepository.findById(issueId).get();

        Optional<Stage> optionalStage=stageRepository.findById(stageId);

        if(!optionalStage.isPresent())
        {
            return Messages.STAGE_DOES_NOT_EXIST;
        }

        issue.setStage(optionalStage.get());
        issueRepository.save(issue);

        return Messages.SUCCESS;
    }

    public String changeStoryPoints(int boardId, int issueId, int value, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return Messages.USER_NOT_BOARD_MEMBER;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return Messages.ISSUE_NOT_PART_OF_BOARD;
        }

        Issue issue =issueRepository.findById(issueId).get();

        issue.setStoryPoints(value);
        issueRepository.save(issue);

        return Messages.SUCCESS;
    }

    public String changePriority(int boardId, int issueId, int priorityId, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return Messages.USER_NOT_BOARD_MEMBER;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return Messages.ISSUE_NOT_PART_OF_BOARD;
        }

        Issue issue =issueRepository.findById(issueId).get();

        Optional<Priority> optionalPriority=priorityRepository.findById(priorityId);

        if(!optionalPriority.isPresent())
        {
            return Messages.PRIORITY_DOES_NOT_EXIST;
        }

        issue.setPriority(optionalPriority.get());
        issueRepository.save(issue);

        return Messages.SUCCESS;
    }

    public String changeIssueLabel(int boardId, int issueId, String labelText, String colour, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return Messages.USER_NOT_BOARD_MEMBER;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return Messages.ISSUE_NOT_PART_OF_BOARD;
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

        return Messages.SUCCESS;
    }

    public String changeIssueCategory(int boardId, int issueId, int categoryId, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return Messages.USER_NOT_BOARD_MEMBER;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return Messages.ISSUE_NOT_PART_OF_BOARD;
        }

        Issue issue =issueRepository.findById(issueId).get();

        Optional<Category> optionalCategory=categoryRepository.findById(categoryId);

        if(!optionalCategory.isPresent())
        {
            return Messages.CATEGORY_DOES_NOT_EXIST;
        }

        issue.setCategory(optionalCategory.get());
        issueRepository.save(issue);

        return Messages.SUCCESS;
    }

    public String changeIssueTitle(int boardId, int issueId, String title, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return Messages.USER_NOT_BOARD_MEMBER;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return Messages.ISSUE_NOT_PART_OF_BOARD;
        }

        Issue issue =issueRepository.findById(issueId).get();

        issue.setTitle(title);
        issueRepository.save(issue);

        return Messages.SUCCESS;
    }

    public String changeIssueText(int boardId, int issueId, String text, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return Messages.USER_NOT_BOARD_MEMBER;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return Messages.ISSUE_NOT_PART_OF_BOARD;
        }

        Issue issue =issueRepository.findById(issueId).get();

        issue.setText(text);
        issueRepository.save(issue);

        return Messages.SUCCESS;
    }

    public String deleteIssue(int boardId, int issueId, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return Messages.USER_NOT_BOARD_MEMBER;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return Messages.ISSUE_NOT_PART_OF_BOARD;
        }

        Issue issue =issueRepository.findById(issueId).get();

        issueRepository.delete(issue);

        return Messages.SUCCESS;
    }

    public String deleteBoard(int boardId, String ownerUsername)
    {
        if(!isUserOwnerOfBoard(ownerUsername,boardId))
        {
            return Messages.USER_NOT_THE_BOARD_OWNER_;
        }

        Optional<Board> optionalBoard=boardRepository.findById(boardId);

        if(!optionalBoard.isPresent())
        {
            return Messages.BOARD_DOES_NOT_EXIST;
        }

        Board board=optionalBoard.get();
        List<Issue> issues=issueRepository.findAllByBoard_Id(boardId);

        issues.forEach((issue)->issueRepository.delete(issue));

        for(User user:board.getUsers())
        {
            user.getBoards().removeIf((userBoard)->board.getId()==userBoard.getId());
            userRepository.save(user);
        }

        return Messages.SUCCESS;
    }

    public String deleteUserFromBoard(int boardId, String username, String ownerUsername)
    {
        if(!isUserOwnerOfBoard(ownerUsername,boardId))
        {
            return Messages.USER_NOT_THE_BOARD_OWNER_;
        }

        if(!isUserPartOfBoard(username,boardId))
        {
            return Messages.USER_NOT_BOARD_MEMBER;
        }

        Optional<User> optionalUser=userRepository.findUserByAccount_Username(username);

        if(!optionalUser.isPresent())
        {
            return Messages.USER_DOES_NOT_EXIST;
        }

        removeUserFromBoardIssues(optionalUser.get(),boardId);
        removeUserFromMtmBoard(optionalUser.get(),boardId);

        return Messages.SUCCESS;
    }

    private void removeUserFromBoardIssues(User user,int boardId)
    {
        List<Issue>issues=issueRepository.findAllByBoard_Id(boardId);
        List<Issue> foundIssues=issues.stream().filter((issue)->issue.getUser().getId()==user.getId()).collect(Collectors.toList());

        foundIssues.forEach((issue)->{issue.setUser(null); issueRepository.save(issue);});
    }

    private void removeUserFromMtmBoard(User user,int boardId)
    {
        user.getBoards().removeIf((userBoard)->userBoard.getId()==boardId);
    }

    public String leaveBoard(int boardId, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return Messages.USER_NOT_BOARD_MEMBER;
        }

        Optional<User> optionalUser=userRepository.findUserByAccount_Username(accountUsername);

        if(!optionalUser.isPresent())
        {
            return Messages.USER_DOES_NOT_EXIST;
        }

        removeUserFromBoardIssues(optionalUser.get(),boardId);
        removeUserFromMtmBoard(optionalUser.get(),boardId);

        return Messages.SUCCESS;
    }

    public String register(String username, String password, String name)
    {
        if(username.equals(""))
        {
            return Messages.USERNAME_EMPTY;
        }

        if(password.equals(""))
        {
            return Messages.PASSWORD_EMPTY;
        }

        if(name.equals(""))
        {
            return Messages.NAME_EMPTY;
        }

        Optional<Account> optionalAccount=accountRepository.findByUsername(username);

        if(optionalAccount.isPresent())
        {
            return Messages.USERNAME_EXISTS;
        }

        Account account=new Account();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));

        User user=new User();
        user.setName(name);
        user.setAccount(account);

        userRepository.save(user);

        return Messages.SUCCESS;
    }

    public String changeName(String name,String accountUsername)
    {
        Optional<User> optionalUser=userRepository.findUserByAccount_Username(accountUsername);

        if(!optionalUser.isPresent())
        {
            return Messages.USER_DOES_NOT_EXIST;
        }

        if(name.equals(""))
        {
            return Messages.NAME_EMPTY;
        }

        User user=optionalUser.get();

        user.setName(name);
        userRepository.save(user);

        return Messages.SUCCESS;
    }
}
