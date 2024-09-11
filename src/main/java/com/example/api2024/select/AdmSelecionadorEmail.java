package com.example.api2024.select;

import java.util.List;

import com.example.api2024.entity.Adm;
import org.springframework.stereotype.Component;

@Component
public class AdmSelecionadorEmail implements Selecionador<Adm, String> {

    @Override
    public Adm selecionar(List<Adm> objects, String ident) {
        Adm adm = null;
        for (Adm object : objects) {
            if (object.getEmail().trim().equalsIgnoreCase(ident.trim())) {
                adm = object;
                break;
            }
        }
        return adm;
    }
}
