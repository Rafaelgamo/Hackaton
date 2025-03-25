package com.hackaton.repository;

import com.hackaton.entity.Medico;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.util.Optional;

@EnableSpringDataWebSupport
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Page<Medico> findAllByAtivoTrue(Pageable paginacao);

    @Bean
    Optional<Medico> findById(Long id);

}


