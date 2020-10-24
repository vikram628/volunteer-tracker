package net.empoweringtechnology.volunteertracker;


public class Hours {
    private String _org;
    private String _activity;
    private String _date;
    private String _hours;


    public Hours(String org, String activity, String date, String hours) {
        this._org = org;
        this._activity = activity;
        this._date = date;
        this._hours = hours;
    }

    public String get_org() {
        return _org;
    }

    public String get_activity() {
        return _activity;
    }

    public String get_date() {
        return _date;
    }

    public String get_hours() {
        return _hours;
    }

    public void set_org() {
        this._org = _org;
    }

    public void set_activity() {
        this._activity = _activity;
    }

    public void set_date() {
        this._date = _date;
    }

    public void set_hours() {
        this._hours = _hours;
    }
}


