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
package top.java.matrix.internal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import top.java.matrix.Dimension;
import top.java.matrix.Matrix;
import top.java.matrix.MatrixFactory;
import top.java.matrix.MatrixOperation;
import top.java.matrix.operations.MatrixElementAccess;
import top.java.matrix.operations.MatrixMultiplication;
import top.java.matrix.operations.access.DirectElementAccess;
import top.java.matrix.operations.multiplication.BasicMultiplication;
import top.java.matrix.util.RawFloatMatrix;

/**
* The {@link StandardMatrix} class provides a basic {@link Matrix} implementation.
*
* @param <ROWS> the rows dimension of the matrix
* @param <COLUMNS> the columns dimension of the matrix
*
* @author Mirko Raner
**/
public class StandardMatrix<ROWS extends Dimension, COLUMNS extends Dimension> extends Matrix<ROWS, COLUMNS>
{
    private final Dimension rows;
    private final Dimension columns;
    private final float[] matrix;

    private final Map<Class<? extends MatrixOperation>, MatrixOperation> operations = new HashMap<>();
    {
        operations.put(MatrixMultiplication.class, new BasicMultiplication<>(StandardMatrix::new));
        operations.put(MatrixElementAccess.class, new DirectElementAccess(StandardMatrix::new));
    }

    public StandardMatrix(RawFloatMatrix matrix)
    {
        rows = Dimension.FACTORY.create(matrix.rows());
        columns = Dimension.FACTORY.create(matrix.columns());
        this.matrix = matrix.matrix();
    }

    public StandardMatrix(Dimension rows, Dimension columns, float[] matrix)
    {
        this.rows = rows;
        this.columns = columns;
        this.matrix = matrix;
    }

    @Override
    public Function<RawFloatMatrix, Matrix<?, ?>> constructor()
    {
        return StandardMatrix::new;
    }

    @Override
    public MatrixFactory factory()
    {
        return StandardMatrix::new;
    }

    @Override
    public <DIMENSION extends Dimension> Matrix<ROWS, DIMENSION> times(Matrix<COLUMNS, DIMENSION> rightHandSide)
    {
        @SuppressWarnings("unchecked")
        MatrixMultiplication<ROWS, DIMENSION, COLUMNS> multiplication = operation(MatrixMultiplication.class);
        return multiplication.apply(this, rightHandSide);
    }

    @Override
    public Matrix<COLUMNS, ROWS> transpose()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getRows()
    {
        return rows.getAsInt();
    }

    @Override
    public int getColumns()
    {
        return columns.getAsInt();
    }

    @Override
    public float[] getValues()
    {
        // TODO: this needs to take access strategy into account
        return matrix;
    }

    @Override
    public float at(int row, int column)
    {
        MatrixElementAccess access = operation(MatrixElementAccess.class);
        return access.elementAt(matrix, rows, columns, row, column);
    }

    @Override
    public int hashCode()
    {
        // TODO: this needs to take access strategy into account
        return Objects.hash(rows, columns, Arrays.hashCode(matrix));
    }

    @Override
    public boolean equals(Object other)
    {
        // TODO: this needs to take access strategy into account
        if (other instanceof Matrix)
        {
            Matrix<?, ?> otherMatrix = (Matrix<?, ?>)other;
            if (rows.getAsInt() == otherMatrix.getRows() && columns.getAsInt() == otherMatrix.getColumns())
            {
                return Arrays.equals(otherMatrix.getValues(), matrix);
            }
        }
        return false;
    }

    private <MO extends MatrixOperation> MO operation(Class<? extends MO> operation)
    {
        @SuppressWarnings("unchecked")
        MO implementation = (MO)operations.get(operation);
        return implementation;
    }
}
