package com.example.birdwatch;

import com.google.gson.annotations.SerializedName;

public class Bird {
    @SerializedName("id")
    private int mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("note")
    private String mNote;
    @SerializedName("rarity")
    private String mRarity;
    @SerializedName("date")
    private String mDate;

    public Bird(int i, String nam, String not, String rar, String dat){
        mId = i;
        mName = nam;
        mNote = not;
        mRarity = rar;
        mDate = dat;
    }

    public Bird () { super(); }

    // getter functions
    public int getId()          { return mId;   }
    public String getName()     { return mName; }
    public String getNote()     { return mNote; }
    public String getRarity()   { return mRarity; }
    public String getDate()     { return mDate; }


    // setter functions
    public void setId(int id)               {      this.mId = id;           }
    public void setDate(String date)        {      this.mDate = date;       }
    public void setName(String name)        {      this.mName = name;       }
    public void setNote(String note)        {      this.mNote = note;       }
    public void setRarity(String rarity)    {      this.mRarity = rarity;   }

    /*
    @Override
    public String toString(){
        return "{\"Birds\": [{\"id\": \"" + id +
                "\", \"Name\" : \"" + mName +
                "\", \"Rarity\" : \"" + mRarity +
                "\", \"Note\" : \"" + mNote +
                "\", \"Date\" : \"" + mDate +
                "\"}]" +
                "}";
    }
    */
}
