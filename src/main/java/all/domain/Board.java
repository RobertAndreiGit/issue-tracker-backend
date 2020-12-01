package all.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "boards")
public class Board
{
    @Id
    private int id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name="id", nullable=false)
    private User account;

    @ManyToMany
    @JoinTable(
            name = "users",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "id"))
    private List<User> users;

    public Board(){}

    public Board(int id, String name, User account)
    {
        this.id = id;
        this.name = name;
        this.account = account;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public User getAccount()
    {
        return account;
    }

    public void setAccount(User account)
    {
        this.account = account;
    }
}
