package top.java.matrix.util;

import pro.projo.triples.Factory;
import static pro.projo.Projo.creates;

/**
* The {@link RawFloatMatrix} describes raw data for {@code float} matrices.
*
* @author Mirko Raner
**/
public interface RawFloatMatrix
{
    int rows();
    int columns();
    float[] matrix();

    Factory<RawFloatMatrix, Integer, Integer, float[]> FACTORY = creates(RawFloatMatrix.class)
        .with(RawFloatMatrix::rows, RawFloatMatrix::columns, RawFloatMatrix::matrix);
}
