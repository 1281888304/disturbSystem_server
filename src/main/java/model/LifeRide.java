package model;

public class LifeRide {
  private Integer time;
  private Integer liftID;

  public LifeRide(Integer time, Integer liftID) {
    this.time = time;
    this.liftID = liftID;
  }

  public Integer getTime() {
    return time;
  }

  public void setTime(Integer time) {
    this.time = time;
  }

  public Integer getLiftID() {
    return liftID;
  }

  public void setLiftID(Integer liftID) {
    this.liftID = liftID;
  }


}