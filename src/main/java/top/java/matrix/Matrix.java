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

import java.util.function.Function;
import top.java.matrix.util.RawFloatMatrix;

/**
* The {@link Matrix} interface describes a single-precision matrix of arbitrary (but type-safe) dimensions.
*
* @param <ROWS> the rows dimension of the matrix
* @param <COLUMNS> the columns dimension of the matrix
*
* @author Mirko Raner
**/
public abstract class Matrix<ROWS extends Dimension, COLUMNS extends Dimension>
{
    public abstract Function<RawFloatMatrix, Matrix<?, ?>> constructor();

    public abstract MatrixFactory factory();

    public abstract <DIMENSION extends Dimension> Matrix<ROWS, DIMENSION> times(Matrix<COLUMNS, DIMENSION> rightHandSide);

    public abstract Matrix<COLUMNS, ROWS> transpose();

    public abstract int getRows();

    public abstract int getColumns();

    public abstract float[] getValues();

    public abstract float at(int zeroIndexedRow, int zeroIndexedColumn);

    public abstract Matrix<ROWS, COLUMNS> using(MatrixOperation... operations);
}
