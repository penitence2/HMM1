package com.company;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws java.lang.Exception
    {
        // HMM1();
        HMM2();
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

        Matrix firstCalcul = pi.multiply(transition);
        Matrix result = firstCalcul.multiply(emission);
        System.out.println(result.getnRows() + " " + result.getnColumns() + " " +  result);
    }

    public static void HMM2() throws java.lang.Exception
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
        observationAsString = "1 " + observationAsString;
        Matrix observation = new Matrix(observationAsString);
        // System.err.println(observation);
        AlphaPass a = new AlphaPass(transition, emission, pi, observation);
        //g
    }
}
