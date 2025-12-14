package entity.items;

import main.Game;
import utilz.ItemSprites;
import java.awt.image.BufferedImage;

public class LevelItemConfig {

    public static class ItemSpawnPoint {
        public final Item item;
        public final BufferedImage sprite;
        public final float x;
        public final float y;

        // ✅ Spawn by PIXEL coordinates
        public ItemSpawnPoint(Item item, String spritePath, float x, float y) {
            this.item = item;
            this.sprite = ItemSprites.getSprite(spritePath);
            this.x = x;
            this.y = y;
        }

        // ✅ FIX: Spawn by TILE coordinates - Correct Y calculation
        public ItemSpawnPoint(Item item, String spritePath, int tileX, int tileY) {
            this.item = item;
            this.sprite = ItemSprites.getSprite(spritePath);
            this.x = tileX * Game.TILES_SIZE;

            // ✅ FIX: Spawn ABOVE the tile, not AT the tile
            // Item size is 32 * SCALE, so subtract from tile position
            this.y = (tileY * Game.TILES_SIZE) - (32 * Game.SCALE);
        }
    }

    public static ItemSpawnPoint[] getLevel1Items() {
        return new ItemSpawnPoint[] {
                // ✅ Health potions on ground level (row 11 is ground)
                new ItemSpawnPoint(
                        new HealthPotionItem(),
                        ItemSprites. Paths.HEALTH_POTION,
                        15, 8  // Spawn ON ground tile row 11
                ),

                new ItemSpawnPoint(
                        new HealthPotionItem(),
                        ItemSprites. Paths.HEALTH_POTION,
                        125, 8
                ),

                // ✅ Lightning power on platform (adjust based on your map)
                new ItemSpawnPoint(
                        new LightningPowerItem(),
                        ItemSprites.Paths.LIGHTNING_POWER,
                        40, 8   // Platform at row 8
                ),

                // ✅ Damage boost
                new ItemSpawnPoint(
                        new DamageBoostItem(),
                        ItemSprites.Paths.DAMAGE_BOOST,
                        30, 8  // On ground
                ),
        };
    }

    public static ItemSpawnPoint[] getItemsForLevel(int levelNumber) {
        switch (levelNumber) {
            case 1:
                return getLevel1Items();
            default:
                return new ItemSpawnPoint[0];
        }
    }
}