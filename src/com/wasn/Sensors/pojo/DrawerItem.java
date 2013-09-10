package com.wasn.Sensors.pojo;

/**
 * POJO class to keep Drawer item attributes
 *
 * @author erangaeb@gmail.com (eranga herath)
 */
public class DrawerItem {
    String name;
    int resourceId;

    public DrawerItem(String name, int resourceId) {
        this.name = name;
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
