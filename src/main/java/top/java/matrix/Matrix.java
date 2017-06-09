package top.java.matrix;

public interface Matrix<ROWS extends Dimension, COLUMNS extends Dimension> {

    <DIMENSION extends Dimension> Matrix<ROWS, DIMENSION> times(Matrix<COLUMNS, DIMENSION> rightHandSide);

    Matrix<COLUMNS, ROWS> transpose();
}
