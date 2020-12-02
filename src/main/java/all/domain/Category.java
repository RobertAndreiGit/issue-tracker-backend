package all.domain;

import all.domain.enums.EnumCategory;

import javax.persistence.*;

@Entity
@Table(name = "categories")
public class Category
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private EnumCategory name;

    public Category(){}

    public Category(int id, EnumCategory name)
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

    public EnumCategory getName()
    {
        return name;
    }

    public void setName(EnumCategory name)
    {
        this.name = name;
    }
}
