package com.example.api2024.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.UUID;

import com.example.api2024.entity.Adm;
import com.example.api2024.repository.AdmRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3030"})
@RestController
@RequestMapping("/adm")
public class AdmController {

    @Autowired
    private AdmRepository admRepository;

     @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    // Método de criação de administrador com token
    @PostMapping("/criar")
    public ResponseEntity<String> criarAdm(
            @RequestBody Adm novoAdm,
            @RequestParam Long idSuperAdm) {
        Optional<Adm> superAdm = admRepository.findById(idSuperAdm);
    
        if (superAdm.isEmpty() || !"1".equals(superAdm.get().getTipo())) {
            return ResponseEntity.status(403).body("Acesso negado: Apenas super administradores podem criar novos administradores.");
        }
    
        // Gera o token de redefinição de senha
        String token = UUID.randomUUID().toString();
        novoAdm.setTokenRedefinicao(token); // Salva o token no banco
        admRepository.save(novoAdm);
    
        try {
            enviarEmailBoasVindas(novoAdm.getEmail(), token); // Passa o token para o e-mail
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Administrador criado, mas o e-mail não pôde ser enviado.");
        }
    
        return ResponseEntity.ok("Administrador criado com sucesso!");
    }
    
    private void enviarEmailBoasVindas(String emailDestino, String token) throws MessagingException {
        MimeMessage mensagem = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensagem, "utf-8");
    
        String mensagemHtml = "<h3>Bem-vindo ao sistema!</h3>"
                + "<p>Olá, você foi cadastrado como administrador em nosso sistema.</p>"
                + "<p><a href='http://localhost:5173/redefinir-senha?token=" + token + "'>Clique aqui para redefinir sua senha</a></p>";
    
        helper.setTo(emailDestino);
        helper.setSubject("Bem-vindo ao Sistema - Redefinição de Senha");
        helper.setText(mensagemHtml, true);
        helper.setFrom("adm06887@gmail.com");
    
        mailSender.send(mensagem);
    }
    
    // Método para redefinir senha usando o token
    @PostMapping("/redefinir-senha")
    public ResponseEntity<String> redefinirSenha(
            @RequestParam String token,
            @RequestBody String novaSenha) {
        Optional<Adm> admOpt = admRepository.findByTokenRedefinicao(token);
    
        if (admOpt.isEmpty()) {
            return ResponseEntity.status(400).body("Token inválido.");
        }
    
        Adm adm = admOpt.get();
        if (adm.getIsSenhaRedefinida()) {
            return ResponseEntity.status(400).body("A senha já foi redefinida anteriormente.");
        }
    
        adm.setSenha(passwordEncoder.encode(novaSenha));
        adm.setIsSenhaRedefinida(true);
        adm.setTokenRedefinicao(null); // Invalida o token
        admRepository.save(adm);
    
        return ResponseEntity.ok("Senha redefinida com sucesso!");
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
