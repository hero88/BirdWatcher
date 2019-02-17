package com.example.birdwatch;

public class Bird {
    private int id;
    private String name;
    private String note;
    private String rarity;
    private String date;

    public Bird(int i, String nam, String not, String rar, String dat){
        super();
        this.id = i;
        this.name = nam;
        this.note = not;
        this.rarity = rar;
        this.date = dat;
    }

    public Bird () { super(); }

    // getter functions
    public String getName()     { return name; }
    public String getNote()     { return note; }
    public String getRarity()   { return rarity; }
    public String getDate()     { return date; }
    public int getId()          { return id; }

    // setter functions
    public void setId(int id)               {      this.id = id;     }
    public void setDate(String date)        {      this.date = date;    }
    public void setName(String name)        {      this.name = name;    }
    public void setNote(String note)        {      this.note = note;    }
    public void setRarity(String rarity)    {      this.rarity = rarity;    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Bird other = (Bird) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString(){
        return "{\"birds\": [{\"id\": \"" + id +
                "\", \"name\" : \"" + name +
                "\", \"rarity\" : \"" + rarity +
                "\", \"note\" : \"" + note +
                "\", \"date\" : \"" + date +
                "\"}]" +
                "}";
    }
}
