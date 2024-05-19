package main.java.menu;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioManager {
    private static AudioManager instance;

    private AudioManager() {
    }

    public static synchronized AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }
}

