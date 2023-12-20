package org.parking.model;

public class Space
{
    private int number;
    private String type;
    private boolean status;
    private String zoneID;
    private String lotName;

    public Space(int number, String type, boolean status, String zoneID, String lotName)
    {
        this.number = number;
        this.type = type;
        this.status = status;
        this.zoneID = zoneID;
        this.lotName = lotName;
    }

    public int getNumber()
    {
        return number;
    }

    public String getType()
    {
        return type;
    }

    public boolean getStatus()
    {
        return status;
    }

    public String getZoneID()
    {
        return zoneID;
    }

    public String getLotName()
    {
        return lotName;
    }
}
