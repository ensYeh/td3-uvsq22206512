package fr.uvsq.cprog.collex;

public class NomMachine {

    private String name;

    public NomMachine(String name) {
        this.name = name.trim();
    }

    public String toString() {
        return name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NomMachine that = (NomMachine) o;
        return name.equalsIgnoreCase(that.name);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }
}
