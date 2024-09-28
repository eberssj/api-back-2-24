package com.example.api2024.service;

import com.example.api2024.entity.Adm;
import com.example.api2024.repository.AdmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdmService {

    @Autowired // Injeta uma instância de AdmRepository
    public AdmRepository admRepository;

    public Optional<Adm> buscarAdm(Long id) { // Método para buscar um administrador pelo ID

        return admRepository.findById(id); // Retorna um Optional contendo o administrador encontrado ou vazio se não encontrado
    }

    public boolean existsByEmail(String email) { // Método para verificar se um administrador existe pelo email

        return admRepository.existsByEmail(email); // Retorna true se o email existir, caso contrário, false
    }
}
