package sit.project.projectv1.repositories.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import sit.project.projectv1.repositories.EntityRepository;

public class EntityRepositoryImpl<T> implements EntityRepository<T> {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void refresh(T o) {
        entityManager.refresh(o);
    }
}
