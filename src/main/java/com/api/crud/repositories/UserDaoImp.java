package com.api.crud.repositories;
import com.api.crud.models.UserModel;
import com.api.crud.services.EmailService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserDaoImp implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EmailService emailService;


    @Transactional
    @Override
    public List<UserModel> getUsers() {
        String query = "FROM UserModel u";
        return entityManager.createQuery(query, UserModel.class).getResultList();
    }

    @Override
    public boolean deleteUser(long id) {
        boolean response=false;
        UserModel user = entityManager.find(UserModel.class, id);
        if (user != null) {
            entityManager.remove(user);
            System.out.println("User with ID " + id + " was erased");
            response=true;
        } else {
            System.out.println("User with ID " + id + " wasn't found");
        }
       return response;
    }

    @Transactional
    public void register(UserModel user) {
        try {
            entityManager.merge(user);
            String subject = "Registration Confirmation";
            String body = "Hello " + user.getFirstName() + ",\n\n Your account has been created successfully.\n\nGreeting!";
            boolean emailSent = emailService.sendEmail(user.getEmail(), subject, body);

            if (!emailSent) {
                // Si no se pudo enviar el correo, lanza una excepción
                throw new RuntimeException("Failed to send confirmation email, User not registered.");
            }

            System.out.println("Your account was created successfully");
        } catch (Exception e) {
            System.out.println("error sending mail " + e.getMessage());
            throw new RuntimeException("Failed to send confirmation email, User not registered");
        }
    }

    public Optional<UserModel> findUserById(Long id) {
        UserModel user = entityManager.find(UserModel.class, id);
        System.out.println(user != null ? "User with ID " + id + " was found" : "User with ID " + id + " wasn't found");
        return Optional.ofNullable(user);
    }

    public Optional<UserModel> findUserByEmail(String email) {
        UserModel user = null;
        try {
            user = entityManager
                    .createQuery("SELECT u FROM UserModel u WHERE u.email = :email", UserModel.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {

        }
        return Optional.ofNullable(user);
    }

  /*  public Optional<UserModel> findUserByName(String firstName) {
        UserModel user = null;
        try {
            user = entityManager
                    .createQuery("SELECT u FROM UserModel u WHERE u.firstName = :firstName", UserModel.class)
                    .setParameter("firstName", firstName)
                    .getSingleResult();
        } catch (NoResultException e) {
        }
        return Optional.ofNullable(user);
    }*/

    public UserModel updateUserById(UserModel Request, Long id) {
        UserModel user = entityManager.find(UserModel.class, id);
        if (user != null) {
            user.setFirstName(Request.getFirstName());
            user.setLastName(Request.getLastName());
            user.setEmail(Request.getEmail());
            user.setPhone(Request.getPhone());
            user.setPassword(Request.getPassword());

            entityManager.merge(user);
            System.out.println("User with ID " + id + " was modified");
        } else {
            System.out.println("User with ID " + id + " wasn't found");
        }
        return user;

    }



}