package de.timmi6790.mcmod.tabsupport;


import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class TabSupportData {
    private final String message;
    private final String[] messageArgs;
    private List<String> tabReturn;

    public TabSupportData(final String message, final List<String> tabReturn) {
        this.message = message == null ? "" : message;
        this.tabReturn = new ArrayList<>(tabReturn);

        String[] args = this.message.trim().split("\\s+");
        if (this.message.endsWith(" ")) {
            args = Arrays.copyOf(args, args.length + 1);
            args[args.length - 1] = " ";
        }
        this.messageArgs = args;
    }

    public boolean isCommand() {
        return this.messageArgs[0].startsWith("/");
    }

    public String[] getMessageArgs() {
        return this.messageArgs.clone();
    }
}
