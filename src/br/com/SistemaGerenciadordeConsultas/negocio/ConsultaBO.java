/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.SistemaGerenciadordeConsultas.negocio;

import br.com.SistemaGerenciadordeConsultas.entidade.Consulta;
import br.com.SistemaGerenciadordeConsultas.entidade.Medico;
import br.com.SistemaGerenciadordeConsultas.entidade.Paciente;
import br.com.SistemaGerenciadordeConsultas.entidade.QuantidadeConsultaPorDia;
import br.com.SistemaGerenciadordeConsultas.excecao.CampoObrigatorioException;
import br.com.SistemaGerenciadordeConsultas.excecao.DataInvalidaException;
import br.com.SistemaGerenciadordeConsultas.excecao.HoraConsultaAgendadaException;
import br.com.SistemaGerenciadordeConsultas.persistencia.ConsultaDAO;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Rayssa
 */
public class ConsultaBO {
    
    public List<Consulta> buscarTodosPorPaciente(Paciente paciente) throws SQLException {
        ConsultaDAO consultaDAO = new ConsultaDAO();
        return consultaDAO.buscarPorPaciente(paciente);
    }

    public List<Consulta> buscarTodosPorMedico(Medico medico) throws SQLException {
        ConsultaDAO consultaDAO = new ConsultaDAO();
        return consultaDAO.buscarPorMedico(medico);
    }

    public List<Consulta> buscarTodos() throws SQLException {
        ConsultaDAO consultaDAO = new ConsultaDAO();
        return consultaDAO.buscarTodos();
    }
    
    public void salvar(Consulta consulta) throws SQLException, ParseException {
        ConsultaDAO consultaDAO = new ConsultaDAO();
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
        String data = (formatador.format(consulta.getData()));
        validaData(data);
        verificaDataHoraConsulta(data,consulta.getHorario());
        consultaDAO.criar(consulta);
    }
    public void editar(Consulta consulta) throws SQLException {
        ConsultaDAO consultaDAO = new ConsultaDAO();
        consultaDAO.alterar(consulta);
    }
    
    public List<QuantidadeConsultaPorDia> buscarConsultasPorDia() throws SQLException {
        ConsultaDAO consultaDAO = new ConsultaDAO();
        return consultaDAO.buscarConsultasPorDia();
    }

    public void removerConsulta(int id) throws SQLException {
        ConsultaDAO consultaDAO = new ConsultaDAO();
        consultaDAO.removerConsulta(id);
    }
    private void validaData(String data) throws ParseException {
        Date dataAtual = new Date(System.currentTimeMillis());
        Date dataInformada = ConverteData.StringForDate(data);
        
        Date dataAtualZera = zeraHoras1(dataAtual);
        
        if (dataInformada.before(dataAtualZera)) {
            throw new DataInvalidaException();
        }       
    }    
    private static Date zeraHoras1(Date data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (Date) cal.getTime();
        
    }

    private void verificaDataHoraConsulta(String data, Time horario) throws SQLException {
        Consulta consultaMarcada = null;
        ConsultaDAO consultaDAO = new ConsultaDAO();
        consultaMarcada = consultaDAO.buscarConsultaMarcada(data,horario);
        if (consultaMarcada != null) {
            throw new HoraConsultaAgendadaException();
        }

        
    }
    
    public List<Consulta> buscarPorDatas(java.sql.Date dataInicial, java.sql.Date dataFinal) throws SQLException {
        ConsultaDAO consultaDAO = new ConsultaDAO();
        return consultaDAO.buscarPorDatas(dataInicial, dataFinal);
    }
    
    
}
