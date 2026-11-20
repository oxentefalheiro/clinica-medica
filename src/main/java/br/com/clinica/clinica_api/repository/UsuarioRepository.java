package br.com.clinica.clinica_api.repository;

import br.com.clinica.clinica_api.dto.UsuarioRequestDTO;
import br.com.clinica.clinica_api.enums.Perfil;
import br.com.clinica.clinica_api.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<UsuarioRequestDTO> findAll (Long id);
    Page<Usuario> findByAtivoTrue (Pageable paginacao);
    Page<Usuario> findByPerfil (Perfil perfil, Pageable paginacao);
    boolean existsByLogin (String login);
    UserDetails findByLogin (String login);

}
