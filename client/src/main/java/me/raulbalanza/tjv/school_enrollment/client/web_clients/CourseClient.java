package me.raulbalanza.tjv.school_enrollment.client.web_clients;

import me.raulbalanza.tjv.school_enrollment.client.dto.ClassIntervalDto;
import me.raulbalanza.tjv.school_enrollment.client.dto.CourseDto;
import me.raulbalanza.tjv.school_enrollment.client.dto.FilterDto;
import me.raulbalanza.tjv.school_enrollment.client.dto.TeacherDto;
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

    public Flux<ClassIntervalDto> getCourseSchedule(String id){
        return courseWebClient.get().uri("/" + id + "/schedule")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(ClassIntervalDto.class);
    }

    public Mono<String> deleteCourseSchedule(String courseID, String day, String start, String finish){
        return courseWebClient.delete().uri("/" + courseID + "/schedule?day=" + day + "&start=" + start + "&finish=" + finish)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<ClassIntervalDto> addCourseSchedule(String courseID, ClassIntervalDto classIntervalDto){
        return courseWebClient.post().uri("/" + courseID + "/schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(classIntervalDto)
                .retrieve()
                .bodyToMono(ClassIntervalDto.class);
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

    public Flux<TeacherDto> getCourseTeachers(String id){
        return courseWebClient.get().uri("/" + id + "/teachers")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(TeacherDto.class);
    }

    public Flux<TeacherDto> removeTeacherFromCourse(String id, String username){
        return courseWebClient.delete().uri("/" + id + "/teachers/" + username)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(TeacherDto.class);
    }

    public Flux<TeacherDto> addTeacherToCourse(String id, String username){
        return courseWebClient.post().uri("/" + id + "/teachers/" + username)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(TeacherDto.class);
    }

}
