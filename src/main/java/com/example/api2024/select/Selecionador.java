package com.example.api2024.select;

import java.util.List;

public interface Selecionador<T,ID>{

    public T selecionar(List<T> objetos, ID identificador);

}