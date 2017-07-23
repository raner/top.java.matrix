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

import top.java.matrix.Dimension;
import top.java.matrix.Matrix;
import top.java.matrix.MatrixFactory;
import top.java.matrix.operations.MatrixMultiplication;

/**
* Basic matrix multiplication coded in plain Java. Dead slow but easy to understand and
* very straightforward.
*
* @author Mirko Raner
**/
public class BasicMultiplication<M extends Dimension, N extends Dimension, K extends Dimension>
extends MatrixMultiplication<M, N, K>
{
    public BasicMultiplication(MatrixFactory factory)
    {
        super(factory);
    }

    @Override
    public Matrix<M, N> apply(Matrix<M, K> left, Matrix<K, N> right)
    {
        int rows = left.getRows();
        int columns = left.getColumns();
        float[] matrix = left.getValues();

        // Multiplication example:
        //
        // ROWS X COLUMNS   COLUMNS X DIMENSION     ROWS x DIMENSION
        //      3 x 5         5 x 2                      3 x 2
        //
        //                  ⎡p q⎤
        //  ⎡a b c d e⎤     ⎢r s⎥       ⎡ap+br+ct+dv+ex   aq+bs+cu+dw+ey⎤
        //  ⎢f g h i j⎥  X  ⎢t u⎥   =   ⎢fp+gr+ht+iv+jx   fq+gs+hu+iw+jy⎥
        //  ⎣k l m n o⎦     ⎢v w⎥       ⎣kp+lr+mt+nv+ox   kq+ls+mu+nw+yo⎦
        //                  ⎣x y⎦
        //
        int dimension = right.getColumns();
        float[] result = new float[rows*dimension];
        for (int x = 0; x < dimension; x++) // columns
        {
            for (int y = 0; y < rows; y++) // rows
            {
                int sum = 0;
                for (int z = 0; z < columns; z++)
                {
                    sum += matrix[y + z*rows] * right.at(z, x);
                }
                result[y + x*rows] = sum;
            }
        }
        return factory.create(Dimension.FACTORY.create(rows), Dimension.FACTORY.create(dimension), result);
    }
}
