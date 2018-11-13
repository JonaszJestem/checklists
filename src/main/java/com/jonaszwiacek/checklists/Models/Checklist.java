package com.jonaszwiacek.checklists.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Checklist
{
    final String name;
    final List<Item> items;


    public Checklist( String name )
    {
        this.name = name;
        items = new ArrayList<>();
    }


    public String getName()
    {
        return name;
    }


    @Override
    public boolean equals( Object o )
    {
        if( this == o )
            return true;
        if( o == null || getClass() != o.getClass() )
            return false;
        Checklist checklist = (Checklist)o;
        return Objects.equals( name, checklist.name );
    }


    @Override
    public int hashCode()
    {
        return Objects.hash( name );
    }
}
