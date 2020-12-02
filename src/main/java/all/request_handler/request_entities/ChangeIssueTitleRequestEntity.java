package all.request_handler.request_entities;

public class ChangeIssueTitleRequestEntity
{
    private int boardId;
    private int issueId;
    private String title;

    public ChangeIssueTitleRequestEntity(){}

    public ChangeIssueTitleRequestEntity(int boardId, int issueId, String title)
    {
        this.boardId = boardId;
        this.issueId = issueId;
        this.title = title;
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

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }
}
