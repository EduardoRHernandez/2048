# 2048 Final Project

Repo for the final project: 2048 Game

# Description

This repository will include all files for setting up and running our project, which models the popular 2048 game. Our version of 2048 also comes with personalized themes in addition to a friend database where you can add friends and view a leaderboard.  

# Installation

To install and run 2048, clone the repository and compile the Java files:
```sh
git clone https://github.com/EduardoRHernandez/2048.git
```

```sh
cd 2048
```

```sh
javac *.java
```

To get the packages needed, download JavaJDK17+ and JavaFX21+.

```sh
brew install openjdk
sudo apt update
sudo apt install openjdk-17-jdk
```

You can verify installation with:
```sh
java -version
javac -version
```

# Usage
To start the game, run the following commands, replacing /path/to/javafx-sdk with the actual path to JavaFX
```sh
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -d bin src/model/*.java src/controller/*.java src/view/*.java

```
Replace /path/to/javafx-sdk with the actual path to JavaFx
```sh
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -cp bin view.GUI
```

# Contributors

Eduardo Hernandez, Zachary Astrowsky, Eric Yoon, Haakon Naughton
