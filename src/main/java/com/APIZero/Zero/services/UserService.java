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
}
