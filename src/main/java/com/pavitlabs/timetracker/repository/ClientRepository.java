package com.pavitlabs.timetracker.repository;

import com.pavitlabs.timetracker.domain.Client;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Client entity.
 */
public interface ClientRepository extends JpaRepository<Client,Long> {

}
