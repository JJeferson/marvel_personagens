package com.a.marvel_personagens.model;

public class modelo {

    String name;
    String path;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public modelo( ) {

    }
    public modelo(String name, String path) {
        this.name = name;
        this.path = path;
    }

}
