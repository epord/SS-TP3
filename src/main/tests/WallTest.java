import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class WallTest {
    @Test
    public void testHorizontalWall(){
        HorizontalWall hwall = new HorizontalWall(1,3,5);
        assertNull(hwall.getCollisionTime(new ParticleImpl(1, 1.0, 0, 0, 0.0, 1.0)));
        assertNull(hwall.getCollisionTime(new ParticleImpl(1, 1.0, 4, 0, 0.0, 1.0)));
        assertEquals(new Double(4.0), hwall.getCollisionTime(new ParticleImpl(1,1.0,3,0,0.0,1.0)));
        assertEquals(new Double(4.0), hwall.getCollisionTime(new ParticleImpl(1,1.0,1,0,0.0,1.0)));
        assertEquals(new Double(4.0), hwall.getCollisionTime(new ParticleImpl(1,1.0,3,0,0.0,1.0)));
    }
    @Test
    public void testVerticalWall(){
        VerticalWall verticalWall = new VerticalWall(5,1,3);
        Double time = verticalWall.getCollisionTime(new ParticleImpl(1,1.0,0,3,1.0,0));
        System.out.println(time);
    }
}
