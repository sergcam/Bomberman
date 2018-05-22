import java.util.List;
import java.util.ArrayList;
public class Bomb{
    private int blastRadius, x, y;
    public Bomb(int b, int x, int y){
        blastRadius = b;
        this.x = x;
        this.y = y;
    }
    //returns Points of affected tiles based on initial point and blastRadius
    public List<Point> explode(){
        List cool = new ArrayList<Point>();
        for(int i = x - blastRadius; i < blastRadius + x; i++){
            if(i != x){
                cool.add(new Point(i, y));
            }
        }
        for(int i = y - blastRadius; i < blastRadius + y; i++){
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
}
