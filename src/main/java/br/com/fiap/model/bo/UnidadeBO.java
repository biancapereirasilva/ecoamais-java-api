package br.com.fiap.model.bo;

import br.com.fiap.exception.RecursoNaoEncontradoException;
import br.com.fiap.exception.RegraDeNegocioException;
import br.com.fiap.model.Unidade;
import br.com.fiap.model.dao.AtendimentoDAO;
import br.com.fiap.model.dao.UnidadeDAO;

import java.util.ArrayList;

public class UnidadeBO {

    private UnidadeDAO unidadeDAO;
    private AtendimentoDAO atendimentoDAO;

    // servicoId opcional: com ele, devolve só as unidades que oferecem o serviço
    public ArrayList<Unidade> findAll(Long servicoId) {
        unidadeDAO = new UnidadeDAO();
        if (servicoId != null) {
            return unidadeDAO.findByServico(servicoId);
        }
        return unidadeDAO.findAll();
    }

    public Unidade findById(Long id) {
        unidadeDAO = new UnidadeDAO();
        Unidade unidade = unidadeDAO.findById(id);
        if (unidade == null) {
            throw new RecursoNaoEncontradoException("Unidade não encontrada.");
        }
        return unidade;
    }

    public Unidade save(Unidade unidade) {
        unidadeDAO = new UnidadeDAO();
        return unidadeDAO.save(unidade);
    }

    public Unidade update(Long id, Unidade unidade) {
        unidadeDAO = new UnidadeDAO();
        findById(id);
        unidade.setId(id);
        unidadeDAO.update(unidade);
        return unidade;
    }

    public void delete(Long id) {
        unidadeDAO = new UnidadeDAO();
        atendimentoDAO = new AtendimentoDAO();
        findById(id);
        validarExclusao(id);
        unidadeDAO.delete(id);
    }

    // Regra de negócio: unidade com atendimentos não pode ser excluída
    private void validarExclusao(Long id) {
        if (atendimentoDAO.existsByUnidade(id)) {
            throw new RegraDeNegocioException("Não é possível excluir: existem atendimentos nesta unidade.");
        }
    }
}
