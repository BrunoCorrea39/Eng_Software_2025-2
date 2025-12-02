package com.escolinha.repository;

import com.escolinha.domain.Usuario; // Importe a classe abstrata/interface
import com.escolinha.domain.TipoUsuario; // Importe o Enum
import java.util.List;
import java.util.Optional;
import com.escolinha.domain.TipoUsuario;


public interface UsuarioRepository {

    /**
     * Salva um usuário (novo ou atualização).
     * @param usuario O usuário a ser salvo.
     * @return O usuário salvo.
     */
    Usuario salvar(Usuario usuario);

    /**
     * Busca um usuário pelo ID.
     * @param id O ID do usuário.
     * @return Optional contendo o usuário se encontrado.
     */
    Optional<Usuario> buscarPorId(int id);

    /**
     * Busca um usuário pelo login (ou email).
     * @param login O login a ser buscado.
     * @return Optional contendo o usuário se encontrado.
     */
    Optional<Usuario> buscarPorLogin(String login);

    /**
     * Lista todos os usuários.
     * @return Lista de todos os usuários.
     */
    List<Usuario> listarTodos();

    /**
     * Lista todos os usuários de um tipo específico.
     * @param tipo O tipo de usuário (ADMINISTRADOR, TREINADOR, RESPONSAVEL).
     * @return Lista de usuários do tipo especificado.
     */
    List<Usuario> buscarPorTipo(TipoUsuario tipo);

    /**
     * Deleta um usuário pelo ID.
     * @param id O ID do usuário.
     * @return true se deletado com sucesso.
     */
    boolean deletarPorId(int id);
}