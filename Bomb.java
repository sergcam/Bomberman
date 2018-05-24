import java.util.List;
import java.util.ArrayList;
public class Bomb{
    private int blastRadius, x, y, time;
    public Bomb(int b, int x, int y){
        blastRadius = b;
        this.x = x;
        this.y = y;
        time = 0;
    }
    //returns Points of affected tiles based on initial point and blastRadius
    public List<Point> explode(){
        List cool = new ArrayList<Point>();
        for(int i = x; i >= x - blastRadius; i -= 50){
            if(i != x){
                cool.add(new Point(i, y));
            }
        }
        for(int i = x; i <= x + blastRadius; i += 50){
            if(i != x){
                cool.add(new Point(i, y));
            }
        }
        for(int i = y; i >= y - blastRadius; i -= 50){
            if(i != y){
                cool.add(new Point(x, i));
            }
        }
        for(int i = y; i <= y + blastRadius; i += 50){
            if(i != y){
                cool.add(new Point(x, i));
            }
        }
        return cool;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void tick(){
        time++;
    }
    public int getTime(){
        return time;
    }
    
    public void setTime(int t) {
    	time = t;
    }
}
