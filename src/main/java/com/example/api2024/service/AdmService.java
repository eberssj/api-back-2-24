package com.example.api2024.service;

import com.example.api2024.entity.Adm;
import com.example.api2024.repository.AdmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdmService {

    @Autowired
    private AdmRepository admRepository;

    public Optional<Adm> buscarAdm(Long id) {
        return admRepository.findById(id);
    }

    public boolean existsByEmail(String email) {
        return admRepository.existsByEmail(email);
    }

    public void criarAdm(Adm adm) {
        admRepository.save(adm);
    }

    public void excluirAdm(Long id) {
        admRepository.deleteById(id);
    }
}
