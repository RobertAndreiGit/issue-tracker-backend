package all.request_handler.request_entities;

public class ChangeIssueTextRequestEntity
{
    private int boardId;
    private int issueId;
    private String text;

    public ChangeIssueTextRequestEntity(){}

    public ChangeIssueTextRequestEntity(int boardId, int issueId, String text)
    {
        this.boardId = boardId;
        this.issueId = issueId;
        this.text = text;
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

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }
}
