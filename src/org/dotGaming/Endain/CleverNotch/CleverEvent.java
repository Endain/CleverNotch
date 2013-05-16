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
    private String format;
    private String bukkitFormat;

    protected CleverEvent(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return this.msg;
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getBukkitFormat() {
        return this.bukkitFormat;
    }

    public void setBukkitFormat(String bukkitFormat) {
        this.bukkitFormat = bukkitFormat;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}