package com.project.fipe.Model;

public record Dados(String codigo, String nome) {
    @Override
    public String toString() {
        return nome;
    }
}
