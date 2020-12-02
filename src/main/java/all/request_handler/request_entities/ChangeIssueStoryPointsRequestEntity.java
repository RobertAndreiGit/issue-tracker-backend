package all.request_handler.request_entities;

public class ChangeIssueStoryPointsRequestEntity
{
    private int boardId;
    private int issueId;
    private int value;

    public ChangeIssueStoryPointsRequestEntity(){}

    public ChangeIssueStoryPointsRequestEntity(int boardId, int issueId, int value)
    {
        this.boardId = boardId;
        this.issueId = issueId;
        this.value = value;
    }

    public int getBoardId()
    {
        return boardId;
    }

    public void setBoardId(int boardId)
    {
        this.boardId = boardId;
    }

    public int getIssueId()
    {
        return issueId;
    }

    public void setIssueId(int issueId)
    {
        this.issueId = issueId;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }
}
