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
    private int timer1, timer2;
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
        timer1 = 0;
        timer2 = 0;
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
        canvas.setOnKeyPressed(e ->
            {
                if(e.getCode() == KeyCode.W && p1.getY() != 100 && map[(p1.getY() - 100 - 50) / 50][p1.getX() / 50] == 0)
                {
                    p1.moveUp();
                }

                if(e.getCode() == KeyCode.S && p1.getY() != 600 && map[(p1.getY() - 100 + 50) / 50][p1.getX() / 50] == 0)
                {
                    p1.moveDown();
                }

                if(e.getCode() == KeyCode.A && p1.getX() != 0 && map[(p1.getY() - 100) / 50][p1.getX() / 50 - 1] == 0)
                {
                    p1.moveLeft();
                }

                if(e.getCode() == KeyCode.D && p1.getX() != 500 && map[(p1.getY() - 100) / 50][p1.getX() / 50 + 1] == 0)
                {
                    p1.moveRight();
                }

                if(e.getCode() == KeyCode.Q && !p1.isPlaced()){
                    p1Bomb = new Bomb(p1.getBombStr(), p1.getX(), p1.getY());
                    map[(p1.getY() - 100)/ 50][p1.getX() / 50] = 2;
                    p1.placeBomb(true);
                }

                if(e.getCode() == KeyCode.UP && p2.getY() != 100 && map[(p2.getY() - 100 - 50) / 50][p2.getX() / 50] == 0)
                {
                    p2.moveUp();
                }

                if(e.getCode() == KeyCode.DOWN && p2.getY() != 600 && map[(p2.getY() - 100 + 50) / 50][p2.getX() / 50] == 0)
                {
                    p2.moveDown();
                }

                if(e.getCode() == KeyCode.LEFT && p2.getX() != 0 && map[(p2.getY() - 100) / 50][p2.getX() / 50 - 1] == 0)
                {
                    p2.moveLeft();
                }

                if(e.getCode() == KeyCode.RIGHT && p2.getX() != 500 && map[(p2.getY() - 100) / 50][p2.getX() / 50 + 1] == 0)
                {
                    p2.moveRight();
                }

                if(e.getCode() == KeyCode.SPACE && !p2.isPlaced()){
                    p2Bomb = new Bomb(p2.getBombStr(), p2.getX(), p2.getY());
                    map[(p2.getY() - 100) / 50][p2.getX() / 50] = 2;
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
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map.length; j++){
                if(map[i][j] == 1){
                    gc.fillRect(j * 50, i * 50 + 100, 50, 50);
                }
            }
        }
        if(p1.isPlaced()){
            gc.setFill(Color.ORANGE);
            gc.fillOval(p1Bomb.getX(), p1Bomb.getY(), 50, 50);
            List<Point> cool = p1Bomb.explode();
            if(timer1 >= 300){
                for(int i = 0; i < cool.size(); i++){
                    if(cool.get(i).getX() >= 0 && cool.get(i).getX() < 550 && cool.get(i).getY() < 650 && cool.get(i).getY() >= 100){
                        gc.fillRect(cool.get(i).getX(), cool.get(i).getY(), 50, 50);
                        map[(cool.get(i).getY() - 100)/ 50][cool.get(i).getX() / 50] = 2;
                    }
                }
                if(timer1 == 360){
                    for(int i = 0; i < cool.size(); i++){
                        if(cool.get(i).getX() >= 0 && cool.get(i).getX() < 550 && cool.get(i).getY() < 650 && cool.get(i).getY() >= 100){
                            map[(cool.get(i).getY() - 100)/ 50][cool.get(i).getX() / 50] = 0;
                        }
                    }
                    p1.placeBomb(false);
                    timer1 = 0;
                }
            }
            timer1++;
        }
        if(p2.isPlaced()){
            gc.setFill(Color.ORANGE);
            gc.fillOval(p2Bomb.getX(), p2Bomb.getY(), 50, 50);
            List<Point> cool = p2Bomb.explode();
            if(timer2 >= 300){
                for(int i = 0; i < cool.size(); i++){
                    if(cool.get(i).getX() >= 0 && cool.get(i).getX() < 550 && cool.get(i).getY() < 650 && cool.get(i).getY() >= 100){
                        gc.fillRect(cool.get(i).getX(), cool.get(i).getY(), 50, 50);
                        map[(cool.get(i).getY() - 100)/ 50][cool.get(i).getX() / 50] = 2;
                    }
                }
                if(timer2 == 360){
                    for(int i = 0; i < cool.size(); i++){
                        if(cool.get(i).getX() >= 0 && cool.get(i).getX() < 550 && cool.get(i).getY() < 650 && cool.get(i).getY() >= 100){
                            map[(cool.get(i).getY() - 100)/ 50][cool.get(i).getX() / 50] = 0;
                        }
                    }
                    p2.placeBomb(false);
                    timer2 = 0;
                }
            }
            timer2++;
        }
        // objects on screen

    }

    // run program
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    public static void checkManHit(){

    }

    public static void checkBlockHit(){

    }
}
