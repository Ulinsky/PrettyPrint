import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

import static javax.swing.JOptionPane.showMessageDialog;

final class PrettyPrint {
    private static final int HEIGHT = 600;
    private static final int WIDTH = 500;
    private static final int PANE_HEIGHT = (int)(HEIGHT*0.7);
    private static final int PANE_WIDTH = (int)(WIDTH*0.6);
    private static final int TEXT_LENGTH = (int)(HEIGHT*0.05);
    private static final int MIN_TEXT_LENGTH = 3;
    private static final int MAX_TEXT_LENGTH = 20;
    private static final char EMPTY_CHAR = '\u2002';

    public static void main(String[] args) {
     SwingUtilities.invokeLater(PrettyPrint::createAndShowGUI);
    }

    private static String createPrettyPrint(String meme) {
        StringBuilder sb = new StringBuilder();
        meme = meme.toUpperCase();
        StringBuilder spacingSb = new StringBuilder();
        for (int i = 0; i < meme.length(); i++) {
            spacingSb.append(meme.charAt(i)).append(EMPTY_CHAR);
        }
        String spacedMeme = spacingSb.toString();
        sb.append(spacedMeme);
        for (int i = 1; i < spacedMeme.length(); i++) {
            if (spacedMeme.charAt(i) ==EMPTY_CHAR) continue;
            sb.append("\n").append(spacedMeme.charAt(i)).append(new String(new char[i]).replace('\0', EMPTY_CHAR)).append(spacedMeme.charAt(i));
        }
        return sb.toString();
    }

    private static void createAndShowGUI() {
        JFrame frame = createFrame();
        frame.pack();
        frame.setLocation(300, 100);
        frame.setSize(new Dimension(WIDTH, HEIGHT));
        frame.setResizable(false);
        frame.setVisible(true);
    }


    private static JFrame createFrame() {
        JFrame frame = new JFrame("Pretty Print");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JTextField memeText;
        memeText = new JTextField(TEXT_LENGTH);
        memeText.setToolTipText("Enter text here.");
        JTextPane previewPane = new JTextPane();
        previewPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        previewPane.setPreferredSize(new Dimension(PANE_WIDTH,PANE_HEIGHT));
        previewPane.setOpaque(false);
        previewPane.setEditable(false);
        JButton copyBtn = new JButton("Copy to clipboard");
        JButton prettyPrntBtn = new JButton("Make text pretty");
        prettyPrntBtn.addActionListener((ActionEvent a) -> {
            if (memeText.getText().isEmpty() || memeText.getText().length() < MIN_TEXT_LENGTH) return;
            if(memeText.getText().length()>MAX_TEXT_LENGTH) {
                showMessageDialog(null, "Exceeds max supported length: " + MAX_TEXT_LENGTH);
                return;
            }
            previewPane.setText(createPrettyPrint(memeText.getText()));
        });
        copyBtn.addActionListener((ActionEvent a) -> {
            if (previewPane.getText().isEmpty())return;
            StringSelection selection = new StringSelection(previewPane.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        });
        JPanel north = new JPanel();
        JPanel center = new JPanel();
        JPanel south = new JPanel();
        north.add(memeText);
        center.add(previewPane);
        south.add(prettyPrntBtn);
        south.add(copyBtn);
        frame.getContentPane().add(north, BorderLayout.NORTH);
        frame.getContentPane().add(center, BorderLayout.CENTER);
        frame.getContentPane().add(south, BorderLayout.SOUTH);
        return frame;
    }
}
