package com.company;

public class BetaPass2 {
    Double[] scaling;
    Matrix transition;
    Matrix emission;
    Matrix observation;
    Double[][] beta;

    int N;
    int T;

    public BetaPass2(Double[] scaling, Matrix transition, Matrix emission, Matrix observation) {
        this.scaling = scaling;
        this.transition = transition;
        this.emission = emission;
        this.observation = observation;

        this.N = transition.getnColumns();
        this.T = observation.getnColumns();
        this.beta = new Double[this.T][this.N];

        calculateBeta();
    }

    private  void calculateBeta()
    {
        for (int i = 0; i <= N -1; i++)
        {
            this.beta[T-1][i] = this.scaling[T-1];
        }
        for (int t = T - 2; t >= 0; t--)
        {
            for (int i = 0; i<= N - 1; i++)
            {
                beta[t][i] = 0.0;
                for (int j = 0; j <= N - 1; j++)
                {
                    beta[t][i] += transition.getElement(i,j) * emission.getElement(j, observation.getElement(0,t+1).intValue()) * beta[t+1][j];
                }
                beta[t][i] = scaling[t] * beta[t][i];
            }
        }
    }

}
