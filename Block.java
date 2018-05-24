public class Block{
    private int x, y, item;
    public Block(int x, int y){
        this.x = x;
        this.y = y;
        double random = Math.random();
        if(random >= .95){
            item = -2;
        }
        else if(random < .95 && random >= .89){
            item = 10;
        }
        else if(random < .89 && random >= .72){
          item = 11;  
        }
        else if(random < .72 && random >= .55){
            item = 12;
        }
        else if(random < .55 && random >= .5){
            item = -3;
        }
        else{
            item = 13;
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
