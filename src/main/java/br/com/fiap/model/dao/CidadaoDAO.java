package br.com.fiap.model.dao;

import br.com.fiap.exception.ErroDeAcessoADadosException;
import br.com.fiap.model.Cidadao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CidadaoDAO {

    public ArrayList<Cidadao> findAll() {
        ArrayList<Cidadao> cidadaos = new ArrayList<>();
        String sql = "select * from tb_cidadao order by cd_cidadao";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Cidadao cidadao = new Cidadao();
                cidadao.setId(rs.getLong("cd_cidadao"));
                cidadao.setNome(rs.getString("nm_cidadao"));
                cidadao.setTelefone(rs.getString("nr_telefone"));
                cidadaos.add(cidadao);
            }
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao consultar cidadãos", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
        return cidadaos;
    }

    public Cidadao findById(Long id) {
        String sql = "select * from tb_cidadao where cd_cidadao = ?";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Cidadao cidadao = new Cidadao();
                cidadao.setId(rs.getLong("cd_cidadao"));
                cidadao.setNome(rs.getString("nm_cidadao"));
                cidadao.setTelefone(rs.getString("nr_telefone"));
                return cidadao;
            }
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao consultar cidadão", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
        return null;
    }

    public Cidadao save(Cidadao cidadao) {
        String sql = "insert into tb_cidadao (cd_cidadao, nm_cidadao, nr_telefone) "
                + "values (sq_tb_cidadao.nextval, ?, ?)";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql, new String[]{"CD_CIDADAO"})) {
            ps.setString(1, cidadao.getNome());
            ps.setString(2, cidadao.getTelefone());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                cidadao.setId(keys.getLong(1));
            }
            return cidadao;
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao salvar cidadão", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    public boolean update(Cidadao cidadao) {
        String sql = "update tb_cidadao set nm_cidadao = ?, nr_telefone = ? where cd_cidadao = ?";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setString(1, cidadao.getNome());
            ps.setString(2, cidadao.getTelefone());
            ps.setLong(3, cidadao.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao atualizar cidadão", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    public boolean delete(Long id) {
        String sql = "delete from tb_cidadao where cd_cidadao = ?";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao excluir cidadão", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
    }
}