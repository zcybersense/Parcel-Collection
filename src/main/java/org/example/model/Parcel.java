package org.example.model;

public class Parcel {
    private String parcelID;
    private String dimension;
    private float weight;
    private int daysInDepot;
    private String status;
    private double fee;

    // Constructor to initialize the Parcel and calculate the fee
    public Parcel(String parcelID, String dimension, float weight, int daysInDepot, String status) {
        this.parcelID = parcelID;
        this.dimension = dimension;
        this.weight = weight;
        this.daysInDepot = daysInDepot;
        this.status = status;
        this.fee = 0.0; // Default fee to 0.0 until calculated

        // Calculate the fee immediately when the object is created
        calculateFee();
    }

    // Method to calculate the fee
    public void calculateFee() {
        // Example calculation logic (you can modify this as per your requirement)
        double baseFee = 5.00; // Base fee for all parcels
        double weightFee = weight * 0.10; // Fee based on weight (for example $0.10 per kg)
        double daysFee = daysInDepot * 0.50; // Fee based on days in depot (for example $0.50 per day)

        this.fee =Math.floor((baseFee + weightFee) * daysFee);  // Calculate the total fee
    }

    // Getters for the Parcel attributes
    public String getParcelID() {
        return parcelID;
    }

    public String getDimension() {
        return dimension;
    }

    public float getWeight() {
        return weight;
    }

    public int getDaysInDepot() {
        return daysInDepot;
    }

    public String getStatus() {
        return status;
    }

    public double getFee() {
        return fee;
    }

    // Method to update the status of the parcel
    public void updateStatus(String status) {
        this.status = status;
    }
}
