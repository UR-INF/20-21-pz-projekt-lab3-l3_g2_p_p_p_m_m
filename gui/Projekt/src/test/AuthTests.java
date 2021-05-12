//package test;
//
//import main.java.controllers.auth.Login;
//import org.junit.jupiter.api.*;
//import org.opentest4j.AssertionFailedError;
//
//public class AuthTests extends Login {
//
//    @Test
//    public void shouldBeEmail(){
//        System.out.println("Test sprawdzający czy string jest emailem");
//
//        String email = "email@gmail.com";
//
//        Assertions.assertTrue(isEmail(email));
//    }
//
//    @Test
//    public void shouldNotBeEmail(){
//        System.out.println("Test sprawdzający czy string jest emailem");
//
//        String email = "email!gmail.com";
//
//        Assertions.assertFalse(isEmail(email));
//    }
//
//    @Test
//    public void loginAndPasswordShouldNotBeEmptyTest(){
//        System.out.println("Test sprawdzający czy podano login i haslo...");
//
//        String email = "email@gmail.com";
//        String password = "Strong_Password2132!";
//
//        Assertions.assertFalse(isEmpty(email, password));
//    }
//
//    @Test
//    public void shouldThrowErrorWhenLoginIsEmptyTest(){
//        System.out.println("Test sprawdzający czy wystapi blad jezeli nie podano adresu email...");
//
//        String email = "";
//        String password = "Strong_Password2132!";
//
//        Assertions.assertThrows(NullPointerException.class,
//                ()->{
//                    isEmpty(email, password);
//                }
//        );
//    }
//
//    @Test
//    public void shouldThrowErrorWhenPasswordIsEmptyTest(){
//        System.out.println("Test sprawdzający czy wystapi blad jezeli nie podano hasla...");
//
//        String email = "email@gmail.com";
//        String password = "";
//
//        Assertions.assertThrows(NullPointerException.class,
//                ()->{
//                    isEmpty(email, password);
//                }
//        );
//    }
//
//    @Test
//    public void shouldReturnUserId(){
//        System.out.println("Test sprawdzający czy zwrocono id uzytkownika o numerze '1'...");
//        setUserID(1);
//
//        Assertions.assertEquals(1,getUserID());
//    }
//
//    @Test
//    public void shouldBeErrorWhileReturningWrongUserId(){
//        System.out.println("Test sprawdzający czy wystąpił błąd przy zwroceniu złego id uzytkownika...");
//        setUserID(1);
//
//        Assertions.assertThrows(AssertionFailedError.class,
//                ()->{
//                    Assertions.assertEquals(2,getUserID());
//                });
//    }
//
//    @Test
//    public void shouldReturnUserInfoId(){
//        System.out.println("Test sprawdzający czy zwrocono user info id o numerze '1'...");
//        setUserInfoID(1);
//
//        Assertions.assertEquals(1,getUserInfoID());
//    }
//
//    @Test
//    public void shouldBeErrorWhileReturningWrongUserInfoId(){
//        System.out.println("Test sprawdzający czy wystąpił błąd przy zwroceniu złego user info id...");
//        setUserInfoID(1);
//
//        Assertions.assertThrows(AssertionFailedError.class,
//                ()->{
//                    Assertions.assertEquals(2,getUserInfoID());
//                });
//    }
//
//    @Test
//    public void shouldReturnUserEmail(){
//        System.out.println("Test sprawdzający czy zwrocono email uzytkownika...");
//        setUserEmail("email@gmail.com");
//
//        Assertions.assertNotNull(getUserEmail());
//    }
//}
