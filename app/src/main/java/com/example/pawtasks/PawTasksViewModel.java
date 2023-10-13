package com.example.pawtasks;

import androidx.lifecycle.ViewModel;

public class PawTasksViewModel extends ViewModel {

    // Holds the users token count
    private int tokenCount = 3;

    // Holds the users pet index

    public int getTokenCount() {
        return tokenCount;
    }

    public void setTokenCount(int count) {
        tokenCount = count;
    }

    public void decrementTokenCount() { --tokenCount; }

    public void incrementTokenCount() { ++tokenCount; }
}