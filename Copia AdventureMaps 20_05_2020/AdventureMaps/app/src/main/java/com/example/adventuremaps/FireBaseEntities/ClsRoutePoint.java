package com.example.adventuremaps.FireBaseEntities;

public class ClsRoutePoint {

    private String routePointId;
    private Long priorityRoute;//En FireBase no podemos almacenar el dato Int
    private String routeId;
    private Double latitude;
    private Double longitude;

    public ClsRoutePoint(){

    }

    public ClsRoutePoint(Double latitude, Double longitude){
        this.routePointId = "";
        this.priorityRoute = (long) 0;
        this.routeId = "";
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public ClsRoutePoint(String routePointId, Long priorityRoute, String routeId, Double latitude, Double longitude){
        this.routePointId = routePointId;
        this.priorityRoute = priorityRoute;
        this.routeId = routeId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //Get y Set
    public String getRoutePointId() {
        return routePointId;
    }

    public void setRoutePointId(String routePointId) {
        this.routePointId = routePointId;
    }

    public Long getPriorityRoute() {
        return priorityRoute;
    }

    public void setPriorityRoute(Long priorityRoute) {
        this.priorityRoute = priorityRoute;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
