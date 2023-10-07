package com.example.pawtasks;

import androidx.lifecycle.ViewModel;

public class PawTasksViewModel extends ViewModel {
    private int tokenCount = 3;

    public int getTokenCount() {
        return tokenCount;
    }

    public void setTokenCount(int count) {
        tokenCount = count;
    }

    public void decrementTokenCount() { --tokenCount; }

    public void incrementTokenCount() { ++tokenCount; }
}