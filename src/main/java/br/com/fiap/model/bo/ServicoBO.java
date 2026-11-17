package br.com.fiap.model.bo;

import br.com.fiap.exception.RecursoNaoEncontradoException;
import br.com.fiap.exception.RegraDeNegocioException;
import br.com.fiap.model.Servico;
import br.com.fiap.model.dao.AtendimentoDAO;
import br.com.fiap.model.dao.ServicoDAO;

import java.util.ArrayList;

public class ServicoBO {

    private ServicoDAO servicoDAO;
    private AtendimentoDAO atendimentoDAO;

    public ArrayList<Servico> findAll() {
        servicoDAO = new ServicoDAO();
        return servicoDAO.findAll();
    }

    public Servico findById(Long id) {
        servicoDAO = new ServicoDAO();
        Servico servico = servicoDAO.findById(id);
        if (servico == null) {
            throw new RecursoNaoEncontradoException("Serviço não encontrado.");
        }
        return servico;
    }

    public Servico save(Servico servico) {
        servicoDAO = new ServicoDAO();
        return servicoDAO.save(servico);
    }

    public Servico update(Long id, Servico servico) {
        servicoDAO = new ServicoDAO();
        findById(id);
        servico.setId(id);
        servicoDAO.update(servico);
        return servico;
    }

    public void delete(Long id) {
        servicoDAO = new ServicoDAO();
        atendimentoDAO = new AtendimentoDAO();
        findById(id);
        validarExclusao(id);
        servicoDAO.delete(id);
    }

    // Regra de negócio: serviço com atendimentos não pode ser excluído
    private void validarExclusao(Long id) {
        if (atendimentoDAO.existsByServico(id)) {
            throw new RegraDeNegocioException("Não é possível excluir: existem atendimentos para este serviço.");
        }
    }
}
