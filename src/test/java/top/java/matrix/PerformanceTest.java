package top.java.matrix;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import top.java.matrix.util.OctaveFloatBinaryReader;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

/**
* {@link PerformanceTest} is an abstract base class for matrix multiplication performance testing.
*
* @param <M> the dimensions of the test matrix
*
* @author Mirko Raner
**/
@RunWith(Parameterized.class)
public abstract class PerformanceTest<M extends Dimension>
{
    public final static int REPETITIONS = 10;

    private OctaveFloatBinaryReader reader = new OctaveFloatBinaryReader();
    private Matrix<M, M> thousand;
    private Matrix<M, M> squared;

    @Parameter
    public int repetition;

    @Parameters(name="Run #{0}")
    public static List<Object[]> implementations()
    {
        return IntStream.range(1, REPETITIONS+1).mapToObj(index -> new Object[] {index}).collect(toList());
    }

    @Before
    public void initializeMatrices() throws IOException
    {
        // TODO: load only once!
        thousand = constructor().construct(reader.readFloatBinaryMatrix(path("Thousand.float.bin")));
        squared = constructor().construct(reader.readFloatBinaryMatrix(path("Squared.float.bin")));
    }

    @Test
    public void testLargeMultiplication1000x1000()
    {
        Matrix<M, M> expected = squared;
        Matrix<M, M> result = thousand.times(thousand);
        assertEquals(expected, result);
    }

    protected abstract MatrixConstructor<M, M> constructor();

    private Path path(String name)
    {
        return new File("src/test/resources/" + name).getAbsoluteFile().toPath();
    }
}
