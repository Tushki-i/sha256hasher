import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class GUI {
    JFrame frame = new JFrame("SHA-256");
    JPanel panel = new JPanel();
    JTextArea messageArea = new JTextArea();
    JTextArea outputField = new JTextArea();
    Font fontA = Font.createFont(Font.TRUETYPE_FONT,new File("resources/SHPinscher-Regular.otf")).deriveFont(18f);
    Font fontB = Font.createFont(Font.TRUETYPE_FONT,new File("resources/SHPinscher-Regular.otf")).deriveFont(24f);

    public GUI() throws IOException, FontFormatException {
        frame.setSize(900, 400);
        resize();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setIconImage(Toolkit.getDefaultToolkit().createImage("resources/IconImage.png"));

        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        components();

        frame.add(panel);

        outputField.setText(Operations.hash(messageArea.getText()));
        frame.setVisible(true);
    }

    public void components() {
        panel.setBackground(Color.BLACK);

        outputField.setEditable(false);
        outputField.setBorder(BorderFactory.createEmptyBorder());
        outputField.setOpaque(false);
        outputField.setFont(fontB);
        outputField.setForeground(Color.lightGray);
        outputField.setLineWrap(true);

        messageArea.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        messageArea.setOpaque(false);
        messageArea.setLineWrap(true);
        messageArea.setFont(fontA);
        messageArea.setForeground(Color.lightGray);

        frame.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                resize();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });

        messageArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                outputField.setText(Operations.hash(messageArea.getText()));
            }

            @Override
            public void keyPressed(KeyEvent e) {
                outputField.setText(Operations.hash(messageArea.getText()));
            }

            @Override
            public void keyReleased(KeyEvent e) {
                outputField.setText(Operations.hash(messageArea.getText()));
            }
        });

        panel.add(outputField);
        panel.add(messageArea);
    }

    public void resize() {
        String pos = String.valueOf(frame.getSize()).replace("java.awt.Dimension[width=", "x").replace(",height=","y").replace("]","");
        int x = Integer.parseInt(pos.substring(1, pos.indexOf("y")));
        int y = Integer.parseInt(pos.substring(pos.indexOf("y") + 1));

        fontA = fontA.deriveFont(Font.PLAIN, x / 100 + y / 100 + 10);
        fontB = fontB.deriveFont(Font.PLAIN, x / 75 + y / 75 + 10);

        messageArea.setFont(fontA);
        outputField.setFont(fontB);

        messageArea.setBounds(20, 20, x - 60, y - 120);
        outputField.setBounds(20, y - 100, x, 60);
    }

    public static void main(String[] args) throws IOException, FontFormatException {
        new GUI();
    }
}
