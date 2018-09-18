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
        long startTimeGlob = System.nanoTime();
        double timeAlapha = 0;
        double timeBeta = 0;
        double timeGamma = 0;
        double timeLambda = 0;
        double timeLogprob = 0;

        do {
            oldLogProb = logProb;
            long startTime = System.nanoTime();
            this.alphaPass = new AlphaPass(this.transition, this.emission, this.pi, this.observations);
            long endTime = System.nanoTime();
            timeAlapha += (endTime - startTime );

            startTime = System.nanoTime();
            this.betaPass = new BetaPass2(this.alphaPass.scaling, this.transition, this.emission, this.observations);
            endTime = System.nanoTime();
            timeBeta += (endTime - startTime );

            this.scaling =  this.alphaPass.scaling;
            this.gamma = new Double[numberOfObservation][numberOfState];
            this.diGamma = new Double[numberOfObservation][numberOfState][numberOfState];

            startTime = System.nanoTime();
            calculateGamma();
            endTime = System.nanoTime();
            timeGamma += (endTime - startTime );

            startTime = System.nanoTime();
            reEstimateLambda();
            endTime = System.nanoTime();
            timeLambda += (endTime - startTime );

            startTime = System.nanoTime();
            logProb = computelogProb();
            endTime = System.nanoTime();
            timeLogprob += (endTime - startTime );
//           System.out.println(this.pi);
        }while (i++ < 1 || logProb > oldLogProb);


        System.out.println(this.transition.asOutput());
        System.out.println(this.emission.asOutput());
        System.out.println(timeAlapha / 1000000 );
        System.out.println(timeBeta / 1000000);
        System.out.println(timeGamma / 1000000);
        System.out.println(timeLambda / 1000000);
        System.out.println(timeLogprob / 1000000);
        long endTimeGlobal = System.nanoTime();
        System.out.println((endTimeGlobal - startTimeGlob )/ 1000000);

    }
    private void calculateGamma()
    {
        double denum;
        for(int t = 0; t < this.numberOfObservation - 1 ;t++)
        {
            denum = 0;
            int o_t = this.observations.getElement(0,t + 1).intValue();

            for (int i=0; i < this.numberOfState; i++)
            {
                for (int j=0; j < this.numberOfState; j++)
                {
                    double alphati =  this.alphaPass.alpha.getElement(t, i);
                    double aij = this.transition.getElement(i,j);
                    double bj = this.emission.getElement(j,o_t);
                    double betaj = this.betaPass.beta[t+1][j];
                    denum +=  alphati * aij * bj * betaj;

                }
            }
            for (int i=0; i < this.numberOfState; i++)
            {
                this.gamma[t][i] = 0.0;
                for (int j=0; j < this.numberOfState; j++) {
                    double alphati =  this.alphaPass.alpha.getElement(t, i);
                    double aij = this.transition.getElement(i,j);
                    double bj = this.emission.getElement(j,o_t);
                    double betaj = this.betaPass.beta[t+1][j];
                    this.diGamma[t][i][j] = alphati * aij * bj * betaj /denum;
                    this.gamma[t][i] += this.diGamma[t][i][j];
                }
            }
        }
        //special cae for T -1
        denum = 0;
        for (int i=0; i < this.numberOfState; i++)
        {
            denum = denum + this.alphaPass.alpha.getElement(this.numberOfObservation - 1, i);
        }
        for (int i=0; i < this.numberOfState; i++)
        {
            this.gamma[this.numberOfObservation - 1][i] =
                    this.alphaPass.alpha.getElement(this.numberOfObservation - 1, i) / denum;
        }
    }
    private void calculateGamma2()
    {
        for (int t = 0; t < this.numberOfObservation; t++)
        {
            for (int i = 0; i <this.numberOfState; i++)
            {
                // this.gamma[t][i] = diGamma.sum(t, i);
            }
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
        Double[][] newPi = new Double[this.pi.getnRows()][this.pi.getnColumns()];
        for (int i = 0 ; i < this.pi.getnColumns();i++)
            newPi[0][i] = gamma[0][i];
        return new Matrix(newPi);
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
        Double[][] newEmission = new Double[this.numberOfState][this.emission.getnColumns()];
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
