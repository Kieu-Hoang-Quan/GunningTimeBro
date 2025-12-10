package world;
import main.Game;
import map.*;

import java.io.IOException;

public class World {
    private final BgManager bgManager;
    private final TileMap tileMap;
    private final int[][] levelGrid;

    public World() throws IOException {
        // Background
        bgManager = new BgManager();
        LevelBackgroundConfig.setupFactoryBackground(bgManager);

        // Tile map + level data
        TileSet tileSet = LevelTileConfig.createTileSet();
        levelGrid = LevelTileConfig.createLevelGrid();
        tileMap = new TileMap(levelGrid, tileSet);
    }

    public BgManager getBgManager() {
        return bgManager;
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public int[][] getLevelGrid() {
        return levelGrid;
    }
}
