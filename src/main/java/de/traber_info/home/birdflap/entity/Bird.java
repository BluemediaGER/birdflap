package de.traber_info.home.birdflap.entity;

import processing.core.PApplet;
import processing.core.PImage;

public class Bird extends Entity {

    // PApplet for rendering
    private PApplet applet;

    // Bird attributes
    private float x;
    private float y;

    private boolean isAlive = true;

    // Height at which bird collides with base
    private int baseCollision;

    // Bird constants
    public final int BIRD_WIDTH       = 44;
    public final int BIRD_HEIGHT      = 31;;
    private final int SHIFT           = 10;
    private final int STARTING_BIRD_X = 100;
    private final int STARTING_BIRD_Y = 100;

    // Texture of bird
    private PImage image;

    // Physics variables
    private float velocity           = 0;
    private float gravity            = .41f;
    private float delay              = 0;

    /**
     * Create a new bird instance
     * @param applet PApplet for rendering
     */
    public Bird (PApplet applet) {
        this.applet = applet;
        this.image = applet.loadImage("textures/bird.png");
        this.image.resize(42,30);
        this.baseCollision = applet.height + BIRD_HEIGHT - 5;
        setGameStartPos();
    }

    /**
     * @return Bird's x-coordinate
     */
    public float getX () {
        return x;
    }

    /**
     * @return Bird's y-coordinate
     */
    public float getY () {
        return y;
    }

    /**
     * @return If bird is alive
     */
    public boolean isAlive () {
        return isAlive;
    }

    /**
     * Kills bird
     */
    public void kill () {
        isAlive = false;
    }

    /**
     * Set new coordinates when starting game
     */
    public void setGameStartPos () {
        x = STARTING_BIRD_X;
        y = STARTING_BIRD_Y;
    }

    /**
     * Reset bird to creation state
     */
    public void reset() {
        setGameStartPos();
        isAlive = true;
    }

    /**
     * Bird jump
     */
    private void jump () {
        if (delay < 1) {
            velocity = -SHIFT;
            delay = SHIFT;
        }
    }

    /**
     * Bird movement during the game
     */
    public void move() {

        // If the bird did not hit the base, lower it
        if (y < baseCollision) {
            // Change and velocity
            velocity += gravity;

            // Lower delay if possible
            if (delay > 0) { delay--; }

            // Add rounded velocity to y-coordinate
            if (y >= 0) y += (int) velocity;

            if (y < 0) {
                y = 0;
                velocity = 0;
            }
        } else {
            // Set state to dead
            isAlive = false;
        }
    }

    /**
     * Render bird
     */
    private void renderBird () {
        // Calculate angle to rotate bird based on y-velocity
        float rotation = (float) (((90 * (velocity + 25) / 25) - 90) * Math.PI / 180);

        // Divide for clean jump
        rotation /= 2;

        // Handle rotation offset
        rotation = rotation > Math.PI / 2 ? (float) (Math.PI / 2) : rotation;

        // Drop bird on death
        if (!isAlive() && y < baseCollision - 10) {
            velocity += gravity;
            y += (int) velocity;
        }

        // Render bird with calculated rotation
        applet.pushMatrix();
        applet.translate(x, y);
        applet.pushMatrix();
        applet.rotate(rotation);
        applet.image(image,0, 0);
        applet.popMatrix();
        applet.popMatrix();
    }

    /**
     * Override tick() from Entity for rendering in draw cycle.
     */
    @Override
    public void tick() {
        move();
        renderBird();
    }

    /**
     * Override handleKeyPress() from Entity for handling key press events (e.g. jump).
     * @param key Key that was pressed.
     */
    @Override
    public void handleKeyPress(char key) {
        if (key == ' ') jump();
    }

}
