package com.example.pawtasks;

import android.app.Dialog;
import android.view.Window;
import android.widget.Button;

import java.util.HashMap;
import java.util.Random;

enum Rarity {
    COMMON,
    RARE,
    EPIC,
    LEGENDARY,
}

public class GachaMachine
{
    GachaMachine() { }
    Rarity pull()
    {
        Random rand = new Random();
        double rarityCode = rand.nextInt(100);
        double pityRatio = 0.10;

        if (rarityCode > 95) {
            // pick random Legendary
            return Rarity.LEGENDARY;
        } else if (rarityCode > 75) {
            // pick random Epic
            return Rarity.EPIC;
        } else if (rarityCode > 50) {
            // pick random Rare
            return Rarity.RARE;
        } else {
            // pick random Common
            return Rarity.COMMON;
        }
    }
}
