package com.company;
import java.util.Arrays;
class BaumWelch {
    Matrix transition;
    Matrix emission;
    Matrix pi;
    Matrix observations;

    AlphaPass alphaPass;
    BetaPass2 betaPass;

    boolean converging = false;
    int numberOfObservation;
    int numberOfState;
    Double[][][] diGamma;
    Double[][] gamma ;
    Double[] scaling;

    Double[][] newPi;
    Double[][] newTransition;
    Double[][] newEmission;

    public BaumWelch(Matrix transition, Matrix emission, Matrix pi, Matrix observations) throws Exception {
        this.transition = transition;
        this.emission = emission;
        this.pi = pi;
        this.observations = observations;
        this.numberOfObservation = this.observations.getnColumns();
        this.numberOfState = transition.getnColumns();
        double oldLogProb;
        double logProb = 0;
        int i =0;
        this.newTransition = new Double[this.numberOfState][this.numberOfState];
        this.newPi = new Double[this.pi.getnRows()][this.pi.getnColumns()];
        this.newEmission = new Double[this.numberOfState][this.emission.getnColumns()];

        this.gamma = new Double[numberOfObservation][numberOfState];
        this.diGamma = new Double[numberOfObservation][numberOfState][numberOfState];
        do {
            oldLogProb = logProb;
            long startTime = System.nanoTime();
            this.alphaPass = new AlphaPass(this.transition, this.emission, this.pi, this.observations);
            this.betaPass = new BetaPass2(this.alphaPass.scaling, this.transition, this.emission, this.observations);
            this.scaling =  this.alphaPass.scaling;
            calculateGamma();
            reEstimateLambda();
            logProb = computelogProb();
//           System.out.println(this.pi);
        }while ((i++ < 1 || logProb > oldLogProb ) && i< 500 );


        System.out.println(this.transition.asOutput());
        System.out.println(this.emission.asOutput());



    }
    private void calculateGamma()
    {
        double denum;
        for(int t = 0; t < this.numberOfObservation - 1 ;t++)
        {
            denum = 0;
            int o_t = this.observations.mAsArray[0][t + 1].intValue();

            for (int i=0; i < this.numberOfState; i++)
            {
                for (int j=0; j < this.numberOfState; j++)
                {
                    denum +=  this.alphaPass.alpha.mAsArray[t][i] * this.transition.mAsArray[i][j] * this.emission.mAsArray[j][o_t] * this.betaPass.beta[t+1][j];

                }
            }
            for (int i=0; i < this.numberOfState; i++)
            {
                this.gamma[t][i] = 0.0;
                for (int j=0; j < this.numberOfState; j++) {
                    this.diGamma[t][i][j] = this.alphaPass.alpha.mAsArray[t][i] * this.transition.mAsArray[i][j] *
                            this.emission.mAsArray[j][o_t] * this.betaPass.beta[t+1][j] / denum;
                    this.gamma[t][i] += this.diGamma[t][i][j];
                }

            }
        }


        //special case for T -1
        denum = 0;
        for (int i=0; i < this.numberOfState; i++)
        {
            denum +=  this.alphaPass.alpha.mAsArray[this.numberOfObservation - 1] [i];
        }
        for (int i=0; i < this.numberOfState; i++)
        {
            this.gamma[this.numberOfObservation - 1][i] =
                    this.alphaPass.alpha.mAsArray[this.numberOfObservation - 1] [i] / denum;
        }
    }


    private void reEstimateLambda()
    {
        Matrix newTransition = estimateTransition();
        Matrix newEmission = estimateEmission();
        Matrix newPi = estimatePi();
        this.transition = newTransition;
        this.emission = newEmission;
        this.pi = newPi;


    }

    private Matrix estimatePi()
    {
        for (int i = 0 ; i < this.pi.getnColumns();i++)
            newPi[0][i] = gamma[0][i];
        return new Matrix(newPi);
    }

    private Matrix estimateTransition()
    {
        for (int i = 0; i <this.numberOfState; i++){
            for (int j = 0; j < this.numberOfState; j++)
            {
                double num = 0.0;
                double denum = 0.0;
                for (int t = 0; t < this.numberOfObservation - 1; t++)
                {
                    num += diGamma[t][i][j];
                    denum += gamma[t][i];
                }
                newTransition[i][j] = num/denum;
            }
        }
        return new Matrix(newTransition);
    }

    private Matrix estimateEmission()
    {
        for (int i = 0; i < this.numberOfState; i++)
        {
            for (int j = 0; j < this.emission.getnColumns() ; j++)
            {
                double denum = 0;
                double num = 0;
                for (int t = 0; t < this.numberOfObservation - 1; t++)
                {
                    denum += gamma[t][i];
                    if (j == this.observations.getElement(0,t))
                    {
                        num += gamma[t][i];
                    }
                }
                newEmission[i][j] = num/denum;
            }
        }
        return new Matrix(newEmission);
    }


    public double computelogProb()
    {
        double logProb = 0;
        for (int i=0; i< this.numberOfObservation;i++){
            logProb += Math.log(this.scaling[i]);
        }
        return -logProb;
    }


}
