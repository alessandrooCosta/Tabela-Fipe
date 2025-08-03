package com.project.fipe.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface InterfaceConverteDados {
    <T> T obterDados(String json, Class<T> classe) throws JsonProcessingException;

    <T>List<T> obterLista(String json, Class<T> classe);
}
