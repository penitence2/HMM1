package com.company;


import java.util.Arrays;

class Matrix {
    public Double[][] mAsArray;

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

    public static Matrix createEmptyMatrix(int nrows, int ncolumn)
    {
        Double[][] mAsArray = new Double[nrows][ncolumn];
        return new Matrix(mAsArray);
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

    public void Matrix2 (String mAsString) throws  Exception {
        String[] stringValues;
        stringValues = mAsString.split(" ");
        int sizeMatrixRow = Integer.valueOf(stringValues[0]);
        int sizeMatrixColumn = Integer.valueOf(stringValues[1]);
        Double[] values = new Double[sizeMatrixRow * sizeMatrixColumn];
        int j = 0;
        String number = "";
        for (int i = 0; i < mAsString.length(); i++) {
            if (mAsString.charAt(i) == ' ') {
                if (j >= 2) {
                    values[j - 2] = Double.valueOf(number);
                }
                j++;
                number = "";
            } else
                number += mAsString.charAt(i);


        }
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

    public Integer[] maxIndex()
    {
        int maxRow = 0;
        int maxColumn = 0;
        Integer[] answer = new Integer[2];
        double max = 0;
        for (int i = 0; i < this.getnRows(); i++) {
            for (int j = 0; j < this.getnColumns(); j++) {
                if (this.mAsArray[i][j] > max) {
                    max = this.mAsArray[i][j];
                    maxRow = i;
                    maxColumn = j;
                }
            }
        }
        answer[0] = maxRow;
        answer[1] = maxColumn;
        return answer;
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
    /*  NOT TESTED*/
    public Matrix addOneColumn(Matrix newColumn)
    {
        Double[][] newMAsArray = new Double[this.getnRows()][(this.getnColumns() + 1)];
        for (int i = 0; i < this.getnRows(); i++) {
            for (int j = 0; j < this.getnColumns(); j++) {
                newMAsArray[i][j] = this.mAsArray[i][j];
            }
            newMAsArray[i][this.getnColumns()] =  newColumn.getElement(i,0);
        }
        mAsArray = newMAsArray;
        return this;
    }

    public Matrix addOnerow(Matrix newRow)
    {
        Double[][] newMAsArray = new Double[this.getnRows() + 1][(this.getnColumns())];
        for (int i = 0; i < this.getnRows(); i++) {
            for (int j = 0; j < this.getnColumns(); j++) {
                newMAsArray[i][j] = this.mAsArray[i][j];
            }
        }
        for (int j = 0; j < this.getnColumns(); j++) {
            newMAsArray[this.getnRows()][j] = newRow.getElement(0,j);
        }
        mAsArray = newMAsArray;
        return this;
    }

    public Matrix addOneNumber(double number) throws Exception
    {
        if(this.getnRows() !=1 )
            throw new Exception("The matrix must have only one row");
        Double[][] newMAsArray = new Double[this.getnRows()][(this.getnColumns() + 1)];
        for (int j = 0; j < this.getnColumns(); j++) {
            newMAsArray[0][j] = this.mAsArray[0][j];
        }
        newMAsArray[0][this.getnColumns()] = number;
        mAsArray = newMAsArray;
        return this;
    }

    public String asOutput(){
        String result = String.valueOf(this.getnRows());
        result += " ";
        result += String.valueOf(this.getnColumns());
        result += " ";
        for (int i = 0; i < this.getnRows(); i++) {
            for (int j = 0; j < this.getnColumns(); j++) {
                result += (this.mAsArray[i][j] + " ");
            }
        }
        return result;
    }

    public Matrix setRow(int numberOfRow, Matrix m)
    {
        setRow(numberOfRow, m.mAsArray);
        return this;
    }

    public  Matrix setRow(int numberOfRow, Double[][] mAsArray)
    {
        for (int i = 0; i < this.getnColumns(); i++)
        {
            this.mAsArray[numberOfRow][i] = mAsArray[0][i];
        }
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Matrix))
            return false;
        Matrix m = (Matrix) obj;
        for (int i = 0; i < this.getnRows(); i++) {
            for (int j = 0; j < this.getnColumns(); j++) {
                if(!this.mAsArray[i][j].equals(m.mAsArray[i][j]))
                    return false;
            }
        }
        return true;
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
