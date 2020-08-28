package com.example.redditClone.repository;

import com.example.redditClone.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

//interface TodoRepository extends Repository<Todo, Long> {
//
//    List<Todo> findByDescriptionContainsOrTitleContainsAllIgnoreCase(String descriptionPart,
//                                                                     String titlePart);
//}
//Spring Data JPA uses the descriptionPart method parameter when it ensures
//        that the description of the returned todo entry contains the given search term.
//        Spring Data JPA uses the titlePart method parameter when it ensures
//        that the title of the returned todo entry contains the given search term.

//JPQL
//
//interface TodoRepository extends Repository<Todo, Long> {
//
//    @Query("SELECT t FROM Todo t WHERE t.title = 'title'")
//    public List<Todo> findByTitle();
//}
//
//SQL
//interface TodoRepository extends Repository<Todo, Long> {
//
//    @Query(value = "SELECT * FROM todos t WHERE t.title = 'title'",
//            nativeQuery=true
//    )
//    public List<Todo> findByTitle();
//}