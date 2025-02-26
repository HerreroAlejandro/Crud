package com.api.crud.repositories;

import com.api.crud.models.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class RoleDaoImp implements RoleDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Role> findRoleByName(String roleName) {
        Role role = null;
        try {
            role = entityManager.createQuery(
                            "SELECT r FROM Role r WHERE r.nameRole = :roleName", Role.class)
                    .setParameter("roleName", roleName)
                    .getSingleResult();
        } catch (NoResultException e) {
          System.out.println ("Role don't found");
        }
        return Optional.ofNullable(role);
    }
    @Override
    public void save(Role role) {
        entityManager.persist(role);
    }

    @Override
    public List<Role> getRoles() {
        return entityManager.createQuery("FROM Role", Role.class).getResultList();
    }
}
