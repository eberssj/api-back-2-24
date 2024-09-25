package com.example.api2024.service;

import com.example.api2024.dto.ProjetoDto;
import com.example.api2024.entity.Adm;
import com.example.api2024.entity.Projeto;
import com.example.api2024.repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private AdmService admService;

    @SuppressWarnings("unused")
    public void cadastrarProjeto(ProjetoDto projetoDto) throws Exception {
        Projeto projeto = projetoDto.toEntity();

        projeto.setReferenciaProjeto(projetoDto.getReferenciaProjeto());
        projeto.setEmpresa(projetoDto.getEmpresa());
        projeto.setObjeto(projetoDto.getObjeto());
        projeto.setDescricao(projetoDto.getDescricao());
        projeto.setCoordenador(projetoDto.getCoordenador());
        projeto.setValor(projetoDto.getValor());
        projeto.setDataInicio(projetoDto.getDataInicio());
        projeto.setDataTermino(projetoDto.getDataTermino());
        projeto.setPropostaRelatorios(projetoDto.getPropostaRelatorios());
        projeto.setContratos(projetoDto.getContratos());
        projeto.setArtigos(projetoDto.getArtigos());
        projeto.setSituacao(projetoDto.getSituacao());
        projeto.setAdministrador(admService.buscarAdm(projetoDto.getIdAdm()));

        projetoRepository.save(projeto);
    }
}
