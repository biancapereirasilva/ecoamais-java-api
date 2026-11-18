package br.com.fiap.controller;

import br.com.fiap.model.Cidadao;
import br.com.fiap.model.bo.CidadaoBO;
import br.com.fiap.model.dto.CidadaoRequest;
import br.com.fiap.model.dto.CidadaoResponse;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;

@RestController
@RequestMapping("/cidadaos")
public class CidadaoController {
    private CidadaoBO cidadaoBO = new CidadaoBO();
    private ModelMapper modelMapper = new ModelMapper();

    public CidadaoController() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @GetMapping
    public ResponseEntity<ArrayList<CidadaoResponse>> findAll() {
        ArrayList<CidadaoResponse> cidadaos = new ArrayList<>();
        for (Cidadao cidadao : cidadaoBO.findAll()) {
            cidadaos.add(modelMapper.map(cidadao, CidadaoResponse.class));
        }
        return ResponseEntity.status(HttpStatus.OK).body(cidadaos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CidadaoResponse> findById(@PathVariable Long id) {
        Cidadao cidadao = cidadaoBO.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(cidadao, CidadaoResponse.class));
    }

    @PostMapping
    public ResponseEntity<CidadaoResponse> save(@RequestBody @Valid CidadaoRequest request) {
        Cidadao cidadao = cidadaoBO.save(modelMapper.map(request, Cidadao.class));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(cidadao.getId()).toUri();
        return ResponseEntity.created(uri).body(modelMapper.map(cidadao, CidadaoResponse.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CidadaoResponse> update(@PathVariable Long id,
                                                  @RequestBody @Valid CidadaoRequest request) {
        Cidadao cidadao = cidadaoBO.update(id, modelMapper.map(request, Cidadao.class));
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(cidadao, CidadaoResponse.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cidadaoBO.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
