package com.example.pawtasks;

import androidx.lifecycle.ViewModel;

import java.util.HashSet;

public class PawTasksViewModel extends ViewModel {

    // Holds the users token count
    private int tokenCount = 3;

    // Holds the users pet index
    private HashSet<Pet> userCollectedPetIndex = new HashSet<>();

    public void addPet(Pet pet) {
        userCollectedPetIndex.add(pet);
    }

    public Pet getFirstPet() {
        for (Pet pet : userCollectedPetIndex) {
            return pet;
        }
        return null; // If the user has no pets
    }

    public Pet getPet(Pet wantedPet) {
        for (Pet pet : userCollectedPetIndex) {
            if (pet.equals(wantedPet)) {
                return pet;
            }
        }
        return null; // If the pet is not found
    }

    public int getTokenCount() {
        return tokenCount;
    }

    public void setTokenCount(int count) {
        tokenCount = count;
    }

    public void decrementTokenCount() { --tokenCount; }

    public void incrementTokenCount() { ++tokenCount; }
}