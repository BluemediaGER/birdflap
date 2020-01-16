package de.traber_info.home.birdflap.entity;

public abstract class Entity {

    /**
     * Abstract function for handling render cycles.
     */
    public abstract void tick();

    /**
     * Abstract function for handling key press events.
     * @param key
     */
    public abstract void handleKeyPress(char key);

}
