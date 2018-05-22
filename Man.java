public class Man{
    private String name;
    private int lives;
    private int x, y, bombStr;
    private boolean placedBomb;
    public Man(String name, int x, int y){
        this.name = name;
        lives = 3;
        this.x = x;
        this.y = y;
        bombStr = 50;
        placedBomb = false;
    }

    public void moveUp(){
        y -= 50;
    }

    public void moveDown(){
        y += 50;
    }

    public void moveRight(){
        x += 50;
    }  

    public void moveLeft(){
        x -= 50;
    }
    
    public void increaseLives(){
        lives++;
    }

    public void decreaseLives(){
        lives --;
    }
    
    public int getLives(){
        return lives;
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public int getBombStr(){
        return bombStr;
    }
    
    public void increaseBombStr(){
        bombStr += 50;
    }
    
    public void setBombStr(int x){
        bombStr = x;
    }
    
    public void placeBomb(boolean x){
        placedBomb = x;
    }
    
    public boolean isPlaced(){
        return placedBomb;
    }
}
