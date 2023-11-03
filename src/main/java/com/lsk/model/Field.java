package com.lsk.model;

public class Field {
    private String meaning;
    private char[] bytes;

    public Field() {
    }

    public Field(String meaning, char[] bytes) {
        this.meaning = meaning;
        this.bytes = bytes;
    }


    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public char[] getBytes() {
        return bytes;
    }

    public void setBytes(char[] bytes) {
        this.bytes = bytes;
    }

}
