package P1;

public class Calcule {

	public Calcule() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public int somme(int num1,int num2){ 
		return num1+num2;
	}
	
	public int produit(int num1,int num2){ 
		return num1*num2;
	}
	
	public String TPReflexion() { 
		Calcule c = new Calcule();
		return "Package: " + c.getClass().getPackage().getName() + " - ClassName: " + c.getClass().getSimpleName();
	}

}
