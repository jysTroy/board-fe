package org.maengle.board.repositories;

import org.maengle.board.entities.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, String>, QuerydslPredicateExecutor<Board> {
    boolean existsByBid(String bid);

    // 전체 조회 메서드 추가했음
    List<Board> findAll();
}
