package com.example.api2024.service;

import com.example.api2024.entity.Adm;
import com.example.api2024.repository.AdmRepository;
import com.example.api2024.select.AdmSelecionadorEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdmDetailsService implements UserDetailsService {

    @Autowired
    private AdmRepository admRepository;

    @Autowired
    private AdmSelecionadorEmail admSelecionadorEmail;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<Adm> adms = admRepository.findAll();
        Adm adm = admSelecionadorEmail.selecionar(adms, email);

        if (adm == null) {
            throw new UsernameNotFoundException(email);
        }
        return new AdmDetails(adm.getEmail(), adm.getSenha());
    }
}