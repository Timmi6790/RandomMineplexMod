package de.timmi6790.basemod.utilities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskScheduler {
    @Getter
    private static final TaskScheduler instance = new TaskScheduler();

    private final List<Task> taskList = Collections.synchronizedList(new ArrayList<>());

    public TaskScheduler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void schedule(final int ticksToWait, final Runnable runnable) {
        this.taskList.add(new Task(runnable, ticksToWait));
    }

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        this.taskList.removeIf(Task::execute);
    }

    @Data
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static class Task {
        private final Runnable task;
        private int ticksLeft;

        boolean execute() {
            if (this.ticksLeft <= 0) {
                this.task.run();
                return true;
            }

            this.ticksLeft--;
            return false;
        }
    }
}
