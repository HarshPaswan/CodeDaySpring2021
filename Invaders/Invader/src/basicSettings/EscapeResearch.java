package basicSettings;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class EscapeResearch extends JFrame  {

    public EscapeResearch() {

        initUI();
    }
    public EscapeResearch(boolean OK) {
    	add(new Board());

        setTitle("Escape Research level 2");
        
        setSize(Coords.BOARD_WIDTH, Coords.BOARD_HEIGHT);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }
    private void initUI() {

        add(new Board());

        setTitle("Escape Research");
        setSize(Coords.BOARD_WIDTH, Coords.BOARD_HEIGHT);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var ex = new EscapeResearch();
            ex.setVisible(true);
        });
        if (Board.repeat == true) {
        	EventQueue.invokeLater(() -> {

                var ex = new EscapeResearch(true);
                ex.setVisible(true);
            });
        }
    }
}
