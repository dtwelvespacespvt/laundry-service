package com.stanzaliving.secure.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType {
    STUDENT("Student"),
    PARENT("Parent"),
    LEGAL("Legal"),
    HR("HR"),
    TECH("Tech"),
    FINANCE("Finance"),
    PROCUREMENT("Procurement"),
    MANAGER("Manager"),
    BD("BD"),
    LEADERSHIP("Leadership"),
    OPS("OPS"),
    SITE_ENGINEER("Site Engineer"),
    PROJECT_MANAGER("Project Manager"),
    ZONAL_HEAD("Zonal Head"),
    NATIONAL_HEAD("National Head"),
    DESIGN_COORDINATOR("Design Coordinator"),
    CONSUMER("Consumer"),
    SYSTEM("System"),
    CITY_TEAM("City Team"),
    CENTRAL_TEAM("Central Team"),
    EXTERNAL("External"),
    GUEST("Guest"),
    INVITED_GUEST("Invited Guest"),
    FOOD_DELIVERY_AGENT("Food Delivery Agent"),
    VENDOR("Vendor"),
    EMPLOYEE("Employee"),
    NA("NA");

    public String typeName;

}
