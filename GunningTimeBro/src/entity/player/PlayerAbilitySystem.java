package entity.player;

import java.util.HashMap;
import java.util.Map;

public class PlayerAbilitySystem {
    private Map<String, Integer> abilityCharges;
    private Map<String, Integer> maxCharges;

    public PlayerAbilitySystem() {
        this.abilityCharges = new HashMap<>();
        this.maxCharges = new HashMap<>();

        // ✅ Only Lightning ability
        registerAbility("LIGHTNING", 0, 3);
    }

    public void registerAbility(String abilityName, int initialCharges, int max) {
        abilityCharges.put(abilityName, initialCharges);
        maxCharges.put(abilityName, max);
    }

    public void addCharges(String abilityName, int amount) {
        int current = abilityCharges.getOrDefault(abilityName, 0);
        int max = maxCharges.getOrDefault(abilityName, 999);
        abilityCharges.put(abilityName, Math.min(current + amount, max));
    }

    public boolean useCharge(String abilityName) {
        int current = abilityCharges.getOrDefault(abilityName, 0);
        if (current <= 0) return false;
        abilityCharges.put(abilityName, current - 1);
        return true;
    }

    public int getCharges(String abilityName) {
        return abilityCharges.getOrDefault(abilityName, 0);
    }

    // Lightning methods
    public boolean addLightningCharge() {
        int current = getLightningCharges();
        int max = maxCharges.getOrDefault("LIGHTNING", 3);

        if (current >= max) {
            return false;
        }

        addCharges("LIGHTNING", 1);
        return true;
    }

    public boolean useLightning() {
        return useCharge("LIGHTNING");
    }

    public int getLightningCharges() {
        return getCharges("LIGHTNING");
    }
}