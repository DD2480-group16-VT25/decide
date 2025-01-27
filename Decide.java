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

    /*
     * There exists at least one set of three data points separated by exactly C-PTS
     * and D-PTS consecutive intervening points, respectively, that form an angle
     * such that:
     * angle < (PI - EPSILON)
     * or
     * angle > (PI + EPSILON)
     * The second point of the set of three points is always the vertex of the
     * angle. If either the first point or the last point (or both) coincide with
     * the vertex, the angle is undefined and the LIC is not satisfied by those
     * three points. When NUMPOINTS < 5, the condition is not met.
     * 
     * 1 <= C_PTS, 1 <= D_PTS
     * C_PTS + D_PTS <= NUMPOINTS - 3
     * 
     * @author Linus Dinesjö
     * 
     * @return true if the condition is met, false otherwise
     */
    public boolean lic9() {
        if (NUMPOINTS != COORDINATES.length) {
            throw new IllegalArgumentException("NUMPOINTS does not match the amount of coordinates in COORDINATES");
        }
        if (PARAMETERS.C_PTS < 1 || PARAMETERS.D_PTS < 1 || PARAMETERS.C_PTS + PARAMETERS.D_PTS > NUMPOINTS - 3) {
            throw new IllegalArgumentException("C_PTS and D_PTS are not valid");
        }
        if (NUMPOINTS < 5) {
            return false;
        }

        for (int i = 0; i < NUMPOINTS - PARAMETERS.C_PTS - PARAMETERS.D_PTS - 2; i++) {
            Point2D[] anglePoints = new Point2D[3];
            anglePoints[0] = COORDINATES[i];
            anglePoints[1] = COORDINATES[i + PARAMETERS.C_PTS + 1];
            anglePoints[2] = COORDINATES[i + PARAMETERS.C_PTS + PARAMETERS.D_PTS + 2];

            // If angle "undefined", continue to next set of points
            if (anglePoints[0].equals(anglePoints[1]) || anglePoints[1].equals(anglePoints[2])) {
                continue;
            }

            // Notation according to Google result for "law of cosines formula"
            double a = anglePoints[0].distance(anglePoints[1]);
            double c = anglePoints[0].distance(anglePoints[2]);
            double b = anglePoints[1].distance(anglePoints[2]);

            // Law of Cosines formula: cos(B) = (a² + b² - c²) / (2ab)
            double result = (a * a + b * b - c * c) / (2 * a * b);
            double angle = Math.acos(result);
            assert angle >= 0 && angle <= Math.PI;

            if (angle < Math.PI - PARAMETERS.EPSILON || angle > Math.PI + PARAMETERS.EPSILON) {
                return true;
            }
        }

        return false;
    }

    /*
     * There exists at least one set of three data points separated by exactly E_PTS
     * and F_PTS consecutive intervening points, respectively, that are the vertices
     * of a triangle with area greater than AREA1. The condition is not met when
     * NUMPOINTS < 5.
     * 
     * 1 <= E_PTS, 1 <= F_PTS
     * E_PTS + F_PTS <= NUMPOINTS -3
     * 
     * @author Linus Dinesjö
     * 
     * @return true if the condition is met, false otherwise
     */
    public boolean lic10() {
        if (NUMPOINTS != COORDINATES.length) {
            throw new IllegalArgumentException("NUMPOINTS does not match the amount of coordinates in COORDINATES");
        }
        if (PARAMETERS.E_PTS < 1 || PARAMETERS.F_PTS < 1 || PARAMETERS.E_PTS + PARAMETERS.F_PTS > NUMPOINTS - 3) {
            throw new IllegalArgumentException("E_PTS and F_PTS are not valid");
        }
        if (NUMPOINTS < 5) {
            return false;
        }

        for (int i = 0; i < NUMPOINTS - PARAMETERS.E_PTS - PARAMETERS.F_PTS - 2; i++) {
            Point2D[] trianglePoints = new Point2D[3];
            trianglePoints[0] = COORDINATES[i];
            trianglePoints[1] = COORDINATES[i + PARAMETERS.E_PTS + 1];
            trianglePoints[2] = COORDINATES[i + PARAMETERS.E_PTS + PARAMETERS.F_PTS + 2];

            // If triangle "undefined", continue to next set of points
            if (trianglePoints[0].equals(trianglePoints[1]) || trianglePoints[1].equals(trianglePoints[2])) {
                continue;
            }

            // Area of a triangle using Heron's formula
            double a = trianglePoints[0].distance(trianglePoints[1]);
            double b = trianglePoints[0].distance(trianglePoints[2]);
            double c = trianglePoints[1].distance(trianglePoints[2]);
            double s = (a + b + c) / 2; // semi-perimeter
            double area = Math.sqrt(s * (s - a) * (s - b) * (s - c));

            if (area > PARAMETERS.AREA1) {
                return true;
            }
        }

        return false;
    }

    /*
     * There exists at least one set of two data points, (X[i],Y[i]) and
     * (X[j],Y[j]), separated by exactly G_PTS consecutive intervening points, such
     * that X[j] - X[i] < 0. (where i < j ) The condition is not met when NUMPOINTS
     * < 3.
     * 
     * 1 <= G_PTS <= NUMPOINTS -2
     * 
     * @author Linus Dinesjö
     * 
     * @return true if the condition is met, false otherwise
     */
    public boolean lic11() {
        if (NUMPOINTS != COORDINATES.length) {
            throw new IllegalArgumentException("NUMPOINTS does not match the amount of coordinates in COORDINATES");
        }
        if (PARAMETERS.G_PTS < 1 || PARAMETERS.G_PTS > NUMPOINTS - 2) {
            throw new IllegalArgumentException("G_PTS is not valid");
        }
        if (NUMPOINTS < 3) {
            return false;
        }

        for (int i = 0; i < NUMPOINTS - PARAMETERS.G_PTS - 1; i++) {
            if (COORDINATES[i].getX() > COORDINATES[i + PARAMETERS.G_PTS + 1].getX()) {
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