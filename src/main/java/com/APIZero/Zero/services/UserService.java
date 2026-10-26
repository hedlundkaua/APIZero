package com.APIZero.Zero.services;

import com.APIZero.Zero.dto.UserRequestDTO;
import com.APIZero.Zero.dto.UserResponseDTO;
import com.APIZero.Zero.entities.User;
import com.APIZero.Zero.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    //injeção de dependencia via construtor
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDTO createUsser(UserRequestDTO dto){
        if(userRepository.existsByEmail(dto.email())){
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail");
        }
        //Cria um objeto user e salva com os dados do dto
        User user = new User(dto.username(), dto.email(), dto.password());

        //Pega o user do objeto e salva no banco de dados
        User savedUser = userRepository.save(user);

        return UserResponseDTO.fromEntity(savedUser);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(UserResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("usuário não encontrado com o ID: " + id));
        return UserResponseDTO.fromEntity(user);
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("usuário não encontrado com o ID: " + id));
       //Valida se o novo email ja existe no Banco de dados

        if(!user.getEmail().equals(dto.email()) && userRepository.existsByEmail(dto.email())){
            throw new IllegalArgumentException("Este email já está em uso por outro usuário");
        }

        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(dto.password());

        return UserResponseDTO.fromEntity(user);
    }

    @Transactional
    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new IllegalArgumentException("Usuário não encontrado com ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
