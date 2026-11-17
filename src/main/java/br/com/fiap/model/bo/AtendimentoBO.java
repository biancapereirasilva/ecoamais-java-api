package br.com.fiap.model.bo;

import br.com.fiap.exception.RecursoNaoEncontradoException;
import br.com.fiap.exception.RegraDeNegocioException;
import br.com.fiap.model.Atendimento;
import br.com.fiap.model.dao.AtendimentoDAO;
import br.com.fiap.model.dao.CidadaoDAO;
import br.com.fiap.model.dao.ServicoDAO;
import br.com.fiap.model.dao.UnidadeDAO;

import java.util.ArrayList;

public class AtendimentoBO {

    private AtendimentoDAO atendimentoDAO;
    private CidadaoDAO cidadaoDAO;
    private ServicoDAO servicoDAO;
    private UnidadeDAO unidadeDAO;

    public ArrayList<Atendimento> findAll() {
        atendimentoDAO = new AtendimentoDAO();
        return atendimentoDAO.findAll();
    }

    public Atendimento findById(Long id) {
        atendimentoDAO = new AtendimentoDAO();
        Atendimento atendimento = atendimentoDAO.findById(id);
        if (atendimento == null) {
            throw new RecursoNaoEncontradoException("Atendimento não encontrado.");
        }
        return atendimento;
    }

    public ArrayList<Atendimento> findByCidadao(Long idCidadao) {
        atendimentoDAO = new AtendimentoDAO();
        validarCidadao(idCidadao);
        return atendimentoDAO.findByCidadao(idCidadao);
    }

    public Atendimento save(Long idCidadao, Atendimento atendimento) {
        atendimentoDAO = new AtendimentoDAO();
        validarCidadao(idCidadao);
        validarServicoEUnidade(atendimento.getIdServico(), atendimento.getIdUnidade());
        // Regra de negócio: um atendimento por horário em cada unidade
        if (atendimentoDAO.existsByUnidadeAndDataHora(atendimento.getIdUnidade(), atendimento.getDataHora())) {
            throw new RegraDeNegocioException("Esse horário já está ocupado nesta unidade. Escolha outro.");
        }
        atendimento.setIdCidadao(idCidadao);
        atendimento.setStatus("AGENDADO");
        return atendimentoDAO.save(atendimento);
    }

    public Atendimento update(Long id, Atendimento atendimento) {
        atendimentoDAO = new AtendimentoDAO();
        findById(id);
        validarCidadao(atendimento.getIdCidadao());
        validarServicoEUnidade(atendimento.getIdServico(), atendimento.getIdUnidade());
        // Atendimento cancelado libera o horário, então só confere conflito se não for cancelamento
        if (!"CANCELADO".equals(atendimento.getStatus())
                && atendimentoDAO.existsByUnidadeAndDataHoraExceto(atendimento.getIdUnidade(), atendimento.getDataHora(), id)) {
            throw new RegraDeNegocioException("Esse horário já está ocupado nesta unidade. Escolha outro.");
        }
        atendimento.setId(id);
        atendimentoDAO.update(atendimento);
        return atendimento;
    }

    public void delete(Long id) {
        atendimentoDAO = new AtendimentoDAO();
        findById(id);
        atendimentoDAO.delete(id);
    }

    private void validarCidadao(Long idCidadao) {
        cidadaoDAO = new CidadaoDAO();
        if (cidadaoDAO.findById(idCidadao) == null) {
            throw new RecursoNaoEncontradoException("Cidadão não encontrado.");
        }
    }

    private void validarServicoEUnidade(Long idServico, Long idUnidade) {
        servicoDAO = new ServicoDAO();
        unidadeDAO = new UnidadeDAO();
        if (servicoDAO.findById(idServico) == null) {
            throw new RecursoNaoEncontradoException("Serviço não encontrado.");
        }
        if (unidadeDAO.findById(idUnidade) == null) {
            throw new RecursoNaoEncontradoException("Unidade não encontrada.");
        }
        // Regra de negócio: a unidade precisa oferecer o serviço escolhido
        if (!unidadeDAO.existsServicoNaUnidade(idUnidade, idServico)) {
            throw new RegraDeNegocioException("Essa unidade não oferece o serviço escolhido.");
        }
    }
}
