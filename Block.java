public class Block{
    private int x, y, item;
    public Block(int x, int y){
        this.x = x;
        this.y = y;
        item = ((int)(Math.random() * 7)) + 10;
    }
    public int destroy(){
        return item;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}
