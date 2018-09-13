package com.company;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws java.lang.Exception
    {
        // HMM0();
        // HMM1();
        // HMM2();
        HMM3();
    }

    public static void HMM0() throws java.lang.Exception
    {
        Scanner scanner = new Scanner(System.in);

        String transitionAsString = scanner.nextLine();
        Matrix transition = new Matrix(transitionAsString);

        String emissionAsString = scanner.nextLine();
        Matrix emission = new Matrix(emissionAsString);

        String piAsString = scanner.nextLine();
        Matrix pi = new Matrix(piAsString);

        Matrix firstCalcul = pi.multiply(transition);
        Matrix result = firstCalcul.multiply(emission);
        System.out.println(result.getnRows() + " " + result.getnColumns() + " " +  result);
    }

    public static void HMM1() throws java.lang.Exception
    {
        Scanner scanner = new Scanner(System.in);

        String transitionAsString = scanner.nextLine();
        Matrix transition = new Matrix(transitionAsString);

        String emissionAsString = scanner.nextLine();
        Matrix emission = new Matrix(emissionAsString);

        String piAsString = scanner.nextLine();
        Matrix pi = new Matrix(piAsString);

        // Here we choose to work on emission as an array and not a matrix for convenience.
        String observationAsString = scanner.nextLine();
        // Little trick so observation can be easily considered as matrix
        // the "1 " need to be understood as 1 row
        observationAsString = "1 " + observationAsString;
        Matrix observation = new Matrix(observationAsString);
        // System.err.println(observation);
        AlphaPass a = new AlphaPass(transition, emission, pi, observation);
        //g
    }

    public static void HMM2() throws java.lang.Exception
    {
        // Q5 if there is T observation, so T state. For delta in each one of those state we keep N
        // (where N is number of state of the HMM). So size(delta) = T*N . Meanwhile we don't need
        // to keep information on indice for the last state so size(deltaidx)= (T-1) * N
        Scanner scanner = new Scanner(System.in);

        String transitionAsString = scanner.nextLine();
        Matrix transition = new Matrix(transitionAsString);

        String emissionAsString = scanner.nextLine();
        Matrix emission = new Matrix(emissionAsString);

        String piAsString = scanner.nextLine();
        Matrix pi = new Matrix(piAsString);

        // Here we choose to work on emission as an array and not a matrix for convenience.
        String observationAsString = scanner.nextLine();
        // Little trick so observation can be easily considered as matrix
        // the "1 " need to be understood as 1 row
        observationAsString = "1 " + observationAsString;
        Matrix observation = new Matrix(observationAsString);

        Viterbi v = new Viterbi(transition, emission, pi, observation);

    }

    public static void HMM3() throws java.lang.Exception {
        Scanner scanner = new Scanner(System.in);

        String transitionAsString = scanner.nextLine();
        Matrix transition = new Matrix(transitionAsString);

        String emissionAsString = scanner.nextLine();
        Matrix emission = new Matrix(emissionAsString);

        String piAsString = scanner.nextLine();
        Matrix pi = new Matrix(piAsString);

        // Here we choose to work on emission as an array and not a matrix for convenience.
        String observationAsString = scanner.nextLine();
        // Little trick so observation can be easily considered as matrix
        // the "1 " need to be understood as 1 row
        observationAsString = "1 " + observationAsString;
        Matrix observation = new Matrix(observationAsString);

        // BaumWelch b = new BaumWelch(transition, emission, pi, observation);
//        BetaPass betaPass = new BetaPass(transition, emission, pi, observation);
//        AlphaPass alphaPass = new AlphaPass(transition, emission, pi, observation);


        BaumWelch  b = new BaumWelch(transition, emission, pi, observation);



    }
}
