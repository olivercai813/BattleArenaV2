package audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

/**
 * The AudioClip class defines an AudioClip object
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public class AudioClip {
    private AudioInputStream ais;
    private Clip clip;
    private URL url;

    /**
     * Creates an AudioClip with the file path of the audio
     * @param audioFilePath The filepath of the audio
     */
    public AudioClip(String audioFilePath) {
        this.url = getClass().getResource(audioFilePath);
    }

    /**
     * Sets up the audio clip
     */
    private void setup() {
        try {
            ais = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the audio clip
     */
    public void play() {
        setup();
        clip.start();
    }

    /**
     * Plays the audio clip and loops it
     */
    public void playAndLoop() {
        if (clip == null) {
            setup();
        }
        if (!clip.isActive()) {
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * Stops the audio clip
     */
    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }

    /**
     * Closes the audioclip
     */
    public void close() {
        try {
            if (clip != null) {
                clip.close();
            }
            if (ais != null) {
                ais.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
