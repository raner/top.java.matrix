package top.java.matrix.naive;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.Before;
import org.junit.Test;
import top.java.matrix.Dimension;
import top.java.matrix.Matrix;
import top.java.matrix.util.OctaveFloatBinaryReader;
import static org.junit.Assert.assertEquals;

public class NaiveMatrixTest<M extends Dimension>
{
    private OctaveFloatBinaryReader reader = new OctaveFloatBinaryReader();
    private Matrix<M, M> B;
    private float δ = 1E-6F;

    @Before
    public void initializeMatrices() throws IOException
    {
        // TODO: load only once!
        B = new NaiveMatrix<>(reader.readFloatBinaryMatrix(path("MatrixB.float.bin")));
    }

    @Test
    public void testAtOrigin()
    {
        assertEquals(3880F, B.at(0,  0), δ);
    }

    @Test
    public void testAtRow0Column1()
    {
        assertEquals(125F, B.at(0,  1), δ);
    }

    @Test
    public void testAtRow1Column0()
    {
        assertEquals(3106F, B.at(1,  0), δ);
    }
    
    private Path path(String name)
    {
        return new File("src/test/resources/" + name).getAbsoluteFile().toPath();
    }
}
