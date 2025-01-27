import java.awt.geom.Point2D;

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
     * There exists at least one set of three data points separated by exactly A_PTS
     * and B_PTS consecutive intervening points, respectively, that cannot be
     * contained within or on a circle of radius RADIUS1. The condition is not met
     * when NUMPOINTS < 5.
     * 
     * @author Linus Dinesjö
     * 
     * @return true if the condition is met, false otherwise
     */
    public boolean lic8() {
        if (NUMPOINTS != COORDINATES.length) {
            throw new IllegalArgumentException("NUMPOINTS does not match the amount of coordinates in COORDINATES");
        }
        if (PARAMETERS.A_PTS < 1 || PARAMETERS.B_PTS < 1 || PARAMETERS.A_PTS + PARAMETERS.B_PTS > NUMPOINTS - 3) {
            throw new IllegalArgumentException("A_PTS and B_PTS are not valid");
        }
        if (NUMPOINTS < 5) {
            return false;
        }

        for (int i = 0; i < NUMPOINTS - PARAMETERS.A_PTS - PARAMETERS.B_PTS - 2; i++) {
            Point2D[] circlePoints = new Point2D[3];
            circlePoints[0] = COORDINATES[i];
            circlePoints[1] = COORDINATES[i + PARAMETERS.A_PTS + 1];
            circlePoints[2] = COORDINATES[i + PARAMETERS.A_PTS + PARAMETERS.B_PTS + 2];
            // Get maximum distance between two of the three points
            double maxDistance = 0;
            for (int j = 0; j < 3; j++) {
                for (int k = j + 1; k < 3; k++) {
                    double distance = circlePoints[j].distance(circlePoints[k]);
                    if (distance > maxDistance) {
                        maxDistance = distance;
                    }
                }
            }

            if (maxDistance > 2 * PARAMETERS.RADIUS1) {
                return true;
            }
        }

        return false;
    }

    public void decide() {
        launch = true;
    }

    public static void main(String[] args) {

    }
}