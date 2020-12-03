package all.request_handler.request_entities;

public class RegisterRequestEntity
{
    private String username;
    private String password;
    private String name;

    public RegisterRequestEntity(){}

    public RegisterRequestEntity(String username, String password, String name)
    {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
