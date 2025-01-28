import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.awt.Point;
import java.awt.geom.Point2D;

public class Tests {
    private Decide decide;

    @Before
    public void setUp()
    {
        decide = new Decide();
        decide.PARAMETERS = decide.new PARAMETERS_T();
    }

    @Test
    public void decideIsTrueInTrivialCase() {
        // Some random parameters, these don't matter
        decide.PARAMETERS.LENGTH1 = 2.0;
        decide.PARAMETERS.RADIUS1 = 4.0;
        decide.PARAMETERS.EPSILON = Math.PI/4;
        decide.PARAMETERS.AREA1 = 4.0;
        decide.PARAMETERS.Q_PTS = 4;
        decide.PARAMETERS.QUADS = 2;
        decide.PARAMETERS.DIST = 1.5;
        decide.PARAMETERS.N_PTS = 3;
        decide.PARAMETERS.K_PTS = 2;
        decide.PARAMETERS.A_PTS = 1;
        decide.PARAMETERS.B_PTS = 1;
        decide.PARAMETERS.C_PTS = 1;
        decide.PARAMETERS.D_PTS = 1;
        decide.PARAMETERS.E_PTS = 1;
        decide.PARAMETERS.F_PTS = 1;
        decide.PARAMETERS.G_PTS = 1;
        decide.PARAMETERS.LENGTH2 = 5.0;
        decide.PARAMETERS.RADIUS2 = 3.0;
        decide.PARAMETERS.AREA2 = 2.0;

        decide.NUMPOINTS = 6;
        decide.COORDINATES = new Point2D[]{
            new Point2D.Double(0,0),
            new Point2D.Double(2,2),
            new Point2D.Double(2,3),
            new Point2D.Double(3,4),
            new Point2D.Double(20,15),
            new Point2D.Double(22,18)
        };
        
        for(int i = 0; i < 15; i++){
            decide.PUV[i] = false;
        }

        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                decide.LCM[i][j] = Decide.CONNECTORS.NOTUSED;
            }
        }
        
        decide.decide();
        assertTrue(decide.launch);
    }

    @Test
    public void decideIsTrueIfLic8True() {
        // Some random parameters, these don't matter
        decide.PARAMETERS.LENGTH1 = 2.0;
        decide.PARAMETERS.EPSILON = Math.PI / 4;
        decide.PARAMETERS.AREA1 = 4.0;
        decide.PARAMETERS.Q_PTS = 4;
        decide.PARAMETERS.QUADS = 2;
        decide.PARAMETERS.DIST = 1.5;
        decide.PARAMETERS.N_PTS = 3;
        decide.PARAMETERS.K_PTS = 2;
        decide.PARAMETERS.C_PTS = 1;
        decide.PARAMETERS.D_PTS = 1;
        decide.PARAMETERS.E_PTS = 1;
        decide.PARAMETERS.F_PTS = 1;
        decide.PARAMETERS.G_PTS = 1;
        decide.PARAMETERS.LENGTH2 = 5.0;
        decide.PARAMETERS.RADIUS2 = 3.0;
        decide.PARAMETERS.AREA2 = 2.0;

        // Parameters for LIC8 true test
        decide.NUMPOINTS = 5;
        decide.PARAMETERS.A_PTS = 1;
        decide.PARAMETERS.B_PTS = 1;
        decide.PARAMETERS.RADIUS1 = 1;
        decide.COORDINATES = new Point2D[] {
                new Point2D.Double(0, 0),
                new Point2D.Double(-1, -1),
                new Point2D.Double(0, 2),
                new Point2D.Double(-1, -1),
                new Point2D.Double(0, 4)
        };

        assertTrue(decide.lic8());

        for (int i = 0; i < 15; i++) {
            decide.PUV[i] = false;
        }
        decide.PUV[8] = true; // lic8 should count

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                decide.LCM[i][j] = Decide.CONNECTORS.ORR;
            }
        }

        decide.decide();
        assertTrue(decide.launch);
    }

    @Test
    public void decideIsFalseIfLic8False() {
        // Some random parameters, these don't matter
        decide.PARAMETERS.LENGTH1 = 2.0;
        decide.PARAMETERS.EPSILON = Math.PI / 4;
        decide.PARAMETERS.AREA1 = 4.0;
        decide.PARAMETERS.Q_PTS = 4;
        decide.PARAMETERS.QUADS = 2;
        decide.PARAMETERS.DIST = 1.5;
        decide.PARAMETERS.N_PTS = 3;
        decide.PARAMETERS.K_PTS = 2;
        decide.PARAMETERS.C_PTS = 1;
        decide.PARAMETERS.D_PTS = 1;
        decide.PARAMETERS.E_PTS = 1;
        decide.PARAMETERS.F_PTS = 1;
        decide.PARAMETERS.G_PTS = 1;
        decide.PARAMETERS.LENGTH2 = 5.0;
        decide.PARAMETERS.RADIUS2 = 3.0;
        decide.PARAMETERS.AREA2 = 2.0;

        // Parameters for LIC8 false test
        decide.NUMPOINTS = 5;
        decide.PARAMETERS.A_PTS = 1;
        decide.PARAMETERS.B_PTS = 1;
        decide.PARAMETERS.RADIUS1 = 3;
        decide.COORDINATES = new Point2D[] {
                new Point2D.Double(0, 0),
                new Point2D.Double(-1, -1),
                new Point2D.Double(0, 2),
                new Point2D.Double(-1, -1),
                new Point2D.Double(0, 4)
        };

        for (int i = 0; i < 15; i++) {
            decide.PUV[i] = false;
        }
        decide.PUV[8] = true; // lic8 should count

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                decide.LCM[i][j] = Decide.CONNECTORS.ORR;
            }
        }

        decide.decide();
        assertFalse(decide.launch);
    }

    @Test
    public void lic0IsTrueInNormalCase()
    {
        decide.PARAMETERS.LENGTH1 = 2;
        decide.NUMPOINTS = 3;
        decide.COORDINATES = new Point2D[]{
            new Point2D.Double(0,0),
            new Point2D.Double(2,2),
            new Point2D.Double(2,3)
        };
        assertTrue(decide.lic0());
    }

    @Test
    public void lic0IsFalseInNormalCase(){
        decide.PARAMETERS.LENGTH1 = 2;
        decide.NUMPOINTS = 3;
        decide.COORDINATES = new Point2D[]{
            new Point2D.Double(0,0),
            new Point2D.Double(1,1),
            new Point2D.Double(2,2)
        };
        assertFalse(decide.lic0());
    }

    @Test
    public void lic1IsTrueInNormalCase()
    {
        decide.PARAMETERS.RADIUS1 = 4;
        decide.NUMPOINTS = 6;
        decide.COORDINATES = new Point2D[]{
            new Point2D.Double(0,0),
            new Point2D.Double(2,2),
            new Point2D.Double(2,3),
            new Point2D.Double(3,4),
            new Point2D.Double(20,15),
            new Point2D.Double(22,18)
        };
        assertTrue(decide.lic1());
    }

    @Test
    public void lic1IsFalseInNormalCase(){
        decide.PARAMETERS.RADIUS1 = 10;
        decide.NUMPOINTS = 6;
        decide.COORDINATES = new Point2D[]{
            new Point2D.Double(0,0),
            new Point2D.Double(1,1),
            new Point2D.Double(2,2),
            new Point2D.Double(3,3),
            new Point2D.Double(4,4),
            new Point2D.Double(4,5)
        };
        assertFalse(decide.lic1());
    }

    @Test
    public void lic2IsTrueInNormalCase()
    {
        decide.PARAMETERS.EPSILON = Math.PI/4;
        decide.NUMPOINTS = 6;
        decide.COORDINATES = new Point2D[]{
            new Point2D.Double(20,20),
            new Point2D.Double(19,19),
            new Point2D.Double(18,19),
            new Point2D.Double(17,17),
            new Point2D.Double(17,15),
            new Point2D.Double(16,10)
        };
        assertTrue(decide.lic2());
    }

    @Test
    public void lic2IsFalseInNormalCase(){
        decide.PARAMETERS.EPSILON = Math.PI/4;
        decide.NUMPOINTS = 6;
        decide.COORDINATES = new Point2D[]{
            new Point2D.Double(20,20),
            new Point2D.Double(20,20),
            new Point2D.Double(19,19),
            new Point2D.Double(18,18),
            new Point2D.Double(17,17),
            new Point2D.Double(16,16)
        };
        assertFalse(decide.lic2());
    }

    @Test
    public void lic3IsTrueInNormalCase()
    {
        decide.PARAMETERS.AREA1 = 4;
        decide.NUMPOINTS = 6;
        decide.COORDINATES = new Point2D[]{
            new Point2D.Double(20,20),
            new Point2D.Double(18,18),
            new Point2D.Double(10,18),
            new Point2D.Double(10,10),
            new Point2D.Double(8,4),
            new Point2D.Double(2,0)
        };
        assertTrue(decide.lic3());
    }

    @Test
    public void lic3IsFalseInNormalCase(){
        decide.PARAMETERS.AREA1 = 4;
        decide.NUMPOINTS = 6;
        decide.COORDINATES = new Point2D[]{
            new Point2D.Double(20,20),
            new Point2D.Double(20,20),
            new Point2D.Double(19,19),
            new Point2D.Double(18,18),
            new Point2D.Double(17,17),
            new Point2D.Double(16,16)
        };
        assertFalse(decide.lic3());
    }

    @Test
    public void lic4IsTrueInNormalCase() {
        decide.PARAMETERS.Q_PTS = 4;
        decide.PARAMETERS.QUADS = 2;
        decide.NUMPOINTS = 6;
        decide.COORDINATES = new Point2D[] {
            new Point2D.Double(1, 1),     // Q I
            new Point2D.Double(-1, 1),      // Q II
            new Point2D.Double(-1, -1),       // Q III
            new Point2D.Double(2, -2),      // Q IV
            new Point2D.Double(3, 3),     // Q I
            new Point2D.Double(-2, 2)       // Q II
        };
        assertTrue(decide.lic4());
    }

    @Test
    public void lic4IsFalseInNormalCase() {
        decide.PARAMETERS.Q_PTS = 4;
        decide.PARAMETERS.QUADS = 2;
        decide.NUMPOINTS = 6;
        decide.COORDINATES = new Point2D[] {
            new Point2D.Double(1, 1),   // Q I
            new Point2D.Double(2, 2),   // Q I
            new Point2D.Double(3, 3),   // Q I
            new Point2D.Double(-1, 2),    // Q II
            new Point2D.Double(-2, 2),    // Q II
            new Point2D.Double(-3, 3)     // Q II
        };
        assertFalse(decide.lic4());
    }

    @Test
    public void lic5IsTrueInNormalCase() {
        decide.NUMPOINTS = 4;
        decide.COORDINATES = new Point2D[]{
            new Point2D.Double(1, 1),
            new Point2D.Double(3, 2),
            new Point2D.Double(2, 3), // X[j] - X[i] < 0
            new Point2D.Double(5, 4),
        };
        assertTrue(decide.lic5());
    }

    @Test
    public void lic5IsFalseInNormalCase() {
        decide.NUMPOINTS = 4;
        decide.COORDINATES = new Point2D[]{
            new Point2D.Double(1, 1),
            new Point2D.Double(2, 2),
            new Point2D.Double(3, 3),
            new Point2D.Double(4, 4),
        };
        assertFalse(decide.lic5());
    }

    @Test
    public void lic6IsTrueInNormalCase() {
        decide.NUMPOINTS = 5;
        decide.PARAMETERS.N_PTS = 3;
        decide.PARAMETERS.DIST = 1.5;
        decide.COORDINATES = new Point2D[]{
            new Point2D.Double(1, 1),
            new Point2D.Double(3, 5),
            new Point2D.Double(6, 4),
            new Point2D.Double(7, 6),
            new Point2D.Double(8, 8)
        };
        assertTrue(decide.lic6());
    }

    @Test
    public void lic6IsFalseInNormalCase() {
        decide.NUMPOINTS = 5;
        decide.PARAMETERS.N_PTS = 3;
        decide.PARAMETERS.DIST = 1.5;
        decide.COORDINATES = new Point2D[]{
            new Point2D.Double(1, 1),
            new Point2D.Double(3, 2),
            new Point2D.Double(5, 3),
            new Point2D.Double(7, 4),
            new Point2D.Double(9, 5)
        };
        assertFalse(decide.lic6());
    }

    @Test
    public void lic7IsTrueInNormalCase() {
        decide.NUMPOINTS = 5;
        decide.PARAMETERS.K_PTS = 2;
        decide.PARAMETERS.LENGTH1 = 4.0;
        decide.COORDINATES = new Point2D[]{
            new Point2D.Double(0, 0),
            new Point2D.Double(1, 1),  
            new Point2D.Double(2, 2),
            new Point2D.Double(5, 5),
            new Point2D.Double(6, 6)
        };
        assertTrue(decide.lic7());
    }

    @Test
    public void lic7IsFalseInNormalCase() {
        decide.NUMPOINTS = 5;
        decide.PARAMETERS.K_PTS = 2;
        decide.PARAMETERS.LENGTH1 = 8.0;
        decide.COORDINATES = new Point2D[]{
            new Point2D.Double(0, 0),
            new Point2D.Double(1, 1),  
            new Point2D.Double(2, 2),
            new Point2D.Double(5, 5),
            new Point2D.Double(6, 6)
        };
        assertFalse(decide.lic7());
    }

    @Test
    public void lic8IsTrueInNormalCase() {
        decide.NUMPOINTS = 5;
        decide.PARAMETERS.A_PTS = 1;
        decide.PARAMETERS.B_PTS = 1;
        decide.PARAMETERS.RADIUS1 = 1;
        decide.COORDINATES = new Point2D[] {
                new Point2D.Double(0, 0),
                new Point2D.Double(-1, -1),
                new Point2D.Double(0, 2),
                new Point2D.Double(-1, -1),
                new Point2D.Double(0, 4)
        };

        assertTrue(decide.lic8());
    }

    @Test
    public void lic8IsFalseInNormalCase() {
        decide.NUMPOINTS = 5;
        decide.PARAMETERS.A_PTS = 1;
        decide.PARAMETERS.B_PTS = 1;
        decide.PARAMETERS.RADIUS1 = 3;
        decide.COORDINATES = new Point2D[] {
                new Point2D.Double(0, 0),
                new Point2D.Double(-1, -1),
                new Point2D.Double(0, 2),
                new Point2D.Double(-1, -1),
                new Point2D.Double(0, 4)
        };

        assertFalse(decide.lic8());
    }

    @Test
    public void lic9IsTrueInNormalCase() {
        decide.NUMPOINTS = 5;
        decide.PARAMETERS.C_PTS = 1;
        decide.PARAMETERS.D_PTS = 1;
        decide.PARAMETERS.EPSILON = 1; // => angle < 2.1415... or angle > 4.1415...
        decide.COORDINATES = new Point2D[] {
                new Point2D.Double(0, 2),
                new Point2D.Double(-1, -1),
                new Point2D.Double(2, 0),
                new Point2D.Double(-1, -1),
                new Point2D.Double(4, 2)
        }; // angle = 1.57...

        assertTrue(decide.lic9());
    }

    @Test
    public void lic9IsFalseInNormalCase() {
        decide.NUMPOINTS = 5;
        decide.PARAMETERS.C_PTS = 1;
        decide.PARAMETERS.D_PTS = 1;
        decide.PARAMETERS.EPSILON = 2; // => angle < 1.1514... or angle > 5.1415...
        decide.COORDINATES = new Point2D[] {
                new Point2D.Double(0, 1),
                new Point2D.Double(-1, -1),
                new Point2D.Double(0, 0),
                new Point2D.Double(-1, -1),
                new Point2D.Double(1, 0)
        }; // angle = 1.57...

        assertFalse(decide.lic9());
    }

    @Test
    public void lic10IsTrueInNormalCase() {
        decide.NUMPOINTS = 5;
        decide.PARAMETERS.E_PTS = 1;
        decide.PARAMETERS.F_PTS = 1;
        decide.PARAMETERS.AREA1 = 1.7;
        decide.COORDINATES = new Point2D[] {
                new Point2D.Double(0, 2),
                new Point2D.Double(-1, -1),
                new Point2D.Double(0, 0),
                new Point2D.Double(-1, -1),
                new Point2D.Double(2, 0)
        }; // area = 2

        assertTrue(decide.lic10());
    }

    @Test
    public void lic10IsFalseInNormalCase() {
        decide.NUMPOINTS = 5;
        decide.PARAMETERS.E_PTS = 1;
        decide.PARAMETERS.F_PTS = 1;
        decide.PARAMETERS.AREA1 = 2.3;
        decide.COORDINATES = new Point2D[] {
                new Point2D.Double(0, 2),
                new Point2D.Double(-1, -1),
                new Point2D.Double(0, 0),
                new Point2D.Double(-1, -1),
                new Point2D.Double(2, 0)
        }; // area = 2

        assertFalse(decide.lic10());
    }

    @Test
    public void lic11IsTrueInNormalCase() {
        decide.NUMPOINTS = 3;
        decide.PARAMETERS.G_PTS = 1;
        decide.COORDINATES = new Point2D[] {
                new Point2D.Double(1, 0),
                new Point2D.Double(-1, -1),
                new Point2D.Double(0, 0),
        }; // X[0] > X[2]

        assertTrue(decide.lic11());
    }

    @Test
    public void lic11IsFalseInNormalCase() {
        decide.NUMPOINTS = 3;
        decide.PARAMETERS.G_PTS = 1;
        decide.COORDINATES = new Point2D[] {
                new Point2D.Double(0, 0),
                new Point2D.Double(-1, -1),
                new Point2D.Double(1, 0),
        }; // X[0] < X[2]

        assertFalse(decide.lic11());
    }

    @Test
    public void lic12IsTrueInNormalCase(){
        decide.NUMPOINTS = 3;
        decide.COORDINATES = new Point2D[]{
            new Point2D.Double(0,0),
            new Point2D.Double(1,1),
            new Point2D.Double(2,2),
        };
        decide.PARAMETERS.LENGTH1 = 1;
        decide.PARAMETERS.LENGTH2 = 5;
        decide.PARAMETERS.K_PTS = 1;

        assertTrue(decide.lic12());
    }

    @Test
    public void lic12IsFalseInNormalCase(){
        decide.NUMPOINTS = 3;
        decide.COORDINATES = new Point2D[]{
            new Point2D.Double(0,0),
            new Point2D.Double(1,1),
            new Point2D.Double(2,2),
        };
        decide.PARAMETERS.LENGTH1 = 5;
        decide.PARAMETERS.LENGTH2 = 1;
        decide.PARAMETERS.K_PTS = 1;

        assertFalse(decide.lic12());
    }
}
