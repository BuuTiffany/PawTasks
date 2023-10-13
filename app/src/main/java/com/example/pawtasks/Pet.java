package com.example.pawtasks;

enum Rarity {
    COMMON,
    RARE,
    EPIC,
    LEGENDARY,
}

public class Pet {
    private String petName;
    private Rarity rarity;
    private int imageResourceId;

    public Pet(String name, Rarity rarity, int id) {
        this.petName = name;
        this.rarity = rarity;
        this.imageResourceId = id;
    }

    public String getPetName() {
        return petName;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
