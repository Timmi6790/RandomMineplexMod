package de.timmi6790.basemod.tabsupport;


import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data
public class TabSupportData {
    private final String message;
    private final String[] messageArgs;
    private final List<String> tabReturn;
    
    public TabSupportData(final String message, final List<String> tabReturn) {
        this.message = message == null ? "" : message;
        this.tabReturn = new ArrayList<>(tabReturn);
        this.messageArgs = this.parseMessageToArgs(this.message);
    }

    private String[] parseMessageToArgs(final String message) {
        String[] args = message.trim().split("\\s+");
        // Add a new entry if the message ends with space. Indicating a new argument
        if (message.endsWith(" ")) {
            args = Arrays.copyOf(args, args.length + 1);
            args[args.length - 1] = " ";
        }
        return args;
    }

    public String getCommand() {
        return this.messageArgs[0];
    }

    public boolean isCommand() {
        return this.getCommand().startsWith("/");
    }

    public String[] getMessageArgs() {
        return this.messageArgs.clone();
    }

    public void setTabReturn(final List<String> newTabReturn) {
        this.tabReturn.clear();
        this.addTabReturn(newTabReturn);
    }

    public void addTabReturn(final String... options) {
        this.addTabReturn(Arrays.asList(options));
    }

    public void addTabReturn(final Collection<String> options) {
        this.tabReturn.addAll(options);
    }
}
