package br.com.fiap.model.dao;

import br.com.fiap.exception.ErroDeAcessoADadosException;
import br.com.fiap.model.Servico;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServicoDAO {

    public ArrayList<Servico> findAll() {
        ArrayList<Servico> servicos = new ArrayList<>();
        String sql = "select * from tb_servico order by cd_servico";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Servico servico = new Servico();
                servico.setId(rs.getLong("cd_servico"));
                servico.setNome(rs.getString("nm_servico"));
                servico.setDescricao(rs.getString("ds_servico"));
                servico.setCategoria(rs.getString("ds_categoria"));
                servico.setPictograma(rs.getString("ds_pictograma"));
                servico.setDuracaoMin(rs.getInt("qt_duracao_min"));
                servico.setDocumentos(separarDocumentos(rs.getString("ds_documentos")));
                servicos.add(servico);
            }
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao consultar serviços", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
        return servicos;
    }

    public Servico findById(Long id) {
        String sql = "select * from tb_servico where cd_servico = ?";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Servico servico = new Servico();
                servico.setId(rs.getLong("cd_servico"));
                servico.setNome(rs.getString("nm_servico"));
                servico.setDescricao(rs.getString("ds_servico"));
                servico.setCategoria(rs.getString("ds_categoria"));
                servico.setPictograma(rs.getString("ds_pictograma"));
                servico.setDuracaoMin(rs.getInt("qt_duracao_min"));
                servico.setDocumentos(separarDocumentos(rs.getString("ds_documentos")));
                return servico;
            }
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao consultar serviço", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
        return null;
    }

    public ArrayList<Servico> findByUnidade(Long idUnidade) {
        ArrayList<Servico> servicos = new ArrayList<>();
        String sql = "select s.* from tb_servico s inner join tb_servico_unidade su "
                + "on su.cd_servico = s.cd_servico where su.cd_unidade = ? order by s.cd_servico";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, idUnidade);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Servico servico = new Servico();
                servico.setId(rs.getLong("cd_servico"));
                servico.setNome(rs.getString("nm_servico"));
                servico.setDescricao(rs.getString("ds_servico"));
                servico.setCategoria(rs.getString("ds_categoria"));
                servico.setPictograma(rs.getString("ds_pictograma"));
                servico.setDuracaoMin(rs.getInt("qt_duracao_min"));
                servico.setDocumentos(separarDocumentos(rs.getString("ds_documentos")));
                servicos.add(servico);
            }
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao consultar serviços da unidade", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
        return servicos;
    }

    public Servico save(Servico servico) {
        String sql = "insert into tb_servico (cd_servico, nm_servico, ds_servico, ds_categoria, ds_pictograma, qt_duracao_min, ds_documentos) "
                + "values (sq_tb_servico.nextval, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql, new String[]{"CD_SERVICO"})) {
            ps.setString(1, servico.getNome());
            ps.setString(2, servico.getDescricao());
            ps.setString(3, servico.getCategoria());
            ps.setString(4, servico.getPictograma());
            ps.setInt(5, servico.getDuracaoMin());
            ps.setString(6, juntarDocumentos(servico.getDocumentos()));
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                servico.setId(keys.getLong(1));
            }
            return servico;
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao salvar serviço", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    public boolean update(Servico servico) {
        String sql = "update tb_servico set nm_servico = ?, ds_servico = ?, ds_categoria = ?, ds_pictograma = ?, "
                + "qt_duracao_min = ?, ds_documentos = ? where cd_servico = ?";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setString(1, servico.getNome());
            ps.setString(2, servico.getDescricao());
            ps.setString(3, servico.getCategoria());
            ps.setString(4, servico.getPictograma());
            ps.setInt(5, servico.getDuracaoMin());
            ps.setString(6, juntarDocumentos(servico.getDocumentos()));
            ps.setLong(7, servico.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao atualizar serviço", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    public boolean delete(Long id) {
        String sql = "delete from tb_servico where cd_servico = ?";
        try (PreparedStatement ps = ConnectionFactory.getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ErroDeAcessoADadosException("Erro ao excluir serviço", e);
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    // Os documentos ficam numa coluna de texto separados por ";" e viram lista na resposta
    private String juntarDocumentos(ArrayList<String> documentos) {
        if (documentos == null || documentos.isEmpty()) {
            return null;
        }
        return String.join(";", documentos);
    }

    private ArrayList<String> separarDocumentos(String texto) {
        ArrayList<String> documentos = new ArrayList<>();
        if (texto != null && !texto.isBlank()) {
            for (String item : texto.split(";")) {
                if (!item.isBlank()) {
                    documentos.add(item.trim());
                }
            }
        }
        return documentos;
    }
}
