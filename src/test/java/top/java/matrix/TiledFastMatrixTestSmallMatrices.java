package top.java.matrix;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import org.junit.Before;
import org.junit.Test;
import top.java.matrix.fast.FastMatrix;
import top.java.matrix.fast.TiledFastMatrix;
import top.java.matrix.util.OctaveFloatBinaryReader;
import top.java.matrix.util.RawFloatMatrix;
import static org.junit.Assert.assertEquals;

/**
* The class {@link TiledFastMatrixTestSmallMatrices} provides additional JUnit tests for
* {@link TiledFastMatrix} that use smaller matrices.
*
* @param <M> the dimension of the matrices (number of rows is the same as number of columns)
*
* @author mirko
**/
public class TiledFastMatrixTestSmallMatrices<M extends Dimension>
{
    private OctaveFloatBinaryReader reader = new OctaveFloatBinaryReader();
    private Matrix<M, M> twenty;
    private Matrix<M, M> squared;

    @Before
    public void initializeMatrices() throws IOException
    {
        // TODO: load only once!
        twenty = new TiledFastMatrix<>(reader.readFloatBinaryMatrix(path("matrix20x20.float.bin")));
        squared = new TiledFastMatrix<>(reader.readFloatBinaryMatrix(path("square20x20.float.bin")));
    }

    @Test
    public void testMultiplication6x6()
    {
        float[] matrix1 =
        {
            9, 5, 0, 0, 8, 3,
            3, 4, 2, 7, 2, 7,
            3, 3, 2, 3, 6, 0,
            5, 4, 9, 7, 5, 6,
            8, 7, 3, 7, 7, 5,
            8, 0, 5, 9, 1, 4
        };
        float[] matrix2 =
        {
            6, 4, 9, 0, 6, 2,
            8, 8, 0, 2, 6, 8,
            2, 5, 7, 3, 5, 6,
            8, 6, 4, 2, 4, 7,
            5, 3, 0, 4, 2, 1,
            4, 2, 8, 5, 0, 0
        };
        float[] product =
        {
            157, 115,  54, 115, 154,  84,
            218, 122,  92, 184, 140, 154,
            157,  98,  96, 166, 124, 108,
            200, 112,  85, 159, 145, 126,
             98,  67,  53,  72,  81,  74,
             91,  72,  65,  73, 109,  56
        };
        Matrix<M, M> left = new TiledFastMatrix<>(RawFloatMatrix.FACTORY.create(6, 6, matrix1));
        Matrix<M, M> right = new TiledFastMatrix<>(RawFloatMatrix.FACTORY.create(6, 6, matrix2));
        Matrix<M, M> expected = new TiledFastMatrix<>(RawFloatMatrix.FACTORY.create(6, 6, product));
        Matrix<M, M> result = left.times(right);
        assertEquals(expected, result);
        assertEquals(FastMatrix.class, result.getClass());
    }

    @Test
    public void testMultiplication20x20()
    {
        Matrix<M, M> expected = squared;
        Matrix<M, M> result = twenty.times(twenty);
        assertEquals(expected, result);
        assertEquals(TiledFastMatrix.class, result.getClass());
    }

    private Path path(String name)
    {
        return new File("src/test/resources/" + name).getAbsoluteFile().toPath();
    }
}
