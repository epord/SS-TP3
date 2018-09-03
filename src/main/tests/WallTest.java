import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class WallTest {
    @Test
    public void testHorizontalWall(){
        HorizontalWall hwall = new HorizontalWall(1,3,5, true);
        assertNull(hwall.getCollisionTime(new ParticleImpl(1, 1.0, 0, 0, 0.0, 1.0)));
        assertNull(hwall.getCollisionTime(new ParticleImpl(1, 1.0, 4, 0, 0.0, 1.0)));
        assertEquals(new Double(4.0), hwall.getCollisionTime(new ParticleImpl(1,1.0,3,0,0.0,1.0)));
        assertEquals(new Double(4.0), hwall.getCollisionTime(new ParticleImpl(1,1.0,1,0,0.0,1.0)));
        assertEquals(new Double(4.0), hwall.getCollisionTime(new ParticleImpl(1,1.0,3,0,0.0,1.0)));
    }
    @Test
    public void testVerticalWall(){
        VerticalWall verticalWall = new VerticalWall(5,1,3, true);
        Double time = verticalWall.getCollisionTime(new ParticleImpl(1,1.0,0,3,1.0,0));
        System.out.println(time);
    }

    @Test
    public void testWallLenght(){
        HorizontalWall hwall = new HorizontalWall(1,3,5, true);
        assertEquals(new Double(4.0),new Double(hwall.getSurface()));
        HorizontalWall hwallDouble = new HorizontalWall(1,3,5, false);
        assertEquals(new Double(2.0),new Double(hwallDouble.getSurface()));

        VerticalWall verticalWall = new VerticalWall(5,1,3, true);
        assertEquals(new Double(4.0),new Double(verticalWall.getSurface()));
        VerticalWall verticalWallDouble = new VerticalWall(5,1,3, false);
        assertEquals(new Double(2.0),new Double(verticalWallDouble.getSurface()));
    }
}
