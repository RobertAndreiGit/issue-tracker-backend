package all.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.PERSIST)
    @JoinColumn(name="account", nullable=false)
    private Account account;

    @Column(nullable = false)
    private String name;

    @ManyToMany(fetch=FetchType.LAZY,cascade=CascadeType.MERGE)
    @JoinTable(
            name = "boards",
            joinColumns = @JoinColumn(name = "user"),
            inverseJoinColumns = @JoinColumn(name = "board"))
    private List<Board> boards;

    public User(){}

    public User(int id, Account account, String name, List<Board> boards)
    {
        this.id = id;
        this.account = account;
        this.name = name;
        this.boards = boards;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Account getAccount()
    {
        return account;
    }

    public void setAccount(Account account)
    {
        this.account = account;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Board> getBoards()
    {
        return boards;
    }

    public void setBoards(List<Board> boards)
    {
        this.boards = boards;
    }
}
