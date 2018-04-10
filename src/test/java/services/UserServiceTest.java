package services;

import domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import services.UserService;
import utilities.AbstractTest;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.Date;

@Transactional
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceTest extends AbstractTest {


    // The SUT
    // ====================================================

    @Autowired
    private UserService userService;


    /*  FUNCTIONAL REQUIREMENT:
     *
     *   An actor who is not authenticated must be able to:
     * - Register to the system as a user creating them.
     */


    public void userRegisterTest(final String username, final String password, final String passwordRepeat, final String name, final String surname, final String phone, final String email, final String postalAddress,  final Class<?> expected) {
        Class<?> caught = null;
        startTransaction();
        try {

            final User result;


            result = this.userService.create();






            result.getUserAccount().setUsername(username);
            result.setName(name);
            result.setSurname(surname);
            result.setPhone(phone);
            result.setEmail(email);
            result.setPostalAddresses(postalAddress);

            result.getUserAccount().setPassword(new Md5PasswordEncoder().encodePassword(password, null));

            this.userService.save(result);
            userService.flush();

        } catch (final Throwable oops) {

            caught = oops.getClass();

        }

        this.checkExceptions(expected, caught);
        rollbackTransaction();

    }

    public void followOrUnFollowUser(final String username, String userBean,
                                     final Class<?> expected) {
        Class<?> caught = null;
        startTransaction();
        try {


            authenticate(username);

            User userToFollow = userService.findOne(getEntityId(userBean));

            userService.follow(userToFollow.getId());

            this.userService.save(userToFollow);
            userService.flush();

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }

        this.checkExceptions(expected, caught);
        rollbackTransaction();
    }

    //Drivers
    // ===================================================

    @Test
    public void driverUserRegisterTest() {



        final Object testingData[][] = {
                // Alguien sin registrar/logueado -> true
                {
                        "user33", "user33", "user33", "userTestName", "userTestSurname", "+34 123456789", "userTest@userTest.com", "addressUser",  null
                },
                // Todos los campos como null --> false
               {
                        null, null, null, null, null, null, null, null, ConstraintViolationException.class
                },
                // Todos los campos completados, excepto la direccion postal -> true
                {
                        "userTest3", "userTest3", "userTest3", "userTestName3", "userTestSurname3","+34 123456789", "userTest@userTest.com", "",  null
                },
                // Username size menor que 5-> false
                {
                        "use", "userTest3", "userTest3", "userTestName3", "userTestSurname3","+34 123456789", "userTest@userTest.com", "",  ConstraintViolationException.class
                },

                // Todos los campos completados, introduciendo un <script> en el nombre -> false
               {
                       "user343", "user343", "user343", "<script>", "userTestSurname43","+34 123456789", "userTest@userTest.com", "",  ConstraintViolationException.class
                },

        };
        for (int i = 0; i < testingData.length; i++)
            this.userRegisterTest((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
                    (String) testingData[i][7], (Class<?>) testingData[i][8]);
    }

    @Test
    public void driverFollowOrUnFollowUser() {



        final Object testingData[][] = {
                // User1 sigue a user2 -> true
                {
                        "user1","user2",  null
                },
                // El usuario a null --> false
                {
                        null, "user1", IllegalArgumentException.class
                },
                // Usuario logueado y el resto a null -> true
//                {
//                        "user1", null, IllegalArgumentException.class
//                },


        };
        for (int i = 0; i < testingData.length; i++)
            this.followOrUnFollowUser((String) testingData[i][0],(String) testingData[i][1],
                    (Class<?>) testingData[i][2]);
    }



}
