package main.java.vue;

public class AudioManagerTest {
    public static void main(String[] args) {
        AudioManager audioManager = AudioManager.getInstance();
        audioManager.playSound("sample-file-1.wav");
    }
}

