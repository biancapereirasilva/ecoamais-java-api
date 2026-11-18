package br.com.fiap.controller;

import br.com.fiap.model.Servico;
import br.com.fiap.model.bo.ServicoBO;
import br.com.fiap.model.dto.ServicoRequest;
import br.com.fiap.model.dto.ServicoResponse;
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
@RequestMapping("/servicos")
public class ServicoController {
    private ServicoBO servicoBO = new ServicoBO();
    private ModelMapper modelMapper = new ModelMapper();

    public ServicoController() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @GetMapping
    public ResponseEntity<ArrayList<ServicoResponse>> findAll() {
        ArrayList<ServicoResponse> servicos = new ArrayList<>();
        for (Servico servico : servicoBO.findAll()) {
            servicos.add(modelMapper.map(servico, ServicoResponse.class));
        }
        return ResponseEntity.status(HttpStatus.OK).body(servicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponse> findById(@PathVariable Long id) {
        Servico servico = servicoBO.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(servico, ServicoResponse.class));
    }

    @PostMapping
    public ResponseEntity<ServicoResponse> save(@RequestBody @Valid ServicoRequest request) {
        Servico servico = servicoBO.save(modelMapper.map(request, Servico.class));
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(servico.getId()).toUri();
        return ResponseEntity.created(uri).body(modelMapper.map(servico, ServicoResponse.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponse> update(@PathVariable Long id,
                                                  @RequestBody @Valid ServicoRequest request) {
        Servico servico = servicoBO.update(id, modelMapper.map(request, Servico.class));
        return ResponseEntity.status(HttpStatus.OK).body(modelMapper.map(servico, ServicoResponse.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servicoBO.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
