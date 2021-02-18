package de.timmi6790.basemod.utilities;

import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

import java.util.Set;

public class MessageBuilder {
    private final IChatComponent messageObject;

    public MessageBuilder(final String message) {
        this.messageObject = new ChatComponentText(message);
    }

    public MessageBuilder(final String message, final EnumChatFormatting colour) {
        this.messageObject = new ChatComponentText(message);
        this.messageObject.getChatStyle().setColor(colour);
    }

    public MessageBuilder addMessage(final IChatComponent chatComponent) {
        this.messageObject.appendSibling(chatComponent);
        return this;
    }

    public MessageBuilder addMessage(final MessageBuilder messageBuilder) {
        this.messageObject.appendSibling(messageBuilder.build());
        return this;
    }

    public MessageBuilder addMessage(final String message) {
        return this.addMessage(message, this.messageObject.getChatStyle().getColor());
    }

    public MessageBuilder addMessage(final EnumChatFormatting colour, final String format, final Object... objects) {
        return this.addMessage(String.format(format, objects), colour);
    }

    public MessageBuilder addMessage(final String message, final EnumChatFormatting colour) {
        final IChatComponent messageNew = new ChatComponentText(message);
        messageNew.getChatStyle().setColor(colour);
        this.messageObject.appendSibling(messageNew);
        return this;
    }

    public MessageBuilder addMessages(final Set<String> messages, final String splitter) {
        return this.addMessages(messages.toArray(new String[0]), splitter);
    }

    public MessageBuilder addMessages(final String[] messages, final String splitter) {
        if (messages.length == 0) {
            return this;
        }

        for (int index = 0; messages.length - 1 > index; index++) {
            this.messageObject.appendSibling(new ChatComponentText(messages[index] + splitter));
        }
        this.messageObject.appendSibling(new ChatComponentText(messages[messages.length - 1]));

        return this;
    }

    public MessageBuilder addHoverEvent(final HoverEvent.Action action, final IChatComponent hoverMessage) {
        this.messageObject.getChatStyle().setChatHoverEvent(new HoverEvent(action, hoverMessage));
        return this;
    }

    public MessageBuilder addHoverEvent(final HoverEvent.Action action, final MessageBuilder hoverMessage) {
        if (hoverMessage == null) {
            return this;
        }

        return this.addHoverEvent(action, hoverMessage.build());
    }

    public MessageBuilder addClickEvent(final ClickEvent.Action action, final String value) {
        this.messageObject.getChatStyle().setChatClickEvent(new ClickEvent(action, value));
        return this;
    }

    public MessageBuilder setBold() {
        this.messageObject.getChatStyle().setBold(true);
        return this;
    }

    public MessageBuilder setItalic() {
        this.messageObject.getChatStyle().setItalic(true);
        return this;
    }

    public MessageBuilder setStrikethrough() {
        this.messageObject.getChatStyle().setStrikethrough(true);
        return this;
    }

    public MessageBuilder setObfuscated() {
        this.messageObject.getChatStyle().setObfuscated(true);
        return this;
    }

    public MessageBuilder setUnderlined() {
        this.messageObject.getChatStyle().setUnderlined(true);
        return this;
    }

    public MessageBuilder setInsertion(final String insertion) {
        this.messageObject.getChatStyle().setInsertion(insertion);
        return this;
    }

    public MessageBuilder setColour(final EnumChatFormatting colour) {
        this.messageObject.getChatStyle().setColor(colour);
        return this;
    }

    public IChatComponent build() {
        return this.messageObject;
    }

    public void sendToPlayer() {
        Shortcuts.getPlayer().ifPresent(player -> player.addChatMessage(this.messageObject));
    }

    public MessageBuilder addBoxToMessage() {
        return this.addBoxToMessage(EnumChatFormatting.GREEN);
    }

    public MessageBuilder addBoxToMessage(final EnumChatFormatting colour) {
        return new MessageBuilder("")
                .addMessage(new MessageBuilder("\n=============================================\n\n", colour)
                        .setStrikethrough()
                        .setBold())
                .addMessage(this.messageObject)
                .addMessage(new MessageBuilder("\n\n=============================================\n", colour)
                        .setStrikethrough()
                        .setBold());
    }

    public void sendToPlayerDelayed(final int tickDelay) {
        TaskScheduler.getInstance().schedule(tickDelay, MessageBuilder.this::sendToPlayer);
    }
}
