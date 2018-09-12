package com.company;

import java.util.Arrays;

public class Viterbi {
    Matrix transition;
    Matrix emission;
    Matrix pi;
    Matrix observations;
    Matrix delta;
    Matrix deltaIdx = null;

    public Viterbi(Matrix transition, Matrix emission, Matrix pi, Matrix observations) {
        this.transition = transition;
        this.emission = emission;
        this.pi = pi;
        this.observations = observations;
        calculateDeltaT(this.observations.getnColumns());
        int[] result = findMostLikelySequence(this.observations.getnColumns());

        for (int el:result)
        {
            System.out.println(el);
        }
    }

    private int mostLikelyState(int T)
    {
        return (delta.selectRow(T-1).maxIndex()[1]);
    }

    private int[] findMostLikelySequence(int T)
    {

        int[] bestSequence = new int[T];
        bestSequence[T - 1] = mostLikelyState(this.observations.getnColumns());
        for(int i = T - 1 ; i > 0; i--)
        {
            bestSequence[i - 1] = (deltaIdx.getElement(i,bestSequence[i]).intValue());
        }
        return bestSequence;
    }

    private void calculateDeltaT(int T)
    {
        if(T == 1)
        {
            baseCase();
        }
        else
        {
            recurrentCase(T);
        }
    }

    private void baseCase()
    {
        int o_t = this.observations.getElement(0,0).intValue();
        // System.err.println("debut " + T);
        Double[][] deltaT = new Double[1][this.pi.getnColumns()];
        Matrix b_ot = this.emission.selectColumn(o_t);
        int N = pi.getnColumns();

        for (int i = 0; i < N ; i++)
        {
            deltaT[0][i] = b_ot.selectRow(i).getElement(0,0) * pi.selectColumn(i).getElement(0,0);
        }
        this.delta = new Matrix(deltaT);

    }

    private void recurrentCase(int T)
    {
        calculateDeltaT(T - 1);
        int N = this.pi.getnColumns();
        Double[][] deltaT = new Double[1][N];
        Double[][] deltaTIdx = new Double[1][N];
        int o_t = this.observations.getElement(0,T-1).intValue();
        Matrix b_ot = this.emission.selectColumn(o_t);
        for (int i = 0; i < N ; i++)
        {
            Double deltMax = 0.0;
            int deltIdxMax = 0;
            Double deltTemp;


            for (int j = 0; j < N ; j++) {
                // T-2 because we want T-1 and our array start at 0
                deltTemp = transition.getElement(j, i) * this.delta.getElement(T - 2, j) *
                        b_ot.selectRow(i).getElement(0,0);
                if (deltTemp > deltMax)
                {
                    deltMax = deltTemp;
                    deltIdxMax = j;
                }
            }
            deltaT[0][i] = deltMax;
            deltaTIdx[0][i] = (double) deltIdxMax;
        }

        Matrix deltaTMatrix = new Matrix(deltaT);
        Matrix deltaTIdxMatrix = new Matrix(deltaTIdx);

        if (this.deltaIdx == null)
        {
            this.deltaIdx = deltaTIdxMatrix ;
        }
        this.deltaIdx.addOnerow(deltaTIdxMatrix);
        this.delta.addOnerow(deltaTMatrix);
    }



}
