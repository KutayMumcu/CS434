package com.ozu.platformrunner.managers;

public class GameManager {
    // 1. Singleton Instance
    private static GameManager instance;

    // Oyun Durumları (İleride State paterni ile değiştirebiliriz ama şimdilik Enum yeterli)
    public enum GameState {
        MENU, PLAYING, PAUSED, GAME_OVER
    }

    private GameState currentState;
    private int score;
    private int currentLevel;

    // 2. Private Constructor
    private GameManager() {
        currentState = GameState.MENU;
        score = 0;
        currentLevel = 1;
    }

    // 3. Global Erişim Noktası
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    // --- Getter & Setter ---
    public GameState getCurrentState() { return currentState; }
    public void setCurrentState(GameState currentState) { this.currentState = currentState; }

    public int getScore() { return score; }
    public void addScore(int value) { this.score += value; }

    public int getCurrentLevel() { return currentLevel; }

    public void resetGame() {
        score = 0;
        currentLevel = 1;
        currentState = GameState.MENU;
    }
}
