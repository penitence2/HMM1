package com.company;

public class BetaPass {
    Matrix transition;
    Matrix emission;
    Matrix pi;
    Matrix observations;
    Matrix beta ;
    Double[] scaling;

    int numberOfState;
    int numberOfObservation;

    public BetaPass(Matrix transition, Matrix emission, Matrix pi, Matrix observations, Double[] scaling) throws Exception{
        this.transition = transition;
        this.emission = emission;
        this.pi = pi;
        this.observations = observations;
        this.numberOfState = transition.getnColumns();
        this.numberOfObservation = observations.getnColumns();
        this.scaling = scaling;
        this.beta = Matrix.createEmptyMatrix(this.numberOfObservation, this.numberOfState);

        Matrix betaT = calculateBetaI(numberOfObservation);
        beta.setRow(numberOfObservation - 1, betaT);

        for(int i = numberOfObservation - 1; i >0 ; i-- )
            beta.setRow( i - 1, calculateBetaI(i));

    }

    private Matrix calculateBetaI(int T) throws Exception
    {
        Double[][] betaI;
        if(T == observations.getnColumns())
        {
            betaI = baseCase();
        }
        else
        {
            betaI = recurrentCase(T -1);
        }
        // System.err.println("fin" + T);
        return(new Matrix(betaI));
    }

    private Double[][] baseCase()
    {
        Double[][] b_t = new Double[1][this.numberOfState];
        for (int i = 0; i < this.numberOfState; i++) {
            b_t[0][i] = 1.0 * this.scaling[this.numberOfState - 1];
        }
        return  b_t;

    }
    private Double[][] recurrentCase(int T) throws Exception
    {
        int o_t = this.observations.getElement(0,T + 1).intValue();
        Matrix b_ot = this.emission.selectColumn(o_t);

        Double[][] beta_t = new Double[1][this.numberOfState];
        double aij;
        double bj;
        double betaj;
        for (int i = 0; i < this.numberOfState; i++)
        {
            beta_t[0][i] = 0.0;
            for (int j = 0; j < this.numberOfState; j++)
            {
                aij = transition.getElement(j,i);
                bj = b_ot.selectRow(j).getElement(0,0);
                betaj = this.beta.getElement(T + 1, j);
                beta_t[0][i] += aij * bj * betaj;
            }
            beta_t[0][i] = beta_t[0][i] * this.scaling[T];
        }
        return beta_t;
    }

}
