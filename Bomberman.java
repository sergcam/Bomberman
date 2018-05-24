import sun.audio.*;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javafx.scene.input.KeyCode;
import javafx.animation.Animation;
import javafx.scene.image.Image;

public class Bomberman extends Application
{
    Scanner in = new Scanner(System.in);
    // private instance variables
    private Man p1, p2;
    private Bomb p1Bomb, p2Bomb;
    private int mapWidth, mapHeight;
    private static int[][] map;
    private List<Bomb> p1Bombs, p2Bombs;
    private List<Block> blocks;
    private static AudioPlayer MGP;
    private static AudioStream BGM;
    private int invTimer1, invTimer2, wCur, bCur, dir;
    Image bg, wall, block, heart, bomb, flame, bombwhite1, bombwhite2, bombwhite3, bombwhiteCur;
    Image whitemandown, whitemanup, whitemanleft, whitemanright, whitemanCur, bmanLogo;
    Image blackmandown, blackmanup, blackmanleft, blackmanright, blackmanCur,bombblack1, bombblack2, bombblack3, bombblackCur;

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
        wCur = 0;
        bCur = 0;
    }

    @Override 
    public void start(Stage stage) throws FileNotFoundException 
    {
        bg = new Image(Bomberman.class.getResourceAsStream("map.png"));
        wall = new Image(Bomberman.class.getResourceAsStream("wall.png"));
        block = new Image(Bomberman.class.getResourceAsStream("block.png"));
        flame = new Image(Bomberman.class.getResourceAsStream("flame.png"));
        heart = new Image(Bomberman.class.getResourceAsStream("heart.png"));
        bomb = new Image(Bomberman.class.getResourceAsStream("bomb.png"));
        bombwhite1 = new Image(Bomberman.class.getResourceAsStream("bombwhite1.png"));
        bombwhite2 = new Image(Bomberman.class.getResourceAsStream("bombwhite2.png"));
        bombwhite3 = new Image(Bomberman.class.getResourceAsStream("bombwhite3.png"));
        bombwhiteCur = bombwhite1;
        bombblack1 = new Image(Bomberman.class.getResourceAsStream("bombblack1.png"));
        bombblack2 = new Image(Bomberman.class.getResourceAsStream("bombblack2.png"));
        bombblack3 = new Image(Bomberman.class.getResourceAsStream("bombblack3.png"));
        bombblackCur = bombblack1;
        whitemanup = new Image(Bomberman.class.getResourceAsStream("whitemanup.png"));
        whitemanright = new Image(Bomberman.class.getResourceAsStream("whitemanright.png"));
        whitemanleft = new Image(Bomberman.class.getResourceAsStream("whitemanleft.png"));
        whitemandown = new Image(Bomberman.class.getResourceAsStream("whitemandown.png"));
        whitemanCur = whitemandown;
        blackmanup = new Image(Bomberman.class.getResourceAsStream("blackmanup.png"));
        blackmanright = new Image(Bomberman.class.getResourceAsStream("blackmanright.png"));
        blackmanleft = new Image(Bomberman.class.getResourceAsStream("blackmanleft.png"));
        blackmandown = new Image(Bomberman.class.getResourceAsStream("blackmandown.png"));
        blackmanCur = blackmandown;
        bmanLogo = new Image(Bomberman.class.getResourceAsStream("bomberman.png"));

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
         * 10: fire up
         * 11: bomb up
         * 12: life up
         * -2 = fire max
         * -3 = bomb max
         */
        canvas.setOnKeyPressed(e ->
            {
                if(e.getCode() == KeyCode.W && p1.getY() != 100 && map[(p1.getY() - 100 - 50) / 50][p1.getX() / 50] != 1 && map[(p1.getY() - 100 - 50) / 50][p1.getX() / 50] != 3 && map[(p1.getY() - 100 - 50) / 50][p1.getX() / 50] != 4)
                {
                    p1.moveUp();
                    whitemanCur = whitemanup;
                }

                if(e.getCode() == KeyCode.S && p1.getY() != 600 && map[(p1.getY() - 100 + 50) / 50][p1.getX() / 50] != 1 && map[(p1.getY() - 100 + 50) / 50][p1.getX() / 50] != 3 && map[(p1.getY() - 100 + 50) / 50][p1.getX() / 50] != 4)
                {
                    p1.moveDown();
                    whitemanCur = whitemandown;
                }

                if(e.getCode() == KeyCode.A && p1.getX() != 0 && map[(p1.getY() - 100) / 50][p1.getX() / 50 - 1] != 1 && map[(p1.getY() - 100) / 50][p1.getX() / 50 - 1] != 3 && map[(p1.getY() - 100) / 50][p1.getX() / 50 - 1] != 4)
                {
                    p1.moveLeft();
                    whitemanCur = whitemanleft;
                }

                if(e.getCode() == KeyCode.D && p1.getX() != 500 && map[(p1.getY() - 100) / 50][p1.getX() / 50 + 1] != 1 && map[(p1.getY() - 100) / 50][p1.getX() / 50 + 1] != 3 && map[(p1.getY() - 100) / 50][p1.getX() / 50 + 1] != 4)
                {
                    p1.moveRight();
                    whitemanCur = whitemanright;
                }

                if(e.getCode() == KeyCode.Q && p1Bombs.size() < p1.getBombStorage()){
                    p1Bombs.add(new Bomb(p1.getBombStr(), p1.getX(), p1.getY()));
                    map[(p1.getY() - 100)/ 50][p1.getX() / 50] = 3;
                    p1.placeBomb(true);
                    wCur = 0;
                }

                if(e.getCode() == KeyCode.UP && p2.getY() != 100 && map[(p2.getY() - 100 - 50) / 50][p2.getX() / 50] != 1 && map[(p2.getY() - 100 - 50) / 50][p2.getX() / 50] != 3 && map[(p2.getY() - 100 - 50) / 50][p2.getX() / 50] != 4)
                {
                    p2.moveUp();
                    blackmanCur = blackmanup;
                }

                if(e.getCode() == KeyCode.DOWN && p2.getY() != 600 && map[(p2.getY() - 100 + 50) / 50][p2.getX() / 50] != 1 && map[(p2.getY() - 100 + 50) / 50][p2.getX() / 50] != 3 && map[(p2.getY() - 100 + 50) / 50][p2.getX() / 50] != 4)
                {
                    p2.moveDown();
                    blackmanCur = blackmandown;
                }

                if(e.getCode() == KeyCode.LEFT && p2.getX() != 0 && map[(p2.getY() - 100) / 50][p2.getX() / 50 - 1] != 1 && map[(p2.getY() - 100) / 50][p2.getX() / 50 - 1] != 3 && map[(p2.getY() - 100) / 50][p2.getX() / 50 - 1] != 4)
                {
                    p2.moveLeft();
                    blackmanCur = blackmanleft;
                }

                if(e.getCode() == KeyCode.RIGHT && p2.getX() != 500 && map[(p2.getY() - 100) / 50][p2.getX() / 50 + 1] != 1 && map[(p2.getY() - 100) / 50][p2.getX() / 50 + 1] != 3 && map[(p2.getY() - 100) / 50][p2.getX() / 50 + 1] != 4)
                {
                    p2.moveRight();
                    blackmanCur = blackmanright;
                }

                if(e.getCode() == KeyCode.SPACE && p2Bombs.size() < p2.getBombStorage()){
                    p2Bombs.add(new Bomb(p2.getBombStr(), p2.getX(), p2.getY()));
                    map[(p2.getY() - 100) / 50][p2.getX() / 50] = 3;
                    p2.placeBomb(true);
                    bCur = 0;
                }
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
        gc.drawImage(bg, 0, 100);
        
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map.length; j++){
                if(map[i][j] == 1){
                    gc.drawImage(wall, j * 50, i * 50 + 100);
                }
                if(map[i][j] == 4){
                    gc.drawImage(block, j * 50, i * 50 + 100);
                    blocks.add(new Block(j * 50, i * 50 + 100));
                }
                if(map[i][j] == 10){
                    gc.drawImage(flame, j * 50, i * 50 + 100);
                }
                if(map[i][j] == 11){
                	gc.drawImage(bomb, j * 50, i * 50 + 100);
                }
                if(map[i][j] == 12){
                	gc.drawImage(heart, j * 50, i * 50 + 100);
                }
                if(map[i][j] > 12){
                	map[i][j] = -1;
                }
                if(map[i][j] == -2){
                    //grapghics for max bomb
                }
                if(map[i][j] == -3){
                    //graphics for max fire
}
            }
        }
        if(checkManHit(p1)) {
        	p1.revInv();
        	invTimer1 = 0;
        }
        invTimer1++;
        if(invTimer1 == 90 && p1.isInvincible()) {
        	p1.revInv();
        }
        if(checkManHit(p2)) {
        	invTimer2 = 0;
        	p2.revInv();
        }
        invTimer2++;
        if(invTimer2 == 90 && p2.isInvincible()) {
        	p2.revInv();
        }
        if(checkManCollect(p1) > 0) {
        	int cur = checkManCollect(p1);
        	if(cur == 10) {
        		p1.increaseBombStr();
        	}
        	if(cur == 11) {
        		p1.increaseBombStorage();
        	}
        	if(cur == 12) {
        		p1.increaseLives();
        	}
        	map[(p1.getY() - 100) / 50][p1.getX() / 50] = 0;
        }
        if(checkManCollect(p2) > 0) {
        	int cur = checkManCollect(p2);
        	if(cur == 10) {
        		p2.increaseBombStr();
        	}
        	if(cur == 11) {
        		p2.increaseBombStorage();
        	}
        	if(cur == 12) {
        		p2.increaseLives();
        	}
        	map[(p2.getY() - 100) / 50][p2.getX() / 50] = 0;
        }
//        if(!checkWin().equals("0")){
//        	AudioPlayer.player.stop(BGM);
//        	int p = 0;
//        	while(p >= 0) {
//        		if(checkWin().equals("p1")) {
//
//        		}
//        		if(checkWin().equals("p2")) {
//
//        		}
//        		if(checkWin().equals("tie")) {
//
//        		}
//        	}
//        }

        gc.setFill(Color.BLUE);
        gc.fillText("LIVES: " + p1.getLives(), 100, 90, 300);
        gc.drawImage(whitemanCur,p1.getX(), p1.getY() - 15);
        gc.setFill(Color.RED);
        gc.fillText("LIVES: " + p2.getLives(), 400, 90, 300);
        gc.drawImage(blackmanCur, p2.getX(), p2.getY() - 15);
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 95, 550, 5);
        gc.drawImage(bmanLogo, 125, 5);
        gc.setFill(Color.GREY);
        if(p1.isPlaced()){
        	for(int b = 0; b < p1Bombs.size(); b++){
        		if(p1Bombs.get(b).getTime() < 359) {
        			if(p1Bombs.get(b).getTime() % 25 == 0){
        				cycleWhite();
        			}
        			gc.drawImage(bombwhiteCur, p1Bombs.get(b).getX(), p1Bombs.get(b).getY());
        		}
        		List<Point> cool = p1Bombs.get(b).explode();
        		p1Bombs.get(b).tick();
        		if(p1Bombs.get(b).getTime() >= 300 && p1Bombs.get(b).getTime() < 360){
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
                            else if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 3){
                                for(int bombInt = 0; bombInt < p1Bombs.size(); bombInt++){
                                    if(p1Bombs.get(bombInt).getX() == cool.get(i).getX() && p1Bombs.get(bombInt).getY() == cool.get(i).getY()){
                                        if(p1Bombs.get(bombInt).getTime() < 300){
                                            p1Bombs.get(bombInt).setTick(p1Bombs.get(b).getTime());
                                        }
                                    }
                                }
                                map[(cool.get(i).getY() - 100)/ 50][cool.get(i).getX() / 50] = 2;
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
                            else if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 3){
                                for(int bombInt = 0; bombInt < p1Bombs.size(); bombInt++){
                                    if(p1Bombs.get(bombInt).getX() == cool.get(i).getX() && p1Bombs.get(bombInt).getY() == cool.get(i).getY()){
                                        if(p1Bombs.get(bombInt).getTime() < 300){
                                            p1Bombs.get(bombInt).setTick(p1Bombs.get(b).getTime());
                                        }
                                    }
                                }
                                map[(cool.get(i).getY() - 100)/ 50][cool.get(i).getX() / 50] = 2;
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
                            else if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 3){
                                for(int bombInt = 0; bombInt < p1Bombs.size(); bombInt++){
                                    if(p1Bombs.get(bombInt).getX() == cool.get(i).getX() && p1Bombs.get(bombInt).getY() == cool.get(i).getY()){
                                        if(p1Bombs.get(bombInt).getTime() < 300){
                                            p1Bombs.get(bombInt).setTick(p1Bombs.get(b).getTime());
                                        }
                                    }
                                }
                                map[(cool.get(i).getY() - 100)/ 50][cool.get(i).getX() / 50] = 2;
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
                            else if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 3){
                                for(int bombInt = 0; bombInt < p1Bombs.size(); bombInt++){
                                    if(p1Bombs.get(bombInt).getX() == cool.get(i).getX() && p1Bombs.get(bombInt).getY() == cool.get(i).getY()){
                                        if(p1Bombs.get(bombInt).getTime() < 300){
                                            p1Bombs.get(bombInt).setTick(p1Bombs.get(b).getTime());
                                        }
                                    }
                                }
                                map[(cool.get(i).getY() - 100)/ 50][cool.get(i).getX() / 50] = 2;
                            }
                            else{
                                gc.fillRect(cool.get(i).getX(), cool.get(i).getY(), 50, 50);
                                map[(cool.get(i).getY() - 100)/ 50][cool.get(i).getX() / 50] = 2;
                            }
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
                    for(int i = 0; i < map.length; i++){
                        for(int j = 0; j < map[0].length; j++){
                            if(map[i][j] == 5){
                                for(int blockInt = 0; blockInt < blocks.size(); blockInt++){
                                    if(blocks.get(blockInt).getX() == j * 50 && blocks.get(blockInt).getY() == i * 50 + 100){
                                        map[i][j] = blocks.get(blockInt).destroy();
                                    }
                                }
                            }
                        }
                    }
                    map[(p1Bombs.get(b).getY() - 100) / 50][p1Bombs.get(b).getX() / 50] = 0;
                    p1Bombs.remove(b);
                }

            }
            if(p1Bombs.size() == 0){
                p1.placeBomb(false);
            }   
}
        if(p2.isPlaced()){
        	for(int b = 0; b < p2Bombs.size(); b++){
        		if(p2Bombs.get(b).getTime() < 359) {
        			if(p2Bombs.get(b).getTime() % 25 == 0){
        				cycleBlack();
        			}
        			gc.drawImage(bombblackCur, p2Bombs.get(b).getX(), p2Bombs.get(b).getY());
        		}
                List<Point> cool = p2Bombs.get(b).explode();
                p2Bombs.get(b).tick();
                if(p2Bombs.get(b).getTime() >= 300 && p2Bombs.get(b).getTime() < 360){
                    gc.setFill(Color.ORANGE);
                    gc.fillRect(p2Bombs.get(b).getX(), p2Bombs.get(b).getY(), 50, 50);
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
                            else if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 3){
                                for(int bombInt = 0; bombInt < p2Bombs.size(); bombInt++){
                                    if(p2Bombs.get(bombInt).getX() == cool.get(i).getX() && p2Bombs.get(bombInt).getY() == cool.get(i).getY()){
                                        if(p2Bombs.get(bombInt).getTime() < 300){
                                            p2Bombs.get(bombInt).setTick(p2Bombs.get(b).getTime());
                                        }
                                    }
                                }
                                map[(cool.get(i).getY() - 100)/ 50][cool.get(i).getX() / 50] = 2;
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
                            else if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 3){
                                for(int bombInt = 0; bombInt < p2Bombs.size(); bombInt++){
                                    if(p2Bombs.get(bombInt).getX() == cool.get(i).getX() && p2Bombs.get(bombInt).getY() == cool.get(i).getY()){
                                        if(p2Bombs.get(bombInt).getTime() < 300){
                                            p2Bombs.get(bombInt).setTick(p2Bombs.get(b).getTime());
                                        }
                                    }
                                }
                                map[(cool.get(i).getY() - 100)/ 50][cool.get(i).getX() / 50] = 2;
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
                            else if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 3){
                                for(int bombInt = 0; bombInt < p2Bombs.size(); bombInt++){
                                    if(p2Bombs.get(bombInt).getX() == cool.get(i).getX() && p2Bombs.get(bombInt).getY() == cool.get(i).getY()){
                                        if(p2Bombs.get(bombInt).getTime() < 300){
                                            p2Bombs.get(bombInt).setTick(p2Bombs.get(b).getTime());
                                        }
                                    }
                                }
                                map[(cool.get(i).getY() - 100)/ 50][cool.get(i).getX() / 50] = 2;
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
                            else if(map[(cool.get(i).getY() - 100) / 50][cool.get(i).getX() / 50] == 3){
                                for(int bombInt = 0; bombInt < p2Bombs.size(); bombInt++){
                                    if(p2Bombs.get(bombInt).getX() == cool.get(i).getX() && p2Bombs.get(bombInt).getY() == cool.get(i).getY()){
                                        if(p2Bombs.get(bombInt).getTime() < 300){
                                            p2Bombs.get(bombInt).setTick(p2Bombs.get(b).getTime());
                                        }
                                    }
                                }
                                map[(cool.get(i).getY() - 100)/ 50][cool.get(i).getX() / 50] = 2;
                            }
                            else{
                                gc.fillRect(cool.get(i).getX(), cool.get(i).getY(), 50, 50);
                                map[(cool.get(i).getY() - 100)/ 50][cool.get(i).getX() / 50] = 2;
                            }
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
                    for(int i = 0; i < map.length; i++){
                        for(int j = 0; j < map[0].length; j++){
                            if(map[i][j] == 5){
                                for(int blockInt = 0; blockInt < blocks.size(); blockInt++){
                                    if(blocks.get(blockInt).getX() == j * 50 && blocks.get(blockInt).getY() == i * 50 + 100){
                                        map[i][j] = blocks.get(blockInt).destroy();
                                    }
                                }
                            }
                        }
                    }
                    map[(p2Bombs.get(b).getY() - 100) / 50][p2Bombs.get(b).getX() / 50] = 0;
                    p2Bombs.remove(b);
                }

            }
            if(p2Bombs.size() == 0){
                p2.placeBomb(false);
            }   
}
        // objects on screen
        if(checkManHit(p1)){
            p1.decreaseLives();
        }
        if(checkManHit(p2)){
            p2.decreaseLives();
        }
    }

    // run program
    public static void main(String[] args)
    {
    	sound("bgm");
        Application.launch(args);
    }

    public static boolean checkManHit(Man x){
    	if(map[(x.getY() - 100) / 50][x.getX() / 50] == 2 && !x.isInvincible()) {
    		return true;    
    	}
    	return false;
    }

    public int checkManCollect(Man x){
    	if(map[(x.getY() - 100) / 50][x.getX() / 50] == 10 || map[(x.getY() - 100) / 50][x.getX() / 50] == 11 || map[(x.getY() - 100) / 50][x.getX() / 50] == 12) {
    		return map[(x.getY() - 100) / 50][x.getX() / 50];    
    	}
    	return -1;
    }
    
    public void cycleWhite() {
    	if(wCur == 0) {
    		bombwhiteCur = bombwhite1;
    		dir = 1;
    		wCur += dir;
    	}
    	else if(wCur == 1) {
    		bombwhiteCur = bombwhite2;
    		wCur += dir;
    	}
    	else if(wCur == 2) {
    		bombwhiteCur = bombwhite3;
    		dir = -1;
    		wCur += dir;
    	}
    }
    
    public void cycleBlack() {
    	if(bCur == 0) {
    		bombblackCur = bombblack1;
    		dir = 1;
    		bCur += dir;
    	}
    	else if(bCur == 1) {
    		bombblackCur = bombblack2;
    		bCur += dir;
    	}
    	else if(bCur == 2) {
    		bombblackCur = bombblack3;
    		dir = -1;
    		bCur += dir;
    	}
    }
    //returns string of winner or tie if die at same time. return 0 if game not over
    public String checkWin() {
    	if(p1.getLives() == 0 || p2.getLives() == 0) {
    		if(p1.getLives() > p2.getLives()) {
    			return "p1";
    		}
    		else if(p1.getLives() < p2.getLives()) {
    			return "p2";
    		}    		
    		return "tie";  		
    	}
    	return "0";
    }
    
  //sound is desired sound to be played
    public static void sound(String sound) {
    	MGP = AudioPlayer.player;

        ContinuousAudioDataStream loop = null;

        try
        {
            InputStream soundFile = null;
            if(sound.equals("bgm")) {
            	soundFile = new FileInputStream("C:\\Users\\sergi\\eclipse-workspace\\Bomberman\\music\\Super Bomberman - Area 1 music.wav");
            }
            BGM = new AudioStream(soundFile);
            AudioPlayer.player.start(BGM);
        }
        catch(FileNotFoundException e){
            System.out.print(e.toString());
        }
        catch(IOException error)
        {
            System.out.print(error.toString());
        }
       
        	MGP.start(loop);
        	
        
    }
}
