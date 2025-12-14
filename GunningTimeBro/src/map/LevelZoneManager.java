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

        zones.add(new LevelZone(
                0,
                0,
                500 * Game.SCALE,
                Game.GAME_HEIGHT,
                "Level 1"
        ));

        zones.add(new LevelZone(
                2500 * Game.SCALE,
                0,
                1000 * Game.SCALE,
                Game.GAME_HEIGHT,
                "Level 2"
        ));

        zones.add(new LevelZone(
                3500 * Game.SCALE,
                0,
                1000 * Game.SCALE,
                Game.GAME_HEIGHT,
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
}
