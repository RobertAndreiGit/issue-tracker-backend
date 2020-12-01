package all.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="labels")
public class Label
{
    @Id
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String colour;

    public Label(){}

    public Label(int id, String name, String colour)
    {
        this.id = id;
        this.name = name;
        this.colour = colour;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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
