package br.com.fiap.controller;

import br.com.fiap.model.Atendimento;
import br.com.fiap.model.bo.AtendimentoBO;
import br.com.fiap.model.dto.AtendimentoAtualizacaoRequest;
import br.com.fiap.model.dto.AtendimentoRequest;
import br.com.fiap.model.dto.AtendimentoResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;

@RestController
public class AtendimentoController {
    private AtendimentoBO atendimentoBO = new AtendimentoBO();
    private ModelMapper modelMapper = new ModelMapper();

    public AtendimentoController() {
        // STRICT: evita confundir id com idServico, idUnidade e idCidadao
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @GetMapping("/atendimentos")
    public ResponseEntity<ArrayList<AtendimentoResponse>> findAll() {
        ArrayList<AtendimentoResponse> atendimentos = new ArrayList<>();
        for (Atendimento atendimento : atendimentoBO.findAll()) {
            atendimentos.add(modelMapper.map(atendimento, AtendimentoResponse.class));
        }
        return ResponseEntity.status(HttpStatus.OK).body(atendimentos);
    }

    @GetMapping("/atendimentos/{id}")
    public ResponseEntity<AtendimentoResponse> findById(@PathVariable Long id) {
        Atendimento atendimento = atendimentoBO.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(atendimento, AtendimentoResponse.class));
    }

    @GetMapping("/cidadaos/{idCidadao}/atendimentos")
    public ResponseEntity<ArrayList<AtendimentoResponse>> findByCidadao(@PathVariable Long idCidadao) {
        ArrayList<AtendimentoResponse> atendimentos = new ArrayList<>();
        for (Atendimento atendimento : atendimentoBO.findByCidadao(idCidadao)) {
            atendimentos.add(modelMapper.map(atendimento, AtendimentoResponse.class));
        }
        return ResponseEntity.status(HttpStatus.OK).body(atendimentos);
    }

    @PostMapping("/cidadaos/{idCidadao}/atendimentos")
    public ResponseEntity<AtendimentoResponse> save(@PathVariable Long idCidadao,
                                                    @RequestBody @Valid AtendimentoRequest request) {
        Atendimento atendimento = atendimentoBO.save(idCidadao, modelMapper.map(request, Atendimento.class));
        URI uri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/atendimentos/{id}").buildAndExpand(atendimento.getId()).toUri();
        return ResponseEntity.created(uri).body(modelMapper.map(atendimento, AtendimentoResponse.class));
    }

    @PutMapping("/atendimentos/{id}")
    public ResponseEntity<AtendimentoResponse> update(@PathVariable Long id,
                                                      @RequestBody @Valid AtendimentoAtualizacaoRequest request) {
        Atendimento atendimento = atendimentoBO.update(id, modelMapper.map(request, Atendimento.class));
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(atendimento, AtendimentoResponse.class));
    }

    @DeleteMapping("/atendimentos/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        atendimentoBO.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
