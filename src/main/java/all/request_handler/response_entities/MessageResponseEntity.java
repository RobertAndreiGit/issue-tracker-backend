package all.request_handler.response_entities;

import java.io.Serializable;

public class MessageResponseEntity implements Serializable
{
    private String message;

    public MessageResponseEntity(){}

    public MessageResponseEntity(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
