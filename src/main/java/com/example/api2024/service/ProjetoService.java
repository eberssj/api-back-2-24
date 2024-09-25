package com.example.api2024.service;

import com.example.api2024.dto.ProjetoDto;
import com.example.api2024.entity.Projeto;
import com.example.api2024.repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    public void cadastrarProjeto(ProjetoDto projetoDto, MultipartFile propostas, MultipartFile contratos, MultipartFile artigos) throws IOException {
        // Criação de entidade do projeto
        Projeto projeto = new Projeto();
        projeto.setReferenciaProjeto(projetoDto.getReferenciaProjeto());
        projeto.setEmpresa(projetoDto.getEmpresa());
        projeto.setObjeto(projetoDto.getObjeto());
        projeto.setDescricao(projetoDto.getDescricao());
        projeto.setCoordenador(projetoDto.getCoordenador());
        projeto.setValor(projetoDto.getValor());
        projeto.setDataInicio(projetoDto.getDataInicio());
        projeto.setDataTermino(projetoDto.getDataTermino());
        projeto.setSituacao(projetoDto.getSituacao());

        // Aqui você pode salvar os arquivos no sistema de arquivos ou outro serviço.
        if (propostas != null && !propostas.isEmpty()) {
            // Lógica para armazenar o arquivo de propostas
            System.out.println("Salvando arquivo de propostas: " + propostas.getOriginalFilename());
        }

        if (contratos != null && !contratos.isEmpty()) {
            // Lógica para armazenar o arquivo de contratos
            System.out.println("Salvando arquivo de contratos: " + contratos.getOriginalFilename());
        }

        if (artigos != null && !artigos.isEmpty()) {
            // Lógica para armazenar o arquivo de artigos
            System.out.println("Salvando arquivo de artigos: " + artigos.getOriginalFilename());
        }

        // Salvar o projeto no banco de dados
        projetoRepository.save(projeto);
    }
}
