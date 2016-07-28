/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.SistemaGerenciadordeConsultas.persistencia;

import br.com.SistemaGerenciadordeConsultas.entidade.Paciente;
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
public class PacienteDAO {

    private static final String SQL_INSERT = "INSERT INTO PACIENTE(NOME,CPF,TELEFONE,ENDERECO,DATA_NASCIMENTO,SEXO) VALUES(?,?,?,?,?,?)";
    private static final String SQL_UPDATE = "UPDATE PACIENTE SET NOME=?, CPF=?, TELEFONE=?, ENDERECO=?, DATA_NASCIMENTO=?, SEXO=? WHERE ID=?";
    private static final String SQL_BUSCA_TODOS = "SELECT * FROM PACIENTE ORDER BY NOME";
    private static final String SQL_BUSCA_TODOS_CPF = "SELECT * FROM PACIENTE WHERE CPF=? ORDER BY NOME";
    private static final String SQL_BUSCAR_CPF_EXISTE = "SELECT CPF FROM PACIENTE WHERE CPF = ?";
    private static final String SQL_BUSCA_TODOS_NOME = "SELECT *FROM PACIENTE WHERE UPPER(nome) LIKE UPPER(?) ";
    private static final String SQL_REMOVER_PACIENTE = "DELETE FROM PACIENTE WHERE ID = ?";

    public void criar(Paciente paciente) throws SQLException {
        PreparedStatement comando = null;
        Connection conexao = null;
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_INSERT);
            comando.setString(1, paciente.getNome());
            comando.setString(2, paciente.getCpf());
            comando.setString(3, paciente.getTelefone());
            comando.setString(4, paciente.getEndereco());
            comando.setDate(5, paciente.getData_nascimento());
            comando.setString(6, paciente.getSexo());

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

    public void alterar(Paciente paciente) throws SQLException {
        PreparedStatement comando = null;
        Connection conexao = null;
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_UPDATE);
            comando.setString(1, paciente.getNome());
            comando.setString(2, paciente.getCpf());
            comando.setString(3, paciente.getTelefone());
            comando.setString(4, paciente.getEndereco());
            comando.setDate(5, paciente.getData_nascimento());
            comando.setString(6, paciente.getSexo());
            comando.setInt(7, paciente.getId());

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

    public List<Paciente> buscarTodosCpf(Paciente filtro) throws SQLException {
        List<Paciente> pacientes = new ArrayList<>();
        Paciente paciente = null;
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;
        PacienteDAO pacienteDAO = new PacienteDAO();
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_BUSCA_TODOS_CPF);
            comando.setString(1, filtro.getCpf());
            resultado = comando.executeQuery();
            while (resultado.next()) {
                paciente = new Paciente();

                paciente.setId(resultado.getInt(1));
                paciente.setNome(resultado.getString(2));
                paciente.setCpf(resultado.getString(3));
                paciente.setTelefone(resultado.getString(4));
                paciente.setEndereco(resultado.getString(5));
                paciente.setData_nascimento(resultado.getDate(6));
                paciente.setSexo(resultado.getString(7));
                pacientes.add(paciente);
            }

        } catch (Exception e) {
            if (conexao != null) {
//                conexao.rollback();
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
        return pacientes;
    }

    public List<Paciente> buscarTodos() throws SQLException {
        List<Paciente> pacientes = new ArrayList<>();
        Paciente paciente = null;
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;
        PacienteDAO pacienteDAO = new PacienteDAO();
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_BUSCA_TODOS);
            resultado = comando.executeQuery();
            while (resultado.next()) {
                paciente = new Paciente();

                paciente.setId(resultado.getInt(1));
                paciente.setNome(resultado.getString(2));
                paciente.setCpf(resultado.getString(3));
                paciente.setTelefone(resultado.getString(4));
                paciente.setEndereco(resultado.getString(5));
                paciente.setData_nascimento(resultado.getDate(6));
                paciente.setSexo(resultado.getString(7));
                pacientes.add(paciente);
            }

        } catch (Exception e) {
            if (conexao != null) {
//                conexao.rollback();
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
        return pacientes;
    }

    public Paciente buscarCpfPaciente(String cpf) throws SQLException {
        Paciente paciente = null;
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_BUSCAR_CPF_EXISTE);
            comando.setString(1, cpf);
            resultado = comando.executeQuery();

            while (resultado.next()) {
                paciente = new Paciente();

                paciente.setCpf(resultado.getString(1));
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
        return paciente;
    }

    public List<Paciente> buscarNomes(String nome) throws SQLException {
        List<Paciente> pacientes = new ArrayList<>();
        Paciente paciente = null;
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;
        PacienteDAO pacienteDAO = new PacienteDAO();
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_BUSCA_TODOS_NOME);
            comando.setString(1, "%" + nome + "%");
            resultado = comando.executeQuery();
            while (resultado.next()) {
                paciente = new Paciente();
                paciente = new Paciente();

                paciente.setId(resultado.getInt(1));
                paciente.setNome(resultado.getString(2));
                paciente.setCpf(resultado.getString(3));
                paciente.setTelefone(resultado.getString(4));
                paciente.setEndereco(resultado.getString(5));
                paciente.setData_nascimento(resultado.getDate(6));
                paciente.setSexo(resultado.getString(7));
                pacientes.add(paciente);

            }

        } catch (Exception e) {
            if (conexao != null) {
//                conexao.rollback();
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
        return pacientes;

    }

    public void removerPaciente(int id) throws SQLException {
        Connection conexao = null;
        PreparedStatement comando = null;
        try {

            conexao = BancoDadosUtil.getConnection();

            comando = conexao.prepareStatement(SQL_REMOVER_PACIENTE);

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

