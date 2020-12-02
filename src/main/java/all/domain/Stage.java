package all.domain;

import all.domain.enums.EnumStage;

import javax.persistence.*;

@Entity
@Table(name = "stages")
public class Stage
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private EnumStage name;

    public Stage(){}

    public Stage(int id, EnumStage name)
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

    public EnumStage getName()
    {
        return name;
    }

    public void setName(EnumStage name)
    {
        this.name = name;
    }
}
