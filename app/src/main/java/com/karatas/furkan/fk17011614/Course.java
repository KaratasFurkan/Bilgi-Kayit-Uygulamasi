package com.karatas.furkan.fk17011614;

public class Course {
    private String name;
    private int numberOfStudents;
    private double mean;

    public Course(String name, int numberOfStudents, double mean) {
        this.name = name;
        this.numberOfStudents = numberOfStudents;
        this.mean = mean;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public double getMean() {
        return mean;
    }

    @Override
    public String toString() {
        return  name + "  " + numberOfStudents + " " + mean;
    }
}
