package service;

import dto.CadastroMedicoDTO;
import dto.MedicoDTO;
import entity.Medico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.MedicoRepository;

import java.util.Optional;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;
    public MedicoService(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    @Transactional
    public Medico cadastroMedico(CadastroMedicoDTO cadastroMedicoDTO){

            var medico = new Medico();

            medico.setNome(cadastroMedicoDTO.nome());
            medico.setCrm(cadastroMedicoDTO.crm());
            medico.setEspecialidade(cadastroMedicoDTO.especialidade());
            medico.setHora_entrada(cadastroMedicoDTO.data_entrada());
            medico.setHora_saida(cadastroMedicoDTO.data_saida());


       var salvo = medicoRepository.save(medico);
       return salvo;

    }

    @Transactional(readOnly = true)
    public Page<MedicoDTO> listrMedicos(Pageable pageable) {
        return medicoRepository.findAllByAtivoTrue(pageable).map(MedicoDTO::new);
    }


    @Transactional
    public void removerMedico(Long id) {
        Optional<Medico> medico = medicoRepository.findById(id);
        medico.ifPresent(value -> value.setAtivo(false));
    }

}
