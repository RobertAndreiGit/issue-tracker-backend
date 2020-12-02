package all.domain;

import all.domain.enums.EnumPriority;

import javax.persistence.*;

@Entity
@Table(name="priorities")
public class Priority
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumPriority name;

    public Priority(){}

    public Priority(int id, EnumPriority name)
    {
        this.id = id;
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public EnumPriority getName()
    {
        return name;
    }

    public void setName(EnumPriority name)
    {
        this.name = name;
    }
}
