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

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CleverEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private String msg;
    private String botName;

    protected CleverEvent(String msg, String botName) {
        this.msg = msg;
        this.botName = botName;
    }

    public String getMessage() {
        return this.msg;
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return this.botName;
    }

    public void setName(String botName) {
        this.botName = botName;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}