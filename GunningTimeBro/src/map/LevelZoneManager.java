package map;
import main.Game;
import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Rectangle2D;

public class LevelZoneManager {
    private final List<LevelZone> zones = new ArrayList<>();
    private final List<LevelZoneListener> listeners = new ArrayList<>();

    private LevelZone currentZone = null;

    public void addListener(LevelZoneListener listener) {
        listeners.add(listener);
    }

    public void setupLevel1Zones() {
        zones.clear();

        zones.add(zoneByTiles(
                1,          // tile start
                116,         // width = 15 tiles
                "Level 1 "
        ));

        zones.add(zoneByTiles(
                117,
                100,
                "Level 2"
        ));

        zones.add(zoneByTiles(
                201,
                100,
                "Level 3"
        ));
    }

    public void update(Rectangle2D.Float playerHitbox) {
        for (LevelZone zone : zones) {
            if (zone.contains(playerHitbox)) {
                if (zone != currentZone) {
                    currentZone = zone;
                    notifyEnter(zone.getLevelName());
                }
                return;
            }
        }
        currentZone = null;
    }

    private void notifyEnter(String levelName) {
        for (LevelZoneListener l : listeners) {
            l.onEnterZone(levelName);
        }
    }
    private LevelZone zoneByTiles(
            int startTileX,
            int tileWidth,
            String name
    ) {
        return new LevelZone(
                startTileX * Game.TILES_SIZE,
                0,
                tileWidth * Game.TILES_SIZE,
                Game.GAME_HEIGHT,
                name
        );
    }
}
