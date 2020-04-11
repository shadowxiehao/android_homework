package Data;

public class Staff_info {
    private int id;
    private String name;
    private String salary;
    private String depart;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setSalary(String salary) {
        this.salary = salary;
    }
    public String getSalary() {
        return salary;
    }
    public void setDepart(String depart) {
        this.depart = depart;
    }
    public String getDepart() {
        return depart;
    }
    @Override
    public String toString() {
        return "Staff_info [id=" + id + ", name=" + name + ", salary="
                + salary + "depart=" + depart +"]";
    }

    public Staff_info(int id,String name,String salary,String depart){
        this.id=id;
        this.name=name;
        this.salary=salary;
        this.depart=depart;
    }

}