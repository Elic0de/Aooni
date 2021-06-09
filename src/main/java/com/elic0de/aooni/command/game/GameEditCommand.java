package com.elic0de.aooni.command.game;

import com.elic0de.aooni.command.Arguments;
import com.elic0de.aooni.command.Command;
import com.elic0de.aooni.command.Sender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GameEditCommand implements Command {

    @Override
    public void onCommand(Sender sender, Arguments args) {
        //プレイヤーでなければ戻る
        if(blockNonPlayer(sender)) return;

        if(hasPermission(sender,this.getClass().getSimpleName())) return;

        //アスレ名が指定されていなければ戻る
        if(!args.hasNext()){
            sender.warn("編集するゲームの名前を指定して下さい。");
            return;
        }

        //第1引数をアスレ名として取得する
        String gameName = args.next();

        //送信者をプレイヤーとして取得する
        Player player = sender.asPlayerCommandSender();
        UUID uuid = player.getUniqueId();
    }
}