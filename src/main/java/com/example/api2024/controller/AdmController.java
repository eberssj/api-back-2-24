package com.example.api2024.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.api2024.entity.Adm;
import com.example.api2024.repository.AdmRepository;

@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3030"})
@RestController
@RequestMapping("/adm")
public class AdmController {

    @Autowired
    private AdmRepository admRepository;

    // Listar todos os administradores
    @GetMapping("/listar")
    public List<Adm> listarAdm() {
        return admRepository.findAll();
    }
    
    // Obter informações do administrador pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Adm> getAdmById(@PathVariable Long id) {
        Optional<Adm> adm = admRepository.findById(id);
        if (adm.isPresent()) {
            return ResponseEntity.ok(adm.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Obter o tipo do administrador pelo email
    @GetMapping("/{email}/tipo")
    public String getTipo(@PathVariable String email) throws Exception {
        return admRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Adm não encontrado"))
                .getTipo();
    }

    // Obter informações do administrador pelo email
    @GetMapping("/{email}/infoAdm")
    public Adm getInfoAdm(@PathVariable String email) throws Exception {
        return admRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Adm não encontrado"));
    }

    // Criar um novo administrador (somente super administrador)
    @PostMapping("/criar")
    public ResponseEntity<String> criarAdm(
            @RequestBody Adm novoAdm,
            @RequestParam Long idSuperAdm) {

        Optional<Adm> superAdm = admRepository.findById(idSuperAdm);

        // Verifica se o criador é um super administrador (tipo == '1')
        if (superAdm.isEmpty() || !"1".equals(superAdm.get().getTipo())) {
            return ResponseEntity.status(403)
                    .body("Acesso negado: Apenas super administradores podem criar novos administradores.");
        }

        admRepository.save(novoAdm);
        return ResponseEntity.ok("Administrador criado com sucesso!");
    }
    
 // Atualizar administrador por ID (somente super administrador)
    @PutMapping("/{id}")
    public ResponseEntity<String> atualizarAdm(
            @PathVariable Long id,
            @RequestBody Adm admAtualizado,
            @RequestParam Long idSuperAdm) {

        Optional<Adm> superAdm = admRepository.findById(idSuperAdm);

        // Verifica se o solicitante é um super administrador (tipo == '1')
        if (superAdm.isEmpty() || !"1".equals(superAdm.get().getTipo())) {
            return ResponseEntity.status(403)
                    .body("Acesso negado: Apenas super administradores podem atualizar administradores.");
        }

        Optional<Adm> admExistente = admRepository.findById(id);
        if (admExistente.isPresent()) {
            Adm adm = admExistente.get();
            adm.setNome(admAtualizado.getNome());
            adm.setEmail(admAtualizado.getEmail());
            adm.setCpf(admAtualizado.getCpf());
            adm.setTelefone(admAtualizado.getTelefone());
            adm.setSenha(admAtualizado.getSenha());
            adm.setTipo(admAtualizado.getTipo());
            adm.setAtivo(admAtualizado.getAtivo());
            admRepository.save(adm);
            return ResponseEntity.ok("Administrador atualizado com sucesso!");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Atualizar o status ativo do administrador por ID
    @PatchMapping("/atualizarStatus/{id}")
    public ResponseEntity<String> patchStatus(@PathVariable Long id, @RequestBody Adm admStatus) {
        Optional<Adm> admExistente = admRepository.findById(id);

        if (admExistente.isPresent()) {
            Adm adm = admExistente.get();
            Boolean novoStatus = admStatus.getAtivo();

            if (novoStatus == null) {
                return ResponseEntity.badRequest().body("O valor de 'ativo' deve ser fornecido.");
            }

            adm.setAtivo(novoStatus);
            admRepository.save(adm);

            String status = novoStatus ? "ativado" : "desativado";
            return ResponseEntity.ok("Administrador " + status + " com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Administrador não encontrado.");
        }
    }


    // Excluir administrador por ID (somente super administrador)
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<String> excluirAdm(
            @PathVariable Long id,
            @RequestParam Long idSuperAdm) {

        Optional<Adm> superAdm = admRepository.findById(idSuperAdm);

        // Verifica se o usuário solicitante é um super administrador (tipo == '1')
        if (superAdm.isEmpty() || !"1".equals(superAdm.get().getTipo())) {
            return ResponseEntity.status(403)
                    .body("Acesso negado: Apenas super administradores podem excluir administradores.");
        }

        admRepository.deleteById(id);
        return ResponseEntity.ok("Administrador excluído com sucesso.");
    }
}
