
    import java.io.File;
    import java.io.IOException;
    import java.net.MalformedURLException;
    import javax.sound.sampled.AudioInputStream;
    import javax.sound.sampled.AudioSystem;
    import javax.sound.sampled.Clip;
    import javax.sound.sampled.LineUnavailableException;
    import javax.sound.sampled.UnsupportedAudioFileException;

    public class Sound {
    	//Takes a sound clip given and plays it.
        private Clip clip;
        public Sound(String fileName) {
        		fileName = System.getProperty("user.dir") + "\\src\\" + fileName;
                try {
                    File file = new File(fileName);
                    if (file.exists()) {
                        AudioInputStream sound = AudioSystem.getAudioInputStream(file);
                        clip = AudioSystem.getClip();
                        clip.open(sound);
                    }
                    else {
                        throw new RuntimeException("Sound: file not found: " + fileName);
                    }
                }
                catch (MalformedURLException e) {
                    throw new RuntimeException("Sound: Malformed URL: " + e);
                }
                catch (UnsupportedAudioFileException e) {
                    throw new RuntimeException("Sound: Unsupported Audio File: " + e);
                }
                catch (IOException e) {
                    throw new RuntimeException("Sound: Input/Output Error: " + e);
                }
                catch (LineUnavailableException e) {
                    throw new RuntimeException("Sound: Line Unavailable: " + e);
                }
        }
        //Different commands for the sound clip.
        public void play(){
            clip.setFramePosition(0);  // Must always rewind!
            clip.loop(0);
            clip.start();
        }
        public void loop(){
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        public void stop(){
            clip.stop();
        }
    }