package me.raulbalanza.tjv.school_enrollment.client.web_clients;

import me.raulbalanza.tjv.school_enrollment.client.dto.CourseDto;
import me.raulbalanza.tjv.school_enrollment.client.dto.FilterDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class CourseClient {

    private final WebClient courseWebClient;

    public CourseClient(@Value("${backend.url}") String baseUrl){
        courseWebClient = WebClient.create(baseUrl + "/courses");
    }

    public Flux<CourseDto> readAllCourses(FilterDto data){
        return courseWebClient.get().uri("?min_free_capacity=" + data.min_free_capacity + "&max_credits=" + data.max_credits)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(CourseDto.class);
    }

    public Mono<CourseDto> getOne(String id){
        return courseWebClient.get().uri("/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CourseDto.class);
    }

    public Mono<CourseDto> create(CourseDto courseDto){
        return courseWebClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(courseDto)
                .retrieve()
                .bodyToMono(CourseDto.class);
    }

    public Mono<CourseDto> update(CourseDto courseDto){
        return courseWebClient.put().uri("/" + courseDto.ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(courseDto)
                .retrieve()
                .bodyToMono(CourseDto.class);
    }

    public Mono<String> delete(String courseID){
        return courseWebClient.delete().uri("/" + courseID)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);
    }

}
