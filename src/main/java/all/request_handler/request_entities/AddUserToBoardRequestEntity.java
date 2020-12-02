package all.request_handler.request_entities;

public class AddUserToBoardRequestEntity
{
    private int idBoard;
    private String username;

    public AddUserToBoardRequestEntity(){}

    public AddUserToBoardRequestEntity(int idBoard, String username)
    {
        this.idBoard = idBoard;
        this.username = username;
    }

    public int getIdBoard()
    {
        return idBoard;
    }

    public void setIdBoard(int idBoard)
    {
        this.idBoard = idBoard;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}
