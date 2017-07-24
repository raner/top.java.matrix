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
package top.java.matrix.operations.multiplication;

import com.aparapi.Kernel;
import com.aparapi.Range;
import top.java.matrix.Dimension;
import top.java.matrix.Matrix;
import top.java.matrix.MatrixFactory;
import top.java.matrix.operations.MatrixMultiplication;

/**
* {@link ReversedFastMultiplication} implements a GPU-accelerated matrix, but with reversed row and column order
* (i.e., target columns first, then target rows). This performs the same function as the regular {@link FastMultiplication},
* but due to the less sequential memory access it is about 2.5x slower (though it is still about 3x faster than naive matrix
* multiplication). Therefore, in practice, there is very little reason to use {@link ReversedFastMultiplication}, and it
* mainly serves as a reference implementation for performance comparisons.
*
* @author Mirko Raner
**/
public class ReversedFastMultiplication<M extends Dimension, N extends Dimension, K extends Dimension>
extends MatrixMultiplication<M, N, K>
{
    public ReversedFastMultiplication(MatrixFactory factory)
    {
        super(factory);
    }

    @Override
    public Matrix<M, N> apply(Matrix<M, K> left, Matrix<K, N> right)
    {
        int numberOfRows = left.getRows();
        int numberOfColumns = left.getColumns();
        int targetNumberOfRows = right.getRows();
        int targetNumberOfColumns = right.getColumns();
        final float[] A = left.getValues();
        final float[] B = right.getValues();
        final float[] C = new float[numberOfRows*targetNumberOfColumns];
        Kernel kernel = new Kernel()
        {
            @Override
            public void run() {
                int row = getGlobalId(1);
                int column = getGlobalId(0);
                int value = 0;
                for (int repeat = 0; repeat < numberOfColumns; repeat++)
                {
                    value += A[row + repeat*numberOfRows] * B[repeat + column*targetNumberOfRows];
                }
                C[row + column*numberOfRows] = value;
            }
         };
         kernel.execute(Range.create2D(targetNumberOfColumns, numberOfRows));
         return factory.create(Dimension.FACTORY.create(numberOfRows), Dimension.FACTORY.create(targetNumberOfColumns), C);
    }
}
