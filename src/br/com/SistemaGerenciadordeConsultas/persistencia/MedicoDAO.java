/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.SistemaGerenciadordeConsultas.persistencia;

import br.com.SistemaGerenciadordeConsultas.entidade.Especialidade;
import br.com.SistemaGerenciadordeConsultas.entidade.Medico;
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
public class MedicoDAO {

    private static final String SQL_INSERT = "INSERT INTO MEDICO(NOME,CPF,CRM,ESPECIALIDADE,TELEFONE,ENDERECO,SEXO) VALUES(?,?,?,?,?,?,?)";
    private static final String SQL_UPDATE = "UPDATE MEDICO SET NOME=?, CPF=?, CRM=?, ESPECIALIDADE=?, TELEFONE=?, ENDERECO=?, SEXO=? WHERE ID=?";
    private static final String SQL_BUSCA_TODOS = "SELECT * FROM MEDICO ORDER BY NOME";
    private static final String SQL_BUSCA_TODOS_FILTRO = "SELECT * FROM MEDICO WHERE NOME LIKE ? OR CPF LIKE ? ORDER BY NOME";
    private static final String SQL_BUSCAR_CPF_EXISTE = "SELECT CPF FROM MEDICO WHERE CPF = ?";
    private static final String SQL_BUSCAR_CRM_EXISTE = "SELECT CRM FROM MEDICO WHERE CRM = ?";
    private static final String SQL_REMOVER_MEDICO = "DELETE FROM MEDICO WHERE ID = ?";

    public void criar(Medico medico) throws SQLException {
        PreparedStatement comando = null;
        Connection conexao = null;
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_INSERT);
            comando.setString(1, medico.getNome());
            comando.setString(2, medico.getCpf());
            comando.setString(3, medico.getCrm());
            comando.setInt(4, medico.getEspecialidade().getId());
            comando.setString(5, medico.getTelefone());
            comando.setString(6, medico.getEndereco());
            comando.setString(7, medico.getSexo());

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

    public void alterar(Medico medico) throws SQLException {
        PreparedStatement comando = null;
        Connection conexao = null;
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_UPDATE);
            comando.setString(1, medico.getNome());
            comando.setString(2, medico.getCpf());
            comando.setString(3, medico.getCrm());
            comando.setInt(4, medico.getEspecialidade().getId());
            comando.setString(5, medico.getTelefone());
            comando.setString(6, medico.getEndereco());
            comando.setString(7, medico.getSexo());
            comando.setInt(8, medico.getId());

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

    public List<Medico> buscarTodosFiltro(Medico filtro) throws SQLException {
        List<Medico> medicos = new ArrayList<>();
        Medico medico = null;
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;
        EspecialidadeDAO especialidadeDAO = new EspecialidadeDAO();
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_BUSCA_TODOS_FILTRO);
            comando.setString(1, filtro.getNome());
            comando.setString(2, filtro.getCpf());
            resultado = comando.executeQuery();
            while (resultado.next()) {
                medico = new Medico();

                if (filtro.getEspecialidade() == null) {
                    medico.setId(resultado.getInt(1));
                    medico.setNome(resultado.getString(2));
                    medico.setCpf(resultado.getString(3));
                    medico.setCrm(resultado.getString(4));
                    for (Especialidade especialidade : especialidadeDAO.buscarTodos()) {
                        if (especialidade.getId() == resultado.getInt(5)) {
                            medico.setEspecialidade(especialidade);
                        }
                    }
                    medico.setTelefone(resultado.getString(2));
                    medico.setEndereco(resultado.getString(2));
                    medico.setSexo(resultado.getString(2));
                    medicos.add(medico);
                } else if (filtro.getEspecialidade().getId() == resultado.getInt(5)) {
                    medico.setId(resultado.getInt(1));
                    medico.setNome(resultado.getString(2));
                    medico.setCpf(resultado.getString(3));
                    medico.setCrm(resultado.getString(4));
                    for (Especialidade especialidade : especialidadeDAO.buscarTodos()) {
                        if (especialidade.getId() == resultado.getInt(5)) {
                            medico.setEspecialidade(especialidade);
                        }
                    }
                    medico.setTelefone(resultado.getString(2));
                    medico.setEndereco(resultado.getString(2));
                    medico.setSexo(resultado.getString(2));
                    medicos.add(medico);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (conexao != null) {
            }
        } finally {
            if (comando != null && !comando.isClosed()) {
                comando.close();
            }
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        }
        return medicos;
    }

    public List<Medico> buscarTodos() throws SQLException {
        List<Medico> medicos = new ArrayList<>();
        Medico medico = null;
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;
        EspecialidadeDAO especialidadeDAO = new EspecialidadeDAO();
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_BUSCA_TODOS);
            resultado = comando.executeQuery();
            while (resultado.next()) {
                medico = new Medico();

                medico.setId(resultado.getInt(1));
                medico.setNome(resultado.getString(2));
                medico.setCpf(resultado.getString(3));
                medico.setCrm(resultado.getString(4));
                for (Especialidade especialidade : especialidadeDAO.buscarTodos()) {
                    if (especialidade.getId() == resultado.getInt(5)) {
                        medico.setEspecialidade(especialidade);
                    }
                }
                medico.setTelefone(resultado.getString(6));
                medico.setEndereco(resultado.getString(7));
                medico.setSexo(resultado.getString(8));
                medicos.add(medico);
            }

        } catch (Exception e) {
            if (conexao != null) {
            }
        } finally {
            if (comando != null && !comando.isClosed()) {
                comando.close();
            }
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        }
        return medicos;
    }

    public Medico buscarCpfMedico(String cpf) throws SQLException {
        Medico medico = null;
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_BUSCAR_CPF_EXISTE);
            comando.setString(1, cpf);
            resultado = comando.executeQuery();

            while (resultado.next()) {
                medico = new Medico();

                medico.setCpf(resultado.getString(1));
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
        return medico;
    }

    public Medico buscarCrmMedico(String crm) throws SQLException {
        Medico medico = null;
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_BUSCAR_CRM_EXISTE);
            comando.setString(1, crm);
            resultado = comando.executeQuery();

            while (resultado.next()) {
                medico = new Medico();

                medico.setCrm(resultado.getString(1));
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
        return medico;
    }

    public void removerMedico(int id) throws Exception {
        Connection conexao = null;
        PreparedStatement comando = null;
        try {

            conexao = BancoDadosUtil.getConnection();

            comando = conexao.prepareStatement(SQL_REMOVER_MEDICO);

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
