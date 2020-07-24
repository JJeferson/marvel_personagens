package com.a.marvel_personagens.model;

public class modelo {

    String name;
    String path;
    String description;
    String dados_adicionais;

    public modelo(String name, String path, String description, String dados_adicionais) {
        this.name = name;
        this.path = path;
        this.description = description;
        this.dados_adicionais = dados_adicionais;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDados_adicionais() {
        return dados_adicionais;
    }

    public void setDados_adicionais(String dados_adicionais) {
        this.dados_adicionais = dados_adicionais;
    }
}
