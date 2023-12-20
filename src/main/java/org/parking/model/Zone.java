package org.parking.model;

import org.parking.model.ParkingLot;

public class Zone
{
    private String id;
    private String lotName;

    public Zone(String id, String lotName)
    {
        this.id = id;
        this.lotName = lotName;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getLotName()
    {
        return lotName;
    }

    public void setLotName(String lotName)
    {
        this.lotName = lotName;
    }
}
