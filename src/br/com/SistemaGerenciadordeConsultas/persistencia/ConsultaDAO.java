/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.SistemaGerenciadordeConsultas.persistencia;

import br.com.SistemaGerenciadordeConsultas.entidade.Consulta;
import br.com.SistemaGerenciadordeConsultas.entidade.Medico;
import br.com.SistemaGerenciadordeConsultas.entidade.Paciente;
import br.com.SistemaGerenciadordeConsultas.entidade.QuantidadeConsultaPorDia;
import br.com.SistemaGerenciadordeConsultas.negocio.ConverteData;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rayssa
 */
public class ConsultaDAO {

    private static final String SQL_INSERT = "INSERT INTO CONSULTA(DATA,HORARIO,MEDICO,PACIENTE,OBSERVACAO) VALUES(?,?,?,?,?)";
    private static final String SQL_UPDATE = "UPDATE CONSEULTA SET DATA=?, HORARIO=?, MEDICO=?, PACIENTE=?, OBSERVACAO=? WHERE ID=?";
    private static final String SQL_BUSCA_TODOS = "SELECT * FROM CONSULTA ORDER BY DATA,HORARIO DESC";
    private static final String SQL_BUSCA_CONSULTA_GROUP_DIA = "SELECT COUNT(*), DATA FROM CONSULTA GROUP BY DATA ORDER BY DATA DESC";
    private static final String SQL_REMOVER_CONSULTA = "DELETE FROM CONSULTA WHERE ID = ?";
    private static final String SQL_BUSCAR_CONSULTA = "SELECT DATA, HORARIO FROM CONSULTA WHERE DATA = ?";
    private static final String SQL_BUSCAR_POR_DATAS = "SELECT C.ID, C.DATA, C.HORARIO, M.NOME, P.NOME, C.OBSERVACAO FROM CONSULTA C JOIN MEDICO M ON C.MEDICO = M.ID JOIN PACIENTE P ON C.PACIENTE = P.ID WHERE C.DATA >= ? AND C.DATA <= ? ORDER BY C.DATA";

    public void criar(Consulta consulta) throws SQLException {
        PreparedStatement comando = null;
        Connection conexao = null;
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_INSERT);
            comando.setDate(1, consulta.getData());
            comando.setTime(2, consulta.getHorario());
            comando.setInt(3, consulta.getMedico().getId());
            comando.setInt(4, consulta.getPaciente().getId());
            comando.setString(5, consulta.getObservacao());

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

    public void alterar(Consulta consulta) throws SQLException {
        PreparedStatement comando = null;
        Connection conexao = null;
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_UPDATE);
            comando.setDate(1, consulta.getData());
            comando.setTime(2, consulta.getHorario());
            comando.setInt(3, consulta.getMedico().getId());
            comando.setInt(4, consulta.getPaciente().getId());
            comando.setString(5, consulta.getObservacao());
            comando.setInt(6, consulta.getId());

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

    public void removerConsulta(int id) throws SQLException {
        Connection conexao = null;
        PreparedStatement comando = null;
        try {

            conexao = BancoDadosUtil.getConnection();

            comando = conexao.prepareStatement(SQL_REMOVER_CONSULTA);

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

    public List<Consulta> buscarTodos() throws SQLException {
        List<Consulta> consultas = new ArrayList<>();
        MedicoDAO medicoDAO = new MedicoDAO();
        PacienteDAO pacienteDAO = new PacienteDAO();
        Consulta consulta = null;
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_BUSCA_TODOS);
            resultado = comando.executeQuery();
            while (resultado.next()) {
                consulta = new Consulta();

                consulta.setId(resultado.getInt(1));
                consulta.setData(resultado.getDate(2));
                consulta.setHorario(resultado.getTime(3));
                for (Medico medico : medicoDAO.buscarTodos()) {
                    if (medico.getId() == resultado.getInt(4)) {
                        consulta.setMedico(medico);
                    }
                }
                for (Paciente paciente : pacienteDAO.buscarTodos()) {
                    if (paciente.getId() == resultado.getInt(5)) {
                        consulta.setPaciente(paciente);
                    }
                }
                consulta.setObservacao(resultado.getString(6));
                consultas.add(consulta);
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
        return consultas;
    }

    public List<Consulta> buscarPorPaciente(Paciente paciente) throws SQLException {
        List<Consulta> consultas = new ArrayList<>();
        MedicoDAO medicoDAO = new MedicoDAO();
        Consulta consulta = null;
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_BUSCA_TODOS);
            resultado = comando.executeQuery();
            while (resultado.next()) {
                if (paciente.getId() == resultado.getInt(5)) {
                    consulta = new Consulta();
                    consulta.setId(resultado.getInt(1));
                    consulta.setData(resultado.getDate(2));
                    consulta.setHorario(resultado.getTime(3));
                    for (Medico medico : medicoDAO.buscarTodos()) {
                        if (medico.getId() == resultado.getInt(4)) {
                            consulta.setMedico(medico);
                        }
                    }
                    consulta.setPaciente(paciente);
                    consulta.setObservacao(resultado.getString(6));
                    consultas.add(consulta);
                }
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
        return consultas;
    }

    public List<Consulta> buscarPorMedico(Medico medico) throws SQLException {
        List<Consulta> consultas = new ArrayList<>();
        PacienteDAO pacienteDAO = new PacienteDAO();
        Consulta consulta = null;
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_BUSCA_TODOS);
            resultado = comando.executeQuery();
            while (resultado.next()) {
                if (medico.getId() == resultado.getInt(4)) {
                    consulta = new Consulta();
                    consulta.setId(resultado.getInt(1));
                    consulta.setData(resultado.getDate(2));
                    consulta.setHorario(resultado.getTime(3));
                    consulta.setMedico(medico);
                    for (Paciente paciente : pacienteDAO.buscarTodos()) {
                        if (paciente.getId() == resultado.getInt(5)) {
                            consulta.setPaciente(paciente);
                        }
                    }
                    consulta.setObservacao(resultado.getString(6));
                    consultas.add(consulta);
                }
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
        return consultas;
    }

    public List<QuantidadeConsultaPorDia> buscarConsultasPorDia() throws SQLException {
        List<QuantidadeConsultaPorDia> consultas = new ArrayList<>();
        QuantidadeConsultaPorDia porDia = null;
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_BUSCA_CONSULTA_GROUP_DIA);
            resultado = comando.executeQuery();
            while (resultado.next()) {
                porDia = new QuantidadeConsultaPorDia();
                porDia.setData(resultado.getDate(2));
                porDia.setQuantidade(resultado.getInt(1));

                consultas.add(porDia);
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
        return consultas;
    }

    public Consulta buscarConsultaMarcada(String data, Time horario) throws SQLException {
        Consulta consulta = null;
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_BUSCAR_CONSULTA);
            comando.setString(1, data);
            comando.setTime(2, horario);
            resultado = comando.executeQuery();

            while (resultado.next()) {
                consulta = new Consulta();

                consulta.setData(resultado.getDate(1));
                consulta.setHorario(resultado.getTime(2));
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
        return consulta;
    }

    public List<Consulta> buscarPorDatas(java.sql.Date dataInicial, java.sql.Date dataFinal) throws SQLException {
        List<Consulta> consultas = new ArrayList<>();
        
        Consulta consulta = null;
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;
        try {
            conexao = BancoDadosUtil.getConnection();
            comando = conexao.prepareStatement(SQL_BUSCAR_POR_DATAS);
            comando.setDate(1, dataInicial);
            comando.setDate(2, dataFinal);
            resultado = comando.executeQuery();
            while (resultado.next()) {
                consulta = new Consulta();

                consulta.setId(resultado.getInt(1));
                Date data = resultado.getDate(2);
                String strData = ConverteData.DateForString(data);
                consulta.setStrData(strData);
                consulta.setHorario(resultado.getTime(3));
                consulta.setStrMedico(resultado.getString(4));
                consulta.setStrPaciente(resultado.getString(5));
                consulta.setObservacao(resultado.getString(6));
                consultas.add(consulta);
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
        return consultas;
    }

}
