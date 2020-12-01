package all.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User
{
    @Id
    private int id;

    @ManyToOne
    @JoinColumn(name="id", nullable=false)
    private Account account;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "boards",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id"))
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
