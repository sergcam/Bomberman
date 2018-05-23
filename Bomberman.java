import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.input.KeyCode;
import javafx.animation.Animation;

public class Bomberman extends Application
{
    Scanner in = new Scanner(System.in);
    // private instance variables
    private Man p1, p2;
    private Bomb p1Bomb, p2Bomb;
    private int mapWidth, mapHeight;
    private int[][] map;
    private List<Bomb> p1Bombs, p2Bombs;
    private List<Block> blocks;
    public Bomberman()
    {
        p1 = new Man("Player 1", 0, 100);
        p2 = new Man("Player 2", 500, 600);
        map = new int[11][11];
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                if(i % 2 == 1 && j % 2 == 1){
                    map[i][j] = 1;
                }
                else{
                    map[i][j] = 0;
                }
            }
        }
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                double randomNum = Math.random();
                if(randomNum >= (1.0 / 3.0) && (i != 0 || j != 0) && (i != 0 || j != 1) && (i != 1 || j != 0) && (i != map.length - 1 || j != map.length - 1) && (i != map.length - 2 || j != map.length - 1) && (i != map.length - 1 || j != map.length - 2) && map[i][j] != 1){
                    map[i][j] = 4;
                }
            }
        }
        p1Bombs = new ArrayList<Bomb>();
        p2Bombs = new ArrayList<Bomb>();
        blocks = new ArrayList<Block>();
    }

    @Override 
    public void start(Stage stage) 
    {
        Canvas canvas = new Canvas(550, 650);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(10), e -> run(gc)));
        tl.setCycleCount(Timeline.INDEFINITE);
        canvas.setFocusTraversable(true);

        // handle mouse and key events
        /* 0: free space
         * 1: unremovable block
         * 2: fire
         * 3: bomb
         * 4: destructable block
         * 5: dropped item
         */
        canvas.setOnKeyPressed(e ->
            {
                if(e.getCode() == KeyCode.W && p1.getY() != 100 && map[(p1.getY() - 100 - 50) / 50][p1.getX() / 50] != 1 && map[(p1.getY() - 100 - 50) / 50][p1.getX() / 50] != 3 && map[(p1.getY() - 100 - 50) / 50][p1.getX() / 50] != 4)
                {
                    p1.moveUp();
                }

                if(e.getCode() == KeyCode.S && p1.getY() != 600 && map[(p1.getY() - 100 + 50) / 50][p1.getX() / 50] != 1 && map[(p1.getY() - 100 + 50) / 50][p1.getX() / 50] != 3 && map[(p1.getY() - 100 + 50) / 50][p1.getX() / 50] != 4)
                {
                    p1.moveDown();
                }

                if(e.getCode() == KeyCode.A && p1.getX() != 0 && map[(p1.getY() - 100) / 50][p1.getX() / 50 - 1] != 1 && map[(p1.getY() - 100) / 50][p1.getX() / 50 - 1] != 3 && map[(p1.getY() - 100) / 50][p1.getX() / 50 - 1] != 4)
                {
                    p1.moveLeft();
                }

                if(e.getCode() == KeyCode.D && p1.getX() != 500 && map[(p1.getY() - 100) / 50][p1.getX() / 50 + 1] != 1 && map[(p1.getY() - 100) / 50][p1.getX() / 50 + 1] != 3 && map[(p1.getY() - 100) / 50][p1.getX() / 50 + 1] != 4)
                {
                    p1.moveRight();
                }

                if(e.getCode() == KeyCode.Q && p1Bombs.size() < p1.getBombStorage()){
                    p1Bombs.add(new Bomb(p1.getBombStr(), p1.getX(), p1.getY()));
                    map[(p1.getY() - 100)/ 50][p1.getX() / 50] = 3;
                    p1.placeBomb(true);
                }

                if(e.getCode() == KeyCode.UP && p2.getY() != 100 && map[(p2.getY() - 100 - 50) / 50][p2.getX() / 50] != 1 && map[(p2.getY() - 100 - 50) / 50][p2.getX() / 50] != 3 && map[(p2.getY() - 100 - 50) / 50][p2.getX() / 50] != 4)
                {
                    p2.moveUp();
                }

                if(e.getCode() == KeyCode.DOWN && p2.getY() != 600 && map[(p2.getY() - 100 + 50) / 50][p2.getX() / 50] != 1 && map[(p2.getY() - 100 + 50) / 50][p2.getX() / 50] != 3 && map[(p2.getY() - 100 + 50) / 50][p2.getX() / 50] != 4)
                {
                    p2.moveDown();
                }

                if(e.getCode() == KeyCode.LEFT && p2.getX() != 0 && map[(p2.getY() - 100) / 50][p2.getX() / 50 - 1] != 1 && map[(p2.getY() - 100) / 50][p2.getX() / 50 - 1] != 3 && map[(p2.getY() - 100) / 50][p2.getX() / 50 - 1] != 4)
                {
                    p2.moveLeft();
                }

                if(e.getCode() == KeyCode.RIGHT && p2.getX() != 500 && map[(p2.getY() - 100) / 50][p2.getX() / 50 + 1] != 1 && map[(p2.getY() - 100) / 50][p2.getX() / 50 + 1] != 3 && map[(p2.getY() - 100) / 50][p2.getX() / 50 + 1] != 4)
                {
                    p2.moveRight();
                }

                if(e.getCode() == KeyCode.SPACE && p2Bombs.size() < p2.getBombStorage()){
                    p2Bombs.add(new Bomb(p2.getBombStr(), p2.getX(), p2.getY()));
                    map[(p2.getY() - 100) / 50][p2.getX() / 50] = 3;
                    p2.placeBomb(true);
                }
            });

        canvas.setOnMouseClicked(e -> 
            {
                // do something
            });

        stage.setTitle("B O M B E R M A N");
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();
        tl.play();
    }

    private void run(GraphicsContext gc)
    {
        // color for background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 550, 650);
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map.length; j++){
                if(map[i][j] == 1){
                    gc.setFill(Color.GREY);
                    gc.fillRect(j * 50, i * 50 + 100, 50, 50);
                }
                if(map[i][j] == 4){
                    gc.setFill(Color.BROWN);
                    gc.fillRect(j * 50, i * 50 + 100, 50, 50);
                    blocks.add(new Block(j * 50, i * 50 + 100));
                }
            }
        }
        gc.setFill(Color.BLUE);
        gc.fillText("LIVES: " + p1.getLives(), 100, 90, 300);
        gc.fillRect(p1.getX(), p1.getY(), 50, 50);
        gc.setFill(Color.RED);
        gc.fillText("LIVES: " + p2.getLives(), 400, 90, 300);
        gc.fillRect(p2.getX(), p2.getY(), 50, 50);
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 95, 550, 5);
        gc.fillText("BOMBERMAN", 225, 15);
        gc.setFill(Color.GREY);
        if(p1.isPlaced()){
            for(int b = 0; b < p1Bombs.size(); b++){
                gc.setFill(Color.BLUE);
                if(p1Bombs.get(b).getTime() % 20 == 0){
                    gc.setFill(Color.WHITE);   
                }
                gc.fillOval(p1Bombs.get(b).getX(), p1Bombs.get(b).getY(), 50, 50);
                List<Point> cool = p1Bombs.get(b).explode();
                p1Bombs.get(b).tick();
                if(p1Bombs.get(b).getTime() >= 300){
                    gc.setFill(Color.ORANGE);
                    gc.fillRect(p1Bombs.get(b).getX(), p1Bombs.get(b).getY(), 50, 50);
                    for(int i = 0; i < cool.size() / 4; i++){
                        if(cool.get(i).getX() >= 0 && cool.get(i).getX() < 550 && cool.get(i).getY() < 650 && cool.get(i).getY() >= 100){
                            if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 1){
                                i = cool.size() / 4;
                            }
                            else if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 4){
                                gc.fillRect(cool.get(i).getX(), cool.get(i).getY(), 50, 50);
                                map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] = 5;
                                i = cool.size() / 4;
                            }
                            else if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 5){
                                gc.fillRect(cool.get(i).getX(), cool.get(i).getY(), 50, 50);
                                i = cool.size() / 4;
                            }
                            else{
                                gc.fillRect(cool.get(i).getX(), cool.get(i).getY(), 50, 50);
                                map[(cool.get(i).getY() - 100)/ 50][cool.get(i).getX() / 50] = 2;
                            }
                        }
                    }
                    for(int i = cool.size() / 4; i < cool.size() / 2; i++){
                        if(cool.get(i).getX() >= 0 && cool.get(i).getX() < 550 && cool.get(i).getY() < 650 && cool.get(i).getY() >= 100){
                            if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 1){
                                i = cool.size() / 2;
                            }
                            else if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 4){
                                gc.fillRect(cool.get(i).getX(), cool.get(i).getY(), 50, 50);
                                map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] = 5;
                                i = cool.size() / 2;
                            }
                            else if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 5){
                                gc.fillRect(cool.get(i).getX(), cool.get(i).getY(), 50, 50);
                                i = cool.size() / 2;
                            }
                            else{
                                gc.fillRect(cool.get(i).getX(), cool.get(i).getY(), 50, 50);
                                map[(cool.get(i).getY() - 100)/ 50][cool.get(i).getX() / 50] = 2;
                            }
                        }
                    }
                    for(int i = cool.size() / 2; i < cool.size() * 3 / 4; i++){
                        if(cool.get(i).getX() >= 0 && cool.get(i).getX() < 550 && cool.get(i).getY() < 650 && cool.get(i).getY() >= 100){
                            if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 1){
                                i = cool.size() * 3 / 4;
                            }
                            else if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 4){
                                gc.fillRect(cool.get(i).getX(), cool.get(i).getY(), 50, 50);
                                map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] = 5;
                                i = cool.size() * 3 / 4;
                            }
                            else if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 5){
                                gc.fillRect(cool.get(i).getX(), cool.get(i).getY(), 50, 50);
                                i = cool.size() * 3 / 4;
                            }
                            else{
                                gc.fillRect(cool.get(i).getX(), cool.get(i).getY(), 50, 50);
                                map[(cool.get(i).getY() - 100)/ 50][cool.get(i).getX() / 50] = 2;
                            }
                        }
                    }
                    for(int i = cool.size() * 3 / 4; i < cool.size(); i++){
                        if(cool.get(i).getX() >= 0 && cool.get(i).getX() < 550 && cool.get(i).getY() < 650 && cool.get(i).getY() >= 100){
                            if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 1){
                                i = cool.size();
                            }
                            else if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 4){
                                gc.fillRect(cool.get(i).getX(), cool.get(i).getY(), 50, 50);
                                map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] = 5;
                                i = cool.size();
                            }
                            else if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 5){
                                gc.fillRect(cool.get(i).getX(), cool.get(i).getY(), 50, 50);
                                i = cool.size();
                            }
                            else{
                                gc.fillRect(cool.get(i).getX(), cool.get(i).getY(), 50, 50);
                                map[(cool.get(i).getY() - 100)/ 50][cool.get(i).getX() / 50] = 2;
                            }
                        }
                    }
                    if(p1Bombs.get(b).getTime() == 360){
                        for(int i = 0; i < map.length; i++){
                            for(int j = 0; j < map[0].length; j++){
                                if(map[i][j] == 2){
                                    map[i][j] = 0;
                                }
                            }
                        }
                        map[(p1Bombs.get(b).getY() - 100) / 50][p1Bombs.get(b).getX() / 50] = 0;
                        p1Bombs.remove(b);
                    }
                }
            }
            if(p1Bombs.size() == 0){
                p1.placeBomb(false);
            }   
        }
        if(p2.isPlaced()){
            for(int b = 0; b < p2Bombs.size(); b++){
                gc.setFill(Color.RED);
                if(p2Bombs.get(b).getTime() % 20 == 0){
                    gc.setFill(Color.WHITE);   
                }
                gc.fillOval(p2Bombs.get(b).getX(), p2Bombs.get(b).getY(), 50, 50);
                List<Point> cool = p2Bombs.get(b).explode();
                p2Bombs.get(b).tick();
                if(p2Bombs.get(b).getTime() >= 300){
                    gc.setFill(Color.ORANGE);
                    gc.fillRect(p2Bombs.get(b).getX(), p2Bombs.get(b).getY(), 50, 50);
                    for(int i = 0; i < cool.size() / 4; i++){
                        if(cool.get(i).getX() >= 0 && cool.get(i).getX() < 550 && cool.get(i).getY() < 650 && cool.get(i).getY() >= 100){
                            if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 1){
                                i = cool.size() / 4;
                            }
                            else{
                                gc.fillRect(cool.get(i).getX(), cool.get(i).getY(), 50, 50);
                                map[(cool.get(i).getY() - 100)/ 50][cool.get(i).getX() / 50] = 2;
                            }
                        }
                    }
                    for(int i = cool.size() / 4; i < cool.size() / 2; i++){
                        if(cool.get(i).getX() >= 0 && cool.get(i).getX() < 550 && cool.get(i).getY() < 650 && cool.get(i).getY() >= 100){
                            if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 1){
                                i = cool.size() / 2;
                            }
                            else{
                                gc.fillRect(cool.get(i).getX(), cool.get(i).getY(), 50, 50);
                                map[(cool.get(i).getY() - 100)/ 50][cool.get(i).getX() / 50] = 2;
                            }
                        }
                    }
                    for(int i = cool.size() / 2; i < cool.size() * 3 / 4; i++){
                        if(cool.get(i).getX() >= 0 && cool.get(i).getX() < 550 && cool.get(i).getY() < 650 && cool.get(i).getY() >= 100){
                            if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 1){
                                i = cool.size() * 3 / 4;
                            }
                            else{
                                gc.fillRect(cool.get(i).getX(), cool.get(i).getY(), 50, 50);
                                map[(cool.get(i).getY() - 100)/ 50][cool.get(i).getX() / 50] = 2;
                            }
                        }
                    }
                    for(int i = cool.size() * 3 / 4; i < cool.size(); i++){
                        if(cool.get(i).getX() >= 0 && cool.get(i).getX() < 550 && cool.get(i).getY() < 650 && cool.get(i).getY() >= 100){
                            if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 1){
                                i = cool.size();
                            }
                            else{
                                gc.fillRect(cool.get(i).getX(), cool.get(i).getY(), 50, 50);
                                map[(cool.get(i).getY() - 100)/ 50][cool.get(i).getX() / 50] = 2;
                            }
                        }
                    }
                    if(p2Bombs.get(b).getTime() == 360){
                        for(int i = 0; i < map.length; i++){
                            for(int j = 0; j < map[0].length; j++){
                                if(map[i][j] == 2){
                                    map[i][j] = 0;
                                }
                            }
                        }
                        map[(p2Bombs.get(b).getY() - 100) / 50][p2Bombs.get(b).getX() / 50] = 0;
                        p2Bombs.remove(b);
                    }
                }
            }
            if(p2Bombs.size() == 0){
                p2.placeBomb(false);
            }   
        }
        // objects on screen
        checkManHit();
        checkManCollect();

    }

    // run program
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    public static void checkManHit(){

    }

    public static void checkManCollect(){

    }
}
