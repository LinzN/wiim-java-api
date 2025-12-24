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
import de.linzn.wiimJavaApi.exceptions.WiimAPIDataPushException;

public class ExampleSwitchNext {
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

        /* Infinity loop to switch to next song */
        while (true) {
            /* Switch every 10000 milliseconds to the next song */
            String name = wiimAPI.getDeviceInformation().get_DeviceName();
            try {
                wiimAPI.getWiimPlayer().set_next();
                System.out.println("Device " + name + " switched to the next song!");
            } catch (WiimAPIDataPushException ignored) {
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}


