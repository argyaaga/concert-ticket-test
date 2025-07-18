package com.ticketing.ticket_reserve.repositories;

import com.ticketing.ticket_reserve.entities.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Integer> {
    //At the moment, nothing is required here for the base functionality
}
