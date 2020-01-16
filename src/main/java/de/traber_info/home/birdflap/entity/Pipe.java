package de.traber_info.home.birdflap.entity;

import processing.core.PApplet;
import processing.core.PImage;

public class Pipe extends Entity {

    // PApplet for rendering
    private PApplet applet;

    // Pipe coordinates
    private int x;
    private int y;

    // Placement (top or bottom) of pipe
    private boolean top;

    // Pipe dimensions
    private int width = 67;
    private int height = 100 + (int) ((1 + 400 - 100) * Math.random());

    // Pipe constants
    public static final int PIPE_DISTANCE = 170; // Horizontal distance between pipes
    public static final int PIPE_SPACING = 200; // Vertical distance between pipes
    private static final int SPEED        = -2;

    // Pipe texture
    private PImage image;

    // If the bird can get a point passing this pipe
    private boolean canAwardPoint = true;

    /**
     * Create new pipe instance
     * @param applet PApplet that should be rendered to
     * @param top True if the pipe is a top pipe false for a bottom pipe
     */
    public Pipe(PApplet applet, boolean top) {
        this.applet = applet;
        this.top = top;
        reset();
    }

    /**
     * Reset pipe placement and generate random height
     */
    public void reset() {
        x = applet.width + 5; // Reset x-coordinate

        // Set boundaries for top pipes
        // This y-coordinte + PIPE_SPACING will be for the bottom pipe
        if (!top) {
            y = Math.max((int) (Math.random() * 320) + 30, 140);
        }
    }

    /**
     * Moves the pipe
     */
    public void move() {
        x += SPEED;
    }

    /**
     * Render pipe after move()
     */
    private void render() {
        if (image == null) {
            if (top) {
                image = applet.loadImage("textures/pipe_top.png");
            } else {
                image = applet.loadImage("textures/pipe_bottom.png");
            }
            image.resize(width, height);
        }
        applet.image(image, x, y);
    }


    /**
     * Checks for bird colliding with pipe
     * @param nX Bird x-coordinate
     * @param nY Bird y-coordinate
     * @param nW Bird width
     * @param nH Bird height
     * @return If bird is colliding with the pipe
     */
    public boolean collide(float nX, float nY, int nW, int nH) {

        // Do not allow bird to jump over pipe
        if (nX > x && nY < 0 && canAwardPoint) {
            return true;
        }

        return nX < x + width &&
                nX + nW > x &&
                nY < y + height &&
                nY + nH > y;

    }

    /**
     * @return Pipe's x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * @return Pipe's y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * @return Pipe's width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Set's pipe's width
     * @param width New width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return Pipe's height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set's pipe's height
     * @param height New height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Set's pipe's y-coordinate (for bottom pipes)
     * @param newY New y-coordinate
     */
    public void setY(int newY) {
        y = newY;
    }

    /**
     * @return If pipe can award points
     */
    public boolean canAwardPoint() {
        return canAwardPoint;
    }

    /**
     * Set in pipe can award points
     * @param canAwardPoint True if the pipe can award points otherwise false
     */
    public void setCanAwardPoint(boolean canAwardPoint) {
        this.canAwardPoint = canAwardPoint;
    }

    /**
     * Override for rendering in draw() cycle
     */
    @Override
    public void tick() {
        render();
    }

    /**
     * Override for key events. Not used by pipe.
     * @param key Key that was pressed.
     */
    @Override
    public void handleKeyPress(char key) {

    }

}
