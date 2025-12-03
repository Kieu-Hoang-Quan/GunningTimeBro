package gamestates;

public enum GameState {
    MENU,
    PLAYING,
    OPTIONS,
    QUIT;

    public static GameState currentState = MENU;

    public static void changeState(GameState newState) {
        currentState = newState;
    }
}
