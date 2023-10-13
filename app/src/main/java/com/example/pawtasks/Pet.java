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

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Pet pet = (Pet) o;

        return  petName.equals(pet.petName) &&
                rarity.equals(pet.rarity) &&
                imageResourceId == pet.imageResourceId;
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
