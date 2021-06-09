package com.elic0de.aooni.command.game;

import com.elic0de.aooni.command.Arguments;
import com.elic0de.aooni.command.Command;
import com.elic0de.aooni.command.Sender;
import com.elic0de.aooni.game.GameManager;
import org.bukkit.entity.Player;

public class GameCommand implements Command {

    private final GameManager gameManager = GameManager.getInstance();

    @Override
    public void onCommand(Sender sender, Arguments args) {
        //送信者がプレイヤーでなければ戻る
        if(blockNonPlayer(sender)) return;

        if(sender.hasPermission("test"))

        //第1引数が無ければ戻る
        if(!args.hasNext()){
            displayCommandUsage(sender);
            return;
        }

        Player player = sender.asPlayerCommandSender();

        String gameName = args.next();

        //第2引数で分岐する
        switch(args.next()) {
            case "create": {
                if (gameManager.containsGame(gameName)) {
                player.sendMessage("すでに存在します");
                return;
                }

                //ファイル作成
                gameManager.makeYaml(gameName);
                //無効化された状態で登録
                gameManager.registerGame(gameName);
                player.sendMessage(gameName + "が作成されました");
                break;
            } case "delete": {
                break;
            } case "enable": {
                break;
            } case "disable": {
                break;
            } case "info": {
                break;
            }
        }
    }

    private void displayCommandUsage(Sender sender){
        sender.warn("/game [gameName] create @ 指定された名前でゲームを作成します。ゲーム名の先頭には必ず装飾コードを置いて下さい。");
        sender.warn("/game [gameName] delete @ ゲームを削除します。");
        sender.warn("/game [gameName] rename [rename] @ ゲームの名前を変更します。");
        sender.warn("/game [gameName] enable @ 有効化し選択画面に表示します。");
        sender.warn("/game [gameName] disable @ 無効化し選択画面から非表示にします。");
        sender.warn("/game [gameName] info @ ゲームの情報を表示します。");
        sender.warn("ゲーム名の装飾コードはアンパサンドを使用して下さい。");
    }
}
