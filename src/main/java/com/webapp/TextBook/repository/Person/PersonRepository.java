package com.webapp.TextBook.repository.Person;

import com.webapp.TextBook.repository.BookCopy.BookCopyRepositoryCustom;
import com.webapp.TextBook.repository.data_access.BookCopy;
import com.webapp.TextBook.repository.data_access.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PersonRepository extends JpaRepository<Student, UUID>, PersonRepositoryCustom {

}
