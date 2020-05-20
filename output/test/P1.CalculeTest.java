import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Random;
import java.util.Scanner;
import P1.Calcule;
public class CalculeTest {
Calcule calcule = new Calcule();
Random rand = new Random();
int upperbound = 21;
@BeforeEach
public void setUp() throws Exception {
System.out.println("What are your expectations: ");
}
int rand11 = rand.nextInt(upperbound);
int rand11 = rand.nextInt(upperbound);
@Test
void Testsomme() {
System.out.println("somme("+ rand11 + ","+ rand21 + ")");
Scanner object1 = new Scanner(System.in);
int result1 = object1.nextInt();
assertEquals(result1,calcule.somme(rand11,rand21));
}
int rand12 = rand.nextInt(upperbound);
int rand12 = rand.nextInt(upperbound);
@Test
void Testproduit() {
System.out.println("produit("+ rand12 + ","+ rand22 + ")");
Scanner object2 = new Scanner(System.in);
int result2 = object2.nextInt();
assertEquals(result2,calcule.produit(rand12,rand22));
}
@Test
void TestTPReflexion() {
System.out.println("TPReflexion():");
Scanner object3 = new Scanner(System.in);
String res3 = object3.nextLine();
assertEquals(res3,calcule.TPReflexion());
}

}