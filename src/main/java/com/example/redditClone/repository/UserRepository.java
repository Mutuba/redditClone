package com.example.redditClone.repository;

import com.example.redditClone.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
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

//interface TodoRepository extends Repository<Todo, Long> {
//
//    @Query(value = "SELECT * FROM todos t WHERE " +
//            "LOWER(t.title) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
//            "LOWER(t.description) LIKE LOWER(CONCAT('%',:searchTerm, '%'))",
//            nativeQuery = true
//    )
//    List<Todo> findBySearchTermNative(@Param("searchTerm") String searchTerm);
//}