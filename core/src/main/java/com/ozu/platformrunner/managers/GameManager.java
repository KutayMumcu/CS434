package com.ozu.platformrunner.managers;

public class GameManager {
    private static GameManager instance;

    public enum GameState {
        MENU, PLAYING, PAUSED, GAME_OVER
    }

    private GameState currentState;
    private int score;
    private int currentLevel;

    private GameManager() {
        currentState = GameState.MENU;
        score = 0;
        currentLevel = 1;
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public GameState getCurrentState() { return currentState; }
    public void setCurrentState(GameState currentState) { this.currentState = currentState; }

    public int getScore() { return score; }
    public void addScore(int value) { this.score += value; }
    public void setScore(int score) { this.score = score; }

    public int getCurrentLevel() { return currentLevel; }
    public void setLevel(int level) { this.currentLevel = level; }

    public void resetGame() {
        score = 0;
        currentLevel = 1;
        currentState = GameState.MENU;
    }
}
