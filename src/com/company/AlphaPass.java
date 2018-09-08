package com.company;

import java.util.Arrays;

public class AlphaPass {

    Matrix transition;
    Matrix emission;
    Matrix pi;
    Matrix observations;
    Matrix NULL = null;

    Double probability = null;


    public AlphaPass(Matrix transition, Matrix emission, Matrix pi, Matrix observations)throws Exception {
        this.transition = transition;
        this.emission = emission;
        this.pi = pi;
        this.observations = observations;
        // System.err.println(this);
        // System.err.println("ans " + calculateAlphaI(observations.getnColumns()));
        System.out.println(calculateAlphaI(observations.getnColumns()).max());
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
       Matrix b_ot = this.emission.selectColumn(o_t);
       int N = pi.getnColumns();

       for (int i = 0; i < N ; i++)
       {
           alphaT[0][i] = b_ot.selectRow(i).getElement(0,0) * pi.selectColumn(i).getElement(0,0);
       }

       return alphaT;
   }

    private Double[][] recurrentCase(int T) throws Exception
    {
        int o_t = this.observations.getElement(0,T-1).intValue();
        Double[][] alphaT = new Double[1][this.pi.getnColumns()];
        Matrix b_ot = this.emission.selectColumn(o_t);

        int N = pi.getnColumns();
        Matrix previousAlpha = this.calculateAlphaI(T - 1);

        double sum;

        for (int i = 0; i < N ; i++)
        {
            sum = 0;
            for (int j = 0; j < N; j++)
            {
                sum += transition.getElement(j,i) * previousAlpha.getElement(0,j);
            }
            alphaT[0][i] = sum * b_ot.selectRow(i).getElement(0,0);
        }
        return alphaT;
    }
}