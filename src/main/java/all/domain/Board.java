package all.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "boards")
public class Board
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name="owner", nullable=false)
    private User owner;

    @ManyToMany
    @JoinTable(
            name = "users",
            joinColumns = @JoinColumn(name = "board"),
            inverseJoinColumns = @JoinColumn(name = "user"))
    private List<User> users;

    public Board(){}

    public Board(String name, User owner, List<User> users)
    {
        this.name = name;
        this.owner = owner;
        this.users = users;
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

    public User getOwner()
    {
        return owner;
    }

    public void setOwner(User owner)
    {
        this.owner = owner;
    }

    public List<User> getUsers()
    {
        return users;
    }

    public void setUsers(List<User> users)
    {
        this.users = users;
    }
}
