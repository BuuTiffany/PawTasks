package com.example.pawtasks;

import java.util.HashMap;

enum Rarity {
    COMMON,
    RARE,
    EPIC,
    LEGENDARY,
}

class Prize
{
    Prize(String _dogType,
          String _name,
          Rarity _rarity)
    {
        dogType = _dogType;
        name    = _name;
        rarity  = _rarity;
    }
    public String dogType;
    public String name;
    public Rarity rarity;
}

public class GachaMachine
{
    private int pityLevel;
    private HashMap<Prize, Rarity> inventory;
    GachaMachine() { }
    void addPrize(Prize _prize, Rarity _rarity)
    {
        inventory.put(_prize, _rarity);
    }
    void pull()
    {

    }
}
