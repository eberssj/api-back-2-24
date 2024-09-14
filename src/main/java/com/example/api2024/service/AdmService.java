package com.example.api2024.service;

import com.example.api2024.entity.Adm;
import com.example.api2024.repository.AdmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdmService {

    @Autowired
    private AdmRepository admRepository;

    public List<Adm> listarAdm() { return admRepository.findAll(); }

    public Adm buscarAdm(Long id) throws Exception {
        Adm adm = admRepository.findById(id).orElseThrow(() -> new Exception("Adm não encontrado"));

        return adm;
    }

    public Adm buscarPorEmail (String email) throws Exception {
        return admRepository.findByEmail(email).orElseThrow(() -> new Exception("Adm não encontrado"));
    }
}
