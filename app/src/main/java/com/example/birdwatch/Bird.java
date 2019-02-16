package com.example.birdwatch;

public class Bird {
    private String id;
    private String name;
    private String note;
    private String rarity;
    private String date;

    public Bird(String i, String nam, String not, String rar, String dat){
        this.id = i;
        this.name = nam;
        this.note = not;
        this.rarity = rar;
        this.date = dat;
    }

    public Bird () {}

    // getter functions
    public String getName()     { return name; }
    public String getNote()     { return note; }
    public String getRarity()   { return rarity; }
    public String getDate()     { return date; }
    public String getId()       { return id; }

    // setter functions
    public void setId(String id)            {      this.id = id;     }
    public void setDate(String date)        {      this.date = date;    }
    public void setName(String name)        {      this.name = name;    }
    public void setNote(String note)        {      this.note = note;    }
    public void setRarity(String rarity)    {      this.rarity = rarity;    }
}
