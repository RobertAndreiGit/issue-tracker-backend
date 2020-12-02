package all.request_handler.request_entities;

public class ChangeIssueStageRequestEntity
{
    private int boardId;
    private int issueId;
    private int stageId;

    public ChangeIssueStageRequestEntity(){}

    public ChangeIssueStageRequestEntity(int boardId, int issueId, int stageId)
    {
        this.boardId = boardId;
        this.issueId = issueId;
        this.stageId = stageId;
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

    public int getStageId()
    {
        return stageId;
    }

    public void setStageId(int stageId)
    {
        this.stageId = stageId;
    }
}
