package com.elic0de.aooni.game;

import com.elic0de.aooni.config.Yaml;
import com.elic0de.aooni.util.loc;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class ChestSet {

    private final Map<Integer, List<ItemStack>> chestItems = new HashMap<>();
    private final List<Location> chestLocations = new ArrayList<>();

    private final Random random = new Random();

    private List<Integer> randomSlot = new ArrayList<>();
    private List<Integer> randomDSlot = new ArrayList<>();

    public ChestSet(Yaml yaml) {
        for (int i = 0; i < 27; i++) {
            randomSlot.add(i);
        }
        for (int i = 0; i < 54; i++) {
            randomDSlot.add(i);
        }


        if(yaml.isConfigurationSection("chestItems")) {

            ConfigurationSection checkAreaSection = yaml.getConfigurationSection("chestItems");

            for (String key : checkAreaSection.getKeys(false)) {

                //整数型に変換する
                int percent = Integer.parseInt(key);

                List<ItemStack> items = (List<ItemStack>) yaml.getList("chestItems." + percent + ".items");

                chestItems.put(percent, items);
            }
        }

        if(yaml.isConfigurationSection("chestLocations")) {
            for (String key : yaml.getStringList("chestLocations")) {
                Location location = loc.stringToLocation(key);
                chestLocations.add(location);
            }
        }
    }

    public void chestAdd(Location location) {
        chestLocations.add(location);

        System.out.println("add");
    }

    public void removeChest(Location location) {
        if(chestLocations.contains(location))
        chestLocations.remove(location);
    }

    public void fillChest() {
        for (Location location : chestLocations) {
            Block block = location.getBlock();

            if(block == null) return;

            if (block.getState() instanceof Chest) {

                Chest chest = (Chest) block.getState();
                Inventory chestInventory = chest.getInventory();

                if (chestInventory != null) {
                    chestInventory.clear();
                    int added = 0;
                    Collections.shuffle(randomSlot);
                    Collections.shuffle(randomDSlot);
                    adding:
                    {
                        for (int chance : chestItems.keySet()) {
                            for (ItemStack item : chestItems.get(chance)) {
                                if (item != null && !item.getType().equals(Material.AIR)) {
                                    if (chest instanceof Chest) {
                                        if (random.nextInt(100) + 1 <= chance) {
                                            chestInventory.setItem(randomSlot.get(added), item);
                                            added++;
                                            if (added >= chestInventory.getSize() - 1 || added >= 5) {
                                                break adding;
                                            }
                                        }
                                    }
                                    if (chest instanceof DoubleChest) {
                                        if (random.nextInt(100) + 1 <= chance) {
                                            chestInventory.setItem(randomDSlot.get(added), item);
                                            added++;
                                            if (added >= chestInventory.getSize() - 1 || added >= 10) {
                                                break adding;

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void save(Yaml yaml) {
        if (yaml.isConfigurationSection("chestItems")) {

            if (!(yaml.getConfigurationSection("chestItems") == null)) {
                for (String index : yaml.getConfigurationSection("chestItems").getKeys(false)) {
                    yaml.set("chestItems." + index, null);
                }
            }

            for (Map.Entry<Integer, List<ItemStack>> checkAreasEntry : chestItems.entrySet()) {
                int majorCheckAreaNumber = checkAreasEntry.getKey();

                yaml.set("chestItems." + majorCheckAreaNumber, checkAreasEntry.getValue());
            }
        }
        yaml.set("chestLocations", chestLocations.stream().map(location -> loc.locationToString(location)).collect(Collectors.toList()));


    }
}