package me.raulbalanza.tjv.school_enrollment.client.web_clients;

import me.raulbalanza.tjv.school_enrollment.client.dto.CourseDto;
import me.raulbalanza.tjv.school_enrollment.client.dto.StudentDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class StudentClient {

    private final WebClient studentWebClient;

    public StudentClient(@Value("${backend.url}") String baseUrl){
        studentWebClient = WebClient.create(baseUrl + "/students");
    }

    public Flux<StudentDto> readAllStudents(){
        return studentWebClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(StudentDto.class);
    }

    public Mono<StudentDto> getOne(String username){
        return studentWebClient.get().uri("/" + username)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(StudentDto.class);
    }

    public Flux<CourseDto> getStudentCourses(String username){
        return studentWebClient.get().uri("/" + username + "/courses")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(CourseDto.class);
    }

    public Mono<StudentDto> create(StudentDto studentDto){
        return studentWebClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(studentDto)
                .retrieve()
                .bodyToMono(StudentDto.class);
    }

    public Mono<StudentDto> update(StudentDto studentDto){
        return studentWebClient.put().uri("/" + studentDto.username)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(studentDto)
                .retrieve()
                .bodyToMono(StudentDto.class);
    }

    public Mono<String> delete(String username){
        return studentWebClient.delete().uri("/" + username)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Flux<CourseDto> enrollStudentInCourse(String username, String courseID){
        return studentWebClient.post().uri("/" + username + "/courses/" + courseID)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(CourseDto.class);
    }

    public Flux<CourseDto> removeEnrollmentFromCourse(String username, String courseID){
        return studentWebClient.delete().uri("/" + username + "/courses/" + courseID)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(CourseDto.class);
    }

}
