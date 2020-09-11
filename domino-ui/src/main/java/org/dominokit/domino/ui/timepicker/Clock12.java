package org.dominokit.domino.ui.timepicker;

import elemental2.core.JsDate;
import org.gwtproject.i18n.shared.cldr.DateTimeFormatInfo;

import java.util.Date;

import static org.dominokit.domino.ui.timepicker.DayPeriod.*;

class Clock12 extends AbstractClock {

    public Clock12(DateTimeFormatInfo dateTimeFormatInfo) {
        this(new JsDate());
        this.dateTimeFormatInfo = dateTimeFormatInfo;
    }

    Clock12(JsDate jsDate) {
        this.dayPeriod = jsDate.getHours() > 11 ? PM : AM;
        this.minute = jsDate.getMinutes();
        this.second = jsDate.getSeconds();
        if (jsDate.getHours() > 12) {
            this.hour = jsDate.getHours() - 12;
        } else if (jsDate.getHours() == 0) {
            this.hour = 12;
        } else {
            this.hour = jsDate.getHours();
        }
    }

    @Override
    public Clock getFor(JsDate jsDate) {
        return new Clock12(jsDate);
    }

    @Override
    public String format() {
        return formatNoPeriod() + " " + formatPeriod();
    }

    @Override
    public String formatNoPeriod() {
        String hourString = this.hour < 10 ? "0" + this.hour : this.hour + "";
        String minuteString = this.minute < 10 ? "0" + this.minute : this.minute + "";
        String secondString = this.second < 10 ? "0" + this.second : this.second + "";
        return hourString + ":" + minuteString + (showSecond ? ":" + secondString : "");
    }

    @Override
    public String formatPeriod() {
        return AM.equals(dayPeriod) ? dateTimeFormatInfo.ampms()[0] : dateTimeFormatInfo.ampms()[1];
    }

    @Override
    public int getStartHour() {
        return 1;
    }

    @Override
    public int getEndHour() {
        return 12;
    }

    @Override
    public void setHour(int hour) {
        if (hour > 12) {
            this.hour = hour - 12;
        } else if (hour == 0) {
            this.hour = 12;
        } else {
            this.hour = hour;
        }
    }

    @Override
    public int getCorrectHour(int hour) {
        if (hour > 12) {
            return hour - 12;
        } else if (hour == 0) {
            return 12;
        } else {
            return hour;
        }
    }

    @Override
    public Date getTime() {
        JsDate jsDate = new JsDate();
        jsDate.setHours(DayPeriod.PM.equals(dayPeriod) ? hour + 12 : hour);
        jsDate.setMinutes(minute);
        jsDate.setSeconds(second);
        return new Date(new Double(jsDate.getTime()).longValue());
    }

}
