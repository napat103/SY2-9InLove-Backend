package sit.project.projectv1.repositories;

public interface EntityRepository<T> {
    void refresh(T t);
}
