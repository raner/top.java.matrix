package top.java.matrix.util;

import java.io.File;
import java.nio.file.Path;
import org.junit.Test;
import top.java.matrix.util.OctaveFloatBinaryReader;
import top.java.matrix.util.RawFloatMatrix;
import static org.junit.Assert.assertEquals;

public class OctaveFloatBinaryReaderTest
{
    @Test
    public void test() throws Exception {
        OctaveFloatBinaryReader reader = new OctaveFloatBinaryReader();
        Path path = new File("src/test/resources/MatrixB.float.bin").getAbsoluteFile().toPath();
        RawFloatMatrix raw = reader.readFloatBinaryMatrix(path);
        assertEquals(4000, raw.rows());
        assertEquals(4000, raw.columns());
        assertEquals(3880F, raw.matrix()[0], 1E-6F);
        assertEquals(3106F, raw.matrix()[1], 1E-6F);
        assertEquals(919F, raw.matrix()[2], 1E-6F);
        assertEquals(2162F, raw.matrix()[3], 1E-6F);
        assertEquals(3196F, raw.matrix()[4], 1E-6F);
        assertEquals(505F, raw.matrix()[15999999], 1E-6F);
    }
}
