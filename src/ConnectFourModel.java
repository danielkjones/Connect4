import java.util.ArrayList;
import java.util.Observable;

/**
 * The model for the Connect Four game board
 *
 * @author Daniel Jones
 */
public class ConnectFourModel extends Observable{

    //The number of rows on the connect four grid
    private int NUMROWS;
    //The number of columns on the connect four grid
    private int NUMCOLS;
    //The number of chips needed to win. Typically 4 as it is called "Connect Four"
    private int CHIPS_TO_WIN = 4;
    //The 2 dimensional array for representing the Connect Four game board
    public Integer[][] board;
    //The current index of the player
    private int currentPlayer;
    //True of false switch for whether or not the game board is full
    private boolean boardFull;
    //True if there has been a winner
    private boolean gameFinished;
    //The symbol for an empty spot
    private int EMPTY = 0;
    //A list of all the players
    private ArrayList<Player> players;

    /**
     *  Model for the Connect Four game
     */
    public ConnectFourModel(){
        this.NUMROWS = 6;
        this.NUMCOLS = 7;
        this.board = new Integer[NUMROWS][NUMCOLS];
        initialize();
        this.players = new ArrayList<>();
        players.add(new Player("Blue"));
        players.add(new Player("Red"));
        this.currentPlayer = 0;
        this.boardFull = false;
    }

    /**
     * Set the initial setup of the board to be a 2 dimensional array of empty spots
     */
    public void initialize(){
        for(int row = 0; row < NUMROWS; row++){
            for(int col = 0; col < NUMCOLS; col++){
                this.board[row][col] = EMPTY;
            }
        }
    }

    /**
     * A function to add a game piece to a column of the game board
     * @param column the column to add the piece to
     * @return true if something was added to the board, false otherwise
     */
    public boolean add(int column){
        for(int row = NUMROWS - 1; row >= 0; row--){
            if(this.board[row][column] == EMPTY){
                //Put in the number of the player from the Players list
                this.board[row][column] = this.players.get(this.currentPlayer).getNumber();
                //Check to see if the board is full, if so the game is finished
                this.boardFull = boardFull();
                //Check to see if a winner has been found
                gameFinished = hasWon(row, column);
                if (!boardFull && !gameFinished) {
                    this.currentPlayer = (this.currentPlayer + 1) % 2;
                }
                setChanged();
                notifyObservers();
                return true;
            }

        }
        return false;
    }

    /**
     * Checks to see if the board is full in which case the game is done and no player has won.
     * @return true if the board is full, false otherwise
     */
    public boolean boardFull(){
        //If the board is full the last row of the game board will be full
        for(int col = 0; col < NUMCOLS; col++) {
            //The row that represents the top of the board in the GUI is the
            //first row in the 2D array
            if(board[0][col] == 0){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a victory has been accomplished by a player from the last piece put in place
     * @return true if the current player has won, false if the game is complete
     */
    private boolean hasWon(int row, int col){

        //check the horizontal for a winning amount of 4 or more

        //A counter for the number of consecutive chips to the right
        int rightCount = 0;
        //check to the right
        for(int c = col + 1; c < NUMCOLS; ++c){
            if(this.board[row][c] != this.players.get(this.currentPlayer).getNumber()){
                break;
            }
            else{
                rightCount += 1;
            }
        }
        //A counter for the number of consecutive chips to the left
        int leftCount = 0;
        //check to the left
        for(int c = col - 1; c >= 0; --c){
            if(this.board[row][c] != this.players.get(this.currentPlayer).getNumber()){
                break;
            }
            else{
                leftCount += 1;
            }
        }

        //The number of chips needed to win must be at least one less than the right and left put
        //together as they are based off the current chip but do not include it
        if( rightCount + leftCount >= CHIPS_TO_WIN - 1){
            return true;
        }

        //check the vertical for winning amount of chips
        //recall that the 0th row index is in fact the top of the GUI board

        //A counter for the number of consecutive chips up
        int upCount = 0;
        //check up
        for(int r = row - 1; r >= 0; --r){
            if(this.board[r][col] != this.players.get(this.currentPlayer).getNumber()){
                break;
            }
            else{
                upCount += 1;
            }
        }
        //A counter for the number of consecutive chips down
        int downCount = 0;
        //check down
        for(int r = row + 1; r < NUMROWS; r++){
            if(this.board[r][col] != this.players.get(this.currentPlayer).getNumber()){
                break;
            }
            else{
                downCount += 1;
            }
        }

        //The number of chips needed to win must be at least one less than the up and down put
        //together as they are based off the current chip but do not include it
        if( upCount + downCount >= CHIPS_TO_WIN - 1){
            return true;
        }

        //Now to count the diagonals

        //Counter for consecutive chips to the up right
        int upRightCount = 0;
        //Column counter for the right piece of the diagonal
        int c = col;
        //The up piece of the counter
        for(int r = row - 1; r >= 0; --r){
            //Need to increment the column counter for each iteration of the for loop of r;
            c += 1;
            //Check to make sure that the columns counter is also not out of bounds
            if(c >= this.NUMCOLS){
                break;
            }
            else if(this.board[r][c] != this.players.get(this.currentPlayer).getNumber()){
                break;
            }
            else{
                upRightCount += 1;
            }
        }

        //Counter for the consecutive chips to the bottom left
        int downLeftCount = 0;
        //Reset the column counter for the left portion of down and left
        c = col;
        //The up piece of the counter
        for(int r = row + 1; r < NUMROWS; ++r){
            //Need to increment the column counter for each iteration of the for loop of r;
            c -= 1;
            //Check to make sure that the columns counter is also not out of bounds
            if(c < 0){
                break;
            }
            else if(this.board[r][c] != this.players.get(this.currentPlayer).getNumber()){
                break;
            }
            else{
                downLeftCount += 1;
            }
        }

        //The up right and down left are paired together for a connection of four
        if (downLeftCount + upRightCount >= CHIPS_TO_WIN - 1){
            return true;
        }

        //Counter fo the consecutive chips to the up left
        int upLeftCount = 0;
        //Reset the column counter for the left portion of up and left
        c = col;
        //The up piece of the counter
        for(int r = row - 1; r >= 0; --r){
            //Need to increment the column counter for each iteration of the for loop of r;
            c -= 1;
            //Check to make sure that the columns counter is also not out of bounds
            if(c < 0){
                break;
            }
            else if(this.board[r][c] != this.players.get(this.currentPlayer).getNumber()){
                break;
            }
            else{
                upLeftCount += 1;
            }
        }

        //Counter for the consecutive chips to the bottom right
        int downRightCount = 0;
        //Reset the column counter for the right portion of down and right
        c = col;
        //The up piece of the counter
        for(int r = row + 1; r < NUMROWS; ++r){
            //Need to increment the column counter for each iteration of the for loop of r;
            c += 1;
            //Check to make sure that the columns counter is also not out of bounds
            if(c >= this.NUMCOLS){
                break;
            }
            else if(this.board[r][c] != this.players.get(this.currentPlayer).getNumber()){
                break;
            }
            else{
                downRightCount += 1;
            }
        }
        //The up left and down right make a straight line connection of four
        if (downRightCount + upLeftCount >= CHIPS_TO_WIN - 1){
            return true;
        }

        return false;

    }

    /**
     * Put together and return a string representation of the game board
     * @return the string representation
     */
    public String toString(){
        String textRep = "";
        for(int row = 0; row <= NUMROWS; row++){
            for(int col = 0; col < NUMCOLS; col++){
                //Print out all the contents of the board
                if(row < NUMROWS) {
                    textRep = textRep + this.board[row][col] + " ";
                }
                //On the last time through just print out the column numbers
                else{
                    textRep = textRep + col + " ";
                }
            }
            textRep = textRep + "\n";
        }
        return textRep;
    }

    /**
     * reset the game back to the beginning
     */
    public void reset(){
        //reset the board
        initialize();
        //reset the current player to the first player
        this.currentPlayer = 0;
        //make sure the game is set to unfinished
        this.boardFull = false;
        this.gameFinished = false;
        setChanged();
        notifyObservers();
    }

    public boolean getBoardFull(){ return this.boardFull;}
    public boolean getGameFinished(){ return this.gameFinished;}

    public int getRows(){ return NUMROWS; }
    public int getCols(){ return NUMCOLS; }

    //Get the player at a particular index of the players late
    public Player getPlayer(int index){ return players.get(index);}

    //Get the index of the current player
    public int getCurrentPlayer(){ return currentPlayer;}

}
