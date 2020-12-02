package all.request_handler.request_entities;

public class ChangeIssueLabelRequestEntity
{
    private int boardId;
    private int issueId;
    private String label;
    private String colour;

    public ChangeIssueLabelRequestEntity(){}

    public ChangeIssueLabelRequestEntity(int boardId, int issueId, String label, String colour)
    {
        this.boardId = boardId;
        this.issueId = issueId;
        this.label = label;
        this.colour = colour;
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

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getColour()
    {
        return colour;
    }

    public void setColour(String colour)
    {
        this.colour = colour;
    }
}
