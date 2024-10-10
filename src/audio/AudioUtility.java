package audio;

/**
 * The AudioUtility class provides useful utility functions for managing audio
 * @author Oliver Cai
 * @version 1.0 June 12, 2023
 */
public class AudioUtility {
    private static AudioClip audioClips[] = new AudioClip[2];

    /**
     * Creates all audio clips
     */
    static {
        audioClips[0] = new AudioClip("/sounds/MenuMusic.wav");
        audioClips[1] = new AudioClip("/sounds/GameMusic.wav");
    }

    /**
     * Closes all audio
     */
    public static void closeAllAudio() {
        for (AudioClip audio : audioClips) {
            if (audio != null) {
                audio.close();
            }
        }
    }

    /**
     * Plays menu music
     */
    public static void playMenuMusic() {
        audioClips[0].playAndLoop();
    }

    /**
     * Stops menu music
     */
    public static void stopMenuMusic() {
        audioClips[0].stop();
    }

    /**
     * Plays game music
     */
    public static void playGameMusic() {
        audioClips[1].playAndLoop();
    }

    /**
     * Stops game music
     */
    public static void stopGameMusic() {
        audioClips[1].stop();
    }
}
