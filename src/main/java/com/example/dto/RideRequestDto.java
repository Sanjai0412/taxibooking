package com.example.dto;

public class RideRequestDto {
    private int pickupLocationId;
    private int dropLocationId;

    public int getPickupLocationId() {
        return pickupLocationId;
    }

    public void setPickupLocationId(int pickupLocationId) {
        this.pickupLocationId = pickupLocationId;
    }

    public int getDropLocationId() {
        return dropLocationId;
    }

    public void setDropLocationId(int dropLocationId) {
        this.dropLocationId = dropLocationId;
    }
}
