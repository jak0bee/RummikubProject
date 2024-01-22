package com.MCTS;

import java.util.*;

import javafx.css.SimpleStyleableDoubleProperty;



public class ActionSpaceGenerator {

    private ArrayList<ArrayList<ArrayList<Integer>>> resultingBoards;
    private ArrayList<Integer> startingBoard;
    private ArrayList<Integer> startingRack;
    private ArrayList<ArrayList<Integer>> allPossibleSets;
    private HashMap<ArrayList<Integer>,HashSet<ArrayList<Integer>>> conflicts;
    private ArrayList<ArrayList<Integer>> possibleSets;
    private ArrayList<Integer> availableTilesStart;
    private ArrayList<ArrayList<Integer>> boardForActionSpace;
    private boolean foundFinish;



    public ActionSpaceGenerator(ArrayList<ArrayList<Integer>> board, ArrayList<Integer> rack){
        this.boardForActionSpace = CustomUtility.deepCopy(board);
        this.allPossibleSets = AllSetGenerator.getInstance().getAllSets();
        this.conflicts = ConflictingSets.getInstance().getAllConflicts();
        this.resultingBoards = new ArrayList<>();
        this.startingBoard = CustomUtility.decompose(boardForActionSpace);
        this.startingRack = new ArrayList<>(rack);
        this.possibleSets = CustomUtility.possibleSets(this.startingRack,this.startingBoard, this.allPossibleSets);
        this.foundFinish = false;
        //now this.possibleConflicts contains only the conflicts for possiblesets
        this.availableTilesStart = new ArrayList<>();
        for(Integer tile: startingRack){
            this.availableTilesStart.add(tile);
        }
        for(Integer tile: startingBoard){
            this.availableTilesStart.add(tile);
        }
        createAllMoves(new ArrayList<>(), this.possibleSets);
    }

    public static void main(String[] args) {
        // Example board and rack for testing
        ArrayList<ArrayList<Integer>> exampleBoard = new ArrayList<>();
        List<List<Integer>> listOfLists = Arrays.asList(
            Arrays.asList(3, 4, 5),
            Arrays.asList(9, 10, 11),
            Arrays.asList(28, 29, 30),
            Arrays.asList(50, 51, 52),
            Arrays.asList(16, 17, 18, 19),
            Arrays.asList(42, 43, 44, 45, 46, 53, 48),
            Arrays.asList(1, 14, 27),
            Arrays.asList(2, 15, 41),
            Arrays.asList(7, 20, 33),
            Arrays.asList(8, 21, 34),
            Arrays.asList(23, 36, 49)
        );

    // Create ArrayList<ArrayList<Integer>> and add the list of lists
    for (List<Integer> list : listOfLists) {
        exampleBoard.add(new ArrayList<>(list));
    }
        ArrayList<Integer> exampleRack = new ArrayList<>(Arrays.asList(25,24,47));
        // Populate exampleRack with your data

        // Record the start time
        long startTime = System.currentTimeMillis();

        // Create an instance of ActionSpaceGenerator
        ActionSpaceGenerator actionSpaceGenerator = new ActionSpaceGenerator(exampleBoard, exampleRack);

        // Get the resulting boards
        ArrayList<ArrayList<ArrayList<Integer>>> resultingBoards = actionSpaceGenerator.getResultingBoards();

        // Record the end time
        long endTime = System.currentTimeMillis();

        // Print the resulting boards for testing purposes
        System.out.println("Resulted in: " + resultingBoards.size() + " boards");

        // Print the execution time
        System.out.println("Execution time: " + (endTime - startTime) + " milliseconds");

        //System.out.println("Pruned " + actionSpaceGenerator.pruningCounter + " times.");
    }

    private void createAllMoves(ArrayList<ArrayList<Integer>> currentBoard ,ArrayList<ArrayList<Integer>> setsNoConflicts){
        if(this.foundFinish){
            return;

        }
        if(new HashSet<>(CustomUtility.decompose(currentBoard)).equals(new HashSet<>(this.availableTilesStart))){
            this.foundFinish = true;
            this.resultingBoards.clear();
            this.resultingBoards.add(currentBoard);
            System.out.println("should fully stop after this");
            return;
        }
        if(setsNoConflicts.isEmpty()){
            return;
        }
        ArrayList<ArrayList<Integer>> conflichtNext = CustomUtility.deepCopy(setsNoConflicts);
        for(ArrayList<Integer> rummikubSet: setsNoConflicts){
            if(foundFinish){
                continue;
            }
            ArrayList<ArrayList<Integer>> newBoard = CustomUtility.deepCopy(currentBoard);
            ArrayList<ArrayList<Integer>> newConflicts = CustomUtility.deepCopy(conflichtNext);
            conflichtNext.remove(rummikubSet);
            newBoard.add(rummikubSet);
            newConflicts.remove(rummikubSet);
            newConflicts.removeIf(this.conflicts.get(rummikubSet)::contains);
            if(!forwardCheck(newBoard, newConflicts)){
                continue;
            }
            if(CustomUtility.validBoard(newBoard, this.startingBoard)){
                this.resultingBoards.add(newBoard);
            }
            createAllMoves(newBoard, newConflicts);
        }
    }


    public boolean checker(ArrayList<ArrayList<Integer>> newBoard){
        for (ArrayList<ArrayList<Integer>> board:resultingBoards){
            if(isEqual(newBoard,board))return true;
        }
        return false;
    }
    public boolean isEqual(ArrayList<ArrayList<Integer>> board1,ArrayList<ArrayList<Integer>> board2){

        for (ArrayList<Integer>set1:board1){
            boolean checker=false;
            for (ArrayList<Integer>set2:board2){
                if (set1.equals(set2)) {
                    checker = true;
                    break;
                }
            }
            if (!checker)return checker;
        }
        return true;
    }


    public ArrayList<ArrayList<ArrayList<Integer>>> getResultingBoards() {
        if(this.resultingBoards.isEmpty()){
            this.resultingBoards.add(this.boardForActionSpace);
        }
        return this.resultingBoards;
    }

    public ArrayList<ArrayList<Integer>> sortPossibleSets(ArrayList<ArrayList<Integer>> possibleSets, ArrayList<Integer> board) {
        // First checks how many tiles from the possible sets are already on the board, then creates a ArrayList that
        // Maps the index in possibleSet to the amount of tiles that it has on the board.
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        HashMap<Integer, Stack<Integer>> amountOfTilesMap = new HashMap<>(); // Maps K: amount of the tiles on the board - > V: set index
        for (int setIndex = 0; setIndex < possibleSets.size(); setIndex++) {
            amountOfTilesMap.put(setIndex, new Stack<>());
        }
        for (int setIndex = 0; setIndex < possibleSets.size(); setIndex++) { // Check each set
            ArrayList<Integer> currentSet = possibleSets.get(setIndex);
            int tilesOnTheBoard = 0;
            for (int tileIndex = 0; tileIndex < currentSet.size(); tileIndex++) { // Check each tile in each set
                int currentTile = currentSet.get(tileIndex);
                if (board.contains(currentTile)) { // Check if the tile is on the board
                    tilesOnTheBoard += 1;
                }
            }
            amountOfTilesMap.get(tilesOnTheBoard).push(setIndex); // Map K: amount of the tiles on the board - > V: set
        }
        List<Integer> keysList = new ArrayList<>(amountOfTilesMap.keySet()); // convert the keys of the dict to a list

        for (int i = keysList.size() - 1; i >= 0; i--) { // Get each entry in the map in reversed order
            int key = keysList.get(i);
            Stack<Integer> currentDictEntry = amountOfTilesMap.get(key);
            while (!currentDictEntry.isEmpty()) { // For each entry add it to the result
                result.add(possibleSets.get(currentDictEntry.pop()) );
            }
        }
        return result;
    }

    public ArrayList<ArrayList<Integer>> getPossibleSets() {
        return possibleSets;
    }
    /**
     * Performs forward checking to determine if the placement of pieces on the new board
     * is consistent with the constraints represented by the new conflicts.
     *
     * @param newBoard     The new board configuration containing placements of pieces.
     * @param newConflicts Represents the tiles which are still able to be added
     * @return True if the placement is consistent and does not violate any constraints,
     *         false otherwise.
     */
    public boolean forwardCheck(ArrayList<ArrayList<Integer>> newBoard,ArrayList<ArrayList<Integer>> newConflicts){
        ArrayList<Integer> allPieces =new ArrayList<>();
        for (ArrayList<Integer> set:newBoard){
            allPieces.addAll(set);
        }
        for (ArrayList<Integer> set:newConflicts){
            allPieces.addAll(set);
        }
        ArrayList<Integer> copy=new ArrayList<>(this.startingBoard);
        copy.removeAll(allPieces);
        return copy.isEmpty();
    }
}

