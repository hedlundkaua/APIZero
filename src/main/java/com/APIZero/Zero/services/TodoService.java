package com.APIZero.Zero.services;

import com.APIZero.Zero.dto.TodoRequestDTO;
import com.APIZero.Zero.dto.TodoResponseDTO;
import com.APIZero.Zero.entities.Todo;
import com.APIZero.Zero.entities.User;
import com.APIZero.Zero.enums.TodoState;
import com.APIZero.Zero.repositories.TodoRepository;
import com.APIZero.Zero.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public TodoResponseDTO createTodo(TodoRequestDTO dto, String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuário autenticado não encontrado no banco de dados"));

        Todo todo = new Todo(dto.title(), dto.description(), dto.state(), user);
        Todo savedTodo = todoRepository.save(todo);

        return TodoResponseDTO.fromEntity(savedTodo);
    }


    public Page<TodoResponseDTO> listTodos(String userEmail, String title, String description, TodoState state, Pageable pageable){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuário autenticado não encontrado no banco de dados"));

        Page<Todo> todosPage = todoRepository.findTodosWithFilters(user.getId(), title, description, state, pageable);

        return todosPage.map(TodoResponseDTO::fromEntity);
    }
    @Transactional
    public TodoResponseDTO updateTodo(Long id, TodoRequestDTO dto, String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuário autenticado não encontrado"));
        Todo todo = todoRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new IllegalAccessError("Tarefa não encontrada ou não pertence a esse usuario"));

        todo.setTitle(dto.title());
        todo.setDescription(dto.description());
        todo.setState(dto.state());

        return TodoResponseDTO.fromEntity(todo);
    }
    @Transactional
    public void deleteTodo(Long id, String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuário autenticado não encontrado"));
        Todo todo = todoRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new IllegalAccessError("Tarefa não encontrada ou não pertence a esse usuario"));

        todoRepository.delete(todo);
    }

    @Transactional
    public TodoResponseDTO updateTodo(String userEmail, Long id, TodoRequestDTO request){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuário autenticado não encontrado"));

        Todo todo = todoRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new IllegalAccessError("Tarefa não encontrada ou não pertence a esse usuario"));

        if(!todo.getUser().getId().equals(user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado a está tarefa");
        }

        todo.setTitle(request.title());
        todo.setDescription(request.description());
        todo.setState(request.state());

        Todo updatedTodo = todoRepository.save(todo);
        return TodoResponseDTO.fromEntity(updatedTodo);
    }

    @Transactional
    public void deleteTodo(String userEmail, Long id){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuário autenticado não encontrado"));

        Todo todo = todoRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new IllegalAccessError("Tarefa não encontrada ou não pertence a esse usuario"));

        if(!todo.getUser().getId().equals(todo.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado a está tarefa");
        }
        todoRepository.delete(todo);
    }
}