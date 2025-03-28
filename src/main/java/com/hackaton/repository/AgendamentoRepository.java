package com.hackaton.repository;

import com.hackaton.entity.Agendamento;
import com.hackaton.entity.Medico;
import com.hackaton.entity.Paciente;
import com.hackaton.entity.StatusConfirmacaoConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    @Query("from Agendamento a " +
            "   where a.data = :data and a.medico.id = :medicoId" +
            "       and a.status != "+ StatusConfirmacaoConstants.DISPONIVEL +
            "       and a.status != "+ StatusConfirmacaoConstants.CANCELADO +
            "   order by a.data, a.hora")
    Set<Agendamento> listarMarcadosPorDataEMedico(LocalDate data, Long medicoId);

    @Query("from Agendamento a " +
            "   where a.medico.id = :medicoId" +
            "       and a.data = :data" +
            "       and a.hora = :hora")
    Agendamento buscarAgendamento(Long medicoId, LocalDate data, LocalTime hora);

    List<Agendamento> findAllByPaciente(Paciente paciente);

    List<Agendamento> findAllByMedico(Medico medico);
}
