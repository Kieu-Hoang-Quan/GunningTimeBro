package entity.player;

import java. util.HashMap;
import java.util.Map;

public class PlayerBuffSystem {

    private Map<String, BuffData> activeBuffs;

    public PlayerBuffSystem() {
        this.activeBuffs = new HashMap<>();
    }

    public void addBuff(String buffName, float duration, float value) {
        if (activeBuffs.containsKey(buffName)) {
            BuffData existing = activeBuffs.get(buffName);
            existing.remainingTime = Math.max(existing.remainingTime, duration);
            System.out.println("[Buff] Extended " + buffName + " to " + existing.remainingTime + "s");
        } else {
            activeBuffs.put(buffName, new BuffData(duration, value));
            System.out.println("[Buff] Activated " + buffName + " for " + duration + "s (value: " + value + ")");
        }
    }

    public void update(float deltaTime) {
        activeBuffs.entrySet().removeIf(entry -> {
            BuffData buff = entry.getValue();
            buff.remainingTime -= deltaTime;

            if (buff.remainingTime <= 0) {
                System.out.println("[Buff] EXPIRED: " + entry.getKey());
                return true;
            }
            return false;
        });
    }

    public boolean hasBuff(String buffName) {
        return activeBuffs. containsKey(buffName);
    }

    public float getBuffValue(String buffName) {
        BuffData buff = activeBuffs.get(buffName);
        if (buff == null) {
            return 0f;
        }
        return buff.value;
    }

    public float getTotalBuffValue(String buffName) {
        float total = 0f;
        for (Map.Entry<String, BuffData> entry : activeBuffs.entrySet()) {
            if (entry.getKey().startsWith(buffName)) {
                total += entry.getValue().value;
            }
        }
        return total;
    }

    public float getRemainingTime(String buffName) {
        BuffData buff = activeBuffs.get(buffName);
        if (buff == null) {
            return 0f;
        }
        return buff.remainingTime;
    }

    public void clear() {
        activeBuffs. clear();
    }

    public String[] getActiveBuffs() {
        return activeBuffs.keySet().toArray(new String[0]);
    }

    private static class BuffData {
        float remainingTime;
        float value;

        BuffData(float duration, float value) {
            this.remainingTime = duration;
            this.value = value;
        }
    }
}