package com.rentguruz.app.model.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TimeLine implements Serializable {
   public String Datetime;
   public List<TimelineData> items = new ArrayList<>();

}
