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
    public void lic8IsFalseInNormalCase() {
        decide.NUMPOINTS = 5;
        decide.PARAMETERS.A_PTS = 1;
        decide.PARAMETERS.B_PTS = 1;
        decide.PARAMETERS.RADIUS1 = 3;
        decide.COORDINATES = new Point2D[] {
                new Point2D.Double(0, 0),
                new Point2D.Double(0, 1),
                new Point2D.Double(0, 2),
                new Point2D.Double(0, 3),
                new Point2D.Double(0, 4)
        };

        assertFalse(decide.lic8());
    }

    @Test
    public void lic8IsTrueInNormalCase() {
        decide.NUMPOINTS = 5;
        decide.PARAMETERS.A_PTS = 1;
        decide.PARAMETERS.B_PTS = 1;
        decide.PARAMETERS.RADIUS1 = 1;
        decide.COORDINATES = new Point2D[] {
                new Point2D.Double(0, 0),
                new Point2D.Double(0, 1),
                new Point2D.Double(0, 2),
                new Point2D.Double(0, 3),
                new Point2D.Double(0, 4)
        };

        assertTrue(decide.lic8());
    }
}
