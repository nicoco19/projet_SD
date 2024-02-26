package src;

public class Road {

  private City cityStart;
  private City cityDest;

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
}
