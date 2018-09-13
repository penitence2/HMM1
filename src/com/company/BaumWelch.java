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
        this.gamma = new Double[numberOfObservation][numberOfState];
        this.diGamma = new Double[numberOfObservation][numberOfState][numberOfState];
        double oldLogProb;
        double logProb = 0;
        int i =0;
        do {
            System.err.println(i);
            oldLogProb = logProb;
            alphaPass = new AlphaPass(this.transition, this.emission, this.pi, this.observations);
            betaPass = new BetaPass(this.transition, this.emission, this.pi, this.observations, alphaPass.scaling);
            this.scaling = alphaPass.scaling;
            calculateGamma();
            reEstimateLambda();
            logProb = computelogProb();
        //System.err.println(betaPass.beta);
              System.out.println(this.transition.asOutput());
              System.out.println(this.emission.asOutput());
//            System.out.println(this.pi);
        }while (i++ < 1 || logProb > oldLogProb);




    }
    private void calculateGamma()
    {
        double denum;
        System.err.println("to del");
        for(int t = 0; t< this.numberOfObservation - 1 ;t++)
        {
            denum = 0;
            int o_t = this.observations.getElement(0,t + 1).intValue();
            Matrix b_ot = this.emission.selectColumn(o_t);

            for (int i=0; i < this.numberOfState; i++)
            {
                for (int j=0; j < this.numberOfState; j++)
                {
                    double alphati =  this.alphaPass.alpha.getElement(t, i);
                    double aij = this.transition.getElement(i,j);
                    double bj = b_ot.selectRow(j).getElement(0,0);
                    double betaj = this.betaPass.beta.getElement(t+1, j);
                    denum +=  alphati * aij * bj * betaj;

                }
            }
            for (int i=0; i < this.numberOfState; i++)
            {
                this.gamma[t][i] = 0.0;
                for (int j=0; j < this.numberOfState; j++) {
                    double alphati =  this.alphaPass.alpha.getElement(t, i);
                    double aij = this.transition.getElement(i,j);
                    double bj = b_ot.selectRow(j).getElement(0,0);
                    double betaj = this.betaPass.beta.getElement(t+1, j);
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

        if (newTransition.equals(this.transition) && newEmission.equals(this.emission))
        {
            this.converging = true;
        }
        else {
            this.transition = newTransition;
            this.emission = newEmission;
            this.pi = newPi;
        }

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
        for (int j = 0; j < this.numberOfState; j++)
        {
            for (int k = 0; k < this.emission.getnColumns() ; k++)
            {
                double denum = 0;
                double num = 0;
                for (int t = 0; t < this.numberOfObservation - 1; t++)
                {
                    denum += gamma[t][j];
                    if (k == this.observations.getElement(0,t))
                    {
                        num += gamma[t][j];
                    }
                }
                newEmission[j][k] = num/denum;
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
