package org.parking.model;

public class Driver {
    private String name;
    private String id;
    private String status;

    public Driver(String id, String name, String status) throws IllegalArgumentException {
        this.name = name;
        this.id = id;
        this.status = status;
    }

    public String getName() {
        return this.name;
    }
    public String getId() {
        return this.id;
    }
    public String getStatus() {
        return this.status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        // Seralize driver object to string
        return "Driver id: " + this.id + "\nName: " + this.name + "\nStatus: " + this.status + "\n";
    }
}
