package com.company;

class BaumWelch {
    Matrix transition;
    Matrix emission;
    Matrix pi;
    Matrix observations;

    AlphaPass alphaPass;
    BetaPass betaPass;

    boolean converging = false;
    int numberOfObservation;
    int numberOfState;
    DiGamma diGamma;
    Double[][] gamma ;


    public BaumWelch(Matrix transition, Matrix emission, Matrix pi, Matrix observations) throws Exception{
        this.transition = transition;
        this.emission = emission;
        this.pi = pi;
        this.observations = observations;
        this.numberOfObservation = this.observations.getnColumns();
        this.numberOfState = transition.getnColumns();
        System.err.println(numberOfObservation);
        this.gamma = new Double[numberOfObservation][numberOfState];
            alphaPass = new AlphaPass(transition, emission, pi, observations);
            betaPass = new BetaPass(transition, emission, pi, observations);
            this.diGamma = new DiGamma(this.numberOfObservation, this.numberOfState, this);
;
            calculateGamma();
            reEstimateLambda();

        System.out.println(transition.asOutput());
        System.out.println(emission.asOutput());

    }

    private void calculateGamma()
    {
        for (int t = 0; t < this.numberOfObservation; t++)
        {
            for (int i = 0; i <this.numberOfState; i++)
            {
                this.gamma[t][i] = diGamma.sum(t, i);
            }
        }
    }

    private void reEstimateLambda()
    {
        Matrix newTransition = estimateTransition();
        Matrix newEmission = estimateEmission();
        if (newTransition.equals(this.transition) && newEmission.equals(this.emission))
        {
            this.converging = true;
        }
        else
        {
            this.transition = newTransition;
            this.observations = newEmission;
        }

    }
    private Matrix estimateTransition()
    {
        Double[][] newTransition = new Double[this.numberOfState][this.numberOfState];
        for (int i = 0; i <this.numberOfState; i++){
            for (int j = 0; j < this.numberOfState; j++)
            {
                double num = 0.0;
                double denum = 0.0;
                for (int t = 0; t < this.numberOfObservation - 1; t++)
                {
                    num += diGamma.getElement(t,i,j);
                    denum += gamma[t][i];
                }
                newTransition[i][j] = num/denum;
            }
        }
        return new Matrix(newTransition);
    }

    private Matrix estimateEmission()
    {
        Double[][] newEmission = new Double[this.numberOfState][this.emission.getnColumns()];
        for (int j = 0; j < this.numberOfState; j++)
            for (int k = 0; k < this.emission.getnColumns() ; k++)
            {
                double denum = 0;
                for (int t = 0; t < this.numberOfObservation - 1; t++)
                {
                    double num = 0;
                    denum += gamma[t][j];
                    if (k == this.observations.getElement(0,t))
                    {
                        num += gamma[t][j];
                    }
                    newEmission[j][k] = num/denum;
                }
            }
        return new Matrix(newEmission);
    }


}
