import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    private JPanel titlePanel;
    private JLabel titlelabel;
    private JTextArea textarea;
    private JScrollPane scrollpane;
    private JPanel textPanel;
    private JPanel buttonPanel;
    private JButton button;
    private JPanel mainPanel;
    public Window() {



        this.mainPanel = new JPanel(new BorderLayout());
        this.titlePanel = new JPanel();
        this.titlelabel = new JLabel("Enter text to identify the language");
        this.titlelabel.setFont(new Font("Arial", Font.BOLD, 16));
        this.titlePanel.add(this.titlelabel);
        this.textarea = new JTextArea();
        this.textarea.setLineWrap(true);
        this.textarea.setWrapStyleWord(true);
        this.scrollpane = new JScrollPane(this.textarea);
        this.textarea.setPreferredSize(new Dimension(600, 400));
        this.scrollpane.setPreferredSize(new Dimension(600, 400));
        this.textPanel = new JPanel();
        this.textPanel.add(this.scrollpane);
        this.buttonPanel = new JPanel();
        this.button = new JButton("Identify");
        this.button.addActionListener(e -> {
            String text = this.textarea.getText();
            String result = String.join("\n",Main.predictLanguage(text));
            JOptionPane.showMessageDialog(this,"Identified language:\n"+ result,
                                        "Result",JOptionPane.INFORMATION_MESSAGE);
            this.textarea.setText("");
        });
        this.buttonPanel.add(this.button);
        this.mainPanel.add(this.titlePanel, BorderLayout.NORTH);
        this.mainPanel.add(this.textPanel, BorderLayout.CENTER);
        this.mainPanel.add(this.buttonPanel, BorderLayout.SOUTH);

        this.setSize(800, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        pack();
        setVisible(true);
    }
}
