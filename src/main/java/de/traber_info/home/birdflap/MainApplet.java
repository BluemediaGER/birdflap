package de.traber_info.home.birdflap;

import de.traber_info.home.birdflap.entity.Bird;
import de.traber_info.home.birdflap.entity.Pipe;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.*;

public class MainApplet extends PApplet {

    // Instance of the bird
    private Bird bird;
    // List of pipes currently in the game
    private List<Pipe> pipes = new ArrayList<>();

    // Player score
    private int score = 0;

    // Background texture
    private PImage background;

    // Distance until next pipe is created
    private int pipeDistTracker;

    public static void main(String args[]) {
        PApplet.main("de.traber_info.home.birdflap.MainApplet");
    }

    /**
     * Set processing windows size.
     */
    public void settings() {
        size(1280, 720);
    }

    /**
     * Setup processing environment.
     */
    public void setup() {
        bird = new Bird(this);
        background = loadImage("textures/background.png");
        background.resize(width, height);
    }

    /**
     * Move and render entities in every draw cycle.
     */
    public void draw() {
        // Clear current frame
        clear();
        // Set background
        background(background);
        // Create and remove pipes
        pipeHandler();
        // Move and render pipes
        renderPipes();
        // Move and render bird
        bird.tick();
        // Render score counter
        textSize(32);
        text(score, 20, 40);
    }

    /**
     * Handle key down events by processing for jump and reset.
     */
    public void keyPressed() {
        bird.handleKeyPress(key);
        if (key == 'r' || key == 'R') {
            // Remove all pipe's
            pipes.clear();
            // Reset score
            score = 0;
            // Reset distance to next pipe
            pipeDistTracker = 0;
            // Reset bird
            bird.reset();
        }
    }

    /**
     * Render pipe's and run collision detection
     */
    public void renderPipes() {
        for (Pipe p : pipes) {

            // Move the pipe
            if (bird.isAlive()) {
                p.move();
            }

            // Render pipe
            p.tick();

            // Check if bird hits pipes
            if (bird.isAlive()) {
                if (p.collide(
                        bird.getX(),
                        bird.getY(),
                        bird.BIRD_WIDTH,
                        bird.BIRD_HEIGHT
                )) {
                    // Kill bird
                    bird.kill();
                } else {

                    // Checks if bird passes a pipe and if the pipe can award points
                    if (bird.getX() >= p.getX() + p.getWidth() / 2 && p.canAwardPoint()) {
                        // Increase score
                        score ++;
                        p.setCanAwardPoint(false);
                    }
                }
            }
        }
    }

    /**
     * Create new pipe's and remove the pipes that are out of screen
     */
    public void pipeHandler() {

        // Decrease distance between pipes
        if (bird.isAlive()) {
            pipeDistTracker --;
        }

        // Delete pipe if it's out of screen
        pipes.removeIf(p -> p.getX() < -70);

        // Check distance to next pipe. If there is no distance,
        // a new pipe is needed.
        if (pipeDistTracker <= 0) {

            // Reset distance
            pipeDistTracker = Pipe.PIPE_DISTANCE;

            // New pipe object for top and bottom pipes
            Pipe topPipe = new Pipe(this, true);
            Pipe bottomPipe = new Pipe(this, false);
            bottomPipe.setCanAwardPoint(false);

            pipes.add(topPipe);
            pipes.add(bottomPipe);

            // Set y-coordinate of bottom pipe based on
            // y-coordinate of top pipe
            bottomPipe.setY(topPipe.getY() + topPipe.getHeight() + Pipe.PIPE_SPACING);
            bottomPipe.setHeight((height - bottomPipe.getY()) + 5);
        }
    }

}
