import java.awt.geom.Point2D;

public class Decide {
    public static enum CONNECTORS{
        ANDD,
        ORR,
        NOTUSED
    }

    // Inputs to the DECIDE() function
    public class PARAMETERS_T {
        double LENTH1;          
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
        double LENTH2;
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
    

    public void decide(){
        launch = true;
    }

    public static void main(String[] args) {
        
    }
}