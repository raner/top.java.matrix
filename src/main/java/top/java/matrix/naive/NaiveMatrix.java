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
package top.java.matrix.naive;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import top.java.matrix.Dimension;
import top.java.matrix.Matrix;
import top.java.matrix.operations.multiplication.BasicMultiplication;
import top.java.matrix.util.RawFloatMatrix;

public class NaiveMatrix<ROWS extends Dimension, COLUMNS extends Dimension> implements Matrix<ROWS, COLUMNS>
{
    private int rows;
    private int columns;
    private float[] matrix;

    public NaiveMatrix(int rows, int columns, float[] matrix)
    {
        this.rows = rows;
        this.columns = columns;
        this.matrix = matrix;
    }

    public NaiveMatrix(Dimension rows, Dimension columns, float[] matrix)
    {
        this.rows = rows.getAsInt();
        this.columns = columns.getAsInt();
        this.matrix = matrix;
    }

    @Override
    public Function<RawFloatMatrix, Matrix<?, ?>> constructor()
    {
        return NaiveMatrix::new;
    }

    public NaiveMatrix(RawFloatMatrix matrix)
    {
        rows = matrix.rows();
        columns = matrix.columns();
        this.matrix = matrix.matrix();
    }

    @Override
    public <DIMENSION extends Dimension> Matrix<ROWS, DIMENSION> times(Matrix<COLUMNS, DIMENSION> rightHandSide)
    {
        BasicMultiplication<ROWS, DIMENSION, COLUMNS> multiplication = new BasicMultiplication<>(NaiveMatrix::new);
        return multiplication.apply(this, rightHandSide);
    }

    @Override
    public Matrix<COLUMNS, ROWS> transpose()
    {
        float[] transpose = new float[rows*columns];
        for (int column = 0; column < columns; column++)
        {
            for (int row = 0; row < rows; row++)
            {
                transpose[column + row*columns] = matrix[row + column*rows];
            }
        }
        return new NaiveMatrix<>(columns, rows, transpose);
    }

    @Override
    public float at(int zeroIndexedRow, int zeroIndexedColumn)
    {
        return matrix[zeroIndexedRow + zeroIndexedColumn*rows];
    }

    @Override
    public int getRows()
    {
        return rows;
    }

    @Override
    public int getColumns()
    {
        return columns;
    }

    @Override
    public float[] getValues()
    {
        return matrix;
    }

    @Override
    public boolean equals(Object other)
    {
        if (other instanceof Matrix)
        {
            Matrix<?, ?> otherMatrix = (Matrix<?, ?>)other;
            if (rows == otherMatrix.getRows() && columns == otherMatrix.getColumns())
            {
                return Arrays.equals(otherMatrix.getValues(), matrix);
            }
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(rows, columns, Arrays.hashCode(matrix));
    }

    @Override
    public String toString()
    {
        StringBuilder string = new StringBuilder();
        for (int row = 0; row < rows; row++)
        {
            for (int column = 0; column < columns; column++)
            {
                string.append(matrix[row + column*rows]).append(' ');
            }
            string.append(System.getProperty("line.separator"));
        }
        return string.toString();
    }
}
