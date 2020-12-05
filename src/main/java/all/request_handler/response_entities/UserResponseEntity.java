package all.request_handler.response_entities;

import java.io.Serializable;

public class UserResponseEntity implements Serializable
{
    private String username;
    private String name;

    public UserResponseEntity(){}

    public UserResponseEntity(String username, String name)
    {
        this.username = username;
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
