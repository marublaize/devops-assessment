package br.com.electroneekrobotics.Service;

import br.com.electroneekrobotics.Exception.UsuarioCadastradoException;
import br.com.electroneekrobotics.Model.UsuarioModel;
import br.com.electroneekrobotics.Repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsuarioService implements UserDetailsService {

    private UsuarioRepository usuarioRepository;

    public UsuarioModel salvar(UsuarioModel usuarioModel){
        boolean exists = usuarioRepository.existsByUsername(usuarioModel.getUsername());
        if(exists){
            throw new UsuarioCadastradoException(usuarioModel.getUsername());
        }
        return usuarioRepository.save(usuarioModel);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioModel usuario = usuarioRepository
                            .findByUsername(username)
                            .orElseThrow(() -> new UsernameNotFoundException("Login não encontrado"));
        return User
                .builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles("USER")
                .build();
    }
}
