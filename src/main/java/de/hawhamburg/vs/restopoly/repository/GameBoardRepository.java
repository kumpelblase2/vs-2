package de.hawhamburg.vs.restopoly.repository;

import de.hawhamburg.vs.restopoly.model.GameBoard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameBoardRepository extends CrudRepository<GameBoard, Integer> {
}
