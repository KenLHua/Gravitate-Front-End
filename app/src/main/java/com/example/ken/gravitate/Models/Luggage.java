package com.example.ken.gravitate.Models;

public class Luggage {

    private int weight;
    private String size;

    public Luggage(int inputWeight,String inputSize){

        weight = inputWeight;
        size = inputSize;
    }

    public int getWeight(){

        return weight;
    }

    public String getSize(){
        return size;
    }


}