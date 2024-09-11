package com.example.api2024.select;

import com.example.api2024.entity.Adm;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SelecionadorAdmId implements Selecionador<Adm, Long>{

    @Override
    public Adm selecionar(List<Adm> objetos, Long identificador) {
        Adm adm = null;
        for (Adm objeto : objetos) {
            if (objeto.getId().longValue() == identificador.longValue()) {
                adm = objeto;
                break;
            }
        }
        return adm;
    }
}