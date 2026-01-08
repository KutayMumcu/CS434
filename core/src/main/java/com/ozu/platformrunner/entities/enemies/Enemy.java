package com.ozu.platformrunner.entities.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ozu.platformrunner.entities.Platform;
import com.ozu.platformrunner.entities.Player;
import com.ozu.platformrunner.managers.ResourceManager;
import com.ozu.platformrunner.utils.GameConstants;

public abstract class Enemy {
    protected Rectangle bounds;
    protected Vector2 velocity;
    protected boolean onGround = false;

    protected float speed = 100f;
    protected static final float JUMP_VELOCITY = 400f;

    protected float jumpCooldown = 1.0f;
    protected float jumpCooldownTimer = 0f;
    private Array<Platform> currentPlatforms;

    protected int maxHealth;
    protected int currentHealth;

    protected Texture textureRight;
    protected Texture textureLeft;
    protected Texture currentTexture;
    private Texture whitePixelTexture;

    public Enemy(float x, float y, float width, float height, int health) {
        this.bounds = new Rectangle(x, y, width, height);
        this.velocity = new Vector2(0, 0);
        this.maxHealth = health;
        this.currentHealth = health;

        textureRight = ResourceManager.getInstance().getTexture(ResourceManager.TEXTURE_ENEMY_RIGHT);
        textureLeft = ResourceManager.getInstance().getTexture(ResourceManager.TEXTURE_ENEMY_LEFT);
        currentTexture = textureRight;
        whitePixelTexture = ResourceManager.getInstance().getTexture(ResourceManager.TEXTURE_WHITE_PIXEL);
    }

    public void update(float delta, Player player, Array<Platform> platforms) {
        this.currentPlatforms = platforms;
        if (jumpCooldownTimer > 0) jumpCooldownTimer -= delta;

        // Physics
        if (!onGround) velocity.y += GameConstants.GRAVITY * delta;

        moveBehavior(delta, player);

        bounds.x += velocity.x * delta;
        bounds.y += velocity.y * delta;

        // Collision
        onGround = false;
        for (Platform platform : platforms) {
            if (Intersector.overlaps(bounds, platform.getBounds())) {
                if (velocity.y < 0 && bounds.y > platform.getBounds().y) {
                    bounds.y = platform.getBounds().y + platform.getBounds().height;
                    velocity.y = 0;
                    onGround = true;
                }
            }
        }

        if (bounds.y < -50) currentHealth = 0;
    }

    protected abstract void moveBehavior(float delta, Player player);

    // --- AI Helper Methods ---

    protected void jump() {
        if (onGround && jumpCooldownTimer <= 0) {
            velocity.y = JUMP_VELOCITY;
            onGround = false;
            jumpCooldownTimer = jumpCooldown;
        }
    }

    protected boolean shouldJumpToPlayer(Player player, float horizontalRange, float verticalThreshold) {
        float horizontalDistance = Math.abs(player.getBounds().x - bounds.x);
        float verticalDistance = player.getBounds().y - bounds.y;

        return horizontalDistance < horizontalRange &&
            verticalDistance > verticalThreshold &&
            verticalDistance < 200f &&
            onGround &&
            jumpCooldownTimer <= 0;
    }

    protected boolean isEdgeAhead(int direction, Array<Platform> platforms) {
        if (!onGround) return false;

        float checkDistance = bounds.width * 0.6f;
        float checkX = bounds.x + (direction > 0 ? bounds.width + checkDistance : -checkDistance);
        float checkY = bounds.y - 10f;

        for (Platform platform : platforms) {
            Rectangle pBounds = platform.getBounds();
            if (checkX >= pBounds.x && checkX <= pBounds.x + pBounds.width &&
                checkY >= pBounds.y - 20 && checkY <= pBounds.y + pBounds.height) {
                return false;
            }
        }
        return true;
    }

    protected Array<Platform> getPlatforms() { return currentPlatforms; }

    // --- Rendering ---

    public void draw(SpriteBatch batch) {
        if (velocity.x > 0) currentTexture = textureRight;
        else if (velocity.x < 0) currentTexture = textureLeft;

        batch.draw(currentTexture, bounds.x, bounds.y, bounds.width, bounds.height);
        drawHealthBar(batch);
    }

    private void drawHealthBar(SpriteBatch batch) {
        float barX = bounds.x;
        float barY = bounds.y + bounds.height + 5;

        batch.setColor(Color.RED);
        batch.draw(whitePixelTexture, barX, barY, bounds.width, 5);

        float healthPercentage = (float) currentHealth / maxHealth;
        batch.setColor(Color.GREEN);
        batch.draw(whitePixelTexture, barX, barY, bounds.width * healthPercentage, 5);

        batch.setColor(Color.WHITE);
    }

    public void takeDamage(int amount) {
        currentHealth -= amount;
        if (currentHealth < 0) currentHealth = 0;
    }

    public boolean isDead() { return currentHealth <= 0; }
    public Rectangle getBounds() { return bounds; }
    public Vector2 getVelocity() { return velocity; }
    public int getCurrentHealth() { return currentHealth; }
    public void setHealth(int health) { this.currentHealth = health; }
}
