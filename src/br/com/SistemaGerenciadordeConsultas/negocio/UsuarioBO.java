/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.SistemaGerenciadordeConsultas.negocio;

import br.com.SistemaGerenciadordeConsultas.entidade.Usuario;
import br.com.SistemaGerenciadordeConsultas.excecao.CampoObrigatorioException;
import br.com.SistemaGerenciadordeConsultas.excecao.EspecialidadeCadastradaException;
import br.com.SistemaGerenciadordeConsultas.excecao.LoginInvalidoException;
import br.com.SistemaGerenciadordeConsultas.excecao.UsuarioExisteException;
import br.com.SistemaGerenciadordeConsultas.persistencia.UsuarioDAO;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Rayssa
 */
public class UsuarioBO {

    public List<Usuario> buscarTodos() throws SQLException {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        return usuarioDAO.buscarTodos();
    }

    public void salvar(Usuario usuario) throws SQLException, CampoObrigatorioException, EspecialidadeCadastradaException {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        if (usuario.getLogin().isEmpty() || usuario.getSenha().isEmpty() || usuario.getNome().isEmpty() || usuario.getGrupo_Usuario().isEmpty()) {
            throw new CampoObrigatorioException();
        }
        this.verificaUsuarioLogin(usuario);
        usuarioDAO.criar(usuario);
    }

    public void verificaUsuarioLogin(Usuario usuario) throws SQLException {
        Usuario usuarioExistente = null;
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarioExistente = usuarioDAO.buscarLoginUsuario(usuario.getLogin());
        if (usuarioExistente != null) {
            throw new UsuarioExisteException();
        }
    }

    public void editar(Usuario usuario) throws SQLException, CampoObrigatorioException {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        if (usuario.getLogin().isEmpty() || usuario.getSenha().isEmpty() || usuario.getNome().isEmpty() || usuario.getGrupo_Usuario().isEmpty()) {
            throw new CampoObrigatorioException();
        }
        this.verificaUsuarioLogin(usuario);
        usuarioDAO.alterar(usuario);
    }

    public void login(Usuario usuario) throws CampoObrigatorioException, LoginInvalidoException {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        if (usuario.getLogin().isEmpty() || usuario.getSenha().isEmpty()) {
            throw new CampoObrigatorioException();
        }
        if (!usuarioDAO.login(usuario)) {
            throw new LoginInvalidoException();
        }
    }

    public void removerUsuario(int id) throws SQLException {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarioDAO.removerUsuario(id);
    }

    public Usuario pesquisarLoginCadastrado(Usuario usuario) throws LoginInvalidoException, SQLException {
        Usuario usuarioBuscado = null;
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarioBuscado = usuarioDAO.buscarLoginUsuario(usuario.getLogin());
        if (usuarioBuscado == null) {
            throw new LoginInvalidoException();
        } else {
            return usuarioBuscado;
        }
    }

}
