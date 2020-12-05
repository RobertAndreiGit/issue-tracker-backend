package all.request_handler.response_entities;

import all.domain.Category;
import all.domain.Label;
import all.domain.Priority;
import all.domain.Stage;

import java.io.Serializable;

public class IssueResponseEntity implements Serializable
{
    private int id;
    private String title;
    private String text;
    private int storyPoints;
    private BoardResponseEntity board;
    private UserResponseEntity user;
    private Label label;
    private Category category;
    private Stage stage;
    private Priority priority;

    public IssueResponseEntity(){}

    public IssueResponseEntity(int id, String title, String text, int storyPoints, BoardResponseEntity board, UserResponseEntity user, Label label, Category category, Stage stage, Priority priority)
    {
        this.id = id;
        this.title = title;
        this.text = text;
        this.storyPoints = storyPoints;
        this.board = board;
        this.user = user;
        this.label = label;
        this.category = category;
        this.stage = stage;
        this.priority = priority;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public int getStoryPoints()
    {
        return storyPoints;
    }

    public void setStoryPoints(int storyPoints)
    {
        this.storyPoints = storyPoints;
    }

    public BoardResponseEntity getBoard()
    {
        return board;
    }

    public void setBoard(BoardResponseEntity board)
    {
        this.board = board;
    }

    public UserResponseEntity getUser()
    {
        return user;
    }

    public void setUser(UserResponseEntity user)
    {
        this.user = user;
    }

    public Label getLabel()
    {
        return label;
    }

    public void setLabel(Label label)
    {
        this.label = label;
    }

    public Category getCategory()
    {
        return category;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }

    public Stage getStage()
    {
        return stage;
    }

    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

    public Priority getPriority()
    {
        return priority;
    }

    public void setPriority(Priority priority)
    {
        this.priority = priority;
    }
}
