import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Othello extends JFrame implements ActionListener {
    private JButton[][] button = new JButton[8][8];
    private boolean blacksTurn = true;
    private static final int NORTH = 1;
    private static final int NE = 2;
    private static final int EAST = 3;
    private static final int SE = 4;
    private static final int SOUTH = 5;
    private static final int SW = 6;
    private static final int WEST = 7;
    private static final int NW = 8;
    private static Othello othello;

    public static void main(String[] args) {
        othello = new Othello();
    }

    private Othello() {
        super("Black's Turn");

        setSize(640, 640);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                int result = JOptionPane.showConfirmDialog(othello, "Are you sure you want to exit the game?", "Exit game", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    othello.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    othello.setVisible(false);
                    othello.dispose();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        GridLayout grid = new GridLayout(8, 8);
        setLayout(grid);

        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                button[i][j] = new JButton();
                button[i][j].addActionListener(this);
                add(button[i][j]);
            }
        }

        setVisible(true);
        startGame();
    }


    private void startGame() {
        button[3][3].setBackground(Color.white);
        button[3][4].setBackground(Color.black);
        button[4][3].setBackground(Color.black);
        button[4][4].setBackground(Color.white);

        blacksTurn = true;
        markValidMoves();
    }

    private int markValidMoves() {
        int retVal = 0;

        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (isValidCell(i, j)) {
                    button[i][j].setBackground(blacksTurn ? Color.blue : Color.red);
                    retVal++;
                } else {
                    if (button[i][j].getBackground() != Color.black &&
                            button[i][j].getBackground() != Color.white) {
                        button[i][j].setBackground(Color.green);
                    }
                }
            }
        }

        return retVal;
    }

    private boolean isValidCell(int i, int j) {
        Color backgroundColor = button[i][j].getBackground();
        if (backgroundColor == Color.black || backgroundColor == Color.white) {
            return false;
        } else {
            // dir north (i--)
            if (checkValidityInDirection(NORTH, i, j)) {
                return true;
            }

            // dir north east (i--, j++)
            if (checkValidityInDirection(NE, i, j)) {
                return true;
            }

            // dir east (j++)
            if (checkValidityInDirection(EAST, i, j)) {
                return true;
            }

            // dir south east (i++, j++)
            if (checkValidityInDirection(SE, i, j)) {
                return true;
            }

            // dir south (i++)
            if (checkValidityInDirection(SOUTH, i, j)) {
                return true;
            }

            // dir south west (i++, j--)
            if (checkValidityInDirection(SW, i, j)) {
                return true;
            }

            // dir west (j--)
            if (checkValidityInDirection(WEST, i, j)) {
                return true;
            }

            // dir north west (i--, j--)
            return checkValidityInDirection(NW, i, j);

            // invalid in all directions
        }
    }

    private boolean checkValidityInDirection(int direction, int i, int j) {
        int initI, initJ;

        Pair pair = checkDirection(direction);
        initI = i + pair.incrI;
        initJ = j + pair.incrJ;
        boolean atleastOneFound = false;
        while (initI >= 0 && initI <= 7 &&
                initJ >= 0 && initJ <= 7 &&
                button[initI][initJ].getBackground() == (blacksTurn ? Color.white : Color.black)) {
            initI += pair.incrI;
            initJ += pair.incrJ;
            atleastOneFound = true;
        }

        return initI >= 0 && initI <= 7 &&
                initJ >= 0 && initJ <= 7 &&
                atleastOneFound &&
                button[initI][initJ].getBackground() == (blacksTurn ? Color.black : Color.white);

    }

    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();

        if (source.getBackground() == (blacksTurn ? Color.blue : Color.red)) {
            // find the button
            int foundI = 0, foundJ = 0;

            for (int i = 0; i <= 7; i++) {
                for (int j = 0; j <= 7; j++) {
                    if (button[i][j] == source) {
                        foundI = i;
                        foundJ = j;
                        break;
                    }
                }
            }

            // make the move
            makeMove(foundI, foundJ);

            // flip next turn
            blacksTurn = !blacksTurn;
            int validMoves = markValidMoves();

            if (validMoves == 0) {
                blacksTurn = !blacksTurn;
                validMoves = markValidMoves();

                if (validMoves == 0) {
                    declareWinner();
                    dispose();
                }
            }
        }
    }

    private void makeMove(int i, int j) {
        rePaintInDirection(NORTH, i, j);
        rePaintInDirection(NE, i, j);
        rePaintInDirection(EAST, i, j);
        rePaintInDirection(SE, i, j);
        rePaintInDirection(SOUTH, i, j);
        rePaintInDirection(SW, i, j);
        rePaintInDirection(WEST, i, j);
        rePaintInDirection(NW, i, j);

        button[i][j].setBackground(blacksTurn ? Color.black : Color.white);
        this.setTitle(blacksTurn ? "White's Turn" : "Black's turn");
    }

    private void rePaintInDirection(int direction, int i, int j) {
        int initI, initJ;

        Pair pair = checkDirection(direction);

        initI = i + pair.incrI;
        initJ = j + pair.incrJ;
        boolean atleastOneFound = false;
        while (initI >= 0 && initI <= 7 &&
                initJ >= 0 && initJ <= 7 &&
                button[initI][initJ].getBackground() == (blacksTurn ? Color.white : Color.black)) {
            initI += pair.incrI;
            initJ += pair.incrJ;
            atleastOneFound = true;
        }

        if (initI >= 0 && initI <= 7 &&
                initJ >= 0 && initJ <= 7 &&
                atleastOneFound &&
                button[initI][initJ].getBackground() == (blacksTurn ? Color.black : Color.white)) {
            // paint
            while (initI != i || initJ != j) {
                initI -= pair.incrI;
                initJ -= pair.incrJ;

                button[initI][initJ].setBackground(blacksTurn ? Color.black : Color.white);
            }
        }
    }

    private void declareWinner() {
        int blacks = 0, whites = 0;

        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                if (button[i][j].getBackground() == Color.black) {
                    blacks++;
                } else if (button[i][j].getBackground() == Color.white) {
                    whites++;
                }
            }
        }

        if (blacks > whites) {
            JOptionPane.showMessageDialog(null, "Black wins");
        } else {
            JOptionPane.showMessageDialog(null, "White wins");
        }
    }

    class Pair {
        int incrI, incrJ;

        Pair(int incrI, int incrJ) {
            this.incrI = incrI;
            this.incrJ = incrJ;
        }
    }

    private Pair checkDirection(int direction) {
        if (direction == NORTH)
            return new Pair(-1, 0);
        if (direction == NE)
            return new Pair(-1, 1);
        if (direction == EAST)
            return new Pair(0, 1);
        if (direction == SE)
            return new Pair(1, 1);
        if (direction == SOUTH)
            return new Pair(1, 0);
        if (direction == SW)
            return new Pair(1, -1);
        if (direction == WEST)
            return new Pair(0, -1);
        if (direction == NW)
            return new Pair(-1, -1);
        return new Pair(0, 0);
    }
}
