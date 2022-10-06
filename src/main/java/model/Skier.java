package model;


public class Skier {

  private Integer skierID;
  private Integer resortID;
  private Integer liftID;
  private String seasonID;
  private String dayID;
  private Integer time;

  public Skier(Integer skierID, Integer resortID, Integer liftID, String seasonID,
      String dayID, Integer time) {
    this.skierID = skierID;
    this.resortID = resortID;
    this.liftID = liftID;
    this.seasonID = seasonID;
    this.dayID = dayID;
    this.time = time;
  }

  public Integer getSkierID() {
    return skierID;
  }

  public void setSkierID(Integer skierID) {
    this.skierID = skierID;
  }

  public Integer getResortID() {
    return resortID;
  }

  public void setResortID(Integer resortID) {
    this.resortID = resortID;
  }

  public Integer getLiftID() {
    return liftID;
  }

  public void setLiftID(Integer liftID) {
    this.liftID = liftID;
  }

  public String getSeasonID() {
    return seasonID;
  }

  public void setSeasonID(String seasonID) {
    this.seasonID = seasonID;
  }

  public String getDayID() {
    return dayID;
  }

  public void setDayID(String dayID) {
    this.dayID = dayID;
  }

  public Integer getTime() {
    return time;
  }

  public void setTime(Integer time) {
    this.time = time;
  }

  //  @Override
//  public String toString() {
//    return "Skier{" +
//        "skierID=" + skierID +
//        ", resortID=" + resortID +
//        ", liftID=" + liftID +
//        ", seasonID=" + seasonID +
//        ", dayId=" + dayID +
//        ", time=" + time +
//        '}';
//  }
}
