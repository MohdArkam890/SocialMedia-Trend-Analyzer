import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SudokuSolverGUI extends JFrame {
    private final JTextField[][] cells = new JTextField[9][9];
    private final int SIZE = 9;
    private int[][] solution;
    private int[][] puzzle;

    public SudokuSolverGUI() {
        setTitle("Sudoku Game");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE));

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                JTextField tf = new JTextField();
                tf.setHorizontalAlignment(JTextField.CENTER);
                tf.setFont(new Font("Arial", Font.BOLD, 20));
                tf.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cells[r][c] = tf;
                gridPanel.add(tf);
            }
        }

        JPanel buttonPanel = new JPanel();
        JButton newGameBtn = new JButton("New Game");
        JButton checkBtn = new JButton("Check");
        JButton clearBtn = new JButton("Clear");

        buttonPanel.add(newGameBtn);
        buttonPanel.add(checkBtn);
        buttonPanel.add(clearBtn);

        add(gridPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        newGameBtn.addActionListener(e -> generateNewPuzzle());
        checkBtn.addActionListener(e -> checkSolution());
        clearBtn.addActionListener(e -> clearUserEntries());

        generateNewPuzzle();

        setVisible(true);
    }

    // Generate a random Sudoku puzzle
    private void generateNewPuzzle() {
        int[][] fullBoard = new int[SIZE][SIZE];
        solveSudoku(fullBoard); // Generate full valid board
        solution = copyBoard(fullBoard);

        puzzle = copyBoard(solution);
        removeRandomCells(puzzle, 45); // Remove some cells for puzzle

        updateGridForGame();
    }

    private void updateGridForGame() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                JTextField tf = cells[r][c];
                tf.setBackground(Color.WHITE);
                tf.setEditable(true);

                if (puzzle[r][c] != 0) {
                    tf.setText(String.valueOf(puzzle[r][c]));
                    tf.setEditable(false);
                    tf.setBackground(new Color(220, 220, 220)); // light gray for fixed cells
                } else {
                    tf.setText("");
                }
            }
        }
    }

    private void checkSolution() {
        boolean correct = true;
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                String text = cells[r][c].getText();
                int val = text.isEmpty() ? 0 : Integer.parseInt(text);
                if (val != solution[r][c]) {
                    cells[r][c].setBackground(new Color(255, 150, 150)); // red for wrong
                    correct = false;
                } else if (puzzle[r][c] == 0) {
                    cells[r][c].setBackground(new Color(200, 255, 200)); // green for correct user fill
                }
            }
        }
        if (correct)
            JOptionPane.showMessageDialog(this, "🎉 Congratulations! You solved it!");
        else
            JOptionPane.showMessageDialog(this, "❌ Some numbers are incorrect. Try again!");
    }

    private void clearUserEntries() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (puzzle[r][c] == 0) {
                    cells[r][c].setText("");
                    cells[r][c].setBackground(Color.WHITE);
                }
            }
        }
    }

    // Sudoku solving and helper functions

    private boolean solveSudoku(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isSafe(board, row, col, num)) {
                            board[row][col] = num;
                            if (solveSudoku(board)) return true;
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isSafe(int[][] board, int row, int col, int num) {
        for (int i = 0; i < SIZE; i++)
            if (board[row][i] == num || board[i][col] == num)
                return false;

        int startRow = row - row % 3, startCol = col - col % 3;
        for (int r = startRow; r < startRow + 3; r++)
            for (int c = startCol; c < startCol + 3; c++)
                if (board[r][c] == num)
                    return false;

        return true;
    }

    private void removeRandomCells(int[][] board, int count) {
        Random rand = new Random();
        while (count > 0) {
            int r = rand.nextInt(SIZE);
            int c = rand.nextInt(SIZE);
            if (board[r][c] != 0) {
                board[r][c] = 0;
                count--;
            }
        }
    }

    private int[][] copyBoard(int[][] board) {
        int[][] copy = new int[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++)
            System.arraycopy(board[r], 0, copy[r], 0, SIZE);
        return copy;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuSolverGUI::new);
    }
}
