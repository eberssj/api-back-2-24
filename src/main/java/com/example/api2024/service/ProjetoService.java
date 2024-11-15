package com.example.api2024.service;

import com.example.api2024.dto.ProjetoDto;
import com.example.api2024.entity.Adm;
import com.example.api2024.entity.Arquivo;
import com.example.api2024.entity.Permissao;
import com.example.api2024.entity.Projeto;
import com.example.api2024.repository.ArquivoRepository;
import com.example.api2024.repository.PermissaoRepository;
import com.example.api2024.repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.stream.Collectors;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private ArquivoRepository arquivoRepository;

    @Autowired
    private PermissaoRepository permissaoRepository;

    @Autowired
    private AdmService admService;


    public Projeto buscarProjetoPorId(Long id) {
        return projetoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado."));
    }

    public void cadastrarProjeto(ProjetoDto projetoDto, MultipartFile propostas, MultipartFile contratos, MultipartFile artigos) throws Exception {
        Projeto projeto = new Projeto();

        // Validar o formato da referência
        String referencia = projetoDto.getReferenciaProjeto();
        if (!referencia.matches("\\d{3}/\\d{2}")) {
            throw new RuntimeException("A referência deve estar no formato XXX/YY.");
        }

        // Validar unicidade da referência
        if (projetoRepository.existsByReferenciaProjeto(referencia)) {
            throw new RuntimeException("A referência fornecida já está em uso.");
        }

        // Populando os dados do projeto
        projeto.setReferenciaProjeto(referencia);
        projeto.setNome(projetoDto.getNome());
        projeto.setEmpresa(projetoDto.getEmpresa());
        projeto.setObjeto(projetoDto.getObjeto());
        projeto.setDescricao(projetoDto.getDescricao());
        projeto.setCoordenador(projetoDto.getCoordenador());
        projeto.setValor(projetoDto.getValor());
        projeto.setOcultarEmpresa(projetoDto.getOcultarEmpresa());
        projeto.setOcultarValor(projetoDto.getOcultarValor());
        projeto.setDataInicio(projetoDto.getDataInicio());
        projeto.setDataTermino(projetoDto.getDataTermino());
        projeto.setSituacao(projetoDto.getSituacao());

        // Verificação do administrador
        Adm administrador = admService.buscarAdm(projetoDto.getAdm())
                .orElseThrow(() -> new RuntimeException("Administrador não encontrado com ID: " + projetoDto.getAdm()));
        projeto.setAdm(administrador);

        // Salvando o projeto
        projetoRepository.save(projeto);

        // Salvando os arquivos
        salvarArquivo(propostas, projeto, "Propostas");
        salvarArquivo(contratos, projeto, "Contratos");
        salvarArquivo(artigos, projeto, "Artigos");
    }

    // Método para listar todos os projetos
    public List<Projeto> listarProjetos() {
        return projetoRepository.findAll();
    }

    // Método para salvar arquivos
    private void salvarArquivo(MultipartFile file, Projeto projeto, String tipoDocumento) throws IOException {
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

    // Método para editar projeto
    @Transactional
    public Projeto editarProjeto(
            Long id,
            ProjetoDto projetoDto,
            MultipartFile propostas,
            MultipartFile contratos,
            MultipartFile artigos,
            List<Long> arquivosExcluidos) throws IOException {

        Projeto projetoExistente = projetoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado com ID: " + id));

        // Atualizar informações do projeto
        projetoExistente.setReferenciaProjeto(projetoDto.getReferenciaProjeto());
        projetoExistente.setNome(projetoDto.getNome());
        projetoExistente.setEmpresa(projetoDto.getEmpresa());
        projetoExistente.setObjeto(projetoDto.getObjeto());
        projetoExistente.setDescricao(projetoDto.getDescricao());
        projetoExistente.setCoordenador(projetoDto.getCoordenador());
        projetoExistente.setOcultarValor(projetoDto.getOcultarValor());
        projetoExistente.setOcultarEmpresa(projetoDto.getOcultarEmpresa());
        projetoExistente.setValor(projetoDto.getValor());
        projetoExistente.setDataInicio(projetoDto.getDataInicio());
        projetoExistente.setDataTermino(projetoDto.getDataTermino());

        // Verificar situação
        String situacao = projetoDto.getDataTermino().isAfter(LocalDate.now()) ? "Em Andamento" : "Encerrado";
        projetoExistente.setSituacao(situacao);

        // Excluir arquivos se solicitado
        if (arquivosExcluidos != null && !arquivosExcluidos.isEmpty()) {
            for (Long arquivoId : arquivosExcluidos) {
                arquivoRepository.deleteById(arquivoId);
            }
        }

        // Salvar novos arquivos, se fornecidos
        if (propostas != null) salvarArquivo(propostas, projetoExistente, "Propostas");
        if (contratos != null) salvarArquivo(contratos, projetoExistente, "Contratos");
        if (artigos != null) salvarArquivo(artigos, projetoExistente, "Artigos");

        return projetoRepository.save(projetoExistente);
    }

    // Método para excluir um projeto e seus arquivos associados
    @Transactional
    public void excluirProjeto(Long projetoId) {
        Projeto projeto = buscarProjetoPorId(projetoId);

        List<Permissao> permissoes = permissaoRepository.findByProjetoId(projetoId);
        if (!permissoes.isEmpty()) {
            permissaoRepository.deleteAll(permissoes);
        }

        List<Arquivo> arquivos = arquivoRepository.findByProjetoId(projetoId);
        if (!arquivos.isEmpty()) {
            arquivoRepository.deleteAll(arquivos);
        }

        projetoRepository.delete(projeto);
    }
    
    public String calcularProximaReferencia() {
        int anoAtual = LocalDate.now().getYear() % 100;
        List<Integer> numerosUtilizados = projetoRepository.findAll()
            .stream()
            .map(Projeto::getReferenciaProjeto)
            .filter(ref -> ref.endsWith("/" + anoAtual))
            .map(ref -> Integer.parseInt(ref.split("/")[0]))
            .sorted()
            .collect(Collectors.toList());

        for (int i = 1; i <= 999; i++) {
            if (!numerosUtilizados.contains(i)) {
                return String.format("%03d/%02d", i, anoAtual);
            }
        }
        throw new RuntimeException("Todas as referências para o ano " + anoAtual + " estão ocupadas.");
    }
}

	
