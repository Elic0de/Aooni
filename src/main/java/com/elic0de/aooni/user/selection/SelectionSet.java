package com.elic0de.aooni.user.selection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import com.elic0de.aooni.game.Game;
import com.elic0de.aooni.game.GameManager;
import com.elic0de.aooni.util.tuplet.Tuple;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class SelectionSet implements Listener {

    private static SelectionSet instance;

    public static void load(){
        instance = new SelectionSet();
    }

    public static SelectionSet getInstance(){
        return instance;
    }
    //範囲選択用のツール
    private final ItemStack selectionTool;
    private final String selectionToolLore = "§7@SelectionTool";

    private final HashMap<UUID, String> selections = new HashMap<>();

    private SelectionSet(){
        selectionTool = new ItemStack(Material.CHEST);

        ItemMeta meta = selectionTool.getItemMeta();
        meta.setLore(Arrays.asList(selectionToolLore));

        selectionTool.setItemMeta(meta);

        //発光用エンチャントを付与する
        //GleamEnchantment.gleam(selectionTool);
    }

    //新しいセレクションを作成する
    public void setNewSelection(UUID uuid, String parkourName){


        //アスレとセレクションを結び付けてセットする
        selections.put(uuid, parkourName);
    }

    //選択中のアスレの名前を取得する
    public String getSelectedParkourName(UUID uuid){
        return selections.containsKey(uuid) ? selections.get(uuid) : null;
    }



    public boolean hasSelection(UUID uuid){
        return selections.containsKey(uuid);
    }

    //新しい範囲選択ツールを作成する
    public ItemStack makeNewSelectionTool(UUID uuid){
        ItemStack clone = selectionTool.clone();
        applySelectionInformationToDisplayName(uuid, clone);
        return clone;
    }

    //アイテムが範囲選択ツールかどうか判定する
    public boolean isSelectionTool(ItemStack item){
        if(item == null || item.getType() != Material.CHEST) return false;

        ItemMeta meta = item.getItemMeta();
        if(meta == null || !meta.hasLore() || !meta.getLore().contains(selectionToolLore)) return false;

        return true;
    }

    //範囲選択ツールの表示名に選択情報を適用する
    public void applySelectionInformationToDisplayName(UUID uuid, ItemStack tool){
        if(!selections.containsKey(uuid)) return;

        //選択中のアスレの名前を取得する
        String parkourName = getSelectedParkourName(uuid);

        //セレクションを取得する

        //表示名を作成する
        String displayName = "デフォルト";

        ItemMeta meta = tool.getItemMeta();

        //表示名を設定する
        meta.setDisplayName(displayName);

        //変更を適用する
        tool.setItemMeta(meta);
    }
    @EventHandler
    public void setSelection(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        //範囲選択中のプレイヤーでなければ戻る
        if(!selections.containsKey(uuid)) return;

        ItemStack clickedItem = event.getItemInHand();

        //範囲選択ツールでなければ戻る
        if(!isSelectionTool(clickedItem)) return;

        makeNewSelectionTool(uuid);
        //置いたブロックの座標を取得する
        Location placedLocation = event.getBlockPlaced().getLocation();

        Game game = GameManager.getInstance().getGame(getSelectedParkourName(uuid));
        game.getChest().chestAdd(placedLocation);

    }
    @EventHandler
    public void removeSelection(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        //範囲選択中のプレイヤーでなければ戻る
        if(!selections.containsKey(uuid)) return;

        ItemStack clickedItem = event.getPlayer().getItemOnCursor();

        //範囲選択ツールでなければ戻る
        if(!isSelectionTool(clickedItem)) return;

        //置いたブロックの座標を取得する
        Location placedLocation = event.getBlock().getLocation();

        Game game = GameManager.getInstance().getGame(getSelectedParkourName(uuid));
        game.getChest().removeChest(placedLocation);

    }


    @EventHandler
    public void clearSelection(PlayerQuitEvent event){
        clearSelection(event.getPlayer());
    }

    //選択をクリアする
    public void clearSelection(Player player){
        selections.remove(player.getUniqueId());
    }

}
