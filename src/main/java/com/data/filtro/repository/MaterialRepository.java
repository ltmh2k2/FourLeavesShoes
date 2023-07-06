package com.data.filtro.repository;

import com.data.filtro.model.Account;
import com.data.filtro.model.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {

    @Query("select f from Material f where f.id =:id")
    Material findById(@Param("id") int id);

    @Modifying
    @Query("update Material f set f.status=0 where f.id=:id")
    void deleteById(@Param("id") int id);

    Page<Material> findAll(Pageable pageable);

    @Query("select a from Material a where a.status = :status")
    List<Material> activeMaterials(@Param("status") int status);
}
