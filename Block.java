public class Block{
    private int x, y, item;
    public Block(int x, int y){
        this.x = x;
        this.y = y;
        double random = Math.random();
        if(random >= .95){
            item = -2;
        }
        else{
            item = ((int) (random * 6)) + 10;
        }
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
