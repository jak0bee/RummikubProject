# Rummikub Project
This is a java and python application developed by the team of student at Maastricht University. We created a Rummikub game with graphics made with JavaFX. The game can be played with other people from 2 to 4 player on a single device, or single player against a bot. We created multiple agents to play against, including an information set MCTS bot with a neural network for predicting oponents tiles. The MCTS algorithm was implemented in java and the neural network was implemented in Python with PyTorch.

Done by group 7 DACS Maastricht 2023/2024: <br />
-Jakub Suszwedyk - Team Leader <br />
-Frederik Grüneberg <br />
-Kees van den Eijnden <br />
-Kasper van der Horst <br />
-Kaloyan Kostov <br />
-Konrad Paszyński <br />
-Julius Verschoof <br />

### Python prerequisites

Python version: 3.10.4

Install the required packages with `pip install -r requirements.txt`.
Pytorch has to be installed separately as it is dependent on the system. For more info
refer to the [documentation](https://pytorch.org/get-started/locally/).

## Scientific paper
The report created for this project was created in LateX, its available as a PDF here: https://drive.google.com/file/d/17FPqpA1eg8pAoMjfWQQhvdlux0Yb4tYr/view?usp=sharing

## How to run
Make sure Python is set up and Python prerequisites are installed properly.

### JAR file
Download the jar file and launch it you will be trowed in a game selection menu where you can play against other people
on the same device or more importantly, if you choose single-player mode you will start a game vs our AI and you can try
and beat it

### Build from source
You can also download the whole source code and run it from your favorite editor.
rummicubei/src/main/java/com/gameEngine/GameEngine.java is the file you need to run to achieve the same result as the jar
file

### Game Options
You can play either in local multiplayer of 2 to 4 people or against a bot. The deafault bot is the baseline agent but its also possible to play  against all the other bots, that can be changed in the addPlayers function in the GameEngine.java class.
