package all.request_handler.request_entities;

public class ChangeIssuePriorityRequestEntity
{
    private int boardId;
    private int issueId;
    private int priorityId;

    public ChangeIssuePriorityRequestEntity(){}

    public ChangeIssuePriorityRequestEntity(int boardId, int issueId, int priorityId)
    {
        this.boardId = boardId;
        this.issueId = issueId;
        this.priorityId = priorityId;
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

    public int getPriorityId()
    {
        return priorityId;
    }

    public void setPriorityId(int priorityId)
    {
        this.priorityId = priorityId;
    }
}
