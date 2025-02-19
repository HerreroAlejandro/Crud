package com.api.crud.repositories;
import com.api.crud.models.UserModel;
import com.api.crud.services.EmailService;
import jakarta.persistence.EntityManager;
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
        String query = "FROM UserModel";
        return entityManager.createQuery(query, UserModel.class).getResultList();
    }

    @Override
    public void deleteUser(long id) {
        UserModel user = entityManager.find(UserModel.class, id);
        if (user != null) {
            entityManager.remove(user);
            System.out.println("User with ID " + id + " was erased");
        } else {
            System.out.println("User with ID " + id + " wasn't found");
        }
    }

    public void signUp(UserModel user) {
        try {
            String subject = "Registration Confirmation";
            String body = "Hello " + user.getFirstName() + ",\n\n Your account has been created successfully.\n\nGreeting!";
            emailService.sendEmail(user.getEmail(), subject, body);

            entityManager.merge(user);
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

    /*
    public UserModel obtenerUsuarioPorCredenciales(UserModel usuario) {
        String query = "FROM Usuario WHERE email = :email "; //Usuario es la clase
        List<UserModel> newList = entityManager.createQuery(query)
                .setParameter("email", usuario.getEmail())
                .getResultList();

        if (newList.isEmpty()) {      //Esto lo hago ya que si el mail no existe, y la lista vuelve vacia, el get de password traería un null y apareceria una excepcion
            return null;
        }
        return usuario;
    }
    */

}