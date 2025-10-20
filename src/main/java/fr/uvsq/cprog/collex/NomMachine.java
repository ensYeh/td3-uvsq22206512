package fr.uvsq.cprog.collex;

/**
 * Thomas Torres 22206512
 * Représente un nom qualifié de machine au format "machine.domaine.local".
 */
public class NomMachine {

  /** Nom complet de la machine. */
  private String name; // machine.domaine.local

  /**
   * Constructeur.
   * @param name le nom de la machine
   */
  public NomMachine(String name) {
    this.name = name.trim();
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NomMachine that = (NomMachine) o;
    return name.equalsIgnoreCase(that.name);
  }

  @Override
  public int hashCode() {
    return name.toLowerCase().hashCode();
  }
}