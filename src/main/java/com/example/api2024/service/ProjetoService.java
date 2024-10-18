package com.example.api2024.service;

import com.example.api2024.dto.ProjetoDto;
import com.example.api2024.entity.Adm;
import com.example.api2024.entity.Arquivo;
import com.example.api2024.entity.Projeto;
import com.example.api2024.repository.ArquivoRepository;
import com.example.api2024.repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private ArquivoRepository arquivoRepository;

    @Autowired
    private AdmService admService;

    // Método para cadastrar um novo projeto
    public void cadastrarProjeto(ProjetoDto projetoDto, MultipartFile propostas, MultipartFile contratos, MultipartFile artigos) throws Exception {
        Projeto projeto = new Projeto();
        // Populando os dados do projeto
        projeto.setReferenciaProjeto(projetoDto.getReferenciaProjeto());
        projeto.setEmpresa(projetoDto.getEmpresa());
        projeto.setObjeto(projetoDto.getObjeto());
        projeto.setDescricao(projetoDto.getDescricao());
        projeto.setCoordenador(projetoDto.getCoordenador());
        projeto.setValor(projetoDto.getValor());
        projeto.setDataInicio(projetoDto.getDataInicio());
        projeto.setDataTermino(projetoDto.getDataTermino());
        projeto.setSituacao(projetoDto.getSituacao());

        // Verificação do administrador
        Optional<Adm> admOptional = admService.buscarAdm(projetoDto.getAdm());
        if (admOptional.isPresent()) {
            projeto.setAdministrador(admOptional.get());
        } else {
            throw new Exception("Administrador não encontrado com ID: " + projetoDto.getAdm());
        }

        // Salvando o projeto
        projetoRepository.save(projeto);

        // Salvando os arquivos
        salvarArquivo(propostas, projeto, "Propostas");
        salvarArquivo(contratos, projeto, "Contratos");
        salvarArquivo(artigos, projeto, "Artigos");
    }

    // Método para salvar arquivos
    private void salvarArquivo(MultipartFile file, Projeto projeto, String tipoDocumento) throws Exception {
        if (file != null && !file.isEmpty()) {
            Arquivo arquivo = new Arquivo();
            arquivo.setNomeArquivo(file.getOriginalFilename());
            arquivo.setTipoArquivo(file.getContentType());
            arquivo.setConteudo(file.getBytes());
            arquivo.setTipoDocumento(tipoDocumento);
            arquivo.setProjeto(projeto);
            arquivoRepository.save(arquivo);
        }
    }

    // Método para listar todos os projetos
    public List<Projeto> listarProjetos() {
        return projetoRepository.findAll();
    }

    // Método para editar projeto
    public Projeto editarProjeto(Long id, ProjetoDto projetoDto, MultipartFile propostas, MultipartFile contratos, MultipartFile artigos) throws IOException {
        Projeto projetoExistente = projetoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado com ID: " + id));

        // Buscar o administrador usando o campo 'adm'
        Adm administrador = admService.buscarAdm(projetoDto.getAdm())
                .orElseThrow(() -> new IllegalArgumentException("Administrador não encontrado com ID: " + projetoDto.getAdm()));

        projetoExistente.setReferenciaProjeto(projetoDto.getReferenciaProjeto());
        projetoExistente.setEmpresa(projetoDto.getEmpresa());
        projetoExistente.setObjeto(projetoDto.getObjeto());
        projetoExistente.setDescricao(projetoDto.getDescricao());
        projetoExistente.setCoordenador(projetoDto.getCoordenador());
        projetoExistente.setValor(projetoDto.getValor());
        projetoExistente.setDataInicio(projetoDto.getDataInicio());
        projetoExistente.setDataTermino(projetoDto.getDataTermino());
        projetoExistente.setAdministrador(administrador);

        String situacao = projetoDto.getDataTermino().isAfter(LocalDate.now()) ? "Em Andamento" : "Encerrado";
        projetoExistente.setSituacao(situacao);

        projetoRepository.save(projetoExistente);

        // Salvar arquivos, se houver
        try {
            if (propostas != null) salvarArquivo(propostas, projetoExistente, "Propostas");
            if (contratos != null) salvarArquivo(contratos, projetoExistente, "Contratos");
            if (artigos != null) salvarArquivo(artigos, projetoExistente, "Artigos");
        } catch (Exception e) {
            System.err.println("Erro ao salvar arquivo: " + e.getMessage());
            e.printStackTrace();
        }

        return projetoExistente;
    }




    // Método para excluir um projeto e seus arquivos associados
    public void excluirProjeto(Long projetoId) {
        Optional<Projeto> projetoOptional = projetoRepository.findById(projetoId);
        if (projetoOptional.isPresent()) {
            Projeto projeto = projetoOptional.get();

            // Primeiro, exclua os arquivos associados a este projeto
            List<Arquivo> arquivos = arquivoRepository.findByProjetoId(projetoId);
            for (Arquivo arquivo : arquivos) {
                arquivoRepository.delete(arquivo);  // Exclui cada arquivo
            }

            // Agora, exclua o projeto
            projetoRepository.delete(projeto);
        } else {
            throw new RuntimeException("Projeto não encontrado com ID: " + projetoId);
        }
    }
}
