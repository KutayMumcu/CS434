package com.ozu.platformrunner.patterns.factory;

import com.ozu.platformrunner.entities.ChasingEnemy;
import com.ozu.platformrunner.entities.Enemy;
import com.ozu.platformrunner.entities.PatrollingEnemy;

public class EnemyFactory {

    public enum EnemyType {
        PATROLLING,
        CHASING
    }

    public static Enemy createEnemy(EnemyType type, float x, float y) {
        switch (type) {
            case PATROLLING:
                return new PatrollingEnemy(x, y);
            case CHASING:
                return new ChasingEnemy(x, y);
            default:
                throw new IllegalArgumentException("Bilinmeyen düşman tipi: " + type);
        }
    }
}
