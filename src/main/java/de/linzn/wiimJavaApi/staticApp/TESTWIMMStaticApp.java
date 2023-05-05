/*
 * Copyright (C) 2023. Niklas Linz - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the given license in the LICENSE file
 */

package de.linzn.wiimJavaApi.staticApp;

import de.linzn.wiimJavaApi.WiimAPI;

public class TESTWIMMStaticApp {

    public static void main(String[] args) throws InterruptedException {
        WiimAPI wiimAPI = new WiimAPI("10.50.0.99");
        wiimAPI.setSslCheck(false);
        wiimAPI.connect();
        while (true) {
            System.out.println("PlayerStatus: " + wiimAPI.getPlayerStatus().get_status());
            Thread.sleep(900);
        }
    }
}
