package com;
import java.util.ArrayList;

public class GameEngine {
    int numberOfRealPlayers;
    int numberOfBots;
    boolean endGame;
    //private ArrayList<Tile> potOfTiles = new ArrayList<Tile>();
    public GameEngine(int numberOfRealPlayers, int numberOfBots){
        this.numberOfRealPlayers=numberOfRealPlayers;
        this.numberOfBots =numberOfBots;
        endGame=false;
        // add other game options such as bot difficulty
    }

    public void gameLoop(){
        generateTiles();
        // Starts the game loop which runs until a game ending event (quit button, or win, etc.)
        while (!endGame){
        gameTurn();
        isGameEnding();}
    }

    private void gameTurn(){
        // Simulate the turn of one player (either bot or normal)
        // waiting for the new board that we get from the gui#
        // Check if it is valid -> if not wait for new board
        // if valid then end turn
    }
    private boolean isGameEnding(){ // check game ending conditions
        /*conditions still need to add:
         * -quit button is pressed
         * -deck of current player is empty
         */
        /*if(potOfTiles.size()==0){
            return true;
        }*/

        return false;
    }

    private boolean isTurnValid(){
        return false;
    }

    private void generateTiles(){
        boolean isJoker= true;
        /*potOfTiles.add(new Tile(0,"",isJoker));
        potOfTiles.add(new Tile(0,"",isJoker));
        isJoker = false;
        for(int i=1;i<14;i++){
         potOfTiles.add(new Tile(i,"red",isJoker));
        }
        for(int i=1;i<14;i++){
         potOfTiles.add(new Tile(i,"blue",isJoker));
        }for(int i=1;i<14;i++){
         potOfTiles.add(new Tile(i,"black",isJoker));
        }
        for(int i=1;i<14;i++){
         potOfTiles.add(new Tile(i,"orange",isJoker));
        }*/
    }

}