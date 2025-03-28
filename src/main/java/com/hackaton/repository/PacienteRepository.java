package com.hackaton.repository;

import com.hackaton.entity.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>  {

    Paciente findByCpf(String cpf);

    Page<Paciente> findAllByAtivoTrue(Pageable paginacao);
}


