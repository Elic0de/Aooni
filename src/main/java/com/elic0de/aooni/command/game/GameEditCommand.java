package com.elic0de.aooni.command.game;

import com.elic0de.aooni.command.Arguments;
import com.elic0de.aooni.command.Command;
import com.elic0de.aooni.command.Sender;
import com.elic0de.aooni.game.GameManager;
import com.elic0de.aooni.user.selection.SelectionSet;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class GameEditCommand implements Command {

    private final SelectionSet selections = SelectionSet.getInstance();

    @Override
    public void onCommand(Sender sender, Arguments args) {
        //プレイヤーでなければ戻る
        if(blockNonPlayer(sender)) return;

        if(hasPermission(sender,this.getClass().getSimpleName())) return;

        //アスレ名が指定されていなければ戻る
        if(!args.hasNext()){
            sender.warn("範囲選択をするゲームの名前を指定して下さい。");
            return;
        }

        //第1引数をアスレ名として取得する
        String gameName = ChatColor.translateAlternateColorCodes('&', args.next());

        //送信者をプレイヤーとして取得する
        Player player = sender.asPlayerCommandSender();
        UUID uuid = player.getUniqueId();

        //finishと入力された場合は範囲選択を終了する
        if(gameName.equals("finish")){
            //範囲選択中でなければ戻る
            if(!selections.hasSelection(uuid)){
                sender.warn("範囲選択中ではありません。");
                return;
            }

            Inventory inventory = player.getInventory();

            //インベントリ内から範囲選択ツールを削除する
            for(ItemStack item : inventory.getContents()) if(selections.isSelectionTool(item))
                inventory.remove(item);

            //セレクションをクリアする
            selections.clearSelection(player);

            sender.info("範囲選択を終了しました。");
            return;
        }

        //アスレが存在しなければ戻る
        if(!GameManager.getInstance().containsGame(gameName)){
            player.sendMessage("存在しません");
            return;
        }

        //新しいセレクションを作成する
        selections.setNewSelection(uuid, gameName);

        //対応した範囲選択ツールを作成する
        ItemStack selectionTool = selections.makeNewSelectionTool(uuid);

        player.getInventory().addItem(selectionTool);

        return;
    }

}