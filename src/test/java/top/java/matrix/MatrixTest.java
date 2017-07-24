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
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import top.java.matrix.internal.StandardMatrix;
import top.java.matrix.operations.multiplication.BasicMultiplication;
import top.java.matrix.operations.multiplication.FastMultiplication;
import top.java.matrix.operations.multiplication.ReversedFastMultiplication;
import top.java.matrix.operations.multiplication.TiledFastMultiplication;
import top.java.matrix.util.OctaveFloatBinaryReader;
import top.java.matrix.util.RawFloatMatrix;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static top.java.matrix.util.RawFloatMatrix.FACTORY;

@RunWith(Parameterized.class)
public class MatrixTest<M extends Dimension>
{
    static class ModifiedMatrixConstructor<M extends Dimension, N extends Dimension> extends MatrixConstructor<M, N>
    {
        private MatrixOperation[] operation;

        ModifiedMatrixConstructor(MatrixOperation... operation)
        {
            super(StandardMatrix::new);
            this.operation = operation;
        }

        @Override
        public Matrix<M, N> construct(RawFloatMatrix rawMatrix)
        {
            return super.construct(rawMatrix).using(operation);
        }

        @Override
        public String toString()
        {
            return Stream.of(operation).map(Object::getClass).map(Class::getSimpleName).collect(Collectors.joining(","));
        }
    }

    private OctaveFloatBinaryReader reader = new OctaveFloatBinaryReader();
    private Matrix<M, M> A;
    private Matrix<M, M> B;
    private Matrix<M, M> thousand;
    private Matrix<M, M> squared;
    private float δ = 1E-6F;

    @Parameter
    public MatrixConstructor<M, M> constructor;

    @Parameters(name="{0}")
    public static List<Object[]> implementations()
    {
        Object[][] implementations =
        {
            {new ModifiedMatrixConstructor<>()},
            {new ModifiedMatrixConstructor<>(new BasicMultiplication<>(StandardMatrix::new))},
            {new ModifiedMatrixConstructor<>(new ReversedFastMultiplication<>(StandardMatrix::new))},
            {new ModifiedMatrixConstructor<>(new FastMultiplication<>(StandardMatrix::new))},
            {new ModifiedMatrixConstructor<>(new TiledFastMultiplication<>(StandardMatrix::new))}
        };
        return Arrays.asList(implementations);
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

    @Test
    public void testSmallTranspose5x2()
    {
        final Matrix<M, M> matrix = constructor.construct(FACTORY.create(5, 2, new float[] {1, 3, 5, 7, 9, 2, 4, 6, 8, 10}));
        Matrix<M, M> expected = constructor.construct(FACTORY.create(2, 5, new float[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}));
        assertEquals(expected, matrix.transpose());
    }

    private Path path(String name)
    {
        return new File("src/test/resources/" + name).getAbsoluteFile().toPath();
    }
}
