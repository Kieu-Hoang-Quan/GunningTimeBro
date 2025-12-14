package ui;

import events.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ItemNotificationUI {

    private List<Notification> notifications;

    public ItemNotificationUI() {
        this.notifications = new ArrayList<>();

        EventBus eventBus = EventBus.getInstance();

        eventBus.subscribe(ItemPickupEvent.class, (ItemPickupEvent event) -> {
            String message = "Picked up: " + event.item.getName();
            notifications.add(new Notification(message, 3000, Color. YELLOW));
        });

        eventBus.subscribe(HealEvent.class, (HealEvent event) -> {
            if (event.target. getClass().getSimpleName().equals("Player")) {
                String message = "+" + (int)event.healAmount + " HP";
                notifications.add(new Notification(message, 2000, Color.GREEN));
            }
        });

        eventBus.subscribe(DeathEvent.class, (DeathEvent event) -> {
            String victimName = event.victim.getClass().getSimpleName();
            String message = victimName + " DIED";
            Color color = victimName.equals("Player") ? Color.RED : Color. ORANGE;
            notifications.add(new Notification(message, 3000, color));
        });
    }

    public void update(float deltaTime) {
        notifications.removeIf(n -> n.isExpired());
        for (Notification n : notifications) {
            n.update(deltaTime);
        }
    }

    public void render(Graphics2D g) {
        int y = 100;
        for (Notification n : notifications) {
            n.render(g, 20, y);
            y += 40;
        }
    }

    private static class Notification {
        String message;
        long startTime;
        long duration;
        float alpha;
        Color color;

        Notification(String message, long duration, Color color) {
            this. message = message;
            this. duration = duration;
            this. color = color;
            this.startTime = System.currentTimeMillis();
            this.alpha = 1.0f;
        }

        void update(float deltaTime) {
            long elapsed = System.currentTimeMillis() - startTime;
            if (elapsed > duration - 1000) {
                alpha = (duration - elapsed) / 1000f;
            }
        }

        boolean isExpired() {
            return System.currentTimeMillis() - startTime > duration;
        }

        void render(Graphics2D g, int x, int y) {
            g.setFont(new Font("Arial", Font. BOLD, 16));
            g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alpha * 255)));
            g.drawString(message, x, y);
        }
    }
}