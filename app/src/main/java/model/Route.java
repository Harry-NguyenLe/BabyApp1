package model;

import java.util.Arrays;

public class Route {
    private int routeID;
    private String routeName;
    private String routeChildName;
    private String routeTime;
    private int check;
    private byte[] icon;
    private int timeInInteger;

    public Route(String routeName, String routeChildName, String routeTime, byte[] icon, int timeInInteger) {
        this.routeName = routeName;
        this.routeChildName = routeChildName;
        this.routeTime = routeTime;
        this.icon = icon;
        this.timeInInteger = timeInInteger;
    }

    public Route() {
    }

    public Route(int routeID, String routeName, String routeChildName, String routeTime, byte[] icon, int timeInInteger) {
        this.routeID = routeID;
        this.routeName = routeName;
        this.routeChildName = routeChildName;
        this.routeTime = routeTime;
        this.check = check;
        this.icon = icon;
        this.timeInInteger = timeInInteger;
    }

    public Route(String routeName, String routeChildName, String routeTime, int check) {
        this.routeName = routeName;
        this.routeChildName = routeChildName;
        this.routeTime = routeTime;
        this.check = check;
    }

    public Route(int routeID, String routeName, String routeChildName, String routeTime) {
        this.routeID = routeID;
        this.routeName = routeName;
        this.routeChildName = routeChildName;
        this.routeTime = routeTime;
    }

    public Route(int routeID, String routeName, String routeChildName, String routeTime, byte[] icon) {
        this.routeID = routeID;
        this.routeName = routeName;
        this.routeChildName = routeChildName;
        this.routeTime = routeTime;
        this.icon = icon;
    }

    public Route(String routeName, String routeChildName, String routeTime) {
        this.routeName = routeName;
        this.routeChildName = routeChildName;
        this.routeTime = routeTime;
    }

    public Route(byte[] icon) {
        this.icon = icon;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public int getRouteID() {
        return routeID;
    }

    public void setRouteID(int routeID) {
        this.routeID = routeID;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteChildName() {
        return routeChildName;
    }

    public void setRouteChildName(String routeChildName) {
        this.routeChildName = routeChildName;
    }

    public String getRouteTime() {
        return routeTime;
    }

    public void setRouteTime(String routeTime) {
        this.routeTime = routeTime;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public int getTimeInInteger() {
        return timeInInteger;
    }

    public void setTimeInInteger(int timeInInteger) {
        this.timeInInteger = timeInInteger;
    }

    @Override
    public String toString() {
        return "Route{" +
                "routeID=" + routeID +
                ", routeName='" + routeName + '\'' +
                ", routeChildName='" + routeChildName + '\'' +
                ", routeTime='" + routeTime + '\'' +
                ", check=" + check +
                ", icon=" + Arrays.toString(icon) +
                ", timeInInteger=" + timeInInteger +
                '}';
    }
}
