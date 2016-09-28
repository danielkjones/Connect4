/**
 * A player class for the players of the game. Keeps track of player game board color and
 * player number.
 *
 * @author Daniel Jones
 */
public class Player {
    //The player's corresponding color for the board
    private String color;
    //The player number
    private int number;
    //Player name
    private String name;
    //The count of player numbers in the game
    private static int numberPlayers;

    /**
     * A constructor that just takes in the color that the player will use. Sets a
     * generic name for the individual
     * @param color the color piece that the player will use on the game board
     */
    public Player(String color){
        this.number = ++ this.numberPlayers;
        this.name = "Player " + this.number;
        this.color = color;
    }

    /**
     * A constructor that will take a name for the certain player
     * @param name the name of the player
     * @param color the color piece that the player will use on the game board
     */
    public Player(String name, String color){
        this.number = ++ this.numberPlayers;
        this.name = name;
        this.color = color;
    }

    /**
     * Resets the player count to no players
     */
    public void reset(){
        this.numberPlayers = 0;
    }

    /**
     * Get functions for the player colors and numbers
     * @return the string representation of the number
     */
    public String getColor(){return this.color;}

    public int getNumber(){return this.number;}

    public String getName(){return this.name;}

    public int getNumberPlayers(){return this.numberPlayers;}
}
