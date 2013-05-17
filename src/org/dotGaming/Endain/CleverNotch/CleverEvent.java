/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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