package Util;

import java.util.Arrays;

/**
 * Created by ptaxom on 13.11.2018.
 */
public class Matrix {

    private int height;
    private int width;

    private double[][] Array;


    //Constructors


    public Matrix() {
    }


    public Matrix(int height, int width) {
        this.height = height;
        this.width = width;
        Array = new double[height][width];
    }

    public Matrix(double[][] array) {
        this.height = array.length;
        this.width = array[0].length;
        Array = new double[height][width];

        for(int i = 0; i < height; i++)
            for(int j = 0; j < width; j++)
                Array[i][j] = array[i][j];
    }


    public Matrix(Matrix matrix){
        clone(matrix);
    }


    //Copy methods

    private void clone(Matrix matrix){
        if (this != matrix){
            this.height = matrix.height;
            this.width = matrix.width;
            this.Array = copyArray(matrix.Array);
        }
    }


    private double[][] copyArray(double[][] array){
        int n = array.length;
        int m = array[0].length;

        double[][] arr = new double[n][m];

        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++)
                arr[i][j] = array[i][j];

        return arr;
    }


    //Operations


    public void Transpose(){
        Matrix C = new Matrix(this.width, this.height);

        for(int i = 0; i < this.width; i++)
            for(int j = 0; j < this.height; j++)
                C.Array[j][i] = this.Array[i][j];

        this.clone(C);
    }

    public void ApplyFunction(Function f){

        for(int i = 0; i < this.width; i++)
            for(int j = 0; j < this.height; j++)
                this.Array[i][j] = f.function(this.Array[i][j]);

    }

    //Throwable operations

    public void Add(Matrix m) throws InvalidMatrixSizeException {
        if (this.height == m.height && this.width == m.width){
            for(int i = 0; i < height; i++)
                for(int j = 0; j < width; j++)
                    Array[i][j] += m.Array[i][j];
        }
        else throw new InvalidMatrixSizeException("Matrix sizes do not match!");
    }

    public void Multiply(Matrix m) throws InvalidMatrixSizeException {
        if (this.width == m.height){
            Matrix C = new Matrix(this.height, m.width);

            for(int i = 0; i < C.height; i++)
                for(int j = 0; j < C.width; j++){
                    double sum = 0;

                    for(int k = 0; k < this.width; k++)
                        sum += this.Array[i][k] * m.Array[k][j];

                    C.Array[i][j] = sum;
                }

            this.clone(C);
        }
        else  throw new InvalidMatrixSizeException("Matrix sizes do not match!");
    }


    public double getVal(int i, int j)// throws InvalidMatrixSizeException
    {
        if (i >= 0 && i < height && j >= 0 && j < width)
            return Array[i][j];
        return 0;
        //else throw new InvalidMatrixSizeException("Out of bounds");
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public String toString() {
        String out = super.toString();
        out+= "\n"+height+"x"+width+"\n";
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
                out += Array[i][j] + " ";
            out += "\n";
        }
        return out;
    }
}
