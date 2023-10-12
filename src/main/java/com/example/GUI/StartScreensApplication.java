package com.example.GUI;
import com.gameEngine.GameEngine;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.gameEngine.GameRunner;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.fxml.Initializable;


import java.io.IOException;
import java.util.Objects;

public class StartScreensApplication extends Application {
    @FXML
    public TextField firstPlayerName;
    @FXML
    public TextField secondPlayerName;
    @FXML
    public TextField thirdPlayerName;
    @FXML
    public TextField fourthPlayerName;
    @FXML
    private ComboBox<String> playerChoiceComboBox;


    private final GameModel gameModel = GameModel.getInstance();

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Start.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    ImageView p00;
    @FXML
    ImageView p01;

    public void handleStartGame(ActionEvent event) throws IOException {
        System.out.println("checkNames:" + checkNames());
        if (checkNames()) {
            GameRunner.gameStartSignal.complete(null);
           // switchScene("GamePane.fxml", event);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("GamePane.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            StartScreensApplication controller = loader.getController();

            Image ima = new Image("painted_tile_black_3.png"); 
            

            controller.p00.setImage(ima);

            System.out.println("checkNames:" + checkNames());


            } else {
            switchScene("ErrorNoName.fxml", event);
        }
        //needs to save names for now it's here but should be moved to game logic
    }



    public void handleSingleAction(ActionEvent event) {
        switchScene("ErrorAI.fxml", event);
    }

    public void handleMultiplayerAction(ActionEvent event) {
        switchScene("SelectPlayerScene.fxml", event);
    }

    public void handleBackToStart(ActionEvent event) {
        switchScene("Start.fxml", event);
    }

    public void handleBackToChoice(ActionEvent event) {
        switchScene("SelectPlayerScene.fxml", event);
    }
    public void handleBackToName(ActionEvent event) {
        int x = gameModel.getNumberOfPlayers();
        if(x==2){
            switchScene("TwoPeopleNameInputScene.fxml", event);
        }  if(x==3){
            switchScene("ThreePeopleNameInputScene.fxml", event);
        }  if(x==4) {
            switchScene("FourPlayerNameInput.fxml", event);
        }
    }

    public void handlePlayerChoice(ActionEvent event) {
        if (playerChoiceComboBox.getValue() == null) {
            switchScene("ErrorNoPlayersSelected.fxml", event);
        } else {
            switch (playerChoiceComboBox.getValue()) {
                case "2 Players" -> {
                    System.out.println("Starting a game for 2 players");
                    gameModel.setNumberOfPlayers(2);
                    switchScene("TwoPeopleNameInputScene.fxml", event);
                }
                case "3 Players" -> {
                    System.out.println("Starting a game for 3 players");
                    gameModel.setNumberOfPlayers(3);
                    switchScene("ThreePeopleNameInputScene.fxml", event);
                }
                case "4 Players" -> {
                    System.out.println("Starting a game for 4 players");
                    gameModel.setNumberOfPlayers(4);
                    switchScene("FourPlayerNameInput.fxml", event);
                }
            }
        }
    }


    public void switchScene(String sceneName, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(sceneName)));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkNames() {
        switch (gameModel.getNumberOfPlayers()) {
            case 2 -> {

                return (!(firstPlayerName.getCharacters().isEmpty() || secondPlayerName.getCharacters().isEmpty()));
            }
            case 3 -> {
                return (!(firstPlayerName.getCharacters().isEmpty() || secondPlayerName.getCharacters().isEmpty() || thirdPlayerName.getCharacters().isEmpty()));
            }
            case 4 -> {
                return (!(firstPlayerName.getCharacters().isEmpty() || secondPlayerName.getCharacters().isEmpty() || thirdPlayerName.getCharacters().isEmpty() || fourthPlayerName.getCharacters().isEmpty()));
            }
            default -> {
                return false;
            }
        }
    }
    public static void main(String[] args) {

        launch();
    }
}
