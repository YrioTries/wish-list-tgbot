package ru.javabot.wish.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javabot.wish.dao.ReferenceDao;

import java.util.List;

public interface ReferenceRepository extends JpaRepository<ReferenceDao, String>  {

    ReferenceDao findByReference(String reference);

    List<ReferenceDao> findByReferenceIn(List<String> stringReference);
}
