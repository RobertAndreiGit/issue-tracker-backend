package all.request_handler.response_entities;

public class BoardResponseEntity
{
    private int id;
    private String name;
    private UserResponseEntity user;

    public BoardResponseEntity(){}

    public BoardResponseEntity(int id, String name, UserResponseEntity user)
    {
        this.id = id;
        this.name = name;
        this.user = user;
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

    public UserResponseEntity getUser()
    {
        return user;
    }

    public void setUser(UserResponseEntity user)
    {
        this.user = user;
    }
}
