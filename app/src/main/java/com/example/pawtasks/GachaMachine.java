package com.example.pawtasks;

import android.app.Dialog;
import android.view.Window;
import android.widget.Button;

import java.util.HashMap;
import java.util.Random;

public class GachaMachine
{
    private Pet[] commonPetIndex;
    private Pet[] rarePetIndex;
    private Pet[] epicPetIndex;
    private Pet[] legendaryPetIndex;

    GachaMachine() {

        // Add the common pets
        commonPetIndex = new Pet[3];
        commonPetIndex[0] = new Pet("Neeko", Rarity.COMMON, R.drawable.neekocommon);
        commonPetIndex[1] = new Pet("Naafiri", Rarity.COMMON, R.drawable.naafiricommon);
        commonPetIndex[2] = new Pet("Doge", Rarity.COMMON, R.drawable.doge);

        // Add the rare pets
        rarePetIndex = new Pet[3];
        rarePetIndex[0] = new Pet("Sophie", Rarity.RARE, R.drawable.rarelab);
        rarePetIndex[1] = new Pet("Checkers", Rarity.RARE, R.drawable.checkers);
        rarePetIndex[2] = new Pet("Toffee", Rarity.RARE, R.drawable.toffee);

        // Add the epic pets
        epicPetIndex = new Pet[3];
        epicPetIndex[0] = new Pet("Liberty", Rarity.EPIC, R.drawable.epicgolden);
        epicPetIndex[1] = new Pet("Oakley", Rarity.EPIC, R.drawable.oakley);
        epicPetIndex[2] = new Pet("Shadow", Rarity.EPIC, R.drawable.shadowgolden);

        legendaryPetIndex = new Pet[1];
        legendaryPetIndex[0] = new Pet("Brutus", Rarity.LEGENDARY, R.drawable.legendarypuppy);

    }
    Pet pull()
    {
        Random rand = new Random();
        double rarityCode = rand.nextInt(99);

        if (rarityCode < 5) { // 5% chance
            // pick random Legendary
            return legendaryPetIndex[rand.nextInt(legendaryPetIndex.length)];
        } else if (rarityCode < 25) { // 20% chance (25 - 5)
            // pick random Epic
            return epicPetIndex[rand.nextInt(epicPetIndex.length)];
        } else if (rarityCode < 65) { // 40% chance (65 - 25)
            // pick random Rare
            return rarePetIndex[rand.nextInt(rarePetIndex.length)];
        } else {
            // pick random Common
            return commonPetIndex[rand.nextInt(rarePetIndex.length)];
        }
    }
}
