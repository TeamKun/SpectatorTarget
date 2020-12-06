package net.teamfruit.spectatortarget;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public final class SpectatorTarget extends JavaPlugin {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2)
            return false;

        Optional<Player> riderOptional = Bukkit.selectEntities(sender, args[0]).stream()
                .filter(Player.class::isInstance)
                .map(Player.class::cast)
                .findFirst();
        if (!riderOptional.isPresent()) {
            sender.sendMessage(new ComponentBuilder()
                    .append("[かめすたプラグイン] ").color(ChatColor.LIGHT_PURPLE)
                    .append("ライダーが見つかりません").color(ChatColor.RED)
                    .create()
            );
            return true;
        }
        Player rider = riderOptional.get();

        Optional<Player> rideeOptional = Bukkit.selectEntities(sender, args[1]).stream()
                .filter(Player.class::isInstance)
                .map(Player.class::cast)
                .findFirst();
        if (!rideeOptional.isPresent()) {
            sender.sendMessage(new ComponentBuilder()
                    .append("[かめすたプラグイン] ").color(ChatColor.LIGHT_PURPLE)
                    .append("ターゲットが見つかりません").color(ChatColor.RED)
                    .create()
            );
            return true;
        }
        Player ridee = rideeOptional.get();

        rider.setGameMode(GameMode.SPECTATOR);
        rider.teleport(ridee);
        ridee.addPassenger(rider);
        rider.setSpectatorTarget(ridee);

        sender.sendMessage(new ComponentBuilder()
                .append("[かめすたプラグイン] ").color(ChatColor.LIGHT_PURPLE)
                .append(rider.getDisplayName()).color(ChatColor.WHITE)
                .append(" を ").color(ChatColor.GREEN)
                .append(ridee.getDisplayName()).color(ChatColor.WHITE)
                .append(" に追従させました。").color(ChatColor.GREEN)
                .create()
        );
        rider.sendMessage(new ComponentBuilder()
                .append("[かめすたプラグイン] ").color(ChatColor.LIGHT_PURPLE)
                .append(ridee.getDisplayName()).color(ChatColor.WHITE)
                .append(" に追従しています。").color(ChatColor.GREEN)
                .create()
        );
        ridee.sendMessage(new ComponentBuilder()
                .append("[かめすたプラグイン] ").color(ChatColor.LIGHT_PURPLE)
                .append(rider.getDisplayName()).color(ChatColor.WHITE)
                .append(" に追従されています。").color(ChatColor.GREEN)
                .create()
        );

        return true;
    }
}
