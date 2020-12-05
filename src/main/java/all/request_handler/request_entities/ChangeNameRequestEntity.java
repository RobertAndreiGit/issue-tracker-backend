package all.request_handler.request_entities;

public class ChangeNameRequestEntity
{
    private String name;

    public ChangeNameRequestEntity(){}

    public ChangeNameRequestEntity(String name)
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
