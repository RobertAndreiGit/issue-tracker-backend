package all.service;

import all.domain.*;
import all.repository.*;
import all.request_handler.request_entities.IssueRequestEntity;
import all.request_handler.response_entities.utils.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;
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

    public User getUser(String accountUsername)
    {
        Optional<User> user=userRepository.findUserByAccount_Username(accountUsername);
        return user.orElse(null);
    }

    public int createBoard(String name, String ownerUsername)
    {
        Optional<User> optionalUser=userRepository.findUserByAccount_Username(ownerUsername);

        if(!optionalUser.isPresent())
        {
            return -1;
        }

        User user=optionalUser.get();

        Board foundBoard=user.getBoards().stream().filter((board)->board.getName().equals(name)).findAny().orElse(null);

        if(foundBoard!=null)
        {
            return -2;
        }

        Board board=new Board();
        board.setName(name);
        board.setOwner(user);
        Board savedBoard=boardRepository.save(board);

        user.getBoards().add(board);
        userRepository.save(user);

        return savedBoard.getId();
    }

    public String addUserToBoard(int boardId, String username, String ownerUsername)
    {
        if(!isUserOwnerOfBoard(ownerUsername,boardId))
        {
            return Messages.USER_NOT_THE_BOARD_OWNER;
        }

        Optional<User> optionalUser=userRepository.findUserByAccount_Username(username);

        if(!optionalUser.isPresent())
        {
            return Messages.USER_ADD_DOES_NOT_EXIST;
        }

        Board board=boardRepository.findById(boardId).get();

        Optional<User> optionalExistentUser=board.getUsers().stream().filter((user)->user.getAccount().getUsername().equals(username)).findFirst();

        if(optionalExistentUser.isPresent())
        {
            return Messages.USER_ALREADY_MEMBER_OF_BOARD;
        }

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

    public String createIssue(IssueRequestEntity entity, String accountUsername)
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

        if(entity.getLabelColour().length()!=7 || entity.getLabelColour().charAt(0)!='#')
        {
            entity.setLabelColour("#FFFFFF");
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


    public String changeIssue(IssueRequestEntity entity, String accountUsername)
    {
        String messageTitle=Messages.SUCCESS;
        String messageText=Messages.SUCCESS;
        String messageCategory=Messages.SUCCESS;
        String messageStage=Messages.SUCCESS;
        String messageLabel=Messages.SUCCESS;
        String messageStoryPoints=Messages.SUCCESS;

        boolean changeTitle=false;
        boolean changeText=false;
        boolean changeCategory=false;
        boolean changeStage=false;
        boolean changeLabel=false;
        boolean changeStrotyPoints=false;

        if(entity.getTitle()!=null)
        {
            messageTitle=checkTitle(entity.getBoardId(), entity.getId(), accountUsername);
            changeTitle=true;
        }

        if(entity.getText()!=null)
        {
            messageText=checkText(entity.getBoardId(), entity.getId(), accountUsername);
            changeText=true;
        }

        if(entity.getCategoryId()!=0)
        {
            messageCategory=checkCategory(entity.getBoardId(), entity.getId(), entity.getCategoryId(), accountUsername);
            changeCategory=true;
        }

        if(entity.getStageId()!=0)
        {
            messageStage=checkStage(entity.getBoardId(), entity.getId(), entity.getStageId(), accountUsername);
            changeStage=true;
        }

        if(entity.getLabelColour()!=null && entity.getLabelColour()!=null)
        {
            messageLabel=checkLabel(entity.getBoardId(), entity.getId(), accountUsername);
            changeLabel=true;
        }

        if(entity.getStoryPoints()!=0)
        {
            messageStoryPoints=checkStoryPoints(entity.getBoardId(), entity.getId(), accountUsername);
            changeStrotyPoints=true;
        }

        String message=getResultMessage(messageTitle,messageText,messageCategory,messageStage,messageLabel,messageStoryPoints);


        if(!message.equals(Messages.SUCCESS))
        {
            return message;
        }

        if(changeTitle)
        {
            changeIssueTitle(entity.getBoardId(), entity.getId(), entity.getTitle(), accountUsername);
        }

        if(changeText)
        {
            changeIssueText(entity.getBoardId(), entity.getId(), entity.getText(), accountUsername);
        }

        if(changeCategory)
        {
            changeIssueCategory(entity.getBoardId(), entity.getId(), entity.getCategoryId(), accountUsername);
        }

        if(changeStage)
        {
            changeIssueStage(entity.getBoardId(), entity.getId(), entity.getStageId(), accountUsername);
        }

        if(changeLabel)
        {
            changeIssueLabel(entity.getBoardId(), entity.getId(), entity.getLabel(), entity.getLabelColour(), accountUsername);
        }

        if(changeStrotyPoints)
        {
            changeStoryPoints(entity.getBoardId(), entity.getId(), entity.getStoryPoints(), accountUsername);
        }

        return message;
    }

    private String getResultMessage(String messageTitle,String messageText,String messageCategory,String messageStage,String messageLabel,String messageStoryPoints)
    {
        Set<String> errorMessages=new HashSet<>();

        if(!messageTitle.equals(Messages.SUCCESS))
        {
            errorMessages.add(messageTitle);
        }

        if(!messageText.equals(Messages.SUCCESS))
        {
            errorMessages.add(messageText);
        }

        if(!messageCategory.equals(Messages.SUCCESS))
        {
            errorMessages.add(messageCategory);
        }

        if(!messageStage.equals(Messages.SUCCESS))
        {
            errorMessages.add(messageStage);
        }

        if(!messageLabel.equals(Messages.SUCCESS))
        {
            errorMessages.add(messageLabel);
        }

        if(!messageStoryPoints.equals(Messages.SUCCESS))
        {
            errorMessages.add(messageLabel);
        }

        if(errorMessages.size()==0)
        {
            return Messages.SUCCESS;
        }

        return errorMessages.stream().reduce("",(partialString,element)->partialString+element);
    }

    private String checkStage(int boardId, int issueId, int stageId, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return Messages.USER_NOT_BOARD_MEMBER;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return Messages.ISSUE_NOT_PART_OF_BOARD;
        }

        Optional<Stage> optionalStage=stageRepository.findById(stageId);

        if(!optionalStage.isPresent())
        {
            return Messages.STAGE_DOES_NOT_EXIST;
        }

        return Messages.SUCCESS;
    }

    public String changeIssueStage(int boardId, int issueId, int stageId, String accountUsername)
    {
        String message=checkStage(boardId,issueId,stageId,accountUsername);

        if(!message.equals(Messages.SUCCESS))
        {
            return message;
        }

        Issue issue=issueRepository.findById(issueId).get();
        Optional<Stage> optionalStage=stageRepository.findById(stageId);

        issue.setStage(optionalStage.get());
        issueRepository.save(issue);

        return message;
    }

    private String checkStoryPoints(int boardId, int issueId, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return Messages.USER_NOT_BOARD_MEMBER;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return Messages.ISSUE_NOT_PART_OF_BOARD;
        }

        return Messages.SUCCESS;
    }
    public String changeStoryPoints(int boardId, int issueId, int value, String accountUsername)
    {
        String message=checkStoryPoints(boardId,issueId,accountUsername);

        if(!message.equals(Messages.SUCCESS))
        {
            return message;
        }

        Issue issue =issueRepository.findById(issueId).get();

        issue.setStoryPoints(value);
        issueRepository.save(issue);

        return message;
    }

    private String checkPriority(int boardId, int issueId, int priorityId, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return Messages.USER_NOT_BOARD_MEMBER;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return Messages.ISSUE_NOT_PART_OF_BOARD;
        }

        Optional<Priority> optionalPriority=priorityRepository.findById(priorityId);

        if(!optionalPriority.isPresent())
        {
            return Messages.PRIORITY_DOES_NOT_EXIST;
        }

        return Messages.SUCCESS;
    }

    public String changePriority(int boardId, int issueId, int priorityId, String accountUsername)
    {
        String message=checkPriority(boardId,issueId,priorityId,accountUsername);

        if(!message.equals(Messages.SUCCESS))
        {
            return message;
        }

        Issue issue =issueRepository.findById(issueId).get();

        Optional<Priority> optionalPriority=priorityRepository.findById(priorityId);

        issue.setPriority(optionalPriority.get());
        issueRepository.save(issue);

        return message;
    }

    private String checkLabel(int boardId, int issueId, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return Messages.USER_NOT_BOARD_MEMBER;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return Messages.ISSUE_NOT_PART_OF_BOARD;
        }

        return Messages.SUCCESS;
    }

    public String changeIssueLabel(int boardId, int issueId, String labelText, String colour, String accountUsername)
    {
        String message=checkLabel(boardId,issueId,accountUsername);

        if(!message.equals(Messages.SUCCESS))
        {
            return message;
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

        return message;
    }

    private String checkCategory(int boardId, int issueId, int categoryId, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return Messages.USER_NOT_BOARD_MEMBER;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return Messages.ISSUE_NOT_PART_OF_BOARD;
        }

        Optional<Category> optionalCategory=categoryRepository.findById(categoryId);

        if(!optionalCategory.isPresent())
        {
            return Messages.CATEGORY_DOES_NOT_EXIST;
        }

        return Messages.SUCCESS;
    }

    public String changeIssueCategory(int boardId, int issueId, int categoryId, String accountUsername)
    {
        String message=checkCategory(boardId,issueId,categoryId,accountUsername);

        if(!message.equals(Messages.SUCCESS))
        {
            return message;
        }

        Issue issue =issueRepository.findById(issueId).get();

        Optional<Category> optionalCategory=categoryRepository.findById(categoryId);

        issue.setCategory(optionalCategory.get());
        issueRepository.save(issue);

        return message;
    }

    private String checkTitle(int boardId, int issueId, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return Messages.USER_NOT_BOARD_MEMBER;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return Messages.ISSUE_NOT_PART_OF_BOARD;
        }

        return Messages.SUCCESS;
    }

    public String changeIssueTitle(int boardId, int issueId, String title, String accountUsername)
    {
        String message=checkTitle(boardId,issueId,accountUsername);

        if(!message.equals(Messages.SUCCESS))
        {
            return message;
        }

        Issue issue =issueRepository.findById(issueId).get();

        issue.setTitle(title);
        issueRepository.save(issue);

        return message;
    }

    private String checkText(int boardId, int issueId, String accountUsername)
    {
        if(!isUserPartOfBoard(accountUsername,boardId))
        {
            return Messages.USER_NOT_BOARD_MEMBER;
        }

        if(!isIssuePartOfBoard(issueId,boardId))
        {
            return Messages.ISSUE_NOT_PART_OF_BOARD;
        }

        return Messages.SUCCESS;
    }

    public String changeIssueText(int boardId, int issueId, String text, String accountUsername)
    {
        String message=checkText(boardId,issueId,accountUsername);

        if(!message.equals(Messages.SUCCESS))
        {
            return message;
        }

        Issue issue =issueRepository.findById(issueId).get();

        issue.setText(text);
        issueRepository.save(issue);

        return message;
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
            return Messages.USER_NOT_THE_BOARD_OWNER;
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

        boardRepository.delete(board);

        return Messages.SUCCESS;
    }

    public String deleteUserFromBoard(int boardId, String username, String ownerUsername)
    {
        if(!isUserOwnerOfBoard(ownerUsername,boardId))
        {
            return Messages.USER_NOT_THE_BOARD_OWNER;
        }

        if(!isUserPartOfBoard(username,boardId))
        {
            return Messages.USER_NOT_BOARD_MEMBER;
        }

        if(isUserOwnerOfBoard(username,boardId))
        {
            return Messages.CANT_DELETE_OWNER;
        }

        Optional<User> optionalUser=userRepository.findUserByAccount_Username(username);

        if(!optionalUser.isPresent())
        {
            return Messages.USER_DOES_NOT_EXIST;
        }

        User user=optionalUser.get();

        removeUserFromBoardIssues(user,boardId);
        removeUserFromMtmBoard(user,boardId);

        userRepository.save(user);

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

        if(isUserOwnerOfBoard(accountUsername,boardId))
        {
            return Messages.CANT_DELETE_OWNER;
        }

        User user=optionalUser.get();

        removeUserFromBoardIssues(user,boardId);
        removeUserFromMtmBoard(user,boardId);

        userRepository.save(user);

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
