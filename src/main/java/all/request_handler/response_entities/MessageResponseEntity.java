package all.request_handler.response_entities;

public class MessageResponseEntity
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
