/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.SistemaGerenciadordeConsultas.persistencia;

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
public class UsuarioDAO {

    private static final String SQL_INSERT = "INSERT INTO USUARIO(NOME,LOGIN,SENHA,TIPO_USUARIO) VALUES(?,?,?,?)";
    private static final String SQL_UPDATE = "UPDATE USUARIO SET NOME = ?, LOGIN =?, SENHA = ?, TIPO_USUARIO =? WHERE ID =?";
    private static final String SQL_LOGIN = "SELECT * FROM USUARIO WHERE LOGIN=? AND SENHA=?";
    private static final String SQL_BUSCA_TODOS = "SELECT * FROM USUARIO";
    private static final String SQL_REMOVER_USUARIO = "DELETE FROM USUARIO WHERE ID = ?";
    private static final String SQL_BUSCAR_LOGIN = "SELECT NOME, LOGIN, SENHA, TIPO_USUARIO FROM USUARIO WHERE LOGIN = ?";

    public Usuario buscarLoginUsuario(String login) throws SQLException {
        Usuario usuario = null;
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_BUSCAR_LOGIN);
            comando.setString(1, login);
            resultado = comando.executeQuery();

            while (resultado.next()) {
                usuario = new Usuario();

                usuario.setNome(resultado.getString(1));
                usuario.setLogin(resultado.getString(2));
                usuario.setSenha(resultado.getString(3));
                usuario.setGrupo_Usuario(resultado.getString(4));

            }

        } catch (Exception e) {
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
        return usuario;
    }

    public void criar(Usuario usuario) throws SQLException {
        Connection conexao = null;
        PreparedStatement comando = null;
        try {

            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_INSERT);
            comando.setString(1, usuario.getNome());
            comando.setString(2, usuario.getLogin());
            comando.setString(3, usuario.getSenha());
            comando.setString(4, usuario.getGrupo_Usuario());

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

    public void alterar(Usuario usuario) throws SQLException {
        Connection conexao = null;
        PreparedStatement comando = null;

        try {

            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_UPDATE);

            comando.setString(1, usuario.getNome());
            comando.setString(2, usuario.getLogin());
            comando.setString(3, usuario.getSenha());
            comando.setString(4, usuario.getGrupo_Usuario());
            comando.setInt(5, usuario.getId());

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

    public boolean login(Usuario usuario) {
        PreparedStatement comando = null;
        Connection conexao = null;
        ResultSet resultado = null;
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_LOGIN);

            comando.setString(1, usuario.getLogin());
            comando.setString(2, usuario.getSenha());
            resultado = comando.executeQuery();
            if (resultado.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public List<Usuario> buscarTodos() throws SQLException {
        List<Usuario> listaUsuarios = new ArrayList<>();
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_BUSCA_TODOS);
            resultado = comando.executeQuery();

            while (resultado.next()) {
                Usuario usuario = new Usuario();

                usuario.setId(resultado.getInt(1));
                usuario.setNome(resultado.getString(2));
                usuario.setLogin(resultado.getString(3));
                usuario.setSenha(resultado.getString(4));
                usuario.setGrupo_Usuario(resultado.getString(5));

                listaUsuarios.add(usuario);
            }

        } finally {

            BancoDadosUtil.fecharChamadasBancoDados(conexao, comando);
        }

        return listaUsuarios;
    }

    public void removerUsuario(int id) throws SQLException {
        Connection conexao = null;
        PreparedStatement comando = null;
        ///id = 4;
        try {
            //Recupera a conexão
            conexao = BancoDadosUtil.getConnection();
            //Cria o comando de inserir dados
            comando = conexao.prepareStatement(SQL_REMOVER_USUARIO);
            //Atribui os parâmetros (Note que no BD o index inicia por 1)
            comando.setInt(1, id);
            //Executa o comando
            comando.execute();
            //Persiste o comando no banco de dados
            conexao.commit();
        } catch (Exception e) {
            //Caso aconteça alguma exeção é feito um rollback para o banco de
            //dados retornar ao seu estado anterior.
            if (conexao != null) {
                conexao.rollback();
            }
            throw e;
        } finally {
            //Todo objeto que referencie o banco de dados deve ser fechado
            BancoDadosUtil.fecharChamadasBancoDados(conexao, comando);
        }

    }
}
