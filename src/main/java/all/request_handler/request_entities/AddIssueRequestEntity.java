package all.request_handler.request_entities;

public class AddIssueRequestEntity
{
    private String title;
    private String text;
    private int boardId;
    private String label;
    private String labelColour;
    private int categoryId;
    private int stageId;
    private int priorityId;
    private int storyPoints;

    public AddIssueRequestEntity(){}

    public AddIssueRequestEntity(String title, String text, int boardId, String label, String labelColour, int categoryId, int stageId, int priorityId, int storyPoints)
    {
        this.title = title;
        this.text = text;
        this.boardId = boardId;
        this.label = label;
        this.labelColour = labelColour;
        this.categoryId = categoryId;
        this.stageId = stageId;
        this.priorityId = priorityId;
        this.storyPoints = storyPoints;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
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

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getLabelColour()
    {
        return labelColour;
    }

    public void setLabelColour(String labelColour)
    {
        this.labelColour = labelColour;
    }

    public int getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(int categoryId)
    {
        this.categoryId = categoryId;
    }

    public int getStageId()
    {
        return stageId;
    }

    public void setStageId(int stageId)
    {
        this.stageId = stageId;
    }

    public int getPriorityId()
    {
        return priorityId;
    }

    public void setPriorityId(int priorityId)
    {
        this.priorityId = priorityId;
    }

    public int getStoryPoints()
    {
        return storyPoints;
    }

    public void setStoryPoints(int storyPoints)
    {
        this.storyPoints = storyPoints;
    }
}
