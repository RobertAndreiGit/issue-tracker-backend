package all.request_handler.request_entities;

public class ChangeIssueCategoryRequestEntity
{
    private int boardId;
    private int issueId;
    private int categoryId;

    public ChangeIssueCategoryRequestEntity(){}

    public ChangeIssueCategoryRequestEntity(int boardId, int issueId, int categoryId)
    {
        this.boardId = boardId;
        this.issueId = issueId;
        this.categoryId = categoryId;
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

    public int getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(int categoryId)
    {
        this.categoryId = categoryId;
    }
}
