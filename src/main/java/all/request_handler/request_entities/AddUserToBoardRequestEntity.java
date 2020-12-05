package all.request_handler.request_entities;

public class AddUserToBoardRequestEntity
{
    private int boardId;
    private String username;

    public AddUserToBoardRequestEntity(){}

    public AddUserToBoardRequestEntity(int boardId, String username)
    {
        this.boardId = boardId;
        this.username = username;
    }

    public int getBoardId()
    {
        return boardId;
    }

    public void setBoardId(int boardId)
    {
        this.boardId = boardId;
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
