package com.elic0de.aooni.menus;

import com.elic0de.aooni.game.Game;
import com.elic0de.aooni.game.GameManager;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.Pagination;
import fr.minuskube.inv.content.SlotIterator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.DARK_GRAY;

public class ArenaMenu implements InventoryProvider {

    public static SmartInventory INVENTORY() {
        return SmartInventory.builder()
                .id("gameList")
                .provider(new ArenaMenu())
                .size(6, 9)
                .title(DARK_GRAY + "ゲームリスト")
                .closeable(true)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        Pagination pagination = contents.pagination();
        List<Game> games = GameManager.getInstance().getEnabledGames().collect(Collectors.toList());
        ClickableItem[] items = new ClickableItem[games.size()];

        for (int i = 0; i < items.length; i++) {
            Game game = games.get(i);
            items[i] = ClickableItem.of(new ItemStack(Material.ARROW),inventoryClickEvent -> game.joinGame(player));
        }

        contents.set(2, 3, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> INVENTORY().open(player, pagination.previous().getPage())));
        contents.set(2, 5, ClickableItem.of(new ItemStack(Material.ARROW),
                e -> INVENTORY().open(player, pagination.next().getPage())));

        pagination.setItems(items);
        pagination.setItemsPerPage(36);

        pagination.addToIterator(contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 0).allowOverride(false));
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }
}