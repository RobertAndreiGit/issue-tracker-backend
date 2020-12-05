package all.request_handler.request_entities;

public class CreateBoardRequestEntity
{
    private String name;

    public CreateBoardRequestEntity(){}

    public CreateBoardRequestEntity(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
