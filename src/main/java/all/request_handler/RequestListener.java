package all.request_handler;

import all.domain.*;
import all.request_handler.request_entities.*;
import all.request_handler.response_entities.BoardResponseEntity;
import all.request_handler.response_entities.IssueResponseEntity;
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
    public int createBoard(@RequestBody String name)
    {
        return service.createBoard(name,getAccountUsername());
    }

    @DeleteMapping("/board/{boardId}")
    public boolean deleteBoard(@PathVariable(name="boardId") int boardId)
    {
        return service.deleteBoard(boardId,getAccountUsername());
    }

    @GetMapping("/board/stages")
    public List<Stage> getStages()
    {
        return service.getStages();
    }

    @GetMapping("/board/{boardId}/participants")
    public List<UserResponseEntity> getBoardParticipants(@PathVariable int boardId)
    {
        List<UserResponseEntity> responseList=new ArrayList<>();
        List<User> boardParticipants=service.getBoardParticipants(boardId,getAccountUsername());
        boardParticipants.forEach((participant)->responseList.add(new UserResponseEntity(participant.getAccount().getUsername(),participant.getName())));
        return responseList;
    }

    @PostMapping("/board/users/add")
    public int addUserToBoard(@RequestBody AddUserToBoardRequestEntity requestEntity)
    {
        return service.addUserToBoard(requestEntity.getIdBoard(),requestEntity.getUsername(),getAccountUsername());
    }

    @DeleteMapping("/board/{boardId}/users/delete/{username}")
    public boolean deleteUserFromBoard(@PathVariable(name="boardId") int boardId,@PathVariable(name="username") String username)
    {
        return service.deleteUserFromBoard(boardId,username,getAccountUsername());
    }

    @DeleteMapping("/board/{boardId}/users/leave")
    public boolean leaveBoard(@PathVariable(name="boardId") int boardId)
    {
        return service.leaveBoard(boardId,getAccountUsername());
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
            UserResponseEntity user=new UserResponseEntity(issue.getUser().getAccount().getUsername(),issue.getUser().getName());

            responseIssueList.add(new IssueResponseEntity(issue.getId(),issue.getTitle(),issue.getText(),issue.getStoryPoints(),boardResponseEntity,
                                    user,issue.getLabel(),issue.getCategory(),issue.getStage(),issue.getPriority()));
        }

        return responseIssueList;
    }

    @GetMapping("/board/{boardId}/issues/{issueId}/take")
    public boolean takeIssue(@PathVariable(name="boardId") int boardId, @PathVariable(name="issueId") int issueId)
    {
        return service.takeIssue(boardId,issueId,getAccountUsername());
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
    public boolean createIssue(@RequestBody AddIssueRequestEntity entity)
    {
        return service.createIssue(entity,getAccountUsername());
    }

    @PostMapping("/board/issues/storyPoints/change")
    public boolean changeStoryPoints(@RequestBody ChangeIssueStoryPointsRequestEntity entity)
    {
        return service.changeStoryPoints(entity.getBoardId(),entity.getIssueId(),entity.getValue(),getAccountUsername());
    }

    @PostMapping("/board/issues/priority/change")
    public boolean changeIssuePriority(@RequestBody ChangeIssuePriorityRequestEntity entity)
    {
        return service.changePriority(entity.getBoardId(),entity.getIssueId(),entity.getPriorityId(),getAccountUsername());
    }


    @PostMapping("/board/issues/stage/change")
    public boolean changeIssueStage(ChangeIssueStageRequestEntity entity)
    {
        return service.changeIssueStage(entity.getBoardId(),entity.getIssueId(),entity.getStageId(),getAccountUsername());
    }

    @PostMapping("/board/issues/category/change")
    public boolean changeIssueCategory(ChangeIssueCategoryRequestEntity entity)
    {
        return service.changeIssueCategory(entity.getBoardId(),entity.getIssueId(),entity.getCategoryId(),getAccountUsername());
    }

    @PostMapping("/board/issues/label/change")
    public boolean changeIssueLabel(ChangeIssueLabelRequestEntity entity)
    {
        return service.changeIssueLabel(entity.getBoardId(),entity.getIssueId(),entity.getLabel(),entity.getColour(),getAccountUsername());
    }

    @PostMapping("/board/issues/title/change")
    public boolean changeIssueTitle(ChangeIssueTitleRequestEntity entity)
    {
        return service.changeIssueTitle(entity.getBoardId(),entity.getIssueId(),entity.getTitle(),getAccountUsername());
    }

    @PostMapping("/board/issues/text/change")
    public boolean changeIssueText(ChangeIssueTextRequestEntity entity)
    {
        return service.changeIssueText(entity.getBoardId(),entity.getIssueId(),entity.getText(),getAccountUsername());
    }

    @DeleteMapping("/board/{boardId}/issues/{issueId}")
    public boolean deleteIssue(@PathVariable(name="boardId") int boardId,@PathVariable(name="issueId") int issueId)
    {
        return service.deleteIssue(boardId,issueId,getAccountUsername());
    }

    @PostMapping("/register")
    public boolean register(@RequestBody RegisterRequestEntity entity)
    {
        return service.register(entity.getUsername(),entity.getPassword(),entity.getName());
    }
}
