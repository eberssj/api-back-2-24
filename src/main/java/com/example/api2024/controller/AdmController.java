package com.example.api2024.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api2024.entity.Adm;
import com.example.api2024.repository.AdmRepository;

@RestController
@RequestMapping("/adm")
public class AdmController {
    
    @Autowired
    private AdmRepository admRepository;

    @GetMapping("/listar")
    public List<Adm> listarAdm() { return admRepository.findAll(); }
}
