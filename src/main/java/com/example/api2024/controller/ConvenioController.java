package com.example.api2024.controller;

import com.example.api2024.entity.Convenio;
import com.example.api2024.service.ConvenioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/convenio")
public class ConvenioController {

    @Autowired
    private ConvenioService convenioService;

    // Create
    @PostMapping("/criar")
    public ResponseEntity<Convenio> criarConvenio(@RequestBody Convenio convenio) {
        Convenio novoConvenio = convenioService.criarConvenio(convenio);
        return new ResponseEntity<>(novoConvenio, HttpStatus.CREATED);
    }

    // Read All
    @GetMapping("/listar")
    public ResponseEntity<List<Convenio>> listarConvenios() {
        List<Convenio> convenios = convenioService.listarConvenios();
        return new ResponseEntity<>(convenios, HttpStatus.OK);
    }

    // Update
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Convenio> atualizarConvenio(@PathVariable Long id, @RequestBody Convenio convenio) {
        Convenio convenioAtualizado = convenioService.atualizarConvenio(id, convenio);
        if (convenioAtualizado != null) {
            return new ResponseEntity<>(convenioAtualizado, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarConvenio(@PathVariable Long id) {
        boolean deletado = convenioService.deletarConvenio(id);
        if (deletado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}