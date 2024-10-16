package com.example.springboottyy.repository;

import com.example.springboottyy.model.SysDictData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SysDictDataRepository extends JpaRepository<SysDictData, Long> {
    /*分页查询*/
    Page<SysDictData> findAll(Pageable pageable);

    Optional<SysDictData> findByDictCode(Long code);
}
