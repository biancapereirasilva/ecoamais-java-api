package br.com.fiap.model.bo;

import br.com.fiap.exception.RecursoNaoEncontradoException;
import br.com.fiap.exception.RegraDeNegocioException;
import br.com.fiap.model.Cidadao;
import br.com.fiap.model.dao.AtendimentoDAO;
import br.com.fiap.model.dao.CidadaoDAO;

import java.util.ArrayList;

public class CidadaoBO {

    private CidadaoDAO cidadaoDAO;
    private AtendimentoDAO atendimentoDAO;

    public ArrayList<Cidadao> findAll() {
        cidadaoDAO = new CidadaoDAO();
        return cidadaoDAO.findAll();
    }

    public Cidadao findById(Long id) {
        cidadaoDAO = new CidadaoDAO();
        Cidadao cidadao = cidadaoDAO.findById(id);
        if (cidadao == null) {
            throw new RecursoNaoEncontradoException("Cidadão não encontrado.");
        }
        return cidadao;
    }

    public Cidadao save(Cidadao cidadao) {
        cidadaoDAO = new CidadaoDAO();
        return cidadaoDAO.save(cidadao);
    }

    public Cidadao update(Long id, Cidadao cidadao) {
        cidadaoDAO = new CidadaoDAO();
        findById(id);
        cidadao.setId(id);
        cidadaoDAO.update(cidadao);
        return cidadao;
    }

    public void delete(Long id) {
        cidadaoDAO = new CidadaoDAO();
        atendimentoDAO = new AtendimentoDAO();
        findById(id);
        validarExclusao(id);
        cidadaoDAO.delete(id);
    }

    // Regra de negócio: cidadão com atendimentos não pode ser excluído
    private void validarExclusao(Long id) {
        if (atendimentoDAO.existsByCidadao(id)) {
            throw new RegraDeNegocioException("Não é possível excluir: este cidadão tem atendimentos.");
        }
    }
}
