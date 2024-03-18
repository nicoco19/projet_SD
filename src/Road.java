package src;

public class Road {

  private City cityStart;
  private City cityDest;
  private int duree;

  public Road(City cityStart, City cityDest) {
    this.cityStart = cityStart;
    this.cityDest = cityDest;
  }

  public City getCityStart() {
    return cityStart;
  }

  public void setCityStart(City cityStart) {
    this.cityStart = cityStart;
  }

  public City getCityDest() {
    return cityDest;
  }

  public void setCityDest(City cityDest) {
    this.cityDest = cityDest;
  }

  public int getDuree() {
    return duree;
  }

  public void setDuree(int duree) {
    this.duree = duree;
  }
}



