package top.java.matrix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import top.java.matrix.Dimension;
import top.java.matrix.Matrix;
import top.java.matrix.fast.FastMatrix;
import top.java.matrix.naive.NaiveMatrix;
import top.java.matrix.util.OctaveFloatBinaryReader;
import static top.java.matrix.util.RawFloatMatrix.FACTORY;

@RunWith(Parameterized.class)
public class MatrixTest<M extends Dimension>
{
    private OctaveFloatBinaryReader reader = new OctaveFloatBinaryReader();
    private Matrix<M, M> A;
    private Matrix<M, M> B;
    private Matrix<M, M> thousand;
    private Matrix<M, M> squared;
    private float δ = 1E-6F;

    @Parameter
    public MatrixConstructor<M, M> constructor;

    @Parameters(name="{0}")
    public static List<Object[]> implementations() {
        MatrixConstructor<?, ?> naive = new MatrixConstructor<>(NaiveMatrix::new);
        MatrixConstructor<?, ?> fast = new MatrixConstructor<>(FastMatrix::new);
        return Arrays.asList(new Object[][] {{naive}, {fast}});
    }

    @Before
    public void initializeMatrices() throws IOException
    {
        // TODO: load only once!
        A = constructor.construct(reader.readFloatBinaryMatrix(path("MatrixA.float.bin")));
        B = constructor.construct(reader.readFloatBinaryMatrix(path("MatrixB.float.bin")));
        thousand = constructor.construct(reader.readFloatBinaryMatrix(path("Thousand.float.bin")));
        squared = constructor.construct(reader.readFloatBinaryMatrix(path("Squared.float.bin")));
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

    @Test
    public void testEqualsEqual()
    {
        assertEquals(A, A);
    }

    @Test
    public void testEqualsNotEqual()
    {
        assertNotEquals(A, B);
    }

    @Test
    public void testSmallMultiplication3x3()
    {
        final Matrix<M, M> X = constructor.construct(FACTORY.create(3, 3, new float[] {8, 5, 2, 1, 9, 7, 3, 0, 4}));
        final Matrix<M, M> Y = constructor.construct(FACTORY.create(3, 3, new float[] {8, 3, 4, 1, 5, 9, 6, 7, 2}));
        Matrix<M, M> expected = constructor.construct(FACTORY.create(3, 3, new float[] {79, 67, 53, 40, 50, 73, 61, 93, 69}));
        Matrix<M, M> result = X.times(Y);
        assertEquals(expected, result);
    }

    @Test
    public void testSmallMultiplication3x5by5x2()
    {
        final Matrix<M, M> X = constructor.construct(FACTORY.create(3, 5, new float[] {1, 6, 11, 2, 7, 12, 3, 8, 13, 4, 9, 14, 5, 10, 15}));
        final Matrix<M, M> Y = constructor.construct(FACTORY.create(5, 2, new float[] {1, 3, 5, 7, 9, 2, 4, 6, 8, 10}));
        Matrix<M, M> expected = constructor.construct(FACTORY.create(3, 2, new float[] {95, 220, 345, 110, 260, 410}));
        Matrix<M, M> result = X.times(Y);
        assertEquals(expected, result);
    }

    @Test
    public void testLargeMultiplication1000x1000()
    {
        Matrix<M, M> expected = squared;
        Matrix<M, M> result = thousand.times(thousand);
        assertEquals(expected, result);
    }

    private Path path(String name)
    {
        return new File("src/test/resources/" + name).getAbsoluteFile().toPath();
    }
}
