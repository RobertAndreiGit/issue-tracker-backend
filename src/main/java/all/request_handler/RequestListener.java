package all.request_handler;

import all.domain.*;
import all.request_handler.request_entities.*;
import all.request_handler.response_entities.BoardResponseEntity;
import all.request_handler.response_entities.IssueResponseEntity;
import all.request_handler.response_entities.MessageResponseEntity;
import all.request_handler.response_entities.UserResponseEntity;
import all.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(path="/issue_tracker")
public class RequestListener
{
    @Autowired
    private Service service;

    private String getAccountUsername()
    {
        Object authenticatedUserDetails=SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(authenticatedUserDetails instanceof UserDetails)
        {
            return ((UserDetails)authenticatedUserDetails).getUsername();
        }
        else
        {
            return null;
        }
    }

    @GetMapping("/current_user")
    public UserResponseEntity getCurrentUser()
    {
        User user=service.getUser(getAccountUsername());

        if(user==null)
        {
            return null;
        }

        return new UserResponseEntity(user.getAccount().getUsername(),user.getName());
    }

    @GetMapping("/boards")
    public List<BoardResponseEntity> getBoards()
    {
        List<BoardResponseEntity> responseList=new ArrayList<>();
        List<Board> boards=service.getBoards(getAccountUsername());

        boards.forEach((board)->responseList.add(new BoardResponseEntity(board.getId(),board.getName(),
                                                    new UserResponseEntity(board.getOwner().getAccount().getUsername(),board.getOwner().getName()))));

        return responseList;
    }

    @PostMapping("/board/create")
    public int createBoard(@RequestBody CreateBoardRequestEntity entity)
    {
        return service.createBoard(entity.getName(),getAccountUsername());
    }

    @DeleteMapping("/board/{boardId}")
    public MessageResponseEntity deleteBoard(@PathVariable(name="boardId") int boardId)
    {
        return new MessageResponseEntity(service.deleteBoard(boardId,getAccountUsername()));
    }

    @GetMapping("/board/stages")
    public List<Stage> getStages()
    {
        return service.getStages();
    }

    @GetMapping("/board/{boardId}/users")
    public List<UserResponseEntity> getBoardParticipants(@PathVariable int boardId)
    {
        List<UserResponseEntity> responseList=new ArrayList<>();
        List<User> boardParticipants=service.getBoardParticipants(boardId,getAccountUsername());
        boardParticipants.forEach((participant)->responseList.add(new UserResponseEntity(participant.getAccount().getUsername(),participant.getName())));
        return responseList;
    }

    @PostMapping("/board/users/add")
    public MessageResponseEntity addUserToBoard(@RequestBody AddUserToBoardRequestEntity requestEntity)
    {
        return new MessageResponseEntity(service.addUserToBoard(requestEntity.getBoardId(),requestEntity.getUsername(),getAccountUsername()));
    }

    @DeleteMapping("/board/{boardId}/users/delete/{username}")
    public MessageResponseEntity deleteUserFromBoard(@PathVariable(name="boardId") int boardId,@PathVariable(name="username") String username)
    {
        return new MessageResponseEntity(service.deleteUserFromBoard(boardId,username,getAccountUsername()));
    }

    @DeleteMapping("/board/{boardId}/users/leave")
    public MessageResponseEntity leaveBoard(@PathVariable(name="boardId") int boardId)
    {
        return new MessageResponseEntity(service.leaveBoard(boardId,getAccountUsername()));
    }

    @GetMapping("/board/{boardId}/issues")
    public List<IssueResponseEntity> getBoardIssues(@PathVariable int boardId)
    {
        List<Issue> issueList=service.getBoardIssues(boardId,getAccountUsername());
        return getResponseIssueList(issueList);
    }

    private List<IssueResponseEntity> getResponseIssueList(List<Issue> issueList)
    {
        List<IssueResponseEntity> responseIssueList=new ArrayList<>();

        for(Issue issue:issueList)
        {
            Board board=issue.getBoard();
            UserResponseEntity boardOwner=new UserResponseEntity(board.getOwner().getAccount().getUsername(),board.getOwner().getName());
            BoardResponseEntity boardResponseEntity=new BoardResponseEntity(board.getId(),board.getName(),boardOwner);
            UserResponseEntity user=new UserResponseEntity();

            if(issue.getUser()!=null)
            {
                user.setUsername(issue.getUser().getAccount().getUsername());
                user.setName(issue.getUser().getName());
            }
            else
            {
                user.setUsername("");
                user.setName("");
            }

            responseIssueList.add(new IssueResponseEntity(issue.getId(),issue.getTitle(),issue.getText(),issue.getStoryPoints(),boardResponseEntity,
                                    user,issue.getLabel(),issue.getCategory(),issue.getStage(),issue.getPriority()));
        }

        return responseIssueList;
    }

    @GetMapping("/board/issues/priorities")
    public List<Priority> getPriorities()
    {
        return service.getPriorities();
    }

    @GetMapping("/board/issues/categories")
    public List<Priority> getCategories()
    {
        return service.getCategories();
    }

    @PostMapping("/board/issues/create")
    public MessageResponseEntity createIssue(@RequestBody IssueRequestEntity entity)
    {
        return new MessageResponseEntity(service.createIssue(entity,getAccountUsername()));
    }

    @PostMapping("/board/issues/change")
    public MessageResponseEntity changeIssue(@RequestBody IssueRequestEntity entity)
    {
        return new MessageResponseEntity(service.changeIssue(entity,getAccountUsername()));
    }

    @DeleteMapping("/board/{boardId}/issues/{issueId}")
    public MessageResponseEntity deleteIssue(@PathVariable(name="boardId") int boardId,@PathVariable(name="issueId") int issueId)
    {
        return new MessageResponseEntity(service.deleteIssue(boardId,issueId,getAccountUsername()));
    }

    @PostMapping("/register")
    public MessageResponseEntity register(@RequestBody RegisterRequestEntity entity)
    {
        return new MessageResponseEntity(service.register(entity.getUsername(),entity.getPassword(),entity.getName()));
    }

    @PostMapping("/user/change/name")
    public MessageResponseEntity changeName(@RequestBody ChangeNameRequestEntity entity)
    {
        return new MessageResponseEntity(service.changeName(entity.getName(),getAccountUsername()));
    }
}
