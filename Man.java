public class Man{
    private String name;
    private int lives;
    private int x, y, bombStr, bombStorage;
    private boolean placedBomb;
    private boolean inv;
    public Man(String name, int x, int y){
        this.name = name;
        lives = 3;
        this.x = x;
        this.y = y;
        bombStr = 50;
        placedBomb = false;
        bombStorage = 1;
        inv = false;
        
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
    
    public void increaseBombStorage(){
        bombStorage++;
    }
    
    public void setBombStorage(int p){
        bombStorage = p;
    }
    
    public int getBombStorage(){
        return bombStorage;
    }
    
    public boolean isInvincible() {
    	return inv;
    }
    
    public void revInv() {
    	inv = !inv;
    }
}
