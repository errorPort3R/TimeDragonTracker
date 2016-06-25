package com.ironyard.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by jeffryporter on 6/24/16.
 */
@Entity
@Table(name = "dragons")


public class Dragon
{
    @Id
    @GeneratedValue
    Integer id;

    @Column(nullable = false, unique = true)
    String name;

    @Column(nullable = false)
    String color;

    @Column(nullable = false)
    String type;

    @Column(nullable = false)
    LocalDateTime birthdate;

    @Column(nullable = false)
    int age;

    boolean isEditable = false;

    @ManyToOne
    User user;

    public Dragon()
    {
    }

    public Dragon(int id, String name, String color, String type, LocalDateTime birthdate, int age, User user)
    {
        this.id = id;
        this.name = name;
        this.color = color;
        this.type = type;
        this.birthdate = birthdate;
        this.age = age;
        this.user = user;
    }

    public Dragon(String name, String color, String type, LocalDateTime birthdate, int age, User user)
    {
        this.name = name;
        this.color = color;
        this.type = type;
        this.birthdate = birthdate;
        this.age = age;
        this.user = user;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public LocalDateTime getBirthdate()
    {
        return birthdate;
    }

    public void setBirthdate(LocalDateTime birthdate)
    {
        this.birthdate = birthdate;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public boolean isEditable()
    {
        return isEditable;
    }

    public void setEditable(boolean editable)
    {
        isEditable = editable;
    }
}
