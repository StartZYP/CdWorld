package com.ipedg.minecraft.CdWorld;

import com.ipedg.minecraft.CdWorld.Entity.CdPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class Main extends JavaPlugin implements Listener {
    private Set<CdPlayer> playerscd = new HashSet<>();
    private String CdWorld;
    private String World;
    private String NoPerms;
    private String LastTimeMsg;
    @Override
    public void onEnable() {
        File config = new File(getDataFolder() + File.separator + "config.yml");
        if (!config.exists()) {
            getConfig().options().copyDefaults(true);
        }
        saveDefaultConfig();
        ReloadConfig();
        new BukkitRunnable(){
            @Override
            public void run() {
                long time = new Date().getTime();
                for (CdPlayer cdPlayer:playerscd){
                    String playername = cdPlayer.getPlayername();
                    OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(playername);
                    if (offlinePlayer.isOnline()){
                        Player player = offlinePlayer.getPlayer();
                        if (Bukkit.getServer().getWorld(CdWorld).getPlayers().contains(player)){
                            long useTime = cdPlayer.getUseTime();
                            long cdTime = cdPlayer.getCDTime();
                            long hastime = (time - useTime) / 1000;
                            if (hastime >=cdTime){
                                playerscd.remove(cdPlayer);
                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"mv tp "+player.getName() +" "+World);
                            }else if (hastime>=(cdTime-15)){
                                player.sendMessage(LastTimeMsg.replace("{s}",(hastime-20)+"-"));
                            }
                        }
                    }else {
                        playerscd.remove(cdPlayer);
                    }
                }
            }
        }.runTaskTimer(this,0L,20L);
        new BukkitRunnable(){
            @Override
            public void run() {
                for (Player player:Bukkit.getServer().getOnlinePlayers()){
                    String worldname = player.getWorld().getName();
                    if (worldname.equalsIgnoreCase(CdWorld)){
                        String name = player.getName();
                        if (!playerscd.contains(new CdPlayer(0,0,name))){
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"mv tp "+player.getName() +" "+World);
                            player.sendMessage(NoPerms);
                        }
                    }
                }
            }
        }.runTaskTimer(this,0L,20L);

        Bukkit.getServer().getPluginManager().registerEvents(this,this);
        super.onEnable();
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()&&args.length==1&&args[0].equalsIgnoreCase("reload")){
            ReloadConfig();
            sender.sendMessage("[CdPlayer]重载成功");
        }else if (args.length==2&&sender.isOp()){
            long hastime = Integer.parseInt(args[0]);
            String playername = String.valueOf(args[1]);
            OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(playername);
            if (offlinePlayer.isOnline()){
                long time = new Date().getTime();
                Player player = offlinePlayer.getPlayer();
                playerscd.add(new CdPlayer(time,hastime,playername));
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"mv tp "+player.getName() +" "+CdWorld);
            }
        }
        return super.onCommand(sender, command, label, args);
    }

    private void ReloadConfig(){
        reloadConfig();
        CdWorld = getConfig().getString("CdWorld");
        World = getConfig().getString("World");
        NoPerms = getConfig().getString("NoPerms");
        LastTimeMsg = getConfig().getString("LastTimeMsg");
    }


}
