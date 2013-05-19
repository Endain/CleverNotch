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
 *  
 *  SPECIAL THANKS: cnaude (For purpleIRC support)
 */
package org.dotGaming.Endain.CleverNotch;

import org.bukkit.ChatColor;

public class CleverConfig {
	// Plugin reference
	private CleverNotch plugin;
	// Config variables
	private String botName;
	private String chatStyle;
	private String trigger;
	private String botSource;
	private int minResponseDelay;
	
	public CleverConfig(CleverNotch plugin) {
		this.plugin = plugin;
		// Load up the config data
		botName = plugin.getConfig().getString("botName");
		chatStyle = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("chatStyle").replace("{botName}", botName));
		trigger = plugin.getConfig().getString("trigger").replace("{botName}", botName);
		botSource = plugin.getConfig().getString("botSource");
		minResponseDelay = plugin.getConfig().getInt("minResponseDelay");
		if(minResponseDelay < 2)
			minResponseDelay = 2;
		if(minResponseDelay > 60)
			minResponseDelay = 60;
	}
	
	public String getBotName() {
		return botName;
	}
	
	public String getChatStyle() {
		return chatStyle;
	}
	
	public String getTrigger() {
		return trigger;
	}
	
	public String getBotSource() {
		return botSource;
	}
	
	public int getMinResponseDelay() {
		return minResponseDelay;
	}
}
