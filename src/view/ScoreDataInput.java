package view;
import score.ScoreData;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoreDataInput extends JDialog {
    private JTextField usernameField;
    private JButton saveButton;
    private ScoreData scoreData;

    public ScoreDataInput(JFrame parent, int finalScore) {
        super(parent, "Ingresar Información", true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(parent);
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JLabel usernameLabel = new JLabel("Nombre de Usuario:");
        usernameField = new JTextField();
        JLabel scoreLabel = new JLabel("Puntuación Final: " + finalScore);
        saveButton = new JButton("Guardar");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(scoreLabel);
        panel.add(saveButton);
        add(panel, BorderLayout.CENTER);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                scoreData = new ScoreData(username, date, finalScore);
                dispose();
            }
        });
    }

    public ScoreData getScoreData() {
        return scoreData;
    }
}
