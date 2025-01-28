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
    * There exists at least one set of three consecutive data points that cannot all be contained
    * within or on a circle of radius RADIUS1.
    * (0 ≤ RADIUS1)
    *
    * @author Robin Gunnarsson
    * @return true if condition is met, otherwise false
    */

    public boolean lic1(){
        if(NUMPOINTS != COORDINATES.length){
            throw new IllegalArgumentException("NUMPOINTS does not match the amount of coordinates in COORDINATES");
        }
        if(PARAMETERS.RADIUS1 < 0){
            throw new IllegalArgumentException("RADIUS1 is less than 0");
        }

        for(int i = 2; i < COORDINATES.length; i++){
            Point2D[] circlePoints = new Point2D[3];
            double sum_x = 0, sum_y = 0, x, y;

            circlePoints[0] = COORDINATES[i-2];
            circlePoints[1] = COORDINATES[i-1];
            circlePoints[2] = COORDINATES[i];

            for (Point2D circlePoint : circlePoints) {
                x = circlePoint.getX();
                y = circlePoint.getY();
                sum_x = sum_x + x;
                sum_y = sum_y + y; 
            }
            
            //Calculates ther center of the three coordinates that is used to determine if any coordinate lies outside the cicle
            Point2D centerPoint = new Point2D.Double(sum_x / circlePoints.length, sum_y / circlePoints.length);

            for (Point2D circlePoint : circlePoints) {
                if (circlePoint.distance(centerPoint) > PARAMETERS.RADIUS1) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
    * There exists at least one set of three consecutive data points which form an angle such that:
    * angle < (PI − EPSILON) or angle > (PI + EPSILON)
    * The second of the three consecutive points is always the vertex of the angle. If either the first
    * point or the last point (or both) coincides with the vertex, the angle is undefined and the LIC
    * is not satisfied by those three points.
    * (0 ≤ EPSILON < PI)
    * 
    * @ author Robin Gunnarsson
    * @ reference: https://shorturl.at/rkxFb (Used for calculating the angle between three points in 2D)
    * @ return true if condition is met, otherwise false
    */

    public boolean lic2(){
        double pi = Math.PI;

        if(NUMPOINTS != COORDINATES.length){
            throw new IllegalArgumentException("NUMPOINTS does not match the amount of coordinates in COORDINATES");
        }
        if(PARAMETERS.EPSILON < 0){
            throw new IllegalArgumentException("EPSILON is less than 0");
        }
        if(PARAMETERS.EPSILON >= pi){
            throw new IllegalArgumentException("EPSILON is greater than, or equal to, 0");
        }
        for(int i = 2; i < COORDINATES.length; i++){
            Point2D[] anglePoints = new Point2D[3];
            

            anglePoints[0] = COORDINATES[i-2];
            anglePoints[1] = COORDINATES[i-1];
            anglePoints[2] = COORDINATES[i];

            if (anglePoints[1] == anglePoints[0] || anglePoints[1] == anglePoints[2]) {
                //Angle is undefined since one or both of the ponits coincides with the vertex  
            }else{
                double angle;
                double x0, x1, x2, y0, y1, y2;
                double magnitudeA, magnitudeB;

                x0 = anglePoints[0].getX();
                x1 = anglePoints[1].getX();
                x2 = anglePoints[2].getX();

                y0 = anglePoints[0].getY();
                y1 = anglePoints[1].getY();
                y2 = anglePoints[2].getY();
                
                magnitudeA = anglePoints[1].distance(anglePoints[0]);
                magnitudeB = anglePoints[1].distance(anglePoints[2]);

                angle = Math.acos(((x0 - x1) * (x2 - x1) + (y0 - y1) * (y2 - y1)) / (magnitudeA * magnitudeB));
                
                if(angle < (pi - PARAMETERS.EPSILON) || angle > (pi + PARAMETERS.EPSILON)){
                    return true;
                }

            }

         
        }
        return false;
    }

    /*
    * There exists at least one set of three consecutive data points that are the vertices of a triangle
    * with area greater than AREA1.
    * (0 ≤ AREA1)
    *
    * @author Robin Gunnarsson
    * @return true if condition is met, otherwise false
    */

    public boolean lic3(){
        if(NUMPOINTS != COORDINATES.length){
            throw new IllegalArgumentException("NUMPOINTS does not match the amount of coordinates in COORDINATES");
        }
        if(PARAMETERS.AREA1 < 0){
            throw new IllegalArgumentException("AREA1 is less than 0");
        }
        for(int i = 2; i < COORDINATES.length; i++){
            Point2D[] trianglePoints = new Point2D[3];
            
            trianglePoints[0] = COORDINATES[i-2];
            trianglePoints[1] = COORDINATES[i-1];
            trianglePoints[2] = COORDINATES[i];

            if (trianglePoints[1] == trianglePoints[0] || trianglePoints[1] == trianglePoints[2]) {
                //area is zero since one or both of the ponits coincides with the vertex of the triangle
            } else{
                Point2D centerBase;
                double area, base, hight, x_centerBase, y_centerBase;

                base = trianglePoints[2].distance(trianglePoints[0]);

                x_centerBase = (trianglePoints[0].getX() + trianglePoints[2].getX()) / 2;
                y_centerBase = (trianglePoints[0].getY() + trianglePoints[2].getY()) / 2;
                centerBase = new Point2D.Double(x_centerBase, y_centerBase); 
                
                hight = centerBase.distance(trianglePoints[1]);

                area = (base * hight) / 2;

                if(area > PARAMETERS.AREA1){
                    return true;
                }
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

    /*
     * There exists at least one set of two consecutive data points, (X[i],Y[i]) and (X[j],Y[j]), such that 
     * X[j] - X[i] < 0. (where i = j-1)
     * 
     * @author Ellen Sigurðardóttir
     */

    public boolean lic5() {
        if (NUMPOINTS < 2) {
            throw new IllegalArgumentException("Not enough NUMPOINTS for two consecutive data points.");
        }

        for (int i = 0; i < NUMPOINTS - 1; i++) {
            if (COORDINATES[i].getX() > COORDINATES[i + 1].getX()) {
                return true;
            }
        }

        return false;
    }

    /*
     * There exists at least one set of N_PTS consecutive data points such that at least one of the 
     * points lies a distance greater than DIST from the line joining the first and last of these N_PTS 
     * points. If the first and last points of these N_PTS are identical, then the calculated distance
     * to compare with DIST will be the distance from the coincident point to all other points of
     * the N_PTS consecutive points. The condition is not met when NUMPOINTS < 3.
     * (3 ≤ N_PTS ≤ NUMPOINTS), (0 ≤ DIST)
     * 
     * @author Ellen Sigurðardóttir
     * @return true if the condition is met, otherwise false
     */

    public boolean lic6() {
        if (NUMPOINTS != COORDINATES.length) {
            throw new IllegalArgumentException("NUMPOINTS does not match the amount of coordinates in COORDINATES");
        }
        if (NUMPOINTS < 3) {
            throw new IllegalArgumentException("Number of NUMPOINTS must be greater than 2");
        }
        if (PARAMETERS.N_PTS < 3 || PARAMETERS.N_PTS > NUMPOINTS) {
            throw new IllegalArgumentException("N_PTS must be in the range 3 ≤ N_PTS ≤ NUMPOINTS");
        }
        if (PARAMETERS.DIST < 0) {
            throw new IllegalArgumentException("DIST must be equal or greater than 0");
        }

        for (int i = 0; i <= NUMPOINTS - PARAMETERS.N_PTS; i++) {
            Point2D first = COORDINATES[i];
            Point2D last = COORDINATES[i + PARAMETERS.N_PTS - 1];

            // first and last points are identical
            if (first.equals(last)) {
                for (int j = i + 1; j < i + PARAMETERS.N_PTS - 1; i++) {
                    double distance = first.distance(COORDINATES[j]);
                    if (distance > PARAMETERS.DIST) return true;
                }
            } else {
                // line between first and last points
                double x1 = first.getX();
                double y1 = first.getY();
                double x2 = last.getX();
                double y2 = last.getY();
                double a = y2 - y1;
                double b = -(x2 - x1);
                double c = x2 * y1 - y2 * x1;

                for (int j = i + 1; j < i + PARAMETERS.N_PTS; j++) {
                    double x = COORDINATES[j].getX();
                    double y = COORDINATES[j].getY();
                    double distance = Math.abs(a * x + b * y + c) / Math.sqrt(a * a + b * b);

                    if (distance > PARAMETERS.DIST) return true;
                }
            }
        }

        return false;
    }

    /*
     * There exists at least one set of two data points separated by exactly K_PTS consecutive 
     * intervening points that are a distance greater than the length, LENGTH1, apart. The condition 
     * is not met when NUMPOINTS < 3. 
     * 1 ≤ K_PTS ≤ (NUMPOINTS − 2)
     */

    public boolean lic7() {
        if (NUMPOINTS != COORDINATES.length) {
            throw new IllegalArgumentException("NUMPOINTS does not match the amount of coordinates in COORDINATES");
        }
        if (NUMPOINTS < 3) {
            throw new IllegalArgumentException("Number of NUMPOINTS can not be less than 3");
        }
        if (PARAMETERS.K_PTS < 2 || PARAMETERS.K_PTS > (NUMPOINTS - 2)) {
            throw new IllegalArgumentException("K_PTS has to be in the rage 1 ≤ K_PTS ≤ (NUMPOINTS − 2)");
        }
        
        for (int i = 0; i < NUMPOINTS - PARAMETERS.K_PTS - 1; i++) {
            int j = i + PARAMETERS.K_PTS + 1;
            
            double distance = COORDINATES[i].distance(COORDINATES[j]);

            if (distance > PARAMETERS.LENGTH1) return true;
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

    /*
     * There exists atleast one set of two data points, seperated by exactly K_PTS
     * consecutive intervening points, which are a distance greater than the length,
     * LENGTH1, apart. In addition, there exists at least one set of two data points
     * (which can be the same or different from the two data points just mentioned),
     * seperated by exactly K_PTS consecutive intervening points, that are a distance
     * les than the length, LENGTH2, apart. Both parts must be true for the LIC to be
     * true. The condition is not met when NUMPOINT < 3.
     * @author Marcus Odin
     * @return
     * 
     */
    public boolean lic12(){
        if (PARAMETERS.LENGTH1 < 0) {
            throw new IllegalArgumentException("LENGTH1 is less than 0");
        }

        if (PARAMETERS.LENGTH2 < 0) {
            throw new IllegalArgumentException("LENGTH2 i s less than 0");
        }

        if(NUMPOINTS < 3 ){
            return false;
        }

        for(int i = 0; i < COORDINATES.length - PARAMETERS.K_PTS - 1; i++) {
            if(COORDINATES[i].distance(COORDINATES[i + PARAMETERS.K_PTS + 1]) > PARAMETERS.LENGTH1){
                for(int j = 0; j < COORDINATES.length - PARAMETERS.K_PTS - 1; j++){
                    if(COORDINATES[j].distance(COORDINATES[j + PARAMETERS.K_PTS + 1]) < PARAMETERS.LENGTH2){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void decide(){
        launch = true;
    }

    public static void main(String[] args) {

    }
}
