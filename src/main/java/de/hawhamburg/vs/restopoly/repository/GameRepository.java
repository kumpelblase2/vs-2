package de.hawhamburg.vs.restopoly.repository;

import de.hawhamburg.vs.restopoly.model.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends CrudRepository<Game, Integer> {
}
