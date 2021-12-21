package me.raulbalanza.tjv.school_enrollment.client.web_clients;

import me.raulbalanza.tjv.school_enrollment.client.dto.TeacherDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TeacherClient {

    private final WebClient teacherWebClient;

    public TeacherClient(@Value("${backend.url}") String baseUrl){
        teacherWebClient = WebClient.create(baseUrl + "/teachers");
    }

    public Flux<TeacherDto> readAllTeachers(){
        return teacherWebClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(TeacherDto.class);
    }

    public Mono<TeacherDto> getOne(String username){
        return teacherWebClient.get().uri("/" + username)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(TeacherDto.class);
    }

    public Mono<TeacherDto> create(TeacherDto teacherDto){
        return teacherWebClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(teacherDto)
                .retrieve()
                .bodyToMono(TeacherDto.class);
    }

    public Mono<TeacherDto> update(TeacherDto teacherDto){
        return teacherWebClient.put().uri("/" + teacherDto.username)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(teacherDto)
                .retrieve()
                .bodyToMono(TeacherDto.class);
    }

    public Mono<String> delete(String username){
        return teacherWebClient.delete().uri("/" + username)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);
    }

}
