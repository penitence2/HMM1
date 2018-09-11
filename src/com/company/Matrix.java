package com.company;


import java.util.Arrays;

class Matrix {
    private Double[][] mAsArray;

    public int getnRows() {
        return mAsArray.length;
    }

    public int getnColumns() {
        return mAsArray[0].length;
    }

    private Double[][] createMatrix(int nrow, int ncol, Double[] value) throws  Exception {
        mAsArray = new Double[nrow][ncol];

        if (value.length != nrow * ncol) throw new Exception("Error of dimension");

        else
        {
            for(int row = 0; row < nrow; row++)
                for(int col = 0; col < ncol; col++)
                    mAsArray[row][col] = value[ row * ncol + (col)];
        }
        return mAsArray;
    }

    Matrix (String mAsString) throws  Exception
    {

        String[] stringValues;
        stringValues = mAsString.split(" ");
        int sizeMatrixRow = Integer.valueOf(stringValues[0]);
        int sizeMatrixColumn = Integer.valueOf(stringValues[1]);
        Double[] values = new Double[sizeMatrixRow * sizeMatrixColumn] ;
        int i = 0; // I don't like java, why no append method on basic array ???
        for (String value: Arrays.copyOfRange(stringValues,2,sizeMatrixRow * sizeMatrixColumn + 2)) {
            values[i++] = (Double.valueOf(value));
        }

        this.mAsArray = this.createMatrix(sizeMatrixRow, sizeMatrixColumn, values);
    }

    Matrix( Double[][] mAsArray)
    {
        this.mAsArray = mAsArray;
    }


    private static Double[][] multiplyArray(Double[][] m1, Double[][] m2)
    {
        /*
        Thanks to https://stackoverflow.com/questions/17623876/matrix-multiplication-using-arrays for the structure.
         */
        int aRows = m1.length;
        int aColumns = m1[0].length;
        int bRows = m2.length;
        int bColumns = m2[0].length;

        if (aColumns != bRows) {
            throw new IllegalArgumentException("Can't multiply those matrix aRows = " + aRows + "aColumns = " + aColumns
            + "bRows = " + bRows + " bColumns = " + bColumns);
        }

        Double[][] C = new Double[aRows][bColumns];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bColumns; j++) {
                C[i][j] = 0.00000;
            }
        }

        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < bColumns; j++) { // bColumn
                for (int k = 0; k < aColumns; k++) { // aColumn
                    C[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }

        return C;
    }

    public Matrix multiply(Matrix m)
    {
        return new Matrix(multiplyArray(this.mAsArray, m.mAsArray));
    }

    public Matrix selectColumn(int colonneNumber)
    {
        Double[][] newMatrixAsArray = new Double[this.getnRows()][1];
        for(int i = 0; i < this.getnRows(); i++ )
        {
            newMatrixAsArray[i][0] = this.mAsArray[i][colonneNumber];
        }
        return new Matrix(newMatrixAsArray);
    }

    public Matrix selectRow(int rowNumber)
    {
        Double[][] newMatrixAsArray = new Double[1][this.getnColumns()];
        for(int i = 0; i < this.getnColumns(); i++ )
        {
            newMatrixAsArray[0][i] = this.mAsArray[rowNumber][i];
        }
        return new Matrix(newMatrixAsArray);
    }

    public Double getElement(int row ,int column)
    {
        return this.mAsArray[row][column];
    }

    public Double max()
    {
        Double max = 0.0;
        for (int i = 0; i < this.getnRows(); i++) {
            for (int j = 0; j < this.getnColumns(); j++) {
                if (this.mAsArray[i][j] > max)
                    max = this.mAsArray[i][j];
            }
        }
        return max;
    }

    public Double sum()
    {
        Double sum = 0.0;
        for (int i = 0; i < this.getnRows(); i++) {
            for (int j = 0; j < this.getnColumns(); j++) {
                    sum += this.mAsArray[i][j];
            }
        }
        return sum;
    }

    @Override
    public String toString() {
        String result = new String();

        for (int i = 0; i < this.getnRows(); i++) {
            for (int j = 0; j < this.getnColumns(); j++) {
                result += (this.mAsArray[i][j] + " ");
            }
            result += ("\n");
        }
        return result;
    }
}
