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
}
