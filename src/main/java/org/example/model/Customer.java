package org.example.model;
public class Customer {
    private String name;
    private String parcelId;

    // Constructor
    public Customer(String name, String parcelId) {
        this.name = name;
        this.parcelId = parcelId;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for parcelId
    public String getParcelId() {
        return parcelId;
    }

    @Override
    public String toString() {
        return "Customer{name='" + name + "', parcelId='" + parcelId + "'}";
    }
}
