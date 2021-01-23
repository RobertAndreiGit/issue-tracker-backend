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

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="owner", nullable=false)
    private User owner;

    @ManyToMany(mappedBy = "boards")
    private List<User> users;

    public Board(){}

    public Board(int id, String name, User owner, List<User> users)
    {
        this.id=id;
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
