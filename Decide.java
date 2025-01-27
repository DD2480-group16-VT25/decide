import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

public class Decide {
    public static enum CONNECTORS{
        ANDD,
        ORR,
        NOTUSED
    }

    // Inputs to the DECIDE() function
    public class PARAMETERS_T {
        double LENGTH1;          
        double RADIUS1;
        double EPSILON;
        double AREA1;
        int Q_PTS;
        int QUADS;
        double DIST;
        int N_PTS;
        int K_PTS;
        int A_PTS;
        int B_PTS;
        int C_PTS;
        int D_PTS;
        int E_PTS;
        int F_PTS;
        int G_PTS;
        double LENGTH2;
        double RADIUS2;
        double AREA2;
    }

    public PARAMETERS_T PARAMETERS;

    // Number of points
    public int NUMPOINTS;

    // 100 x and y coordinates
    public Point2D[] COORDINATES = new Point2D[100];

    // Logical connector matrix (LCM)
    public CONNECTORS[][] LCM = new CONNECTORS[15][15];

    // Preliminary Unlocking Vector (PUV)
    public boolean[] PUV = new boolean[15];

    // Preliminary Unlocking Matrix (PUM)
    boolean[][] PUM = new boolean[15][15];

    // Conditions Met Vecttor (CMV)
    boolean []CMV = new boolean[15];

    // Final Unlocking Vector (FUV)
    boolean[] FUV = new boolean[15];

    // Decision: Launch or Not Launch
    boolean launch;
        
    ///////////////////////////////////////

    /*
     * There exists atleast one set of two consecutive data points 
     * that are greater distance than the length LENGTH1 apart
     * @author Marcus Odin
     * @return
     */
    public boolean lic0(){
        if(NUMPOINTS != COORDINATES.length){
            throw new IllegalArgumentException("NUMPOINTS does not match the amount of coordinates in COORDINATES");
        }
        if(PARAMETERS.LENGTH1 < 0){
            throw new IllegalArgumentException("LENGTH1 is less than 0");
        }

        for(int i = 1; i < COORDINATES.length; i++){
            if(COORDINATES[i-1].distance(COORDINATES[i]) > PARAMETERS.LENGTH1){
                return true;
            }
        }
        return false;
    }

    /*
    * There exists at least one set of Q_PTS consecutive data points that lie in more than QUADS
    * quadrants. Where there is ambiguity as to which quadrant contains a given point, priority
    * of decision will be by quadrant number, i.e., I, II, III, IV. For example, the data point (0,0)
    * is in quadrant I, the point (-1,0) is in quadrant II, the point (0,-1) is in quadrant III, the point
    * (0,1) is in quadrant I and the point (1,0) is in quadrant I.
    * (2 ≤ Q_PTS ≤ NUMPOINTS), (1 ≤ QUADS ≤ 3)
    *
    * @author Ellen Sigurðardóttir
    * @return true if condition is met, otherwise false
    */

    public boolean lic4() {
        Set<Integer> in_quad = new HashSet<>();
        double x, y;

        if(NUMPOINTS != COORDINATES.length){
            throw new IllegalArgumentException("NUMPOINTS does not match the amount of coordinates in COORDINATES");
        }
        if (PARAMETERS.Q_PTS < 2 || PARAMETERS.Q_PTS > NUMPOINTS) {
            throw new IllegalArgumentException("Q_PTS must be in the range 2 ≤ Q_PTS ≤ NUMPOINTS.");
        }
        if (PARAMETERS.QUADS < 1 || PARAMETERS.QUADS > 3) {
            throw new IllegalArgumentException("QUADS must be in the range 1 ≤ QUADS ≤ 3.");
        }

        for (int i = 0; i <= NUMPOINTS - PARAMETERS.Q_PTS; i++) {
            in_quad.clear();

            for (int j = 0; j < PARAMETERS.Q_PTS; j++) {
                x = COORDINATES[i + j].getX();
                y = COORDINATES[i + j].getY();

                if (x > 0 && y > 0) in_quad.add(1);
                if (x < 0 && y >= 0) in_quad.add(2);
                if (x <= 0 && y < 0) in_quad.add(3);
                if (x > 0 && y < 0) in_quad.add(4);
            }
        }
        
        // if consecutive points lie in more than QUADS quadrants
        if (in_quad.size() > PARAMETERS.QUADS) return true;

        return false;
    }

    public void decide(){
        launch = true;
    }

    public static void main(String[] args) {
        
    }
}