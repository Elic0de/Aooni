package com.elic0de.aooni.util.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemStackBuilder {

    private static final String COLORS = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";
    private static final String NULL = String.valueOf(Character.MIN_VALUE);

    private Material material;
    private String displayName;
    private int amount = 1;
    private ArrayList<String> lore = new ArrayList<>();
    private Map<Enchantment, Integer> enchantments = new HashMap<>();
    private Set<ItemFlag> flags = new HashSet<>();

    public ItemStackBuilder(Material material) {
        this.material = material;
    }

    public static ItemStack createItem(ItemStack item, String itemName, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(itemName);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStackBuilder builder(Material material) {
        return new ItemStackBuilder(material);
    }

    public ItemStackBuilder setDisplayName(String name) {
        this.displayName = color(name);
        return this;
    }

    public ItemStackBuilder setLore(Collection<String> lore) {
        lore.forEach(
                s -> this.lore.add(color(s))
        );
        return this;
    }

    public ItemStackBuilder setAmount(int amount){
        this.amount = amount;
        return this;
    }

    public ItemStackBuilder addEnchantment(Enchantment enchantment){
        return addEnchantment(enchantment, 1);
    }

    public ItemStackBuilder addEnchantment(Enchantment enchantment, int level){
        enchantments.put(enchantment, level);
        return this;
    }

    public ItemStackBuilder addFlag(ItemFlag flag){
        flags.add(flag);
        return this;
    }

    public ItemStack build(){
       ItemStack item = new ItemStack(material);

        item.setAmount(amount);

        ItemMeta meta = item.getItemMeta();

        if(meta != null){
            //if(meta instanceof Damageable) ((Damageable) meta).setDamage(damage);

            meta.setDisplayName(displayName);
            meta.setLore(lore);

            enchantments.entrySet().forEach(entry -> meta.addEnchant(entry.getKey(), entry.getValue(), true));

            meta.addItemFlags(flags.toArray(new ItemFlag[flags.size()]));

            item.setItemMeta(meta);
        }

       return item;
    }

    private String color(String text) {
        //文字列を1文字ずつに分解する
        char[] characters = text.toCharArray();

        //各文字に対して処理をする
        for(int i = 0; i < characters.length - 1; i++){
            char color = characters[i + 1];

            //装飾コードでなければ戻る
            if(characters[i] != '&' || COLORS.indexOf(color) <= -1) continue;

            if(i > 0 && characters[i - 1] == '-') characters[i - 1] = Character.MIN_VALUE;

            characters[i] = '§';
            characters[i + 1] = Character.toLowerCase(color);

            if(i < characters.length - 2 && characters[i + 2] == '-'){
                characters[i + 2] = Character.MIN_VALUE;
                i += 2;
            }else{
                i++;
            }
        }

        return new String(characters).replace(NULL, "");
    }

}
