public class Block{
    private int x, y, item;
    public Block(int x, int y){
        this.x = x;
        this.y = y;
        item = (int)Math.random() * 4;
    }
    public int destroy(){
        return item;
    }
}
