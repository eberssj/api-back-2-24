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
        projeto.setReferenciaProjeto(projetoDto.getReferenciaProjeto());
        projeto.setEmpresa(projetoDto.getEmpresa());
        projeto.setObjeto(projetoDto.getObjeto());
        projeto.setDescricao(projetoDto.getDescricao());
        projeto.setCoordenador(projetoDto.getCoordenador());
        projeto.setValor(projetoDto.getValor());
        projeto.setDataInicio(projetoDto.getDataInicio());
        projeto.setDataTermino(projetoDto.getDataTermino());
        projeto.setSituacao(projetoDto.getSituacao());

        Optional<Adm> admOptional;
        if (projetoDto.getAdm() != null) {
            admOptional = admService.buscarAdm(projetoDto.getAdm()); // Busca o administrador pelo ID fornecido no DTO do projeto
            if (admOptional.isPresent()) { // Verifica se o administrador foi encontrado
                projeto.setAdministrador(admOptional.get()); // Define o administrador do projeto
            } else {
                throw new Exception("Administrador não encontrado com ID: " + projetoDto.getAdm()); // Lança uma exceção se o administrador não for encontrado
            }
        } else {
            throw new Exception("ID do administrador não pode ser nulo"); // Lança uma exceção se o ID do administrador for nulo
        }

        projetoRepository.save(projeto);

        salvarArquivo(propostas, projeto, "Propostas");
        salvarArquivo(contratos, projeto, "Contratos");
        salvarArquivo(artigos, projeto, "Artigos");
    }

    // Método para salvar arquivos na tabela Arquivo
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
}
