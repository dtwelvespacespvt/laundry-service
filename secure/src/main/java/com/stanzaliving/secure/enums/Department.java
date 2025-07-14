package com.stanzaliving.secure.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

@Getter
@AllArgsConstructor
public enum Department {
    TECH("Tech","TP","TCH"),
    FINANCE("Finance","FN","FIN"),
    HR("Talent Management","HR","HRS"),
    LEGAL("Legal","LG","LGL"),
    SUPERADMIN("Superadmin",null,null),
    LEADERSHIP("Leadership",null,null),
    BUSINESS_DEVELOPMENT("Real Estate","RE","RET"),
    OPS("Operations","OP","OPS"),
    FOOD_OPS("Food Operations","FB","FNB"),
    PROCUREMENT("Procurement",null,"TRN"),
    DESIGN("Design",null,"TRN"),
    PROJECTS("Projects",null,"TRN"),
    TRANSFORMATIONS("Transformation","TN","TRN"),
    SALES("Sales","SM","SNM"),
    WEB("Web",null,null),
    GC("GC Procurement",null,"TRN"),
    WAREHOUSE("Warehouse",null,null),
    CX("Customer Experience","CX","CXP"),
    ADMIN("Admin","AD","ADM"),
    MARKETING("Marketing","MK","MKT"),
    SCM("Supply Chain Mgmt","SM","TRN"),
    GROWTH_STRATEGY("Growth and Strategy","GS","GRS"),
    E_COMMERCE("E-Commerce", "EC", "ECE"),
    LND("Learning & Development","LD","LND"),
    PUBLIC_POLICY("Public Policy","PP","PPO"),
    CORPORATE_COMM("Corporate Communication","CC","COC"),
    PERFORMANCE_MARK("Performance Marketing","PM","PMR"),
    SALES_SBR("Sales- SBR","SB","SLB"),
    BRAND_MKTNG("Brand Marketing","BM","BRM"),
    RES21("RE - S21","RS","RES"),
    CORPORATE_DESIGN("Corporate Design","CD","CDS"),
    SALES_IS("Sales IS", "IS", "SIS"),
    RE_BROKERAGE("RE Brokerage","RB","RBK"),
    TECH_PILOT("Tech Pilot", "TPL", "TPLT");

    public String departmentName;
    public String shortCode;
    public String genericPoShortCode;
    private static SortedMap<String, Department> departmentMap = new TreeMap<String, Department>();

    static {
        for (Department d : Department.values()) {
            departmentMap.put(d.getDepartmentName(), d);
        }
    }

    public static Collection<Department> getDepartments() {
        return departmentMap.values();
    }
}
