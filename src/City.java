package src;

import java.util.Objects;

public class City {

  private int id;
  private String name;
  private double longitude;
  private double latitude;
  private int tempsEtiquetteProvisoire; // on doit conserver le temps d'atteinte pour la méthode de Dijkstra

  public City(int id, String name, double longitude, double latitude) {
    this.id = id;
    this.name = name;
    this.longitude = longitude;
    this.latitude = latitude;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public int getTempsEtiquetteProvisoire() {
    return tempsEtiquetteProvisoire;
  }

  public void setTempsEtiquetteProvisoire(int tempsEtiquetteProvisoire) {
    this.tempsEtiquetteProvisoire = tempsEtiquetteProvisoire;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    City city = (City) o;
    return id == city.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
