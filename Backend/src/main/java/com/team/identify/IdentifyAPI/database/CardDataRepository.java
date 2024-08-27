package com.team.identify.IdentifyAPI.database;

import com.team.identify.IdentifyAPI.model.CardData;
import com.team.identify.IdentifyAPI.model.IDCollectionPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CardDataRepository extends JpaRepository<CardData, UUID> {

    Page<CardData> findAllByCollectionPoint(IDCollectionPoint collectionPoint, Pageable pageable);

    int countAllByCollectionPointIs(IDCollectionPoint collectionPoint);

    List<CardData> findAllByCollectionPoint(IDCollectionPoint collectionPoint);

    CardData findByJobId(UUID id);

}
