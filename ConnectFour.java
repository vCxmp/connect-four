/*
 * Veer Desai
 * 1/27/2025
 * CSE 123
 * TA: Isayiah Lim
 */
import java.util.*;
/*
 * This class makes the classic connect four game. It provides a game board and lets the users
 *      drop their tokens in any column they want unless of course the whole row is full. 
 *      The rules of the game are exactly what the classic connect four game has. 
 */
public class ConnectFour extends AbstractStrategyGame {
    private static final String PLAYER_1_TOKEN = "X";
    private static final String PLAYER_2_TOKEN = "Y";
    private static final int PLAYER_1 = 1;
    private static final int PLAYER_2 = 2;
    private static final int TIE = 0;
    private static final int GAME_IS_OVER = -1;
    private static final int GAME_IS_NOT_OVER = -1;
    private String[][] board;
    private boolean isXTurn;
    
    /*
     * This constructs a ConnectFour object. It makes the whole 6 by 7 connect four board. 
     */
    public ConnectFour () {
        this.board = new String[][] {{"1", "2", "3", "4", "5", "6", "7"},
                                     {"1", "2", "3", "4", "5", "6", "7"}, 
                                     {"1", "2", "3", "4", "5", "6", "7"},
                                     {"1", "2", "3", "4", "5", "6", "7"},
                                     {"1", "2", "3", "4", "5", "6", "7"},
                                     {"1", "2", "3", "4", "5", "6", "7"}};
        this.isXTurn = true;                      
    }

    /*
     * This contains the rules of the connect four game that the user will very first see 
     *      within the program.
     * Return:
     *      - String: the rules of the game
     */
    public String instructions() {
        String rules = "";
        rules += ("Welcome to Connect 4!\n");
        rules += ("Player 1 will go first and their moves will be marked as X on the board \n");
        rules += ("Player 2 will go next and will be marked as Y on the board \n");
        rules += ("The winner is whoever has a four in a row vertically, diagnolly, ");
        rules += ("or horizontally. If the game board is full, then it is a tie game.");
        rules += ("The players will choose which column of the game board they want to make"  
                    + " a move in.");
        return rules;
    }

    /*
     * This allows either player one or player 2, depending on whose turn is it, to make their move
     *      of dropping their token in any column they want. After the player makes a move, it 
     *      gives the other player the turn. 
     * Parameters: 
     *      - input: this looks out for the column number the player wants to dop their token in.
     * Exceptions: 
     *      - IllegalArgumentException(): gets thrown if the user column number input is null
     */
    public void makeMove(Scanner input) {
        if (input == null) {
            throw new IllegalArgumentException("input cannot be null");
        }
        String currToken = "";
        if (isXTurn) {
            currToken = PLAYER_1_TOKEN;
            isXTurn = false;
        }
        else {
            currToken = PLAYER_2_TOKEN;
            isXTurn = true;
        }
        System.out.print("Column? ");
        int column = input.nextInt();
        makeMove(currToken, column - 1);
    }

    /*
     * This helper method makes the player, whoever made the move, token be marked
     *      on the board. Whatever column the player selected, this method makes the mark
     *      in whichever bottommost empty row in the column there is. 
     * Parameters: 
     *      - currToken: the marking token of whichever player whose turn it is
     *      - col: the column that the player chooses to drop their token in. However, this 
     *          is one less than the number the player chooses as the program is on zero-based 
     *          indexing
     * Exceptions: 
     *      - IllegalArgumentException #1: gets thrown if the player inputs in their desired column
     *          as a number less than 0 or greater than the amount of columns that exist
     *          on the board
     *      - IllegalArgumentException #2: gets thrown if the desired player column is already 
     *          full beforehand
     */
    private void makeMove(String currToken, int col) {  
        int row = 0;
        if (col >= board[0].length || col < 0) {
            ifException(currToken);
            throw new IllegalArgumentException("invalid cell number");
        }
        for (int i = board.length - 1; i >= 0; i--) {
            if (isEmpty(i, col)) {
                row = i;
                i = -1;
            }
            if (i==0) {
                ifException(currToken);
                throw new IllegalArgumentException("this column is already full. No more adding!");
            }
        }    
        board[row][col] = currToken;
    }

    /*
     * This determines if a player won the game, the game was tied, or if the game is not over yet.
     * The winning situations include four in a row vertically, horixontally, or diagnolly. 
     * Return: 
     *      - int: Either 1 if player 1 won, 2 if player 2 won, 0 if the game is tied, 
     *              or -1 if the game is not over yet
     */
    public int getWinner() {
        for (int i = 0; i < board.length; i++) {
            int rowWinner = getRowWinner(i);
            if (rowWinner != GAME_IS_NOT_OVER) {
                return rowWinner;
            }
        }
        for (int j = 0; j < board[0].length; j++) {
            int colWinner = getColumnWinner(j);
            if (colWinner != GAME_IS_NOT_OVER) {
                return colWinner;
            }
        }
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                int diagWinner = getDiagonalWinner(r, c);
                if (diagWinner != GAME_IS_NOT_OVER) {
                    return diagWinner;
                }
            }
        }
        if (checkTie() == TIE) {
            return TIE;
        }
        return GAME_IS_NOT_OVER;
    }

    /*
     * This tells which player's turn it is. If the game is over at any point, then this 
     *      tells that too. 
     * Return: 
     *      - int: 1 if it is player 1's turn, 2 if it is player 2's turn, 
     *          and -1 if the game is over
     */
    public int getNextPlayer() {
        if (isGameOver()) {
            return GAME_IS_OVER;
        }
        if (isXTurn) {
            return PLAYER_1;
        }
        return PLAYER_2;
    }

    /*
     * This makes the game board into a readable format for the players. 
     * This formatting also includes the current state of the board, which includes
     *      the token markers of the players wherever they made their moves.
     * Return: 
     *      - String: a readable format of the game board which is updates to its current state
     */
    public String toString() {
        String answer = "";
        for (int i = 0; i < board.length; i++) {
            answer += " | ";
            for (int j = 0; j < board[0].length; j++) {
                answer += board[i][j] + " | ";
            }
            answer += "\n";
        }
        return answer;
    }

    /*
     * This method determines if a player has won the game by a 4 in a row horizontal situation. 
     * The tokens must be matched together consectively and wil not count if there is 
     *      even one token of the other player in between the four in a row. 
     * Parameters: 
     *      - row: the specific row on the game board that is being checked for any 4 in a row 
     *          horizontally situation.
     * Return: 
     *      - int: 1 if Player 1 won, 2 if Player 2 won, or -1 if there is no winner yet
     */
    private int getRowWinner(int row) {
        for (int i = 0; i <=board[row].length / 2; i++) {
            int matchingTiles = 0;
            String first = board[row][i];
            for (int j = 1; j < 4; j++) {
                if (first.equals(board[row][i + j])) {
                    matchingTiles++;
                }
            }
            if (matchingTiles == 3) {
                return getPlayer(row, i);
            }
        }
        return GAME_IS_NOT_OVER;
    }

    /*
     * This determines if a player won by a four in a row vertically situation. The tokens must be
     *      be matched together consectively by the same player and will not count if even one
     *      token by the other player is in between. 
     * Parameters: 
     *      - col: the specific column thta is being checked on the game board for a four in a row
     *      vertical situation.
     * Return:
     *      - int: 1 if Player 1 won, 2 if Player 2 won, or -1 if there is no winner yet
     */
    private int getColumnWinner(int col) {
        for (int i = 0; i < board.length / 2; i++) {
            int matchingTiles = 0;
            String first = board[i][col];
            for (int j = 1; j < 4; j++) {
                if (first.equals(board[i + j][col])) {
                    matchingTiles++;
                }
            }
            if (matchingTiles == 3) {
                return getPlayer(i, col);
            }
        }
        return GAME_IS_NOT_OVER;
    }

    /*
     * This determines if a player won by a four in a row diagnolly situation. The tokens must be
     *      be matched together consectively by the same player and will not count if even one
     *      token by the other player is in between.
     * Parameters: 
     *      - row: the specific row being checked on the game board
     *      - col: the specific column being checked on the game board
     * Return: 
     *      - int: 1 if Player 1 won, 2 if Player 2 won, -1 if there is no winner yet
     */
    private int getDiagonalWinner(int row, int col) {
        if (col >= board[0].length / 2 && row < board.length / 2) {
            String first = board[row][col];
            int matchingTiles = 0;
            int counter = 1;
            for (int i = row + 1; i < row + 4; i++) {
                if (first.equals(board[i][col - counter])) {
                    matchingTiles++;
                }
                counter++;
            }
            if (matchingTiles == 3) {
                return getPlayer(row, col);
            }
        }
        if (col <= board[0].length / 2 && row < board.length / 2) {
            String first = board[row][col];
            int matchingTiles = 0;
            int counter = 1;
            for (int r = row + 1; r < row + 4; r++) {
                    if (first.equals(board[r][col + counter])) {
                        matchingTiles++;
                    }
                counter++;
            } 
            if (matchingTiles == 3) {
                return getPlayer(row, col);
            }
        }
        return GAME_IS_NOT_OVER;
    }

    /*
     * This is a helper method that checks if a certain space on the game board if taken by a 
     *      player or empty. 
     * Parameters: 
     *      - row: the specific row of the space on the game board being checked
     *      - col: the specific column of the space on the game board being checked
     * Return: 
     *      - boolean: true if space is empty and false if taken
     */
    private boolean isEmpty(int row, int col) {
        if (board[row][col].equals(PLAYER_1_TOKEN) || board[row][col].equals(PLAYER_2_TOKEN)) {
            return false;
        }
        return true;
    }

    /*
     * This tells what player is taking a specific spot on the game board. 
     * Parameters: 
     *      - row: the row of the specific spot
     *      - col: the column of the specific spot
     * Return: 
     *      - int: 1 if player 1 is taking the spot, 2 if it is player 2 taking the spot, 
     *      -1 if no one is taking the spot (the spot is empty)
     */
    private int getPlayer(int row, int col) {
        if (board[row][col].equals(PLAYER_1_TOKEN)) {
            return PLAYER_1;
        }
        else if (board[row][col].equals(PLAYER_2_TOKEN)) { 
            return PLAYER_2;
        }
        return GAME_IS_NOT_OVER;
    }
    
    /*
     * This method ensures that whenever a player does an illegal move on the board,
     *      they still get their turn back so that they can make a legal move. 
     * Parameter: 
     *      - currToken: the token of the player that made the illegal move
     */
    private void ifException(String currToken) {
        if (currToken.equals(PLAYER_1_TOKEN)) {
            isXTurn = true;
        }
        else {
            isXTurn = false;
        }
    }

    /*
     * This method checks if the entire game board is filled up with player tokens and there 
     *      is no winner still that arises which means that the game ends in a tie.
     * Return: 
     *      - int: will be -1 if there are still empty spaces remaining on the board
     *              and 0 if there are none and the game actually ends in a tie
     */
    private int checkTie() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (isEmpty(i,j)) {
                    return GAME_IS_NOT_OVER;
                }
            }
        }
        return TIE;
    }
}
