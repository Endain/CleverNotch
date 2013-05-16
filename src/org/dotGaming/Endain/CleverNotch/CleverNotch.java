/*
 *  CleverNotch. A Minecraft plugin to interface with online chat bots!
 *  Copyright (C) 2013 Endain
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dotGaming.Endain.CleverNotch;

import java.util.LinkedList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import com.sun.corba.se.impl.protocol.giopmsgheaders.MessageHandler;
import org.bukkit.Bukkit;

public class CleverNotch extends JavaPlugin implements Listener {
    // Cleverbot related classes, courtesy Chatter-Bot-API

    private ChatterBotFactory cbf;
    private ChatterBot cb;
    protected ChatterBotSession bot;
    // Chatting and queuing variables
    protected LinkedList<String> messages;
    private boolean enabled;
    protected String botName;
    protected ChatColor botColor;
    private String botSource;
    protected int minResponseDelay;
    protected long talkTick;

    @Override
    public void onEnable() {
        // Verify config file exists, create it if not
        saveDefaultConfig();
        // Initialize variables and objects
        messages = new LinkedList<String>();
        enabled = true;
        botName = getConfig().getString("botName");
        botColor = getColor(getConfig().getString("botColor"));
        botSource = getConfig().getString("botSource");
        minResponseDelay = getConfig().getInt("minResponseDelay");
        talkTick = getServer().getWorlds().get(0).getFullTime();
        // Verify parameters from config file are valid
        if (botName.length() > 20) {
            botName = botName.substring(0, 20);
        }
        if (minResponseDelay < 2) {
            minResponseDelay = 2;
        }
        if (minResponseDelay > 60) {
            minResponseDelay = 60;
        }
        // Set up Cleverbot related objects
        try {
            cbf = new ChatterBotFactory();
            // Pick a bot based on the AI source. Default to PandoraBots if invalid selection.

            // Cleverbot disabled for now! Legal issues need to be resolved!
            //if(botSource.equalsIgnoreCase("cleverbot"))
            //	cb = cbf.create(ChatterBotType.CLEVERBOT);

            if (botSource.equalsIgnoreCase("pandorabots")) {
                cb = cbf.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
            } else if (botSource.equalsIgnoreCase("jabberwacky")) {
                cb = cbf.create(ChatterBotType.JABBERWACKY);
            } else {
                cb = cbf.create(ChatterBotType.PANDORABOTS, "b0dafd24ee35a477");
            }
            bot = cb.createSession();
        } catch (Exception e) {
            // There was an error setting up the bot, stop the plugin
            getLogger().severe("CleverNotch could not initialize! It will be shut down!");
            getServer().getPluginManager().disablePlugin(this);
        }
        // Register commands
        getCommand("clevernotch").setExecutor(this);
        // Register events
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Nothing in particular to to here for now...
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("clevernotch")) {
            if (sender.isOp()) {
                // Flip the status of CleverNotch
                enabled = !enabled;
                // Notify of the new status
                if (enabled) {
                    sender.sendMessage(ChatColor.YELLOW + "CleverNotch is now " + ChatColor.GREEN + "ON");
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "CleverNotch is now " + ChatColor.RED + "OFF");
                }
            } else {
                // Notify that the user must be an OP
                sender.sendMessage(ChatColor.RED + "Only OPs can toggle CleverNotch.");
            }
        }
        // No incorrect usage since there are no parameters
        return true;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        // Check for a message sent to CleverNotch
        if (enabled) {
            if (event.getMessage().length() > botName.length()) {
                if (event.getMessage().substring(0, botName.length()).equalsIgnoreCase(botName)) {
                    String clean = cleanMessage(event.getMessage());
                    // Do some very, very basic spam prevention
                    for (String msg : messages) {
                        if (msg.equalsIgnoreCase(clean)) {
                            event.getPlayer().sendMessage(ChatColor.RED + "Please do not spam " + botColor + botName + ChatColor.RED + "!");
                            event.setCancelled(true);
                            return;
                        }
                    }
                    // Add the clean message to the queue and modify the chat message
                    messages.push(clean);
                    event.setMessage(clean);
                    // Schedule a bot query if this is the only message in the queue
                    if (messages.size() == 1 && getServer().getWorlds().get(0).getFullTime() > talkTick) {
                        talkTick = getServer().getWorlds().get(0).getFullTime() + (minResponseDelay * 20);
                        getServer().getScheduler().runTaskAsynchronously(this, new Think(this, messages.peek()));
                    }
                }
            }
        }
    }

    private String cleanMessage(String msg) {
        // Strip botname from string
        msg = msg.substring(botName.length());
        // Trim off any whitespace
        msg = msg.trim();
        // Strip remaining whitspace and punctuation
        while (Character.isWhitespace(msg.charAt(0)) || (!Character.isLetter(msg.charAt(0)) && !Character.isDigit(msg.charAt(0)))) {
            msg = msg.substring(1);
        }
        // Return the cleaned message
        return msg;
    }

    private ChatColor getColor(String color) {
        // Return a chatColor from a color string
        if (color.equalsIgnoreCase("Black")) {
            return ChatColor.BLACK;
        } else if (color.equalsIgnoreCase("DarkBlue")) {
            return ChatColor.DARK_BLUE;
        } else if (color.equalsIgnoreCase("DarkGreen")) {
            return ChatColor.DARK_GREEN;
        } else if (color.equalsIgnoreCase("DarkAqua")) {
            return ChatColor.DARK_AQUA;
        } else if (color.equalsIgnoreCase("DarkRed")) {
            return ChatColor.DARK_RED;
        } else if (color.equalsIgnoreCase("DarkPurple")) {
            return ChatColor.DARK_PURPLE;
        } else if (color.equalsIgnoreCase("Gold")) {
            return ChatColor.GOLD;
        } else if (color.equalsIgnoreCase("Gray")) {
            return ChatColor.GRAY;
        } else if (color.equalsIgnoreCase("DarkGray")) {
            return ChatColor.DARK_GRAY;
        } else if (color.equalsIgnoreCase("Blue")) {
            return ChatColor.BLUE;
        } else if (color.equalsIgnoreCase("Green")) {
            return ChatColor.GREEN;
        } else if (color.equalsIgnoreCase("Aqua")) {
            return ChatColor.AQUA;
        } else if (color.equalsIgnoreCase("Red")) {
            return ChatColor.RED;
        } else if (color.equalsIgnoreCase("LightPurple")) {
            return ChatColor.LIGHT_PURPLE;
        } else if (color.equalsIgnoreCase("Yellow")) {
            return ChatColor.YELLOW;
        } else if (color.equalsIgnoreCase("White")) {
            return ChatColor.WHITE;
        }
        // Default to magic for a bit-o-fun on improper configuration
        return ChatColor.MAGIC;
    }

    private class Think implements Runnable {

        private CleverNotch cn;
        private String msg;

        public Think(CleverNotch cn, String msg) {
            this.cn = cn;
            this.msg = msg;
        }

        @Override
        public void run() {
            // Query Cleverbot for a response
            String response = null;
            try {
                response = cn.bot.think(msg);
            } catch (Exception e) {
                cn.getLogger().info("Error querying Cleverbot, no response will be given!");
            }
            // Replace any reference to Cleverbot with our selected name
            int index = response.toLowerCase().indexOf("cleverbot");
            while (index > -1) {
                response = response.substring(0, index) + cn.botName + response.substring(index + 9);
                index = response.toLowerCase().indexOf("cleverbot");
            }
            // Schedule the response to be relayed
            long delay = cn.talkTick - cn.getServer().getWorlds().get(0).getFullTime();
            if (delay < 0) {
                delay = 0;
            }
            cn.getServer().getScheduler().runTaskLater(cn, new Talk(cn, response), delay);
        }
    }

    private class Talk implements Runnable {

        private CleverNotch cn;
        private String msg;

        public Talk(CleverNotch cn, String msg) {
            this.cn = cn;
            this.msg = msg;
        }

        @Override
        public void run() {
            // Send the response to all players
            cn.getServer().broadcastMessage("<" + cn.botColor + cn.botName + ChatColor.WHITE + "> " + msg);
            // Throw an event
            CleverEvent cleverEvent = throwCleverEvent(msg.toString());
            cleverEvent.setMessage(msg);
            // Poll the queue to mark the input as finished
            cn.messages.poll();
            // If there are more messages in the queue, process another one
            if (cn.messages.size() > 0) {
                cn.talkTick = getServer().getWorlds().get(0).getFullTime() + (cn.minResponseDelay * 20);
                cn.getServer().getScheduler().runTaskAsynchronously(cn, new Think(cn, cn.messages.poll()));
            }
        }
    }

    public static CleverEvent throwCleverEvent(String msg) {
        CleverEvent event = new CleverEvent(msg);
        Bukkit.getPluginManager().callEvent(event);
        return event;
    }
}
