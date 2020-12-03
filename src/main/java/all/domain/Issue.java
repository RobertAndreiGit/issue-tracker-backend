package all.domain;

import javax.persistence.*;

@Entity
@Table(name = "issues")
public class Issue
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private int storyPoints;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="board", nullable=false)
    private Board board;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="assigned_to")
    private User user;

    @ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.PERSIST)
    @JoinColumn(name="label", nullable=false)
    private Label label;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="category", nullable=false)
    private Category category;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="stage", nullable=false)
    private Stage stage;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="priority", nullable=false)
    private Priority priority;

    public Issue(){}

    public Issue(int id, String title, String text, int storyPoints, Board board, User user, Label label, Category category, Stage stage, Priority priority)
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

    public Board getBoard()
    {
        return board;
    }

    public void setBoard(Board board)
    {
        this.board = board;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
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
