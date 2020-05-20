package P1;

public class Employee {
    public String name;
    public int age;
    public String designation;
    private double salary;
    
    public Employee(String name) {this.name = name;}
    public void empAge(int empAge) {age = empAge;}
    public void empDesignation(String empDesig) {designation = empDesig;}
    public void empSalary(double empSalary) {salary = empSalary;}
    public void printName() {System.out.println("Name:"+ name );}
}