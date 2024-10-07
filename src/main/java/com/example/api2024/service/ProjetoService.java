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

    // Método para editar um projeto existente
    public Projeto editarProjeto(Long id, ProjetoDto projetoDto, MultipartFile propostas, MultipartFile contratos, MultipartFile artigos) throws Exception {
        Optional<Projeto> projetoOptional = projetoRepository.findById(id);
        if (projetoOptional.isPresent()) {
            Projeto projeto = projetoOptional.get();
            // Atualizando os dados do projeto
            projeto.setReferenciaProjeto(projetoDto.getReferenciaProjeto());
            projeto.setEmpresa(projetoDto.getEmpresa());
            projeto.setObjeto(projetoDto.getObjeto());
            projeto.setDescricao(projetoDto.getDescricao());
            projeto.setCoordenador(projetoDto.getCoordenador());
            projeto.setValor(projetoDto.getValor());
            projeto.setDataInicio(projetoDto.getDataInicio());
            projeto.setDataTermino(projetoDto.getDataTermino());
            projeto.setSituacao(projetoDto.getSituacao());

            // Atualizando o administrador
            Optional<Adm> admOptional = admService.buscarAdm(projetoDto.getAdm());
            if (admOptional.isPresent()) {
                projeto.setAdministrador(admOptional.get());
            } else {
                throw new Exception("Administrador não encontrado com ID: " + projetoDto.getAdm());
            }

            projetoRepository.save(projeto);

            // Atualizando ou inserindo novos arquivos, caso existam
            salvarArquivo(propostas, projeto, "Propostas");
            salvarArquivo(contratos, projeto, "Contratos");
            salvarArquivo(artigos, projeto, "Artigos");

            return projeto;
        } else {
            throw new Exception("Projeto não encontrado com ID: " + id);
        }
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
