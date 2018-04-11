package com.emraz.parkinghero.rest.model;

import com.emraz.parkinghero.util.Constants;

/**
 * Created by Yousuf on 7/17/2017.
 */

public class IBMParkingLocationStreet {

  String street;
  IbmParkingLocationClass disabledType, taxiType, truckType, pickupType, touringBusType, publicType, valetParkingType, chargingStationType;

  public IBMParkingLocationStreet(String street) {
    this.street = street;
    this.disabledType = new IbmParkingLocationClass(Constants.LOCATION_CLASS_DISABLED);
    this.taxiType = new IbmParkingLocationClass(Constants.LOCATION_CLASS_TAXI);
    this.truckType = new IbmParkingLocationClass(Constants.LOCATION_CLASS_TRUCK);
    this.pickupType = new IbmParkingLocationClass(Constants.LOCATION_CLASS_PICKUP);
    this.touringBusType = new IbmParkingLocationClass(Constants.LOCATION_CLASS_TOURING_BUS);
    this.publicType = new IbmParkingLocationClass(Constants.LOCATION_CLASS_PUBLIC);
    this.valetParkingType = new IbmParkingLocationClass(Constants.LOCATION_CLASS_VALET_PARKING);
    this.chargingStationType = new IbmParkingLocationClass(Constants.LOCATION_CLASS_CHARGING_STATION);
  }


  public void addLocationToType(IbmResponseLocation responseLocation, String locationClass) {

    if (responseLocation == null) {
      return;
    }

    switch (locationClass) {
      case Constants.LOCATION_CLASS_PUBLIC:
        this.addToPublicList(responseLocation);
        break;

      case Constants.LOCATION_CLASS_DISABLED:
        this.addToDisabledList(responseLocation);
        break;

      case Constants.LOCATION_CLASS_TAXI:
        this.addToTaxiList(responseLocation);
        break;

      case Constants.LOCATION_CLASS_TRUCK:
        this.addToTruckList(responseLocation);
        break;

      case Constants.LOCATION_CLASS_PICKUP:
        this.addToPickupList(responseLocation);
        break;

      case Constants.LOCATION_CLASS_TOURING_BUS:
        this.addToTouringBusList(responseLocation);
        break;
      case Constants.LOCATION_CLASS_VALET_PARKING:
        this.addToValetParkingList(responseLocation);
        break;
      case Constants.LOCATION_CLASS_CHARGING_STATION:
        this.addToChargingStationList(responseLocation);
        break;
      default:
        break;
    }

  }


  public void addToDisabledList(IbmResponseLocation location) {
    this.disabledType.addToList(location);
  }

  public void addToTaxiList(IbmResponseLocation location) {
    this.taxiType.addToList(location);
  }

  public void addToTruckList(IbmResponseLocation location) {
    this.truckType.addToList(location);
  }

  public void addToPickupList(IbmResponseLocation location) {
    this.pickupType.addToList(location);
  }

  public void addToTouringBusList(IbmResponseLocation location) {
    this.touringBusType.addToList(location);
  }

  public void addToPublicList(IbmResponseLocation location) {
    this.publicType.addToList(location);
  }

  public void addToValetParkingList(IbmResponseLocation location) {
    this.valetParkingType.addToList(location);
  }

  public void addToChargingStationList(IbmResponseLocation location) {
    this.chargingStationType.addToList(location);
  }

  public IbmParkingLocationClass getDisabledType() {
    return disabledType;
  }

  public void setDisabledType(IbmParkingLocationClass disabledType) {
    this.disabledType = disabledType;
  }

  public IbmParkingLocationClass getTaxiType() {
    return taxiType;
  }

  public void setTaxiType(IbmParkingLocationClass taxiType) {
    this.taxiType = taxiType;
  }

  public IbmParkingLocationClass getTruckType() {
    return truckType;
  }

  public void setTruckType(IbmParkingLocationClass truckType) {
    this.truckType = truckType;
  }

  public IbmParkingLocationClass getPickupType() {
    return pickupType;
  }

  public void setPickupType(IbmParkingLocationClass pickupType) {
    this.pickupType = pickupType;
  }

  public IbmParkingLocationClass getTouringBusType() {
    return touringBusType;
  }

  public void setTouringBusType(IbmParkingLocationClass touringBusType) {
    this.touringBusType = touringBusType;
  }

  public IbmParkingLocationClass getPublicType() {
    return publicType;
  }

  public void setPublicType(IbmParkingLocationClass publicType) {
    this.publicType = publicType;
  }

  public IbmParkingLocationClass getValetParkingType() {
    return valetParkingType;
  }

  public void setValetParkingType(IbmParkingLocationClass valetParkingType) {
    this.valetParkingType = valetParkingType;
  }

  public IbmParkingLocationClass getChargingStationType() {
    return chargingStationType;
  }

  public void setChargingStationType(IbmParkingLocationClass chargingStationType) {
    this.chargingStationType = chargingStationType;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }


  @Override
  public String toString() {
    return "IBMParkingLocationStreet{"
            + "street='"
            + street
            + '\''
            + ", disabledType="
            + disabledType
            + ", taxiType="
            + taxiType
            + ", truckType="
            + truckType
            + ", pickupType="
            + pickupType
            + ", touringBusType="
            + touringBusType
            + ", valetParkingType="
            + valetParkingType
            + ", chargingStationType="
            + chargingStationType
            + ", publicType="
            + publicType
            + '}';
  }
}
