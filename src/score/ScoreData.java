package score;

public class ScoreData {
    private String username;
    private String date;
    private int finalScore;

    public ScoreData(String username, String date, int finalScore) {
        this.username = username;
        this.date = date;
        this.finalScore = finalScore;
    }

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date;
    }

    public int getFinalScore() {
        return finalScore;
    }

    @Override
    public String toString() {
        return "Nombre de usuario: " + username + "\n" + "Fecha: " + date + "\n" + "Puntuación final: " + finalScore + "\n";
    }
}
