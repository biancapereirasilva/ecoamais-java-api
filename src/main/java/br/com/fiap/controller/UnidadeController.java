package br.com.fiap.controller;

import br.com.fiap.model.Unidade;
import br.com.fiap.model.bo.UnidadeBO;
import br.com.fiap.model.dto.UnidadeRequest;
import br.com.fiap.model.dto.UnidadeResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;

@RestController
@RequestMapping("/unidades")
public class UnidadeController {
    private UnidadeBO unidadeBO = new UnidadeBO();
    private ModelMapper modelMapper = new ModelMapper();

    public UnidadeController() {
        // STRICT: só copia campos com nome idêntico
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    // GET /unidades ou GET /unidades?servicoId=1 (só as unidades que oferecem o serviço)
    @GetMapping
    public ResponseEntity<ArrayList<UnidadeResponse>> findAll(@RequestParam(required = false) Long servicoId) {
        ArrayList<UnidadeResponse> unidades = new ArrayList<>();
        for (Unidade unidade : unidadeBO.findAll(servicoId)) {
            unidades.add(modelMapper.map(unidade, UnidadeResponse.class));
        }
        return ResponseEntity.status(HttpStatus.OK).body(unidades);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnidadeResponse> findById(@PathVariable Long id) {
        Unidade unidade = unidadeBO.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(unidade, UnidadeResponse.class));
    }

    @PostMapping
    public ResponseEntity<UnidadeResponse> save(@RequestBody @Valid UnidadeRequest request) {
        Unidade unidade = unidadeBO.save(modelMapper.map(request, Unidade.class));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(unidade.getId()).toUri();
        return ResponseEntity.created(uri).body(modelMapper.map(unidade, UnidadeResponse.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnidadeResponse> update(@PathVariable Long id,
                                                  @RequestBody @Valid UnidadeRequest request) {
        Unidade unidade = unidadeBO.update(id, modelMapper.map(request, Unidade.class));
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(unidade, UnidadeResponse.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        unidadeBO.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
