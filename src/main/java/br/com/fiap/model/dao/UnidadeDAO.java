package br.com.fiap.model.dao;

import br.com.fiap.exception.ErroDeAcessoADadosException;
import br.com.fiap.model.Unidade;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UnidadeDAO {

    public ArrayList<Unidade> findAll() {
        ArrayList<Unidade> unidades = new ArrayList<>();
        String sql = "select * from tb_unidade order by cd_unidade";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Unidade unidade = new Unidade();
                unidade.setId(rs.getLong("cd_unidade"));
                unidade.setNome(rs.getString("nm_unidade"));
                unidade.setTipo(rs.getString("ds_tipo"));
                unidade.setEndereco(rs.getString("ds_endereco"));
                unidade.setBairro(rs.getString("nm_bairro"));
                unidades.add(unidade);
            }
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao consultar unidades", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
        return unidades;
    }

    public Unidade findById(Long id) {
        String sql = "select * from tb_unidade where cd_unidade = ?";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Unidade unidade = new Unidade();
                unidade.setId(rs.getLong("cd_unidade"));
                unidade.setNome(rs.getString("nm_unidade"));
                unidade.setTipo(rs.getString("ds_tipo"));
                unidade.setEndereco(rs.getString("ds_endereco"));
                unidade.setBairro(rs.getString("nm_bairro"));
                return unidade;
            }
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao consultar unidade", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
        return null;
    }

    public ArrayList<Unidade> findByServico(Long idServico) {
        ArrayList<Unidade> unidades = new ArrayList<>();
        String sql = "select u.* from tb_unidade u inner join tb_servico_unidade su "
                + "on su.cd_unidade = u.cd_unidade where su.cd_servico = ? order by u.cd_unidade";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, idServico);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Unidade unidade = new Unidade();
                unidade.setId(rs.getLong("cd_unidade"));
                unidade.setNome(rs.getString("nm_unidade"));
                unidade.setTipo(rs.getString("ds_tipo"));
                unidade.setEndereco(rs.getString("ds_endereco"));
                unidade.setBairro(rs.getString("nm_bairro"));
                unidades.add(unidade);
            }
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao consultar unidades do serviço", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
        return unidades;
    }

    // Regra de negócio: a unidade precisa oferecer o serviço escolhido no atendimento
    public boolean existsServicoNaUnidade(Long idUnidade, Long idServico) {
        String sql = "select 1 from tb_servico_unidade where cd_unidade = ? and cd_servico = ?";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, idUnidade);
            ps.setLong(2, idServico);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao verificar serviço da unidade", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    public boolean vincularServico(Long idUnidade, Long idServico) {
        String sql = "insert into tb_servico_unidade (cd_servico, cd_unidade) values (?, ?)";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, idServico);
            ps.setLong(2, idUnidade);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao vincular serviço à unidade", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    public boolean desvincularServico(Long idUnidade, Long idServico) {
        String sql = "delete from tb_servico_unidade where cd_servico = ? and cd_unidade = ?";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, idServico);
            ps.setLong(2, idUnidade);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao desvincular serviço da unidade", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    public Unidade save(Unidade unidade) {
        String sql = "insert into tb_unidade (cd_unidade, nm_unidade, ds_tipo, ds_endereco, nm_bairro) "
                + "values (sq_tb_unidade.nextval, ?, ?, ?, ?)";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql, new String[]{"CD_UNIDADE"})) {
            ps.setString(1, unidade.getNome());
            ps.setString(2, unidade.getTipo());
            ps.setString(3, unidade.getEndereco());
            ps.setString(4, unidade.getBairro());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                unidade.setId(keys.getLong(1));
            }
            return unidade;
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao salvar unidade", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    public boolean update(Unidade unidade) {
        String sql = "update tb_unidade set nm_unidade = ?, ds_tipo = ?, ds_endereco = ?, nm_bairro = ? where cd_unidade = ?";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setString(1, unidade.getNome());
            ps.setString(2, unidade.getTipo());
            ps.setString(3, unidade.getEndereco());
            ps.setString(4, unidade.getBairro());
            ps.setLong(5, unidade.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao atualizar unidade", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    public boolean delete(Long id) {
        String sql = "delete from tb_unidade where cd_unidade = ?";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao excluir unidade", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
    }
}
