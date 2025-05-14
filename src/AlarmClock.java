import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Scanner;

public class AlarmClock implements Runnable {

    private final LocalTime alarmTime;
    private final String filePath;

    private final Scanner scanner;

    public AlarmClock(LocalTime alarmTime, String filePath, Scanner scanner) {
        this.alarmTime = alarmTime;
        this.filePath = filePath;
        this.scanner = scanner;
    }

    @Override
    public void run() {
        while (LocalTime.now().isBefore(alarmTime)) {
            try {
                LocalTime now = LocalTime.now();
                System.out.printf("\rCurrent Time: %02d:%02d:%02d", now.getHour(), now.getMinute(), now.getSecond());
                System.out.flush();
                Thread.sleep(1000); // wait 1 second
            } catch (InterruptedException e) {
                System.out.println("Thread was interrupted");
                return;
            }
        }

        // Alarm triggered
        System.out.println("\n Alarm time reached!");
        Toolkit.getDefaultToolkit().beep();
        System.out.println("*Alarm Noise*");
        playSound(filePath);
    }

    private void playSound(String filePath) {
        File audioFile = new File(filePath);

        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile)) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            System.out.print(" Press *Enter* to stop the alarm: ");
            scanner.nextLine();
            clip.stop();
            scanner.close();
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Audio file format is not supported");
        } catch (LineUnavailableException e) {
            System.out.println("Audio line is unavailable");
        } catch (IOException e) {
            System.out.println("Error reading the audio file");
        }
    }
}
