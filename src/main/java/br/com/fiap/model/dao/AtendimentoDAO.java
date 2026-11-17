package br.com.fiap.model.dao;

import br.com.fiap.exception.ErroDeAcessoADadosException;
import br.com.fiap.model.Atendimento;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AtendimentoDAO {

    public ArrayList<Atendimento> findAll() {
        ArrayList<Atendimento> atendimentos = new ArrayList<>();
        String sql = "select * from tb_atendimento order by cd_atendimento";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Atendimento atendimento = new Atendimento();
                atendimento.setId(rs.getLong("cd_atendimento"));
                atendimento.setIdCidadao(rs.getLong("cd_cidadao"));
                atendimento.setIdServico(rs.getLong("cd_servico"));
                atendimento.setIdUnidade(rs.getLong("cd_unidade"));
                atendimento.setDataHora(rs.getTimestamp("dt_hora").toLocalDateTime());
                atendimento.setStatus(rs.getString("ds_status"));
                atendimentos.add(atendimento);
            }
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao consultar atendimentos", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
        return atendimentos;
    }

    public Atendimento findById(Long id) {
        String sql = "select * from tb_atendimento where cd_atendimento = ?";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Atendimento atendimento = new Atendimento();
                atendimento.setId(rs.getLong("cd_atendimento"));
                atendimento.setIdCidadao(rs.getLong("cd_cidadao"));
                atendimento.setIdServico(rs.getLong("cd_servico"));
                atendimento.setIdUnidade(rs.getLong("cd_unidade"));
                atendimento.setDataHora(rs.getTimestamp("dt_hora").toLocalDateTime());
                atendimento.setStatus(rs.getString("ds_status"));
                return atendimento;
            }
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao consultar atendimento", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
        return null;
    }

    public ArrayList<Atendimento> findByCidadao(Long idCidadao) {
        ArrayList<Atendimento> atendimentos = new ArrayList<>();
        String sql = "select * from tb_atendimento where cd_cidadao = ? order by dt_hora";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, idCidadao);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Atendimento atendimento = new Atendimento();
                atendimento.setId(rs.getLong("cd_atendimento"));
                atendimento.setIdCidadao(rs.getLong("cd_cidadao"));
                atendimento.setIdServico(rs.getLong("cd_servico"));
                atendimento.setIdUnidade(rs.getLong("cd_unidade"));
                atendimento.setDataHora(rs.getTimestamp("dt_hora").toLocalDateTime());
                atendimento.setStatus(rs.getString("ds_status"));
                atendimentos.add(atendimento);
            }
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao consultar atendimentos do cidadão", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
        return atendimentos;
    }

    // Regra de negócio: só um atendimento por horário em cada unidade (cancelado libera o horário)
    public boolean existsByUnidadeAndDataHora(Long idUnidade, LocalDateTime dataHora) {
        return existsByUnidadeAndDataHoraExceto(idUnidade, dataHora, null);
    }

    // Igual ao anterior, mas ignora um atendimento (usado no PUT para não conflitar consigo mesmo)
    public boolean existsByUnidadeAndDataHoraExceto(Long idUnidade, LocalDateTime dataHora, Long idIgnorado) {
        String sql = "select 1 from tb_atendimento where cd_unidade = ? and dt_hora = ? "
                + "and ds_status <> 'CANCELADO' and cd_atendimento <> ? and rownum = 1";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, idUnidade);
            ps.setTimestamp(2, Timestamp.valueOf(dataHora));
            ps.setLong(3, idIgnorado == null ? -1L : idIgnorado);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao verificar horário da unidade", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    // Regra de negócio: cidadão com atendimentos não pode ser excluído
    public boolean existsByCidadao(Long idCidadao) {
        String sql = "select 1 from tb_atendimento where cd_cidadao = ? and rownum = 1";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, idCidadao);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao verificar atendimentos do cidadão", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    // Regra de negócio: serviço com atendimentos não pode ser excluído
    public boolean existsByServico(Long idServico) {
        String sql = "select 1 from tb_atendimento where cd_servico = ? and rownum = 1";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, idServico);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao verificar atendimentos do serviço", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    // Regra de negócio: unidade com atendimentos não pode ser excluída
    public boolean existsByUnidade(Long idUnidade) {
        String sql = "select 1 from tb_atendimento where cd_unidade = ? and rownum = 1";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, idUnidade);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao verificar atendimentos da unidade", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    public Atendimento save(Atendimento atendimento) {
        String sql = "insert into tb_atendimento (cd_atendimento, cd_cidadao, cd_servico, cd_unidade, dt_hora, ds_status) "
                + "values (sq_tb_atendimento.nextval, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql, new String[]{"CD_ATENDIMENTO"})) {
            ps.setLong(1, atendimento.getIdCidadao());
            ps.setLong(2, atendimento.getIdServico());
            ps.setLong(3, atendimento.getIdUnidade());
            ps.setTimestamp(4, Timestamp.valueOf(atendimento.getDataHora()));
            ps.setString(5, atendimento.getStatus());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                atendimento.setId(keys.getLong(1));
            }
            return atendimento;
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao salvar atendimento", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    public boolean update(Atendimento atendimento) {
        String sql = "update tb_atendimento set cd_cidadao = ?, cd_servico = ?, cd_unidade = ?, dt_hora = ?, ds_status = ? "
                + "where cd_atendimento = ?";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, atendimento.getIdCidadao());
            ps.setLong(2, atendimento.getIdServico());
            ps.setLong(3, atendimento.getIdUnidade());
            ps.setTimestamp(4, Timestamp.valueOf(atendimento.getDataHora()));
            ps.setString(5, atendimento.getStatus());
            ps.setLong(6, atendimento.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao atualizar atendimento", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    public boolean delete(Long id) {
        String sql = "delete from tb_atendimento where cd_atendimento = ?";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao excluir atendimento", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
    }
}
