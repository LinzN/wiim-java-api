/*
 * Copyright (c) 2025 MirraNET, Niklas Linz. All rights reserved.
 *
 * This file is part of the MirraNET project and is licensed under the
 * GNU Lesser General Public License v3.0 (LGPLv3).
 *
 * You may use, distribute and modify this code under the terms
 * of the LGPLv3 license. You should have received a copy of the
 * license along with this file. If not, see <https://www.gnu.org/licenses/lgpl-3.0.html>
 * or contact: niklas.linz@mirranet.de
 */
package examples;

import de.linzn.wiimJavaApi.WiimAPI;

public class ExampleDeviceStatus {
    /* IP Address of the device */
    static String wiimDeviceIPAddress = "10.50.0.99";

    public static void main(String[] args) throws InterruptedException {
        /* Creating a new wiimAPI instance with the ip address of the device */
        WiimAPI wiimAPI = new WiimAPI(wiimDeviceIPAddress);
        /* Disable ssl check because no valid certificate is given from the wiiMDevice by default */
        wiimAPI.setSslCheck(false);
        /* Connect to the httpAPI from the WiiMDevice */
        /* The current dataSet will be pulled automatic every 1000 ms*/
        wiimAPI.connect();

        /* Infinity loop to check frequently device status and the name of the device */
        while (true) {
            /* Get the current status of the player. Like "stop, play, ..."*/
            String name = wiimAPI.getDeviceInformation().get_DeviceName();
            String currentStatus = wiimAPI.getWiimPlayer().get_status();
            System.out.println("Current status of the device " + name + " is " + currentStatus);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}


