package com.company;

import java.util.Arrays;

public class AlphaPass {

    Matrix transition;
    Matrix emission;
    Matrix pi;
    Matrix observations;
    Double[] scaling;

    Matrix alpha ;

    public AlphaPass(Matrix transition, Matrix emission, Matrix pi, Matrix observations)throws Exception {
        this.transition = transition;
        this.emission = emission;
        this.pi = pi;
        this.observations = observations;
        alpha = Matrix.createEmptyMatrix(this.observations.getnColumns(), this.emission.getnRows());
        // System.err.println(this);
        // System.err.println("ans " + calculateAlphaI(observations.getnColumns()));
        this.scaling = new Double[this.observations.getnColumns()];
        Matrix alphaT = calculateAlphaI(observations.getnColumns());
        // System.out.println(String.format("%.5g%n",alphaT.sum()));
    }

    @Override
    public String toString() {
        return "AlphaPass{" +
                "transition=" + transition +
                ", emission=" + emission +
                ", pi=" + pi +
                ", observations=" + observations +
                '}';
    }

    private Matrix calculateAlphaI(int T) throws Exception
   {
       Double[][] alphaT;
       if(T == 1)
       {
           alphaT = baseCase();
       }
       else
       {
           alphaT = recurrentCase(T);
       }
       // System.err.println("fin" + T);
       return(new Matrix(alphaT));
   }

   private Double[][] baseCase()
   {
       int o_t = this.observations.getElement(0,0).intValue();
       // System.err.println("debut " + T);
       Double[][] alphaT = new Double[1][this.pi.getnColumns()];
       this.scaling[0] = 0.0;
       int N = pi.getnColumns();

       for (int i = 0; i < N ; i++)
       {
           alphaT[0][i] = this.emission.getElement(i,o_t) * pi.getElement(0,i);
           this.scaling[0] += alphaT[0][i];
       }
       this.scaling[0] = 1/this.scaling[0];
       for (int i = 0; i < N ; i++)
       {
           alphaT[0][i] *= this.scaling[0];
       }
       this.alpha.setRow(0, alphaT);
       return alphaT;
   }

    private Double[][] recurrentCase(int T) throws Exception
    {
        int o_t = this.observations.getElement(0,T-1).intValue();
        Double[][] alphaT = new Double[1][this.pi.getnColumns()];

        int N = pi.getnColumns();
        this.scaling[T - 1] = 0.0;
        Matrix previousAlpha = this.calculateAlphaI(T - 1);
        double sum;

        for (int i = 0; i < N ; i++)
        {
            sum = 0;
            for (int j = 0; j < N; j++)
            {
                sum += transition.getElement(j,i) * previousAlpha.getElement(0,j);
            }
            alphaT[0][i] = sum * this.emission.getElement(i,o_t);
            this.scaling[T - 1] += alphaT[0][i];
        }
        this.scaling[T-1] = 1/this.scaling[T-1];
        for (int i = 0; i < N ; i++)
        {
            alphaT[0][i] = alphaT[0][i] * this.scaling[T-1];
        }
            this.alpha.setRow(T-1, alphaT);
        return alphaT;
    }
}
