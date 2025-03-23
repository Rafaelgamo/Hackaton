package repository;

import entity.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.web.config.EnableSpringDataWebSupport;


@EnableSpringDataWebSupport
public interface MedicoRepository extends JpaRepository<Medico, Long>  {

    // Page<MedicoDTO> listarMedicos(Pageable pageable);

    Page<Medico> findAllByAtivoTrue(Pageable paginacao);

    // void CadastroMedicoDTO(CadastroMedicoDTO cadastroMedicoDTO);
    // void removerMedico(Long id);

}


