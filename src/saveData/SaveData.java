package saveData;

import score.ScoreData;
import java.io.FileWriter;
import java.io.IOException;

public class SaveData {
    public static void saveUserData(ScoreData scoreData) {
        try {
            FileWriter writer = new FileWriter("scores.txt", true);
            writer.write(scoreData.toString() + "\n\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}