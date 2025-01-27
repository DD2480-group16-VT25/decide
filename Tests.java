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

}
