package com.company;

public class DiGamma {
    Double[][][] diGamma;
    int numberOfObservation;
    int numberOfState;

    public DiGamma(int numberOfObservation, int numberOfState, BaumWelch b) {
        this.numberOfObservation = numberOfObservation;
        this.numberOfState = numberOfState;
        this.diGamma = new Double[numberOfObservation][numberOfState][numberOfState];

        for(int t = 0; t < numberOfObservation; t++)
        {
            int o_t = b.observations.getElement(0,t ).intValue();
            Matrix b_ot = b.emission.selectColumn(o_t);
            for (int i = 0; i < numberOfState; i++)
            {
                for (int j = 0; j < numberOfState; j++) {
                    double alphati = b.alphaPass.alpha.getElement(t, j);
                    double  aij = b.transition.getElement(i,j);
                    double bj = b_ot.selectRow(j).getElement(0,0);
                    double betaj = b.betaPass.beta.getElement(t , j);
                    double num = alphati * aij * bj * betaj;
                    double denum = 0;
                    for (int k = 0; k < b.numberOfState; k++)
                         denum +=  b.alphaPass.alpha.getElement(b.numberOfObservation - 1, k);
                    this.diGamma[t][i][j] = num / denum;
                }
            }

        }
    }

    public double getElement(int t, int i, int j)
    {
        return diGamma[t][i][j];
    }

    public double sum(int t, int i)
    {
        double result = 0;
        for (int j = 0; j < this.numberOfState; j++)
        {
            result += diGamma[t][i][j];
        }
        return result;
    }
}
