package world;

import entity.items.ItemManager;
import map.*;
import utilz.ItemSprites;
import java.io.IOException;

public class World {
    private final BgManager bgManager;
    private final TileMap tileMap;
    private final int[][] levelGrid;
    private final ItemManager itemManager;

    public World() throws IOException {
        // ✅ Preload item sprites
        ItemSprites.preloadAll();

        bgManager = new BgManager();
        LevelBackgroundConfig.setupFactoryBackground(bgManager);

        TileSet tileSet = LevelTileConfig.createTileSet();
        levelGrid = LevelTileConfig.createLevelGrid();
        tileMap = new TileMap(levelGrid, tileSet);

        itemManager = new ItemManager();

        // ✅ Load items for level 1
        itemManager. loadItemsForLevel(1);
    }

    public BgManager getBgManager() { return bgManager; }
    public TileMap getTileMap() { return tileMap; }
    public int[][] getLevelGrid() { return levelGrid; }
    public ItemManager getItemManager() { return itemManager; }
}