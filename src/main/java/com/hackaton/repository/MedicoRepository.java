package com.hackaton.repository;

import com.hackaton.entity.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long>  {

    // Page<MedicoDTO> listarMedicos(Pageable pageable);

    Page<Medico> findAllByAtivoTrue(Pageable paginacao);

    // void CadastroMedicoDTO(CadastroMedicoDTO cadastroMedicoDTO);
    // void removerMedico(Long id);

}


