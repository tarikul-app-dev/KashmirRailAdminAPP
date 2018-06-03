package limited.it.planet.traintimingapp.model;

/**
 * Created by Tarikul on 5/20/2018.
 */

public class TrainTimingM {
    String train_number;
    String day;
    String timing;

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    int serialNumber;

    public String getStation_title() {
        return station_title;
    }

    public void setStation_title(String station_title) {
        this.station_title = station_title;
    }

    String station_title;

    public String getTrain_number() {
        return train_number;
    }

    public void setTrain_number(String train_number) {
        this.train_number = train_number;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

  public TrainTimingM(){

  }



}
