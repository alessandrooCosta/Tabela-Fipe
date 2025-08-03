package com.project.fipe.Model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Veiculos(
        @JsonAlias("Valor") String valor,
        @JsonAlias("Marca") String marca,
        @JsonAlias("Modelo") String modelo,
        @JsonAlias("AnoModelo") Integer ano,
        @JsonAlias("Combustivel") String tipoCombustivel,
        @JsonAlias("CodigoFipe") String codigoFipe,
        @JsonAlias("MesReferencia") String mesRef
) {
    @Override
    public String toString() {
        return""+ano;
    }
}
