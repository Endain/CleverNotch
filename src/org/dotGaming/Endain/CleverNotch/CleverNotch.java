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

public class CleverNotch extends JavaPlugin implements Listener {
	// Cleverbot related classes, courtesy Chatter-Bot-API
	private ChatterBotFactory cbf;
	private ChatterBot cb;
	protected ChatterBotSession bot;
	// Chatting and queuing variables
	protected LinkedList<String> messages;
	private boolean enabled;
	protected String botName;
	
	@Override
	public void onEnable() {
		// Verify config file exists, create it if not
		saveDefaultConfig();
		// Set up Cleverbot related objects
		try {
			cbf = new ChatterBotFactory();
			cb = cbf.create(ChatterBotType.CLEVERBOT);
			bot = cb.createSession();
		} catch(Exception e) {
			// There was an error setting up the bot, stop the plugin
			getLogger().severe("CleverNotch could not initialize! It will be shut down!");
			getServer().getPluginManager().disablePlugin(this);
		}
		// Initialize other variables and objects
		messages = new LinkedList<String>();
		enabled = true;
		botName = getConfig().getString("botName");
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
		if(command.getName().equalsIgnoreCase("clevernotch")) {
			if(sender.isOp()) {
				// Flip the status of CleverNotch
				enabled = !enabled;
				// Notify of the new status
				if(enabled)
					sender.sendMessage(ChatColor.YELLOW + "CleverNotch is now " + ChatColor.GREEN + "ON.");
				else
					sender.sendMessage(ChatColor.YELLOW + "CleverNotch is now " + ChatColor.RED + "OFF.");
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
		if(enabled) {
			if(event.getMessage().length() > botName.length()) {
				if(event.getMessage().substring(0, botName.length()).equalsIgnoreCase(botName)) {
					messages.push(cleanMessage(event.getMessage()));
					// Schedule a bot query if this is the only message in the queue
					if(messages.size() == 1) {
						getServer().getScheduler().runTaskAsynchronously(this, new Think(this, messages.poll()));
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
		while(Character.isWhitespace(msg.charAt(0)) || (!Character.isLetter(msg.charAt(0)) && !Character.isDigit(msg.charAt(0))))
			msg = msg.substring(1);
		// Return the cleaned message
		return msg;
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
			while(index > -1) {
				response = response.substring(0, index) + cn.botName + response.substring(index + 9);
				index = response.toLowerCase().indexOf("cleverbot");
			}
			// Schedule the response to be relayed
			cn.getServer().getScheduler().runTask(cn, new Talk(cn, response));
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
			cn.getServer().broadcastMessage(ChatColor.YELLOW + cn.botName + ChatColor.WHITE + ": " + msg);
			// If there are more messages in the queue, process another one
			if(cn.messages.size() > 0)
				cn.getServer().getScheduler().runTaskAsynchronously(cn, new Think(cn, cn.messages.poll()));
		}
		
	}
}
