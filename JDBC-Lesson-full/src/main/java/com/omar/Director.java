package com.omar;

public class Director {
    private int directorId;
    private String firstName;
    private String lastName;

    public Director(int directorId, String firstName, String lastName) {
        this.directorId = directorId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Director{" +
                "directorId=" + directorId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
