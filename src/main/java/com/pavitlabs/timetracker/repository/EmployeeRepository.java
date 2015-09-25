package com.pavitlabs.timetracker.repository;

import com.pavitlabs.timetracker.domain.Employee;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Employee entity.
 */
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    @Query("select employee from Employee employee left join fetch employee.tasks where employee.id =:id")
    Employee findOneWithEagerRelationships(@Param("id") Long id);

}
