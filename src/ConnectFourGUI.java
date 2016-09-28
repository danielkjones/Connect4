import javafx.application.Application;

import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.*;
import javafx.scene.shape.*;
import javafx.scene.paint.Paint;
import javafx.scene.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Button;

/**
 * A GUI class for the user interface for the game Connect Four
 *
 * @author Daniel Jones
 */
public class ConnectFourGUI extends Application implements Observer {

    //The observable board for the GUI observer
    ConnectFourModel model;
    //Circles of the GUI game board held in an Array
    ArrayList<Circle> gameSpots;
    //The message at the top of the game board to communicated with the users
    Label message;

    public ConnectFourGUI(){
        this.model = new ConnectFourModel();
        this.gameSpots = new ArrayList<>();
        this.model.addObserver(this);
    }

    /**
     * Updates the user interface after a change has been made to the board
     * @param t
     * @param o
     */
    public void update(Observable t, Object o){
        updateMessage();
        updateBoard();
    }


    /**
     * Initializes the GUI
     * @param stage
     */
    public void start(Stage stage){
        BorderPane mainPane = new BorderPane();
        mainPane.setTop(makeMessage());
        mainPane.setCenter(makeBoard());
        mainPane.setBottom(makeReset());

        stage.setScene(new Scene(mainPane));
        stage.setResizable(false);
        stage.setTitle("Connect Four!");
        stage.show();

    }

    /**
     * A make function for when creating the main board for the connect four board
     * @return the GridPane to be used for the main board
     */
    private GridPane makeBoard(){
        GridPane board = new GridPane();
        for(int row = 0; row < this.model.getRows(); row++){
            for(int col = 0; col < this.model.getCols(); col++){
                Circle slot = new Circle(25);
                //Default paint value to show the spot is empty
                slot.setFill(Paint.valueOf("lightgray"));
                //adds the spot to the list of gamespots
                this.gameSpots.add(slot);
                //On the click of a mouse refers to the choose function at that column
                slot.setOnMouseClicked( event -> choose(getColumn(this.gameSpots.indexOf(slot))));
                board.add(slot, col, row);
            }
        }
        board.setPadding(new Insets(15));
        board.setHgap(5);
        board.setVgap(5);
        return board;
    }

    /**
     * Updates the board section of the user interface
     */
    private void updateBoard(){
        int spotIndex = 0;
        for(int row = 0; row < model.getRows(); row++) {
            for (int col = 0; col < model.getCols(); col++) {
                //For now keeping it hard coded for two players, update later as necessary
                //The first player is at the 0th index
                if(this.model.board[row][col] == this.model.getPlayer(0).getNumber()){
                    gameSpots.get(spotIndex).setFill(Paint.valueOf(this.model.getPlayer(0).getColor()));
                }
                //The second player is at the first index
                else if(this.model.board[row][col] == this.model.getPlayer(1).getNumber()){
                    gameSpots.get(spotIndex).setFill(Paint.valueOf(this.model.getPlayer(1).getColor()));
                }
                //If the spot is empty, change to the empty color
                else if(this.model.board[row][col] == 0){
                    gameSpots.get(spotIndex).setFill(Paint.valueOf("lightgrey"));
                }
                //the spot index is in charge of incrementing to the corresponding circle
                spotIndex++;
            }
        }
    }

    /**
     * Make-function for the creation of the label for informing the user
     * @return A Label
     */
    private Label makeMessage(){
        this.message =  new Label(this.model.getPlayer(this.model.getCurrentPlayer()).getName() + "'s turn");
        this.message.setTextFill(Paint.valueOf(this.model.getPlayer(this.model.getCurrentPlayer()).getColor()));
        this.message.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        this.message.setPadding(new Insets(20, 30, 5, 30));
        return this.message;
    }

    /**
     * An update function for the message
     */
    private void updateMessage(){
        if(this.model.getBoardFull() ){
            this.message.setText("Game board full! It's a tie!!");
            this.message.setTextFill(Paint.valueOf("Black"));
        }
        else if(this.model.getGameFinished()){
            this.message.setText(this.model.getPlayer(this.model.getCurrentPlayer()).getName() + " has won the game!");
            this.message.setTextFill(Paint.valueOf(this.model.getPlayer(this.model.getCurrentPlayer()).getColor()));

        }
        else {
            this.message.setText(this.model.getPlayer(this.model.getCurrentPlayer()).getName() + "'s turn");
            this.message.setTextFill(Paint.valueOf(this.model.getPlayer(this.model.getCurrentPlayer()).getColor()));
        }
    }

    /**
     * Make the reset for the bottom of the main border pane
     */
    private HBox makeReset(){
        Button reset = new Button("Reset");
        reset.setPadding(new Insets(7.5, 15, 7.5, 15));
        reset.setOnAction( event -> this.model.reset());
        HBox buttons = new HBox(reset);
        buttons.setPadding(new Insets(0, 20, 20, 30));
        return buttons;
    }

    /**
     * A function to choose a column
     * @param column the column that was chosen for an action
     */
    public void choose(int column){
        //the point of choose is to add a game piece to a column
        //If there is not a winner already found then try to add to the board
        if(!this.model.getGameFinished()){ this.model.add(column); }

    }

    /**
     * Using the index of a spot in the board ArrayList retrieves the column number
     * for the corresponding spot on the game board in the model
     * @param index the index of a spot in the baord ArrayList
     * @return the column number of that spot
     */
    private Integer getColumn(Integer index){
        return index % this.model.getCols();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
