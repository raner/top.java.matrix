//                                                                          //
// Copyright 2017 Mirko Raner                                               //
//                                                                          //
// Licensed under the Apache License, Version 2.0 (the "License");          //
// you may not use this file except in compliance with the License.         //
// You may obtain a copy of the License at                                  //
//                                                                          //
//     http://www.apache.org/licenses/LICENSE-2.0                           //
//                                                                          //
// Unless required by applicable law or agreed to in writing, software      //
// distributed under the License is distributed on an "AS IS" BASIS,        //
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. //
// See the License for the specific language governing permissions and      //
// limitations under the License.                                           //
//                                                                          //
package top.java.matrix;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import top.java.matrix.internal.StandardMatrix;
import top.java.matrix.operations.multiplication.TiledFastMultiplication;
import top.java.matrix.util.OctaveFloatBinaryReader;
import top.java.matrix.util.RawFloatMatrix;
import static org.junit.Assert.assertEquals;

/**
* The class {@link TiledFastMatrixTestSmallMatrices} provides additional JUnit tests for
* {@link TiledFastMatrix} that use smaller matrices.
*
* TODO: for some reason this test class does not get picked up by the Maven Surefire plugin
*
* @param <M> the dimension of the matrices (number of rows is the same as number of columns)
*
* @author Mirko Raner
**/
@RunWith(Parameterized.class)
public class TiledFastMatrixTestSmallMatrices<M extends Dimension>
{
    private static OctaveFloatBinaryReader reader = new OctaveFloatBinaryReader();

    @Parameter(0)
    public int size;

    @Parameter(1)
    public Matrix<M, M> left;

    @Parameter(2)
    public Matrix<M, M> right;

    @Parameter(3)
    public Matrix<M, M> expected;

    @Parameters(name="{0}\u00D7{0} matrix")
    public static List<Object[]> inputsAndOutputs() throws Exception
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
        abstract class M implements Dimension {/**/}
        final TiledFastMultiplication<M, M, M> TFM = new TiledFastMultiplication<>(StandardMatrix::new);
        Matrix<M, M> left = new StandardMatrix<>(RawFloatMatrix.FACTORY.create(6, 6, matrix1));
        Matrix<M, M> right = new StandardMatrix<>(RawFloatMatrix.FACTORY.create(6, 6, matrix2));
        Matrix<M, M> expected = new StandardMatrix<>(RawFloatMatrix.FACTORY.create(6, 6, product));
        Matrix<M, M> twenty = new StandardMatrix<M, M>(reader.readFloatBinaryMatrix(path("matrix20x20.float.bin"))).using(TFM);
        Matrix<M, M> squared = new StandardMatrix<M, M>(reader.readFloatBinaryMatrix(path("square20x20.float.bin"))).using(TFM);
        Object[][] parameters =
        {
                {left.getRows(), left, right, expected},
                {twenty.getRows(), twenty, twenty, squared}
        };
        return Arrays.asList(parameters);
    }

    @Test
    public void testMultiplication()
    {
        assertEquals(expected, left.times(right));
    }

    private static Path path(String name)
    {
        return new File("src/test/resources/" + name).getAbsoluteFile().toPath();
    }
}
