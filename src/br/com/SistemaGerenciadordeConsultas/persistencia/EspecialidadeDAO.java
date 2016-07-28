/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.SistemaGerenciadordeConsultas.persistencia;

import br.com.SistemaGerenciadordeConsultas.entidade.Especialidade;
import br.com.SistemaGerenciadordeConsultas.entidade.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rayssa
 */
public class EspecialidadeDAO {

    private static final String SQL_INSERT = "INSERT INTO ESPECIALIDADE(NOME) VALUES(?)";
    private static final String SQL_UPDATE = "UPDATE ESPECIALIDADE SET NOME=? WHERE ID=?";
    private static final String SQL_BUSCA_TODOS = "SELECT * FROM ESPECIALIDADE ORDER BY NOME";
    private static final String SQL_VALIDAR = "SELECT * FROM ESPECIALIDADE WHERE NOME=?";
    private static final String SQL_REMOVER_ESPECIALIDADE = "DELETE FROM ESPECIALIDADE WHERE ID = ?";

    public void criar(Especialidade especialidade) throws SQLException {
        PreparedStatement comando = null;
        Connection conexao = null;
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_INSERT);
            comando.setString(1, especialidade.getNome());

            comando.execute();
            conexao.commit();
        } catch (Exception e) {

            if (conexao != null) {
                conexao.rollback();
            }
            throw e;
        } finally {

            BancoDadosUtil.fecharChamadasBancoDados(conexao, comando);
        }
    }

    public boolean validar(Especialidade especialidade) {
        PreparedStatement comando = null;
        Connection conexao = null;
        ResultSet resultado = null;
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_VALIDAR);
            comando.setString(1, especialidade.getNome());

            resultado = comando.executeQuery();
            if (resultado.next()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void alterar(Especialidade especialidade) throws SQLException {
        PreparedStatement comando = null;
        Connection conexao = null;
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_UPDATE);
            comando.setString(1, especialidade.getNome());
            comando.setInt(2, especialidade.getId());

            comando.execute();
            conexao.commit();
        } catch (SQLException e) {
            if (conexao != null) {
                conexao.rollback();
            }
            throw new RuntimeException();
        } finally {
            if (comando != null && !comando.isClosed()) {
                comando.close();
            }
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        }
    }

    public List<Especialidade> buscarTodos() throws SQLException {
        List<Especialidade> especialidades = new ArrayList<>();
        Especialidade especialidade = null;
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_BUSCA_TODOS);
            resultado = comando.executeQuery();
            while (resultado.next()) {
                especialidade = new Especialidade();

                especialidade.setId(resultado.getInt(1));
                especialidade.setNome(resultado.getString(2));

                especialidades.add(especialidade);
            }

        } catch (Exception e) {
            if (conexao != null) {
                conexao.rollback();
            }
//            throw new RuntimeException();
        } finally {
            if (comando != null && !comando.isClosed()) {
                comando.close();
            }
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        }
        return especialidades;
    }

    public void removerEspecialidade(int id) throws Exception {
        Connection conexao = null;
        PreparedStatement comando = null;
        try {

            conexao = BancoDadosUtil.getConnection();

            comando = conexao.prepareStatement(SQL_REMOVER_ESPECIALIDADE);

            comando.setInt(1, id);

            comando.execute();
            conexao.commit();
        } catch (Exception e) {
            if (conexao != null) {
                conexao.rollback();
            }
            throw e;
        } finally {

            BancoDadosUtil.fecharChamadasBancoDados(conexao, comando);
        }
    }
}
